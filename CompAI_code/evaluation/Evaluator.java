package lu.svv.saa.linklaters.privacypolicies.evaluation;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasConsumer_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import lu.svv.saa.linklaters.privacypolicies.type.PPTuple;
import lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata;
import lu.svv.saa.linklaters.privacypolicies.utils.FileHandler;
import lu.svv.saa.linklaters.privacypolicies.utils.PARAMETERS_VALUES;

public class Evaluator extends JCasConsumer_ImplBase implements PARAMETERS_VALUES{

  public static final String PARAM_GS_LOCATION = "gsLocation";
  @ConfigurationParameter(name = PARAM_GS_LOCATION, mandatory = true,
      description = "the location of the gold standard files",
      defaultValue = "src/main/resources/4'testing_pipeline/goldstandard/")
  private String gsLocation;
  
  Map<String, List<String>> goldstandard;
  Map<String, List<String>> predictions;
  Map<String, Integer> TPs;
  Map<String, Integer> totalActual;
  Map<String, Integer> totalPredicted;
  
  Map<String, Set<String>> actualTPs;
  Map<String, Set<String>> FPs;
  Map<String, Set<String>> FNs;
  /*Map<String, List<String>> FPsM2;
  Map<String, List<String>> FNsM2;
  Map<String, List<String>> FPsM3;
  Map<String, List<String>> FNsM3;*/
  
  final static int SENT_INDEX = 0;
  final static int M1_INDEX = 1;
  final static int M2_INDEX = 2;
  final static int M3_INDEX = 3;
  final static int M4_INDEX = 4;
  final static int KW_INDEX = 5;
  final static int V_INDEX = 6;

  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    goldstandard = new HashMap<String, List<String>>();
    predictions = new HashMap<String, List<String>>();
    TPs = new HashMap<String, Integer>();
    FPs = new HashMap<String, Set<String>>();
    FNs = new HashMap<String, Set<String>>();
    actualTPs = new HashMap<String, Set<String>>();

    totalActual = new HashMap<String, Integer>();
    totalPredicted = new HashMap<String, Integer>();
  }
  
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // read from GS
    String documentTitle = DocumentMetaData.get(aJCas).getDocumentTitle();   
    try {
      readGS(documentTitle);
      getPredictions(aJCas);
      evaluate(documentTitle);
          } catch (Exception e) {      
      e.printStackTrace();
    }
    
    System.out.println("DONE");
    try {
      System.in.read();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
 
  }

  private void evaluate(String documentTitle) throws Exception {
    // loop over sentences in predictions or goldstandard
    List<String> FPsM1 = new ArrayList<String>();
    List<String> FNsM1 = new ArrayList<String>();
    List<String> FPsM2 = new ArrayList<String>();
    List<String> FNsM2 = new ArrayList<String>();
    List<String> FPsM3 = new ArrayList<String>();
    List<String> FNsM3 = new ArrayList<String>();
    List<String> FPsM4 = new ArrayList<String>();
    List<String> FNsM4 = new ArrayList<String>();
    
    for(Entry<String, List<String>> entry : goldstandard.entrySet()) {
      // for each sentence get the List of annotations
      String sentence = entry.getKey().trim();
      List<String> actualLabels = entry.getValue();
      List<String> predictedLabels = predictions.get(sentence.trim());
      if(predictedLabels==null) {
        System.out.println("null: "+ sentence);
        //System.in.read();
        //throw new Exception("The sent: " + sentence + " is not identical in goldstandard and predictions");
      }
      Set<String> m1Actual = new TreeSet<String>();
      Set<String> m1Predicted = new TreeSet<String>();
      Set<String> m2Actual = new TreeSet<String>();
      Set<String> m2Predicted = new TreeSet<String>();
      Set<String> m3Actual = new TreeSet<String>();
      Set<String> m3Predicted = new TreeSet<String>();
      Set<String> m4Actual = new TreeSet<String>();
      Set<String> m4Predicted = new TreeSet<String>();
 
      for(String al: actualLabels) {
        if(!al.contains(";")) {
          m1Actual.add(al);
        }
        else {
          m1Actual.add(al.split(";")[M1_INDEX-1]);
        }
        if(al.contains(";") && al.split(";").length>=2) {
          String m2 = al.split(";")[M2_INDEX-1];
          m2Actual.add(m2);
        }
        if(al.contains(";") && al.split(";").length>=3) {
          String m3 = al.split(";")[M3_INDEX-1];
          m3Actual.add(m3);
        }
        if(al.contains(";") && al.split(";").length>=4) {
          String m4 = al.split(";")[M4_INDEX-1];
          m4Actual.add(m4);
        }
      }
      if(predictedLabels!=null) {
      for(String pl: predictedLabels) {
        if(!pl.contains(";")) {
          m1Predicted.add(pl);
        }
        else {
          m1Predicted.add(pl.split(";")[M1_INDEX-1]);
        }
        if(pl.contains(";") && pl.split(";").length>=2) {
          String m2 = pl.split(";")[M2_INDEX-1];
          m2Predicted.add(m2);
        }
        if(pl.contains(";") && pl.split(";").length>=3) {
          String m3 = pl.split(";")[M3_INDEX-1];
          m3Predicted.add(m3);
        }
        if(pl.contains(";") && pl.split(";").length>=4) {
          String m4 = pl.split(";")[M4_INDEX-1];
          m4Predicted.add(m4);
        }
      }
    }
      
      FPsM1 = new ArrayList<String>(m1Predicted);
      FNsM1 = new ArrayList<String>(m1Actual);
      // TPs: Get intersection
      List<String> intersection = new ArrayList<String>(m1Actual);
      intersection.retainAll(m1Predicted);
      for(String tp: intersection) {
        if(actualTPs.get(tp)==null) {
          Set<String> tpTemp = new TreeSet<String>();
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
        else {
          Set<String> tpTemp = new TreeSet<String>(actualTPs.get(tp));
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
      }
      FPsM1.removeAll(intersection);
      for(String fp: FPsM1) {
        if(FPs.get(fp)==null) {
          Set<String> fpTemp = new TreeSet<String>();
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
        else {
          Set<String> fpTemp = new TreeSet<String>(FPs.get(fp));
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
      }
      FNsM1.removeAll(intersection);
      for(String fn: FNsM1) {
        if(FNs.get(fn)==null) {
          Set<String> fnTemp = new TreeSet<String>();
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
        else {
          Set<String> fnTemp = new TreeSet<String>(FNs.get(fn));
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
      }
      
      for(String tp: intersection) {
        if(TPs.get(tp)==null) {
          TPs.put(tp, 1);
        }
        else {
          TPs.put(tp, TPs.get(tp)+1);
        }
      }
      
      FPsM2 = new ArrayList<String>(m2Predicted);
      FNsM2 = new ArrayList<String>(m2Actual);
      intersection = new ArrayList<String>(m2Actual);
      intersection.retainAll(m2Predicted);
      for(String tp: intersection) {
        if(actualTPs.get(tp)==null) {
          Set<String> tpTemp = new TreeSet<String>();
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
        else {
          Set<String> tpTemp = new TreeSet<String>(actualTPs.get(tp));
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
      }
      
      FPsM2.removeAll(intersection);
      for(String fp: FPsM2) {
        if(FPs.get(fp)==null) {
          Set<String> fpTemp = new TreeSet<String>();
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
        else {
          Set<String> fpTemp = new TreeSet<String>(FPs.get(fp));
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
      }
      FNsM2.removeAll(intersection);
      for(String fn: FNsM2) {
        if(FNs.get(fn)==null) {
          Set<String> fnTemp = new TreeSet<String>();
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
        else {
          Set<String> fnTemp = new TreeSet<String>(FNs.get(fn));
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
      }
      for(String tp: intersection) {
        if(TPs.get(tp)==null) {
          TPs.put(tp, 1);
        }
        else {
          TPs.put(tp, TPs.get(tp)+1);
        }
      }
      
      FPsM3 = new ArrayList<String>(m3Predicted);
      FNsM3 = new ArrayList<String>(m3Actual);
      
      intersection = new ArrayList<String>(m3Actual);
      intersection.retainAll(m3Predicted);
      FPsM3.removeAll(intersection);
      for(String fp: FPsM3) {
        if(FPs.get(fp)==null) {
          Set<String> fpTemp = new TreeSet<String>();
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
        else {
          Set<String> fpTemp = new TreeSet<String>(FPs.get(fp));
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
      }
      FNsM3.removeAll(intersection);
      for(String fn: FNsM3) {
        if(FNs.get(fn)==null) {
          Set<String> fnTemp = new TreeSet<String>();
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
        else {
          Set<String> fnTemp = new TreeSet<String>(FNs.get(fn));
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
      }
      for(String tp: intersection) {
        if(actualTPs.get(tp)==null) {
          Set<String> tpTemp = new TreeSet<String>();
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
        else {
          Set<String> tpTemp = new TreeSet<String>(actualTPs.get(tp));
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
      }
      for(String tp: intersection) {
        if(TPs.get(tp)==null) {
          TPs.put(tp, 1);
        }
        else {
          TPs.put(tp, TPs.get(tp)+1);
        }
      }
      
      FPsM4 = new ArrayList<String>(m4Predicted);
      FNsM4 = new ArrayList<String>(m4Actual);
          
      intersection = new ArrayList<String>(m4Actual);
      intersection.retainAll(m4Predicted);
      for(String tp: intersection) {
        if(actualTPs.get(tp)==null) {
          Set<String> tpTemp = new TreeSet<String>();
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
        else {
          Set<String> tpTemp = new TreeSet<String>(actualTPs.get(tp));
          tpTemp.add(sentence);
          actualTPs.put(tp, tpTemp);
        }
      }
      FPsM4.removeAll(intersection);
      for(String fp: FPsM4) {
        if(FPs.get(fp)==null) {
          Set<String> fpTemp = new TreeSet<String>();
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
        else {
          Set<String> fpTemp = new TreeSet<String>(FPs.get(fp));
          fpTemp.add(sentence);
          FPs.put(fp, fpTemp);
        }
      }
      FNsM4.removeAll(intersection);
      for(String fn: FNsM4) {
        if(FNs.get(fn)==null) {
          Set<String> fnTemp = new TreeSet<String>();
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
        else {
          Set<String> fnTemp = new TreeSet<String>(FNs.get(fn));
          fnTemp.add(sentence);
          FNs.put(fn, fnTemp);
        }
      }
      for(String tp: intersection) {
        if(TPs.get(tp)==null) {
          TPs.put(tp, 1);
        }
        else {
          TPs.put(tp, TPs.get(tp)+1);
        }
      }

      // FPs: what is left out in the predictions
      for(String pl: m1Predicted) {
        if(totalPredicted.get(pl)==null) {
          totalPredicted.put(pl, 1);
        }
        else {
          totalPredicted.put(pl, totalPredicted.get(pl)+1);
        }
      }
      for(String pl: m2Predicted) {
        if(totalPredicted.get(pl)==null) {
          totalPredicted.put(pl, 1);
        }
        else {
          totalPredicted.put(pl, totalPredicted.get(pl)+1);
        }
      }
      for(String pl: m3Predicted) {
        if(totalPredicted.get(pl)==null) {
          totalPredicted.put(pl, 1);
        }
        else {
          totalPredicted.put(pl, totalPredicted.get(pl)+1);
        }
      }
      for(String pl: m4Predicted) {
        if(totalPredicted.get(pl)==null) {
          totalPredicted.put(pl, 1);
        }
        else {
          totalPredicted.put(pl, totalPredicted.get(pl)+1);
        }
      }
      // FNs: what is left out in the actual
      for(String al: m1Actual) {
        if(totalActual.get(al)==null) {
          totalActual.put(al, 1);
        }
        else {
          totalActual.put(al, totalActual.get(al)+1);
        }
      }
      for(String al: m2Actual) {
        if(totalActual.get(al)==null) {
          totalActual.put(al, 1);
        }
        else {
          totalActual.put(al, totalActual.get(al)+1);
        }
      }
      for(String al: m3Actual) {
        if(totalActual.get(al)==null) {
          totalActual.put(al, 1);
        }
        else {
          totalActual.put(al, totalActual.get(al)+1);
        }
      }
      for(String al: m4Actual) {
        if(totalActual.get(al)==null) {
          totalActual.put(al, 1);
        }
        else {
          totalActual.put(al, totalActual.get(al)+1);
        }
      }
      } 

    StringBuilder results = new StringBuilder();
    List<String> evaluated = FileHandler.readFromFile(METADATA_MODEL + "evaluated");
    
//--------------------------------------------------------------------------------------------------------------
//Per instance information
//--------------------------------------------------------------------------------------------------------------
      results.append("Per instance analysis\n");
      results.append("Metadata\tTP\tFP\tFN\n");
      for(String metadata:evaluated) {
          int numAnnotated = (totalActual.get(metadata)==null?0:totalActual.get(metadata));
          int numPredicted = (totalPredicted.get(metadata)==null?0:totalPredicted.get(metadata));
          int tp = (TPs.get(metadata)==null?0:TPs.get(metadata));
          int fp = 0, fn = 0, tpcc = 0, fpcc = 0, fncc = 0;								//Auxiliary variables

          fp = numPredicted-tp;								//Calculating FPs
          fn = numAnnotated-tp;								//Calculating FNs 
          
          if(tp!=0) {
          	tpcc=1;
          	fpcc=0;
          	fncc=0;
          }
          
          else {
          	tpcc=0;
          	if(fp!=0)
          		fpcc=1;
          	if(fn!=0)
          		fncc=1;
          }
          
          results.append(metadata + "\t" + tpcc + "\t" + fpcc + "\t" + fncc);	//Adding the information
          results.append("\n");
        } 
//--------------------------------------------------------------------------------------------------------------
//Per instance information
//--------------------------------------------------------------------------------------------------------------     
    results.append("\n"); 
    results.append("Metadata\tTP\tFP\tFN\tGS\tPrec\tRecall\n");
    for(String metadata:evaluated) {
      int numAnnotated = (totalActual.get(metadata)==null?0:totalActual.get(metadata));
      int numPredicted = (totalPredicted.get(metadata)==null?0:totalPredicted.get(metadata));
      int tp = (TPs.get(metadata)==null?0:TPs.get(metadata));
      int fp = 0, fn = 0;								//Auxiliary variables
      float p = 0;
      float r = 0;
      if(numAnnotated != 0) {
        r = (float) tp/numAnnotated;
        }
      if(numPredicted != 0) {
        p = (float) tp/numPredicted;
        }
      fp = numPredicted-tp;								//Calculating FPs
      fn = numAnnotated-tp;								//Calculating FNs
      results.append(metadata + "\t" + tp + "\t" + fp + "\t" + fn + "\t" + numAnnotated + "\t" + p + "\t" + r);	//Adding the information
      results.append("\n");
    }
    
    for(String metadata: evaluated) {
      results.append("\n True Positives (" + metadata + ")\n");
      if(actualTPs.get(metadata)==null) {
        results.append("\tNo TPs\n");
      }else {
        for(String tp:actualTPs.get(metadata)) {
          results.append(" " + tp + "\n");
        }
      }
      results.append("\n False Positives (" + metadata + ")\n");
      if(FPs.get(metadata)==null) {
        results.append("\tNo FPs\n");
      }else {
        for(String fp:FPs.get(metadata)) {
          results.append(" " + fp + "\n");
        }
      }
      results.append("\n False Negatives (" + metadata + ")\n");
      if(FNs.get(metadata)==null) {
        results.append("\tNo FNs\n");
      } else {
        for(String fn:FNs.get(metadata)) {
          results.append(" " + fn + "\n");
        }
      }
      results.append("-------------------------------------------------------\n");
   }
   
   FileHandler.writeToFile(TEST_OUTPUT+documentTitle, results.toString());
   System.out.println(documentTitle);
   System.out.println(results);
    
    //System.in.read();        
  }

  @SuppressWarnings("resource")
  private void readGS(String doc) throws Exception {
    File file = new File(gsLocation + doc + ".txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF16"));
    String line;
    while ((line = br.readLine()) != null) {
      //String[] fields = line.split("\t");
      List<String> fields = getCells(line);
      if(fields.size()!=7)
        throw new Exception("Error parsing the following sentence: \"" + line + "\"");
      String sentence = fields.get(SENT_INDEX);
            
      String annotation = fields.get(M1_INDEX);
      if(!fields.get(M2_INDEX).equalsIgnoreCase("x"))
        annotation = annotation + ";" + fields.get(M2_INDEX);
      if(!fields.get(M3_INDEX).equalsIgnoreCase("x"))
        annotation = annotation + ";" + fields.get(M3_INDEX);
      if(!fields.get(M4_INDEX).equalsIgnoreCase("x"))
        annotation = annotation + ";" + fields.get(M4_INDEX);
            
      if(goldstandard.get(sentence) == null) {
        List<String> annotations = new ArrayList<String>();
        annotations.add(annotation);
        goldstandard.put(sentence, annotations);
      } else {
        List<String> annotations = new ArrayList<String>(
            goldstandard.get(sentence));
        annotations.add(annotation);
        goldstandard.put(sentence, annotations);
      }     
    }
    System.out.println("gold: " + goldstandard);
    System.out.println(goldstandard.size());
    
  }
  
  private static List<String> getCells(String line) {
	    List<String> cells = new ArrayList<String>();
	    String[] cols = line.split("\t");
	    for (String c : cols) {
	      if(c.trim().isEmpty())
	        break;
	      if (c.startsWith("\"") && c.endsWith("\"")) {
	        c = c.substring(1, c.length() - 1);
	      }
	      cells.add(c.trim().replaceAll("\\s+", " "));
	    }
	    return cells;
  }

  private void getPredictions(JCas aJCas) {
    for(PPTuple tuple: select(aJCas, PPTuple.class)) {
      if(selectCovered(PredictedMetadata.class,tuple).size()==0)
        System.out.println(tuple.getText());
      for(PredictedMetadata pm: selectCovered(PredictedMetadata.class,tuple)) {
        System.out.println(tuple.getCoveredText());
        System.out.println(pm);
        String annotation = pm.getMetadata1();
        
        if(!pm.getMetadata2().equalsIgnoreCase("x"))
          annotation = annotation + ";" + pm.getMetadata2();
        if(!pm.getMetadata3().equalsIgnoreCase("x"))
          annotation = annotation + ";" + pm.getMetadata3();
        if(!pm.getMetadata4().equalsIgnoreCase("x"))
          annotation = annotation + ";" + pm.getMetadata4();
        
        if(predictions.get(tuple.getText())==null) {
          List<String> annotations = new ArrayList<String>();
          annotations.add(annotation);
          predictions.put(tuple.getText().trim(), annotations);
        }
        else {
          List<String> annotations = new ArrayList<String>(predictions.get(tuple.getText()));
          annotations.add(annotation);
          predictions.put(tuple.getText().trim(), annotations);
        }  
      }
          }
System.out.println("sys: "+predictions);
System.out.println(predictions.size());
  }
  
}
