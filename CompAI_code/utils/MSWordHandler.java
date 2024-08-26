package lu.svv.saa.linklaters.privacypolicies.utils;

import static org.apache.uima.fit.util.JCasUtil.select;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import lu.svv.saa.linklaters.privacypolicies.type.PPTuple;

public class MSWordHandler extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    for (Sentence sent : select(aJCas, Sentence.class)) 
    {
      //System.out.println(sent.getCoveredText());
      newPPTuple(aJCas, sent.getBegin(), sent.getEnd(), 
          sent.getCoveredText().replace("\t", " ").replaceAll("\\s+", " "));//.replaceAll("\\s+", "\\s"));
    }
    
  }
  private void newPPTuple(JCas jCas, int begin, int end, String text) {
    PPTuple tuple = new PPTuple(jCas);
    //System.out.println(text);
    tuple.setBegin(begin);
    tuple.setEnd(end);
    tuple.setText(text);
    tuple.setMetadata1("?");
    tuple.setMetadata2("?");
    tuple.setMetadata3("?");
    tuple.setMetadata4("?");
    tuple.setKeywords("-");
    tuple.setValue("?");

    tuple.addToIndexes();
  }

}
