package lu.svv.saa.linklaters.privacypolicies.utils;

import java.util.Arrays;
import java.util.List;
import lu.svv.saa.linklaters.privacypolicies.type.SentenceOperation;

public interface PARAMETERS_VALUES {

  static final String PATH = "src/main/resources/";
  static final String DOCUMENT_PATH = "src/main/resources/input/";
  
  static final String ANNOTATION_INPUT = "src/main/resources/1annotation_pipeline/input_docx/";
  static final String LICENSE_PATH = "src/main/resources/license";
  static final String ANNOTATION_OUTPUT = "src/main/resources/1annotation_pipeline/output_csv/";
  static final String KWS_PATH = "src/main/resources/keywords/";
  static final String CURATION_PATH = "src/main/resources/2curation_pipeline/";
  static final String CURATION_INPUT = CURATION_PATH + "input/";
  static final String CURATION_TESTSET = CURATION_PATH + "test_set/";
  
  static final String TRAINING_PATH = "src/main/resources/3'training_pipeline/";

  //Keywords location for lexical containment
  static final String KWS_LOCATION = "src/main/resources/keywords/";
  static final int NUM_CONTAINED_WORDS = 1;
  
  static final String EMBEDDINGS_LOCATION = "src/main/resources/vector_models/";

  static final String TRAINING_CLASSIFICATION = TRAINING_PATH + "classification/";
  static final String TRAINING_CLUSTERING = TRAINING_PATH + "clustering/";
    
  static final String TEST_INPUT = "src/main/resources/4'testing_pipeline/input/";
  static final String TEST_OUTPUT = "src/main/resources/4'testing_pipeline/output/";
  
  static final String METADATA_MODEL = "src/main/resources/metadata_model/";
  
  // PARAMTERS 
  // To generalize the sentence by replacing NEs, phones, emails & websites
  static final boolean GENERALIZE = true;
  static final String STOPWORDS_PATH = "classpath:/stopwords/stoplist_shorter_en.txt";
   
  // Path to the pretrained embeddings models
  // starting with glove pre-trained
  static final List<String> GLOVE_VAR =
     Arrays.asList("glove.6B.100d.txt", "glove.42B.300d.txt", "glove.840B.300d.txt");
  static final String EMBEDDINGS_MODEL_LOCATION =
      "src/main/resources/vector_models/" + GLOVE_VAR.get(0);
  
  // How to combine the word embeddings of a sentence 
  static final String OPERATION = SentenceOperation.AVERAGE.name();
  
  // Evaluation
  static final String GS_LOCATION = "src/main/resources/4'testing_pipeline/goldstandard/";

}
