package lu.svv.saa.linklaters.privacypolicies.pipelines;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.deeplearning4j.clustering.algorithm.Distance;
import org.uimafit.component.xwriter.CASDumpWriter;

import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import lu.svv.saa.linklaters.privacypolicies.algorithms.Clusterer;
import lu.svv.saa.linklaters.privacypolicies.algorithms.MetadataPredictorV4;
import lu.svv.saa.linklaters.privacypolicies.algorithms.MultiBinaryClassifier;
import lu.svv.saa.linklaters.privacypolicies.analysis.DataPreprocessor;
import lu.svv.saa.linklaters.privacypolicies.analysis.KeywordsContainerVer5;
import lu.svv.saa.linklaters.privacypolicies.analysis.Vectorizer;
import lu.svv.saa.linklaters.privacypolicies.analysis.EmbeddingsHandler;
import lu.svv.saa.linklaters.privacypolicies.io.POIReader;
import lu.svv.saa.linklaters.privacypolicies.utils.FileHandler;
import lu.svv.saa.linklaters.privacypolicies.utils.MSWordHandler;
import lu.svv.saa.linklaters.privacypolicies.utils.PARAMETERS_VALUES;

public class TestingPipeline implements PARAMETERS_VALUES {

  static final boolean INITIALIZE_WITH_KWS = false;
  static final boolean EXTEND_KWS = false;
  // Enriching clusters
  static final boolean ENRICH = false;
  // Distance function for the clustering algorithm
  static final String DISTANCE = Distance.COSINE_SIMILARITY.name();
  static final boolean MAXIMIZE = true;
  static final float DIST_THRESHOLD = (float) 0.037;  
  static final int TOPK = 1;
  static final String CONTROLLER_NAME = "WhatsApp LLC";	//BNP Paribas, Allfunds Bank, Altis Investment Management AG, Artemis Fund Managers Limited, Ashmore, Azimut Holding, Baillie Gifford, Bentham Asset Management Pty Limited, Brevan Howard Liquid Portfolio Strategies, BlueBay, Bank of New York Mellon, Candriam, Raiffeisen KAG, Conventum Asset Management, Cube Infrastructure Managers, DNCA, Degroof Petercam Group, DTZ, Edgewood, Eurobank Private Bank Luxembourg SA, Erste, Fundsquare, GAM, Groupe Bruxelles Lambert SA, General Motors Pensioenfonds, Groupama Asset Management, HSBC Group, HarbourVest, Hermes, Inter Fund Management, ING, Invest Europe AISBL, Bank J. Safra Sarasin Ltd, Lhoist, Lombard Odier, Mesirow Financial, Maggie, J.P. Morgan Asset Management International Limited, Morgan Stanley, NN IP, Prudential, Putnam, Radian, Reckitt Benckiser, Rothschild & Co, StarCapital AG, Stone Harbor ... New examples: Banque de Luxembourg, Societe Generale, TikTok, Unicredit, WhatsApp LLC
  static final String CONTROLLER_REPRESENTATIVE_NAME = "Orlando Amaral Cejas";
  static final boolean CC_DSR_LB = true;
  static final boolean CC_TOE = true;
  static final int CC_PDO = 0;
  static final int CC_PDC = 0;
  static final boolean CC_R = true;
  static final boolean CC_PDTS = true;
  static final int CC_PDPO = 0;
  static final boolean CC_PP = true;
  /*static final boolean CC_PDS = true;
  static final boolean CC_ADM = true;
  static final boolean CC_CH = true;*/
  static final boolean CC_DPO = true;
  static final boolean CC_C = true;
  static final boolean CC_CR= true;
  
  static String input_path = "src/main/resources/4'testing_pipeline/input/", pq01 = "CONTROLLER_NAME", pq02 = "CONTROLLER_REPRESENTATIVE_NAME", pq4 = "Luxembourg", pq6 = "Public";
  static boolean pq1 = false, pq21 = false, pq22 = false, pq23 = false, pq3 = false, pq5 = false, pq51 = false, pq4bool = false;
  
  public static void main(String[] args) throws UIMAException, IOException {
	  
	  List<String> CountryNames = new ArrayList<String>(Arrays.asList("luxembourg","spain","The rest of the European countries"));
	  Map<String, String> arguments = new HashMap<String, String>();
	    int i=0;
	    while(i<args.length) {
	      String arg = args[i].toLowerCase();
	      if(arg.startsWith("-")) {
	        arguments.put(arg, args[i+1]);
	      }
	        i++;
	    }
	    if (args.length > 0) {
	      if(arguments.get("-path")!=null)
	    	  input_path = arguments.get("-path");
	      if(arguments.get("-pq01")!=null)
	    	  pq01 = arguments.get("-pq01");
	      if(arguments.get("-pq02")!=null)
	    	  pq02 = arguments.get("-pq02");
	      if(arguments.get("-pq1")!=null)
	    	  pq1 = Boolean.valueOf(arguments.get("-pq1"));
	      if(arguments.get("-pq21")!=null)
		      pq21 = Boolean.valueOf(arguments.get("-pq21"));
	      if(arguments.get("-pq22")!=null)
		      pq22 = Boolean.valueOf(arguments.get("-pq22"));
	      if(arguments.get("-pq23")!=null)
		      pq23 = Boolean.valueOf(arguments.get("-pq23"));
	      if(arguments.get("-pq3")!=null)
		      pq3 = Boolean.valueOf(arguments.get("-pq3"));
	      if(arguments.get("-pq4")!=null)					//With this country name I have to search in a list of EU countries
	    	  pq4 = arguments.get("-pq4");
	      if(arguments.get("-pq5")!=null)
		      pq5 = Boolean.valueOf(arguments.get("-pq5"));
	      if(arguments.get("-pq51")!=null)
		      pq51 = Boolean.valueOf(arguments.get("-pq51"));
	      if(arguments.get("-pq6")!=null)
	    	  pq6 = arguments.get("-pq6");
	    }
	    
	    if (CountryNames.contains(pq4))
	    	pq4bool = true;
	    else 
	    	pq4bool = false;
	    
	    List<String> fileNames = new ArrayList<String>(FileHandler.getFileNames(input_path, ".docx"));	    
		Collections.sort(fileNames);
		String testDocument = fileNames.get(4);
		System.out.println("currently processing ... " + testDocument);
		MetadataExtraction(testDocument);		   
  }
  
  public static void MetadataExtraction(String documentName) throws UIMAException, IOException {
	    	    
	    // Read from MS-Word
	    //CollectionReaderDescription reader = createReaderDescription(MSWordParser.class, MSWordParser.PARAM_INPUT_PATH,TEST_INPUT + documentName , MSWordParser.PARAM_LICENSE_PATH, LICENSE_PATH);
	    CollectionReaderDescription reader = createReaderDescription(POIReader.class, POIReader.PARAM_INPUT_PATH, TEST_INPUT + documentName);
	    
	    // Create PPTuples
	    AnalysisEngineDescription handler = createEngineDescription(MSWordHandler.class);
	    
	    // Tokenize
	    AnalysisEngineDescription segmenter = createEngineDescription(LanguageToolSegmenter.class);
	    
	    // NER
	    AnalysisEngineDescription ner = createEngineDescription(StanfordNamedEntityRecognizer.class);
	    
	    //Embedding handler
	    AnalysisEngineDescription embeddinghandler = createEngineDescription(EmbeddingsHandler.class, 
	            EmbeddingsHandler.PARAM_EMBEDDINGS_LOCATION, EMBEDDINGS_LOCATION,
	            EmbeddingsHandler.PARAM_INPUT_PATH, DOCUMENT_PATH + documentName);
	    
	    // Preprocessing annotations per sentence: Tokenize, Lemmatize, Remove Stopwords, 
	    // Generalize if requested
	    // create the UoAs
	    AnalysisEngineDescription preprocessor = createEngineDescription(DataPreprocessor.class,
	        DataPreprocessor.PARAM_GENERALIZE, GENERALIZE); 

	    // Lexical Containment 
	    AnalysisEngineDescription container = createEngineDescription(KeywordsContainerVer5.class,
	       KeywordsContainerVer5.PARAM_KWS_LOCATION, KWS_LOCATION,
	       KeywordsContainerVer5.PARAM_NUM_CONTAINED_KWS, NUM_CONTAINED_WORDS); 

	    // Vectorizing using Glove or word2vec
	    AnalysisEngineDescription vectorizer = createEngineDescription(Vectorizer.class, Vectorizer.PARAM_MODEL_LOCATION,
	    		EMBEDDINGS_LOCATION + "optimizedEmbeddings.txt", Vectorizer.PARAM_SENTENCE_OPERATION, OPERATION);

	    // Clustering the UoAs using K-means (semi-supervised, soft/fuzzy) 
	    AnalysisEngineDescription clusterer = createEngineDescription(Clusterer.class,
	        Clusterer.PARAM_MODEL_LOCATION, EMBEDDINGS_LOCATION + "optimizedEmbeddings.txt",
	        Clusterer.PARAM_TRAINING_LOCATION, TRAINING_CLUSTERING,
	        Clusterer.PARAM_SENTENCE_OPERATION, OPERATION,
	        Clusterer.PARAM_INITIALIZE_WITH_KWS, INITIALIZE_WITH_KWS,
	        Clusterer.PARAM_EXTEND_KWS, EXTEND_KWS,
	        Clusterer.PARAM_ENRICH_CLUSTERS, ENRICH,
	        Clusterer.PARAM_DISTANCE_FUNCTION, DISTANCE);
	   
	    // Classifying the UoAs using using multiple binary classifiers (supervised, SVM) 
	    AnalysisEngineDescription classifier = createEngineDescription(MultiBinaryClassifier.class,
	        MultiBinaryClassifier.PARAM_MODEL_LOCATION, TRAINING_CLASSIFICATION);
	        
	    // predictor    
	    AnalysisEngineDescription predictor = createEngineDescription(MetadataPredictorV4.class,
	        MetadataPredictorV4.PARAM_MAXIMIZE, MAXIMIZE,
	        MetadataPredictorV4.PARAM_DISTANCE_THRESHOLD, DIST_THRESHOLD,
	        MetadataPredictorV4.PARAM_CONTROLLER_NAME, pq01,
	        MetadataPredictorV4.PARAM_CONTROLLER_REPRESENTATIVE_NAME, pq02,
	        MetadataPredictorV4.PARAM_CC_DSR_LB, CC_DSR_LB,
	        MetadataPredictorV4.PARAM_CC_TOE, pq1, MetadataPredictorV4.PARAM_CC_PDO, CC_PDO,
	        MetadataPredictorV4.PARAM_CC_PDC, CC_PDC, MetadataPredictorV4.PARAM_CC_R, pq3,
	        MetadataPredictorV4.PARAM_CC_PDTS, CC_PDTS, MetadataPredictorV4.PARAM_CC_PDPO, CC_PDPO,
	        MetadataPredictorV4.PARAM_CC_PP, CC_PP, //MetadataPredictorV4.PARAM_CC_PDS, CC_PDS,
	        //MetadataPredictorV4.PARAM_CC_ADM, CC_ADM, MetadataPredictorV4.PARAM_CC_CH, CC_CH,
	        MetadataPredictorV4.PARAM_CC_DPO, pq21||pq22||pq23, MetadataPredictorV4.PARAM_CC_C, CC_C,
	        MetadataPredictorV4.PARAM_CC_CR, pq4bool);

//	    AnalysisEngineDescription evaluator = createEngineDescription(Evaluator.class, Evaluator.PARAM_GS_LOCATION, GS_LOCATION);

	    // Dump Consumer
	    AnalysisEngineDescription dumpConsumer = createPrimitiveDescription(CASDumpWriter.class);

	    // Pipeline
	    SimplePipeline.runPipeline(reader,segmenter,handler,ner, embeddinghandler, preprocessor,container,vectorizer,clusterer,classifier,predictor,dumpConsumer);	//evaluator,
	  
  }  
  
}