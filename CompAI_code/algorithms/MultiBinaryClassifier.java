package lu.svv.saa.linklaters.privacypolicies.algorithms;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;
import java.util.ArrayList;
import java.util.List;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair;
import lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult;
import lu.svv.saa.linklaters.privacypolicies.type.SentenceVector;
import lu.svv.saa.linklaters.privacypolicies.type.UoA;
import lu.svv.saa.linklaters.privacypolicies.utils.FileHandler;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MultiBinaryClassifier extends JCasAnnotator_ImplBase {

  public static final String PARAM_MODEL_LOCATION = "modelLocation";
  @ConfigurationParameter(name = PARAM_MODEL_LOCATION, mandatory = true,
      description = "the location of the pretrained classifiers",
      defaultValue = "src/main/resources/")
  private String modelLocation;

  List<Classifier> pretrainedModels;
  List<String> modelNames;

  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    pretrainedModels = new ArrayList<Classifier>();
    // load pretrained model(s) for binary classifiers, or train new ones
    modelNames = FileHandler.getFileNames(modelLocation, ".model");
    for (String modelName : modelNames) {
      try {
        pretrainedModels.add((Classifier) weka.core.SerializationHelper.read(modelLocation + modelName));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // loop over uoa
    // get vector
    // transform into instance
    // loop over pre-trained models
    // predict distribution
    // Save in a classificationResult

    for (UoA uoa : select(aJCas, UoA.class)) {
      FSArray probOfClasses = new FSArray(aJCas, modelNames.size());
      // Get the vector of each sentence
      for (SentenceVector sv : selectCovered(SentenceVector.class, uoa)) {
        for (int model = 0; model < pretrainedModels.size(); ++model) {
          // Get the corresponding dataset
          DataSource source;
          try {
            source = new DataSource(modelLocation+modelNames.get(model).replace("model", "arff"));
            Instances dataset = source.getDataSet();
            if (dataset.classIndex() == -1) {
              dataset.setClassIndex(dataset.numAttributes() - 1);
            }
            // Transform the vector into a test instance
            Instance inst = new DenseInstance(sv.getVectorDimension() + 2);
            inst.setDataset(dataset);
            inst.setValue(0, "\"" + sv.getCoveredText() + "\"");
            String[] vector = sv.getVector().split(",");
            for (int i = 1; i < sv.getVectorDimension() + 1; ++i) {
              inst.setValue(i, (double) Double.parseDouble(vector[i - 1].trim()));
            }
            inst.setClassValue(modelNames.get(model).replaceAll(".model", ""));
            
            /*Filter filter  = new StringToWordVector();
            filter.setInputFormat(dataset);
            filter.input(inst);
            
            Instances filteredInst = filter.getOutputFormat();
            if(filter.output() != null)
                filteredInst.add(filter.output());
            else
              filteredInst.add(inst);*/
            
            // get predicted distributions
            double[] prediction = pretrainedModels.get(model).distributionForInstance(inst);
            String label = "";
            double prob = -1.0;
            for (int k = 0; k < prediction.length; ++k) {
              if (prediction[k] > prob) {
                prob = prediction[k];
                label = dataset.classAttribute().value(k);
              }
            }
            // Create a <label, probability> pair for this uoa
            ClassProbabilityPair cp =
                newClassProbabilityPair(aJCas, uoa.getBegin(), uoa.getEnd(), label, prob);
            /*if(label.equalsIgnoreCase("DATA_SUBJECT_RIGHT")) {
            System.out.println(uoa.getCoveredText());
            System.out.println(inst);
            System.out.println(label + ":" + prob);
            System.in.read();
            }*/
            probOfClasses.set(model, cp);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }        
        newClassificationResult(aJCas, uoa.getBegin(), uoa.getEnd(), probOfClasses);
      }
    }
  }

  private ClassProbabilityPair newClassProbabilityPair(JCas aJCas, int begin, int end, String label,
      double prob) {
    ClassProbabilityPair cp = new ClassProbabilityPair(aJCas);

    cp.setBegin(begin);
    cp.setEnd(end);
    cp.setClassLabel(label);
    cp.setProbability(prob);

    return cp;
  }
  

  private void newClassificationResult(JCas aJCas, int begin, int end, FSArray probOfClasses) {
    ClassificationResult result = new ClassificationResult(aJCas);
    
    result.setBegin(begin);
    result.setEnd(end);
    result.setProbabilitiesOfClasses(probOfClasses);
    result.addToIndexes();

  }


}
