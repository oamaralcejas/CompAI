package lu.svv.saa.linklaters.privacypolicies.analysis;

import static org.apache.uima.fit.util.JCasUtil.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math3.stat.Frequency;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;

import lu.svv.saa.linklaters.privacypolicies.type.UoA;
import lu.svv.saa.linklaters.privacypolicies.utils.FileHandler;
import lu.svv.saa.linklaters.privacypolicies.utils.PARAMETERS_VALUES;

public class KeywordsContainerVer5 extends JCasAnnotator_ImplBase implements PARAMETERS_VALUES{

  // Get the location of keywords
  public static final String PARAM_KWS_LOCATION = "kwsLocation";
  @ConfigurationParameter(name = PARAM_KWS_LOCATION, mandatory = true,
      description = "the location of the keywords files",
      defaultValue = "src/main/resources/keywords/")
  private String kwsLocation;

  public static final String PARAM_NUM_CONTAINED_KWS = "numContainedKWs";
  @ConfigurationParameter(name = PARAM_NUM_CONTAINED_KWS, mandatory = true,
      description = "the location of the keywords files", defaultValue = "1")
  private int numContainedKWs;

  private Map<String, List<String>> metadataKWs;
  private boolean maintainOrder = false;
  StringBuilder log;

  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);

    List<String> fileNames = FileHandler.getFileNames(kwsLocation, "");
    metadataKWs = new HashMap<String, List<String>>();
    for (String fileName : fileNames) {
      List<String> keywords = FileHandler.readFromFile(kwsLocation + fileName);
      metadataKWs.put(fileName, keywords);
    }
    log = new StringBuilder();
    
  }


  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    //List<String> generic = FileHandler.readFromFile(METADATA_MODEL + "L1");
    log.append(String.format("\t\t%s%n%n", "KEYWORDS CONTAINMENT"));
    for (UoA uoa : select(aJCas, UoA.class)) {
      log.append(String.format("Sent: %s%n", uoa.getCoveredText()));

      Set<String> containedMD = new TreeSet<String>();
      Frequency frequencyDistributionOfMD = new Frequency();
      for (Entry<String, List<String>> kws : metadataKWs.entrySet()) {
        for(String kw: kws.getValue()) {
        List<String> splittedKWs = new ArrayList<String>(Arrays.asList(kw.split(" ")));
        if(containedInSentence(uoa.getCoveredText(), splittedKWs)) {
          containedMD.add(kws.getKey());
          System.out.println(splittedKWs+" from "+kws.getKey());
          log.append(String.format("Got %s from metadata <%s>%n", splittedKWs, kws.getKey()));
          frequencyDistributionOfMD.addValue(kws.getKey());
        }
        }        
      }
      
    String containment = "";
    ListIterator<Comparable<?>> mode = frequencyDistributionOfMD.getMode().listIterator();
    while(mode.hasNext()) {
      String md = mode.next().toString();
      containment = containment + md + ";";
      containedMD.remove(md);
    }
    for(String remaining: containedMD) {
      if(!frequencyDistributionOfMD.getMode().isEmpty() 
          && relevant(remaining, frequencyDistributionOfMD.getMode()))
        containment = containment + remaining + ";";
    }
     uoa.setContainedKWs(containment); 
     System.out.println(uoa);
     System.out.println(frequencyDistributionOfMD);
     System.out.println(frequencyDistributionOfMD.getMode());
     
     log.append(String.format("%nFreqTable: %n%s%n", frequencyDistributionOfMD));

     /*if(uoa.getCoveredText().contains("within the framework"))
       try { System.in.read(); } catch (IOException e) { 
         e.printStackTrace(); }*/
    }
    
    /*try { System.in.read(); } catch (IOException e) { 
      e.printStackTrace(); }*/
    //DocumentMetaData dmd = DocumentMetaData.get(aJCas);
    //String doc = dmd.getDocumentTitle();
    //FileHandler.writeToFile("src/main/resources/4'testing_pipeline/"+doc+".log", log.toString());
  }

  private boolean relevant(String remaining, List<Comparable<?>> mode) {
    Map<String, String> index = FileHandler.readIndex(METADATA_MODEL+"index");
    String relevantMD = index.get(remaining);
    System.out.println(relevantMD + " : " + remaining);
    ListIterator<Comparable<?>> modeIterator = mode.listIterator();
    while(modeIterator.hasNext()) {
      String mfMD = modeIterator.next().toString();
      String parent = index.get(mfMD).split(";")[3];
      if(relevantMD.contains(mfMD) || relevantMD.contains(parent))
        return true;
    }
    return false;
  }


  private boolean containedInSentence(String sent,
      List<String> KWs) {
    int i=0;
    for(String kw:KWs) {
      if(sent.toLowerCase().contains(kw)) {
        i++;
        if(maintainOrder)
          sent = sent.substring(sent.indexOf(kw)+kw.length());
      }
    }
    return i==KWs.size();
  }
}
