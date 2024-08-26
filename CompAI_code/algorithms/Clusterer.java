package lu.svv.saa.linklaters.privacypolicies.algorithms;

import static org.apache.uima.fit.util.JCasUtil.select;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.deeplearning4j.clustering.algorithm.Distance;
import org.deeplearning4j.clustering.cluster.Cluster;
import org.deeplearning4j.clustering.cluster.ClusterSet;
import org.deeplearning4j.clustering.cluster.Point;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair;
import lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult;
import lu.svv.saa.linklaters.privacypolicies.type.SentenceOperation;
import lu.svv.saa.linklaters.privacypolicies.type.UoA;
import lu.svv.saa.linklaters.privacypolicies.utils.FileHandler;
import lu.svv.saa.linklaters.privacypolicies.utils.KeywordsExtender;

public class Clusterer extends JCasAnnotator_ImplBase {
  
  public static final String PARAM_MODEL_LOCATION = "modelLocation";
  @ConfigurationParameter(name = PARAM_MODEL_LOCATION, mandatory = true,
      description = "the location of the pretrained word vector models",
      defaultValue = "src/main/resources/vector_models/glove.6B.100d.txt")
  private String modelLocation;

  public static final String PARAM_SENTENCE_OPERATION = "operation";
  @ConfigurationParameter(name = PARAM_SENTENCE_OPERATION, mandatory = true,
      description = "the operation used to combine word vectors into a sentence vector"
          + ": average or add", defaultValue = "AVERAGE")
  private SentenceOperation operation;
  
  // Get the location of keywords
  public static final String PARAM_KWS_LOCATION = "kwsLocation";
  @ConfigurationParameter(name = PARAM_KWS_LOCATION, mandatory = true,
      description = "the location of the keywords files",
      defaultValue = "src/main/resources/keywords/Generic_Metadata/")
  private String kwsLocation;
  
  // Whether to extend the keywords with synonyms
  public static final String PARAM_INITIALIZE_WITH_KWS = "initializeWithKWs";
  @ConfigurationParameter(name = PARAM_INITIALIZE_WITH_KWS, mandatory = true,
      description = "whether to use KWs as initial seeds for the clusters",
      defaultValue = "false")
  private boolean initializeWithKWs;
  
//Whether to extend the keywords with synonyms
 public static final String PARAM_EXTEND_KWS = "extendKWs";
 @ConfigurationParameter(name = PARAM_EXTEND_KWS, mandatory = true,
     description = "whether to extend the keywords using WordNet",
     defaultValue = "false")
 private boolean extendKWs;
  
  // Whether to use first sense only or all senses
  public static final String PARAM_FIRST_SENSE = "firstSense";
  @ConfigurationParameter(name = PARAM_FIRST_SENSE, mandatory = true,
      description = "whether to use first sense only or all senses for keywords extension",
      defaultValue = "true")
  private boolean firstSense;
  
  // Get the location of the training examples
  public static final String PARAM_TRAINING_LOCATION = "trainLocation";
  @ConfigurationParameter(name = PARAM_TRAINING_LOCATION, mandatory = true,
      description = "the location of the training examples used for enriching clusters",
      defaultValue = "src/main/resources/2training_pipeline/clustering_trainset/")
  private String trainLocation;
  
  // Whether to enrich the clusters with training examples or not
  public static final String PARAM_ENRICH_CLUSTERS = "enrichClusters";
  @ConfigurationParameter(name = PARAM_ENRICH_CLUSTERS, mandatory = true,
      description = "whether to enrich clusters with annotated data from a training set",
      defaultValue = "true")
  private boolean enrichClusters;
  
  // The distance function used to do the clustering
  public static final String PARAM_DISTANCE_FUNCTION = "distance";
  @ConfigurationParameter(name = PARAM_DISTANCE_FUNCTION, mandatory = true,
      description = "what is the distance function to be used for clustering",
      defaultValue = "COSINE_SIMILARITY")
  private Distance distance;
  
  private WordVectors vectorModel;
  private Map<String, List<INDArray>> vectorsPerMetadata;
  private ClusterSet labeledClusterSet;

  @SuppressWarnings("deprecation")
  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    vectorsPerMetadata = new HashMap<String, List<INDArray>>();
    // Load pre-trained vector model
    File model = new File(modelLocation);
    // Glove model
    if (modelLocation.contains("glove"))
      try {
        vectorModel = WordVectorSerializer.loadTxtVectors(model);
      } catch (IOException e) {
        e.printStackTrace();
      }
    // Word2Vec model
    else
      vectorModel = WordVectorSerializer.readWord2VecModel(model);
    
    // get the vectors per keyword file
    // initialize the clusters
    try {
      getEmbeddings();
      initializeClusters();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // read from annotated/labeled files and assign them to a particular cluster
    if(enrichClusters) {
     try {
       enrichClusters();
     } catch (IOException e) {
       e.printStackTrace();
     }
    }
    }
  
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // for each uoa predict the metadata using cosine distance
    // assign predictedLabel
    for (UoA uoa : select(aJCas, UoA.class)) {
      try {
        String uoaText = (uoa.getPreprocessed().isEmpty()?uoa.getGeneralized():uoa.getPreprocessed());
        INDArray uoaVec = getSentenceVector(uoaText.toLowerCase());
        if(uoaVec.isEmpty()) {
          ClusterDistancePair pair = newClusterDistancePair(aJCas, uoa.getBegin(), uoa.getEnd(), "N/A", 0.0);
          FSArray pairs = new FSArray(aJCas, 1);
          pairs.set(0, pair);
          newClusteringResult(aJCas, uoa.getBegin(), uoa.getEnd(), pairs);
        } else {
        Point uoaPnt = new Point(uoaVec);
        
        int i = 0;
        FSArray pairs = new FSArray(aJCas, labeledClusterSet.getClusterCount());
        for (Cluster c : labeledClusterSet.getClusters()) {
          
          double sim = c.getDistanceToCenter(uoaPnt);          
          ClusterDistancePair pair = newClusterDistancePair(aJCas, uoa.getBegin(), uoa.getEnd(), c.getLabel(), sim); 
          pairs.set(i, pair);
          i++;
        }
        newClusteringResult(aJCas, uoa.getBegin(), uoa.getEnd(), pairs);
        }
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    
  }
  
  private void newClusteringResult(JCas aJCas, int begin, int end, FSArray pairs) {
    ClusteringResult clusteringResult = new ClusteringResult(aJCas);
    
    clusteringResult.setBegin(begin);
    clusteringResult.setEnd(end);
    clusteringResult.setDistancesToClusters(pairs);
    clusteringResult.addToIndexes();
  }

  private ClusterDistancePair newClusterDistancePair(JCas aJCas, int begin, int end, String clusterLabel,
      double distance) {
    ClusterDistancePair pair = new ClusterDistancePair(aJCas);
    pair.setBegin(begin);
    pair.setEnd(end);
    pair.setClusterLabel(clusterLabel);
    pair.setDistance(distance);
    return pair;
  }

  //Get word embeddings from the keywords of the metadata files
  private void getEmbeddings() throws ResourceInitializationException, IOException {
    String location = trainLocation;
    List<String> trainingFiles = new ArrayList<String>();
    if(initializeWithKWs)  {
       location = kwsLocation;
    }
    trainingFiles = FileHandler.getFileNames(trainLocation, ""); 
    
   for (String fileName : trainingFiles) {
	   if(fileName.startsWith("."))
	       continue;
     List<INDArray> vectors = new ArrayList<INDArray>();
     List<String> lines =
         new ArrayList<String>(FileHandler.readFromFile(location + fileName));
     for (String line : lines) {
       if(extendKWs) {
         KeywordsExtender kwExt = new KeywordsExtender();
         line = kwExt.extendKeywords(line, firstSense);
       }
       INDArray phraseVector = getSentenceVector(line.toLowerCase());       
       if(!phraseVector.isEmpty())
         vectors.add(phraseVector);
     }
     vectorsPerMetadata.put(fileName, vectors);
   }    
 }
 
 private INDArray getSentenceVector(String sentence) throws IOException {
   INDArray wordsVectors = vectorModel.getWordVectors(new ArrayList<String>(Arrays.asList(sentence.split(" "))));

   if (wordsVectors.isEmpty()) {
     return wordsVectors;
   }
   // average: default
   INDArray sentenceVector = wordsVectors.mean(0);
   // add
   if (operation.equals(SentenceOperation.ADD))
     sentenceVector = wordsVectors.sum(0);
   return sentenceVector;
 }

 private void initializeClusters() throws IOException, ResourceInitializationException {
   // create clusters = generic metadata, assign the keywords per metadata and compute the center
   List<Cluster> clustersPerMetadata = new ArrayList<Cluster>();
   for (Entry<String, List<INDArray>> perMetadata : vectorsPerMetadata.entrySet()) {
     String metadata = perMetadata.getKey();
     List<INDArray> vectors = perMetadata.getValue();
     Cluster cluster = new Cluster();
     cluster.setDistanceFunction(distance);
     cluster.setLabel(metadata);
     for(INDArray vec: vectors) {
       if (!vec.isEmpty()) {
         Point metadataPoint = new Point("", metadata, vec);    
       if (cluster.getCenter() == null)
         cluster.setCenter(metadataPoint);
       else
         cluster.addPoint(metadataPoint, true);
       }
     }       
     clustersPerMetadata.add(cluster);
   }
   labeledClusterSet = new ClusterSet(false);
   labeledClusterSet.setDistanceFunction(distance);
   labeledClusterSet.setClusters(clustersPerMetadata);
 }
 
 private void enrichClusters() throws ResourceInitializationException, IOException {
   List<String> fileNames = FileHandler.getFileNames(trainLocation, ".txt");
   //Map <String, List<INDArray>> vectorsPerMetadata = new HashMap<String, List<INDArray>>();
       
   for (String fileName : fileNames) {
     List<String> sentences = new ArrayList<String>(FileHandler.readFromFile(trainLocation + fileName));
     String label = fileName;
     List<INDArray> vectors = new ArrayList<INDArray>();
     for(String sentence: sentences) {
       
       INDArray sentVec = getSentenceVector(sentence);
       if(!sentVec.isEmpty())
         vectors.add(sentVec);
     } 
       if(vectorsPerMetadata.get(label)==null) {
         vectorsPerMetadata.put(label, vectors);
         }
       else {
         vectors.addAll(vectorsPerMetadata.get(label));
         vectorsPerMetadata.put(label, vectors);
       }
     
     }
   List<Cluster> enrichedClusters = new ArrayList<Cluster>();
   for(Cluster c: labeledClusterSet.getClusters()) {
     List<Point> points = new ArrayList<Point>(Point.toPoints(vectorsPerMetadata.get(c.getLabel())));

     for(Point p:points) {
       if(!p.getArray().isEmpty())
         c.addPoint(p, true); // move center
     }
     enrichedClusters.add(c);
   }
   labeledClusterSet.setClusters(enrichedClusters);
 }
}
