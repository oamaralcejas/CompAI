package lu.svv.saa.linklaters.privacypolicies.analysis;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;
import java.util.List;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import lu.svv.saa.linklaters.privacypolicies.type.PPTuple;
import lu.svv.saa.linklaters.privacypolicies.type.UoA;
import lu.svv.saa.linklaters.privacypolicies.utils.EnglishStopLemmatizer;
import lu.svv.saa.linklaters.privacypolicies.utils.PatternRecognizer;
/**
 * Preprocessing the sentences, generalizing the NEs, phones, emails and websires 
 * to their values, removing stopwords, and creating the UoAs objects.
 * 
 * Input: PPTuple
 * Output: UoA
 * 
 * @author <a href="mailto:abualhaija@svv.lu">Sallam Abualhaija</a>
 */

public class DataPreprocessor extends JCasAnnotator_ImplBase {

  public static final String PARAM_GENERALIZE = "generalize";
  @ConfigurationParameter(name = PARAM_GENERALIZE, mandatory = true,
      description = "whether to retrieve the keywords from the annotated files or not",
      defaultValue = "true")
  private boolean generalize;

  private PatternRecognizer pr;
  private EnglishStopLemmatizer esl;

  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    pr = new PatternRecognizer();
    esl = new EnglishStopLemmatizer();
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // loop over sentences
    // ignore sentences with no words
    // replace the named entities by their type
    // get lemmas, with no stopwords

    for (PPTuple tuple : select(aJCas, PPTuple.class)) {
      String sentText = tuple.getText();
      if (numTokens(sentText) < 2)
        continue;
      boolean hasNE = false;
      for (NamedEntity ne : selectCovered(NamedEntity.class, tuple)) {
        //System.out.println(ne.getCoveredText()+ "_" + ne.getValue());
        if(ne.getCoveredText().length()>1 && 
            (ne.getValue().equalsIgnoreCase("ORGANIZATION")||ne.getValue().equalsIgnoreCase("LOCATION")) &&
            (!ne.getCoveredText().equalsIgnoreCase("general data protection regulation") || ne.getCoveredText().equalsIgnoreCase("gdpr"))) {
          hasNE = selectCovered(NamedEntity.class, tuple).size()>0;
          sentText = sentText.replace(ne.getCoveredText(), "_" + ne.getValue());
          }
      }

      if (generalize)
        sentText = pr.generalize(sentText);
      StringBuilder preprocessedSent = new StringBuilder();
      // Lemmatize and generalize -lrb- -rrb-
      List<String> lemmas = esl.tokenize(sentText);
      for (String l : lemmas) {
        if (!(l.equals("-lrb-") || l.equalsIgnoreCase("-rrb-")) && l.length() > 1) {
          String str = l;
          if (str.startsWith("_"))
            str = str.replace("_", "").toUpperCase();
          preprocessedSent.append(str + " ");
        }
      }
      
      int begin = tuple.getBegin();
      if(tuple.getCoveredText().startsWith("\""))
        begin++;
      newUoV(aJCas, begin, begin + tuple.getText().length() , sentText, preprocessedSent.toString(),
          hasNE);
    }
  }

  private void newUoV(JCas jcas, int begin, int end, String generalSent, String preprocessedSent, boolean hasNE) {
    UoA unitOfAnalysis = new UoA(jcas);

    unitOfAnalysis.setBegin(begin);
    unitOfAnalysis.setEnd(end);

    unitOfAnalysis.setPreprocessed(preprocessedSent.trim());
    unitOfAnalysis.setGeneralized(generalSent.trim());
    unitOfAnalysis.setHasNE(hasNE);
    unitOfAnalysis.setHasPHONE(generalSent.contains("PHONE"));
    unitOfAnalysis.setHasEMAIL(generalSent.contains("EMAIL"));
    unitOfAnalysis.setHasWEBSITE(generalSent.contains("WEBSITE"));
    unitOfAnalysis.setHasADDRESS(generalSent.contains("ADDRESS"));
    
    /*System.out.println(generalSent);
    try {
      System.in.read();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }*/
    unitOfAnalysis.addToIndexes();
  }

  private int numTokens(String sentence) {
    sentence = sentence.trim();
    return sentence.split(" ").length;
  }

}
