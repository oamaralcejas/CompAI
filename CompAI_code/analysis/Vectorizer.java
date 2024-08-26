package lu.svv.saa.linklaters.privacypolicies.analysis;

import static org.apache.uima.fit.util.JCasUtil.select;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import lu.svv.saa.linklaters.privacypolicies.type.SentenceOperation;
import lu.svv.saa.linklaters.privacypolicies.type.SentenceVector;
import lu.svv.saa.linklaters.privacypolicies.type.UoA;


public class Vectorizer extends JCasAnnotator_ImplBase {

  public static final String PARAM_MODEL_LOCATION = "modelLocation";
  @ConfigurationParameter(name = PARAM_MODEL_LOCATION, mandatory = true,
      description = "where are the training models",
      defaultValue = "src/main/resources/vector_models/glove.6B.100d.txt")
  private String modelLocation;

  public static final String PARAM_SENTENCE_OPERATION = "operation";
  @ConfigurationParameter(name = PARAM_SENTENCE_OPERATION, mandatory = true,
      description = "how to combine word vectors: average or add", defaultValue = "AVERAGE")
  private SentenceOperation operation;

  private WordVectors vectorModel;
  private String reason;
  private boolean transformedPreprocessed;

  @SuppressWarnings("deprecation")
  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    reason = "";
    transformedPreprocessed = true;    
    // load the pre-trained model
    File model = new File(modelLocation);
    
    // Glove models
    if (modelLocation.contains("glove"))
      try {
        vectorModel = WordVectorSerializer.loadTxtVectors(model);
      } catch (IOException e) {
        e.printStackTrace();
      }
    // Word2Vec models
    else
      vectorModel = WordVectorSerializer.readWord2VecModel(model);
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // Loop over the UoAs and create a vector for the preprocessed sentence
    // or generalized sentece is preprocessed is empty or contains words not in Vocab
    for (UoA uoa : select(aJCas, UoA.class)) {
      String vector = "";
      int dim = 0;

      try {
        INDArray sentenceVector = getSentenceVector(uoa);        
        if(!sentenceVector.isEmpty()) {
          vector = toString(sentenceVector);
          dim = sentenceVector.columns();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      newSentenceVector(aJCas, uoa.getBegin(), uoa.getEnd(), vector, dim);
    }
  }

  private void newSentenceVector(JCas jcas, int begin, int end, String vector, int dim) {
    SentenceVector sentenceVector = new SentenceVector(jcas);

    sentenceVector.setBegin(begin);
    sentenceVector.setEnd(end);
    sentenceVector.setVector(vector);
    sentenceVector.setVectorDimension(dim);
    sentenceVector.setTransformPreporcessed(transformedPreprocessed);
    sentenceVector.setReasonNotTransformed(reason);
    
    sentenceVector.addToIndexes();
  }
  
  private String toString(INDArray sentenceVector) {
    String vector = sentenceVector.getRow(0).toString();
    return vector.replace("[", "").replace("]", "");
  }

  private INDArray getSentenceVector(UoA uoa) throws IOException {
    String sentence = uoa.getPreprocessed().toLowerCase();
    if(sentence.isEmpty()) {
      reason = "IsEmpty";
      transformedPreprocessed = false;
    }
    INDArray wordsVectors = vectorModel.getWordVectors(new ArrayList<String>(Arrays.asList(sentence.split(" "))));

    if (wordsVectors.isEmpty()) {
      sentence = uoa.getGeneralized().toLowerCase();
      wordsVectors = vectorModel.getWordVectors(new ArrayList<String>(Arrays.asList(sentence.split(" "))));
      reason = "NotInVocab";
      transformedPreprocessed = false;
    }
    if (wordsVectors.isEmpty()) {
      return wordsVectors;
    }
    // average: default
    INDArray sentenceVector = wordsVectors.mean(0);
    // add
    if (operation.equals(SentenceOperation.ADD))
      sentenceVector = wordsVectors.sum(0);

    /*else if (operation.equals(SentenceOperation.CONCAT)) {
      // concat
      sentenceVector = wordsVectors.getRow(0);
      for (int i = 1; i < wordsVectors.rows(); ++i) {
        INDArray append = wordsVectors.getRow(i);
        sentenceVector = Nd4j.concat(0, sentenceVector, append);
      }
    }*/
    return sentenceVector;
  }

}
