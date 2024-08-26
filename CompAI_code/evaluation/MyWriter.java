package lu.svv.saa.linklaters.privacypolicies.evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class MyWriter extends JCasConsumer_ImplBase {

	static final String OUTPUT_DOCS = "src/main/resources/4'testing_pipeline/output/";
	static final String CC_PATH = "src/main/resources/4'testing_pipeline/completeness_checking/Controller/";
	static final String METADATA_NAME = "Controller";
	static final String PI_PARAM = "_PI";
	static final String PISI_PARAM = "_PISI";
	static final String PS_PARAM = "_PS";
	static final String PSSI_PARAM = "_PSSI";
	static final String CCA_PARAM = "_CCA";
	static final String CRITERION = "Controller missing";	
	static final int SIZE = 24;
	
	@Override
	  public void process(JCas aJCas) throws AnalysisEngineProcessException {
	    try {
	    	SI_PI(aJCas);
	    	SI_PS(aJCas);
	    	Summarizing();				//This function should be called only once!!
	    	CompletenessChecking();		//This function should be called only once!!
	        } catch (Exception e) {      
	        e.printStackTrace();
	      }    
	} // process()
	 
	  @SuppressWarnings("resource")
	void SI_PI(JCas aJCas) throws IOException{
		  
		  String documentTitle = DocumentMetaData.get(aJCas).getDocumentTitle();	
		  File file = new File(OUTPUT_DOCS + documentTitle);
		  System.out.println("\n");
	      System.out.println("Inicio del documento " + documentTitle);
		  BufferedWriter writer = new BufferedWriter(new FileWriter(CC_PATH + METADATA_NAME + PI_PARAM, true));		  	
	      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	      List<String> fields; br.readLine(); br.readLine();
	      //writer.write("Document" + "\t" + "TP" + "\t" + "FP"+ "\t" + "FN");
	      //writer.write("\n");			
	      writer.write(documentTitle);
	      writer.write("\t");
	      fields = getCells(br.readLine());
	      for (int j = 1; j < 4; j++) {
	    	  writer.write(fields.get(j));
	    	  if(j==3)
	    		  break;
	    	  writer.write("\t");				
	      }
	      writer.write("\n");
	      writer.close();
	      System.out.println("Fin del documento " + DocumentMetaData.get(aJCas).getDocumentId());
	      System.out.println("\n");		  
	  }
	  
	  @SuppressWarnings("resource")
	void SI_PS(JCas aJCas) throws IOException{
		  
		  String documentTitle = DocumentMetaData.get(aJCas).getDocumentTitle();	
		  File file = new File(OUTPUT_DOCS + documentTitle);
		  System.out.println("\n");
	      System.out.println("Inicio del documento " + documentTitle);
		  BufferedWriter writer = new BufferedWriter(new FileWriter(CC_PATH + METADATA_NAME + PS_PARAM, true));		  
	      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	      List<String> fields; br.readLine(); br.readLine(); br.readLine(); br.readLine(); br.readLine();
	      //writer.write("Document" + "\t" + "TP" + "\t" + "FP" + "\t" + "FN" + "\t" + "Precision" + "\t" + "Recall");
	      //writer.write("\n");			
	      writer.write(documentTitle);
	      writer.write("\t");
	      fields = getCells(br.readLine());
	      for (int j = 1; j < 7; j++) {
	    	  writer.write(fields.get(j));
	    	  if(j==6)
	    		  break;
			  writer.write("\t");				
	      }
	      writer.write("\n");
	      writer.close();
	      System.out.println("Fin del documento " + DocumentMetaData.get(aJCas).getDocumentId());
	      System.out.println("\n");		      
	  }
	
	  private static List<String> getCells(String line) {
		    List<String> cells = new ArrayList<String>();
		    String [] cols = line.split("\t");
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
	  
	  @SuppressWarnings({ "resource", "deprecation" })
		void Summarizing() throws IOException{
		  
		  File file = new File(CC_PATH + METADATA_NAME + PI_PARAM);
		  BufferedWriter writer = new BufferedWriter(new FileWriter(CC_PATH + METADATA_NAME + PISI_PARAM));		
		  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		  List<Integer> numbers = new ArrayList<>();
		  br.readLine();
		  int TPs = 0, FPs = 0, FNs = 0;
		  float P = 0, R = 0;
		  //writer.write("Metadata" + "\t" + "TP" + "\t" + "FP" + "\t" + "FN" + "\t" + "Precision" + "\t" + "Recall");
		  //writer.write("\n");
		  //writer.write(METADATA_NAME + "\t");
		  for(int i=0;i<SIZE;i++) {
			  numbers = getNums(br.readLine());
			  TPs += numbers.get(0);
			  FPs += numbers.get(1);
			  FNs += numbers.get(2);
		  }
		  float aux1 = new Float(TPs);
		  float aux2 = new Float(FPs);
		  P = aux1*100/(aux1+aux2);
		  aux2 = new Float(FNs);
		  R = aux1*100/(aux1+aux2);
		  
		  String TP = String.valueOf(TPs);
		  String FP = String.valueOf(FPs);
		  String FN = String.valueOf(FNs);
		  String Precision = new DecimalFormat("#.##").format(P);
		  String Recall = new DecimalFormat("#.##").format(R);
		  
		  writer.write("Metadata" + "\t" + "TP" + "\t" + "FP" + "\t" + "FN" + "\t" + "Prec" + "\t" + "Recall");
		  writer.write("\n");
		  writer.write(METADATA_NAME + "\t");
		  
		  writer.write(TP + "\t" + FP + "\t" + FN + "\t" + Precision + "%" + "\t" + Recall + "%");
		  writer.write("\n");
		  writer.close();
		  
		  file = new File(CC_PATH + METADATA_NAME + PS_PARAM);
		  writer = new BufferedWriter(new FileWriter(CC_PATH + METADATA_NAME + PSSI_PARAM));		
		  br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		  br.readLine();
		  TPs = 0; FPs = 0; FNs = 0;
		  //writer.write("Metadata" + "\t" + "TP" + "\t" + "FP" + "\t" + "FN" + "\t" + "Precision" + "\t" + "Recall");
		  //writer.write("\n");
		  //writer.write(METADATA_NAME + "\t");
		  for(int i=0;i<SIZE;i++) {
			  numbers = getNums(br.readLine());
			  TPs += numbers.get(0);
			  FPs += numbers.get(1);
			  FNs += numbers.get(2);
		  }
		  aux1 = new Float(TPs);
		  aux2 = new Float(FPs);
		  P = aux1*100/(aux1+aux2);
		  aux2 = new Float(FNs);
		  R = aux1*100/(aux1+aux2);
		  	  
		  TP = String.valueOf(TPs);
		  FP = String.valueOf(FPs);
		  FN = String.valueOf(FNs);
		  Precision = new DecimalFormat("#.##").format(P);
		  Recall = new DecimalFormat("#.##").format(R);
		  
		  writer.write("Metadata" + "\t" + "TP" + "\t" + "FP" + "\t" + "FN" + "\t" + "Prec" + "\t" + "Recall");
		  writer.write("\n");
		  writer.write(METADATA_NAME + "\t");
		  
		  writer.write(TP + "\t" + FP + "\t" + FN + "\t" + Precision + "%" + "\t" + Recall + "%");
		  writer.write("\n");
		  writer.close();
		  
	  }
	  
	  @SuppressWarnings({ "resource", "deprecation" })
		void CompletenessChecking() throws IOException{
		  
		  File file = new File(CC_PATH + METADATA_NAME + PI_PARAM);
		  BufferedWriter writer = new BufferedWriter(new FileWriter(CC_PATH + METADATA_NAME + CCA_PARAM));		
		  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		  List<Integer> numbers = new ArrayList<>();
		  br.readLine();
		  int TPs = 0, FPs = 0, FNs = 0;
		  float P = 0, R = 0;

		  for(int i=0;i<SIZE;i++) {
			  numbers = getNums(br.readLine());
			  if(numbers.get(0)==0 && numbers.get(1)==0 && numbers.get(2)==0)
				  TPs++;
			  else if(numbers.get(0)==0) {
				  if(numbers.get(1)!=0)
					  FNs++;
				  if(numbers.get(2)!=0)
					  FPs++;		  
			  }
		  }
		  float aux1 = new Float(TPs);
		  float aux2 = new Float(FPs);
		  P = aux1*100/(aux1+aux2);
		  aux2 = new Float(FNs);
		  R = aux1*100/(aux1+aux2);
		  
		  String TP = String.valueOf(TPs);
		  String FP = String.valueOf(FPs);
		  String FN = String.valueOf(FNs);
		  String Precision = String.valueOf(P);
		  String Recall = String.valueOf(R);
		  
		  writer.write("Criteria" + "\t" + "\t" + "TP" + "\t" + "FP" + "\t" + "FN" + "\t" + "Prec" + "\t" + "Recall");
		  writer.write("\n");
		  writer.write(CRITERION + "\t");
		  
		  writer.write(TP + "\t" + FP + "\t" + FN + "\t" + Precision + "%" + "\t" + Recall + "%");
		  writer.write("\n");
		  writer.close();
		  
	  }
	  
	  private static List<Integer> getNums(String line) {
		    List<Integer> nums = new ArrayList<Integer>();		    
		    String [] cols = line.split("\t");
		    int num = Integer.parseInt(cols[1]);
		    nums.add(num);
		    num = Integer.parseInt(cols[2]);
		    nums.add(num);
		    num = Integer.parseInt(cols[3]);
		    nums.add(num);
		    return nums;
	  }
	
} // class
