package lu.svv.saa.linklaters.privacypolicies.algorithms;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair;
import lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult;
import lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair;
import lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult;
import lu.svv.saa.linklaters.privacypolicies.type.PPTuple;
import lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata;
import lu.svv.saa.linklaters.privacypolicies.type.UoA;
import lu.svv.saa.linklaters.privacypolicies.utils.FileHandler;
import lu.svv.saa.linklaters.privacypolicies.utils.PARAMETERS_VALUES;

public class MetadataPredictorV4 extends JCasAnnotator_ImplBase implements PARAMETERS_VALUES {
  // Whether to maximize similarity to cluster centers o.w. minimize distance
  public static final String PARAM_MAXIMIZE = "maximize";
  @ConfigurationParameter(name = PARAM_MAXIMIZE, mandatory = true,
      description = "if to maximize the distance(sim) from instance to cluster center",
      defaultValue = "true")
  private boolean maximize;

  // distance/similarity to cluster center threshold
  public static final String PARAM_DISTANCE_THRESHOLD = "distThreshold";
  @ConfigurationParameter(name = PARAM_DISTANCE_THRESHOLD, mandatory = true,
      description = "threshold to control distance to cluster centers", defaultValue = "0.1")
  private double distThreshold;

  // top-k considered for soft clustering ~3 for generic metadata
  public static final String PARAM_TOP_K = "topK";
  @ConfigurationParameter(name = PARAM_TOP_K, mandatory = true,
      description = "threshold to control distance to cluster centers", defaultValue = "6")
  private int topK;

  // probability threshold of class distribution
  public static final String PARAM_PROBABILITY_THRESHOLD = "probThreshold";
  @ConfigurationParameter(name = PARAM_PROBABILITY_THRESHOLD, mandatory = true,
      description = "threshold to control distance to cluster centers", defaultValue = "0.5")
  private double probThreshold;
  
  // Expected name of the controller
  public static final String PARAM_CONTROLLER_NAME = "controllerName";
  @ConfigurationParameter(name = PARAM_CONTROLLER_NAME, mandatory = true,
      description = "name to compare with", defaultValue = "Controller")
  private String controllerName;
  
//Expected name of the controller representative
 public static final String PARAM_CONTROLLER_REPRESENTATIVE_NAME = "controllerRepresentativeName";
 @ConfigurationParameter(name = PARAM_CONTROLLER_REPRESENTATIVE_NAME, mandatory = true,
     description = "name to compare with", defaultValue = "Representative")
 private String controllerRepresentativeName;

//Indication of analysis for DSR and LB Compliance Checking 
public static final String PARAM_CC_DSR_LB = "false";
@ConfigurationParameter(name = PARAM_CC_DSR_LB, mandatory = true,
   description = "is necessary to check the compliance for DSR and LB", defaultValue = "false")
private boolean CC_DSR_LB;

//Indication of analysis for TOE Compliance Checking 
public static final String PARAM_CC_TOE = "false";
@ConfigurationParameter(name = PARAM_CC_TOE, mandatory = true,
    description = "is necessary to check the compliance for TOE", defaultValue = "false")
private boolean CC_TOE;

//Indication of analysis for PDO Compliance Checking 
public static final String PARAM_CC_PDO = "0";
@ConfigurationParameter(name = PARAM_CC_PDO, mandatory = true,
  description = "is necessary to check the compliance for PDO", defaultValue = "0")
private int CC_PDO;

//Indication of analysis for PDC Compliance Checking 
public static final String PARAM_CC_PDC = "0";
@ConfigurationParameter(name = PARAM_CC_PDC, mandatory = true,
description = "is necessary to check the compliance for PDC", defaultValue = "0")
private int CC_PDC;

//Indication of analysis for R Compliance Checking 
public static final String PARAM_CC_R = "false";
@ConfigurationParameter(name = PARAM_CC_R, mandatory = true,
description = "is necessary to check the compliance for R", defaultValue = "false")
private boolean CC_R;

//Indication of analysis for PDTS Compliance Checking 
public static final String PARAM_CC_PDTS = "false";
@ConfigurationParameter(name = PARAM_CC_PDTS, mandatory = true,
description = "is necessary to check the compliance for PDTS", defaultValue = "false")
private boolean CC_PDTS;

//Indication of analysis for PDPO Compliance Checking 
public static final String PARAM_CC_PDPO = "0";
@ConfigurationParameter(name = PARAM_CC_PDPO, mandatory = true,
description = "is necessary to check the compliance for PDPO", defaultValue = "0")
private int CC_PDPO;

//Indication of analysis for PP Compliance Checking 
public static final String PARAM_CC_PP = "false";
@ConfigurationParameter(name = PARAM_CC_PP, mandatory = true,
description = "is necessary to check the compliance for PDC", defaultValue = "false")
private boolean CC_PP;

//Indication of analysis for PDS Compliance Checking 
/*public static final String PARAM_CC_PDS = "false";
@ConfigurationParameter(name = PARAM_CC_PDS, mandatory = true,
description = "is necessary to check the compliance for PDS", defaultValue = "false")
private boolean CC_PDS;

//Indication of analysis for ADM Compliance Checking 
public static final String PARAM_CC_ADM = "false";
@ConfigurationParameter(name = PARAM_CC_ADM, mandatory = true,
description = "is necessary to check the compliance for ADM", defaultValue = "false")
private boolean CC_ADM;

//Indication of analysis for CH Compliance Checking 
public static final String PARAM_CC_CH = "false";
@ConfigurationParameter(name = PARAM_CC_CH, mandatory = true,
description = "is necessary to check the compliance for CH", defaultValue = "false")
private boolean CC_CH;*/

//Indication of analysis for DPO Compliance Checking 
public static final String PARAM_CC_DPO = "false";
@ConfigurationParameter(name = PARAM_CC_DPO, mandatory = true,
description = "is necessary to check the compliance for DPO", defaultValue = "false")
private boolean CC_DPO;

//Indication of analysis for C Compliance Checking 
public static final String PARAM_CC_C = "false";
@ConfigurationParameter(name = PARAM_CC_C, mandatory = true,
description = "is necessary to check the compliance for C", defaultValue = "false")
private boolean CC_C;

//Indication of analysis for CR Compliance Checking 
public static final String PARAM_CC_CR = "false";
@ConfigurationParameter(name = PARAM_CC_CR, mandatory = true,
description = "is necessary to check the compliance for CR", defaultValue = "false")
private boolean CC_CR;

  private Map<Double, String> metadataDists;
  private List<String> metadataL1;
  List<String> metadataL2;
  List<String> metadataL3;
  Map<String, String> metadataIndex;

  // Results
  Map<String, List<String>> classifierLabelsPerTuple;
  Map<String, List<String>> clustererLabelsPerTuple;
  Map<String, List<String>> KWsPerTuple;
  // List<UoA> uoaTuples;
  private double secondThreshold = 0.1, TOEThreshold = 0.1, PDOThreshold = 0.1, RThreshold = 0.1;
  StringBuilder log;
  boolean PP = true, flag = false, AD_Flag = false, EM_Flag = false, PH_Flag = false, NE_Flag = false, CName_flag = false, CRName_flag = false, Cookie_Flag = false, Text_flag = false, Month_flag = false;
  boolean DSR_PP_Flag = false; //Still not considering well the Post-processing
  
  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    
    this.metadataDists = new TreeMap<Double, String>();// Collections.reverseOrder());
    metadataL1 = FileHandler.readFromFile(METADATA_MODEL + "L1");
    metadataL2 = FileHandler.readFromFile(METADATA_MODEL + "L2");
    metadataL3 = FileHandler.readFromFile(METADATA_MODEL + "L3");
    metadataIndex = FileHandler.readIndex(METADATA_MODEL + "index");
    

    classifierLabelsPerTuple = new HashMap<String, List<String>>();
    clustererLabelsPerTuple = new HashMap<String, List<String>>();
    KWsPerTuple = new HashMap<String, List<String>>();
    log = new StringBuilder();

  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException{
	  
    log.append("sentence,metadata");
    log.append("\n");
    /*boolean PDO_flag = false;
    int PDO_context = -1;
    boolean PDC_flag = false;
    int PDC_context = -1;*/
    boolean DPO_flag = false;
    int DPO_context = -1;
    boolean C_flag = false;
    int C_context = -1;
    boolean R_flag = false;
    int R_context = -1;
    boolean PP_flag = false;
    int PP_context = -1;
    boolean LB_flag = false;
    String documentTitle = DocumentMetaData.get(aJCas).getDocumentTitle();
    //String CC_DOCS_DIR = "/Users/angelo.rizzi/Desktop/";
    String CC_DOCS_DIR = TEST_OUTPUT;
    Set<String> AllDSRs = new TreeSet<String>();
    Set<String> AllLBs = new TreeSet<String>();
    Set<String> AllTOEs = new TreeSet<String>();
    Set<String> AllPDOs = new TreeSet<String>();
    Set<String> AllPDCs = new TreeSet<String>();
    Set<String> AllRs = new TreeSet<String>();
    Set<String> AllPDTSs = new TreeSet<String>();
    Set<String> AllPDPOs = new TreeSet<String>();
    Set<String> AllPPs = new TreeSet<String>();
    /*Set<String> AllPDSs = new TreeSet<String>();
    Set<String> AllADMs = new TreeSet<String>();
    Set<String> AllCHs = new TreeSet<String>();*/
    Set<String> AllDPOs = new TreeSet<String>();
    Set<String> AllCs = new TreeSet<String>();
    Set<String> AllCRs = new TreeSet<String>();

    for (PPTuple tuple : select(aJCas, PPTuple.class)) {
      // tupleIndex++;
    	
      // No Unit of Analysis
      if (selectCovered(UoA.class, tuple).size() == 0) {
        newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), "N/A", "X", "X", "X");
      }
      // UoA exists
      boolean labeled = false;
      for (UoA uoa : selectCovered(UoA.class, tuple)) {
      
    	//Working with KWs
  	    List<String> fileNames = null;
		try {
			fileNames = new ArrayList<String>(FileHandler.getFileNames(KWS_PATH, ""));	//KWS_PATH is the path where the KWs are
		} catch (ResourceInitializationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  	    
		String presentKWs = null;
		
  	    for (final String filename : fileNames){ 	    	
  	    	String sCurrentLine;
  	    	
  	    		if(!filename.equals(".DS_Store")) {
  	    			//System.out.println("Metadata: " + filename);	    			
  	    			FileReader fr = null;

  						try {
								fr = new FileReader(KWS_PATH + filename);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}    						
  						try {
  							BufferedReader br = new BufferedReader(fr);
								while ((sCurrentLine = br.readLine()) != null) {
									//System.out.println("Current line: " + sCurrentLine);
									String[] Tokens = sCurrentLine.split(" ");
									boolean present = true;
									for (String token:Tokens){
										if (!uoa.getPreprocessed().contains(token))
											present = false;
									}
									if (present) {
										presentKWs = presentKWs + ";" + filename;
										break;
									}
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
  						try {
  							fr.close();
  						} catch (IOException e) {
  							// TODO Auto-generated catch block
  							e.printStackTrace();
  						}
  						
  	    		}
  	    		//System.out.println("KWs: " + presentKWs + "\n");	
  	    		if (presentKWs != null)
  	    			uoa.setContainedKWs(presentKWs);
  	    }
      
        if (uoa.hasNE())
        	NE_Flag=true;
        else
        	NE_Flag=false;
        if (uoa.hasADDRESS())
         	AD_Flag=true;
        else
         	AD_Flag=false;
        if (uoa.hasEMAIL())
         	EM_Flag=true;
        else
         	EM_Flag=false;
        if (uoa.hasPHONE())
         	PH_Flag=true;
        else
         	PH_Flag=false;

//----------------------------------------------------------------------------------------------------
// DATA_SUBJECT_RIGHT call
//----------------------------------------------------------------------------------------------------
        List<String> classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        List<String> KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));
        List<String> softLabels = new ArrayList<String>(getClusteringResults(uoa, "DATA_SUBJECT_RIGHT", distThreshold));
        
        System.out.println("Predicting DSR:" + classifierLabels + softLabels + KWs);
        Set<String> DSRs = new TreeSet<String>();
        DSRs = predictDataSubjectRight(classifierLabels, softLabels, KWs, uoa);
        AllDSRs.addAll(DSRs);
        System.out.println("DSRs: ");
        
        for (String cl : DSRs) {
          System.out.println("\t"+ cl);
          String[] labels = metadataIndex.get(cl).split(";");
          newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2],labels[1], labels[0]);
        }
//----------------------------------------------------------------------------------------------------
// DATA_SUBJECT_RIGHT call
//----------------------------------------------------------------------------------------------------        

//----------------------------------------------------------------------------------------------------
// LEGAL_BASIS call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));
        softLabels = new ArrayList<String>(getClusteringResults(uoa, "LEGAL_BASIS", secondThreshold));
        
        System.out.println("Predicting LB from classification/clustering/keywords:" + classifierLabels + softLabels + KWs);
        Set<String> LBs = new TreeSet<String>();
        LBs = predictLegalBasis(classifierLabels, softLabels, KWs);
        AllLBs.addAll(LBs);
        System.out.println("LBs: ");

        for (String cl : LBs) {
          System.out.println("\t"+ cl);
          String[] labels = metadataIndex.get(cl).split(";");
          newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
        }
//----------------------------------------------------------------------------------------------------
// LEGAL_BASIS call
//----------------------------------------------------------------------------------------------------  
 
//----------------------------------------------------------------------------------------------------
// TRANSFER_OUTSIDE_EUROPE Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));
        softLabels = new ArrayList<String>(getClusteringResults(uoa, "TRANSFER_OUTSIDE_EUROPE", TOEThreshold));        
        System.out.println("Predicting TOEs from classification/clustering/keywords:" + classifierLabels + softLabels + KWs);

        Set<String> TOEs= new TreeSet<String>();
        TOEs = predictTransferOutsideEurope(classifierLabels, softLabels, KWs);
        AllTOEs.addAll(TOEs);
        System.out.println("TOEs: ");

        for (String cl : TOEs) {
        	System.out.println("\t"+ cl);
          	String[] labels = metadataIndex.get(cl).split(";");
          	newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
        }
//----------------------------------------------------------------------------------------------------
// TRANSFER_OUTSIDE_EUROPE Call
//----------------------------------------------------------------------------------------------------
       
//----------------------------------------------------------------------------------------------------
// PD_ORIGIN Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));
        softLabels = new ArrayList<String>(getClusteringResults(uoa, "PD_ORIGIN", PDOThreshold));        
        System.out.println("Predicting PDOs from classification/clustering/keywords:" + classifierLabels + softLabels + KWs);

        Set<String> PDOs= new TreeSet<String>();
        PDOs = predictPDOrigin(classifierLabels, softLabels, KWs);
        AllPDOs.addAll(PDOs);
        System.out.println("PDOs: ");

        for (String cl : PDOs) {
        	System.out.println("\t"+ cl);
          	String[] labels = metadataIndex.get(cl).split(";");
          	newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
          	//PDO_flag = true;
            //PDO_context = 0;
        }
         
//----------------------------------------------------------------------------------------------------
// PD_ORIGIN Call
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
// PD_CATEGORY Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting PDCs from classification/clustering/keywords:" + classifierLabels + KWs);

        Set<String> PDCs= new TreeSet<String>();
        PDCs = predictPDCategory(classifierLabels, KWs);
        //AllPDCs.addAll(PDCs);
        System.out.println("PDCs: ");

//----------------------------------------------------------------------------------------------------
// PD_CATEGORY Call
//----------------------------------------------------------------------------------------------------
        
//----------------------------------------------------------------------------------------------------
// TYPE Call
//----------------------------------------------------------------------------------------------------
        System.out.println("Predicting TYPE");
        Set<String> Ts= new TreeSet<String>();
        Ts = predictTYPE(PDOs, PDCs);	//, PDO_flag, PDC_flag, PDO_context, PDC_context);
        PDCs.addAll(Ts);
        AllPDCs.addAll(PDCs);
        for (String cl : PDCs) {
            System.out.println("\t"+ cl);
            String[] labels = metadataIndex.get(cl).split(";");
            newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
          }       
//----------------------------------------------------------------------------------------------------
// TYPE Call
//----------------------------------------------------------------------------------------------------
        
//----------------------------------------------------------------------------------------------------
// RECIPIENTS Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));
        softLabels = new ArrayList<String>(getClusteringResults(uoa, "RECIPIENTS", RThreshold)); 
        System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);

        Set<String> Rs= new TreeSet<String>();
        Rs = predictRecipients(classifierLabels, softLabels, KWs);
        AllRs.addAll(Rs);
        System.out.println("Rs: ");

        for (String cl : Rs) {
          System.out.println("\t"+ cl);
          String[] labels = metadataIndex.get(cl).split(";");
          newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
          R_flag = true;
          R_context = 0;         
        }
        
        R_context++;
        if(R_context==2) {
          R_flag = false;
          R_context = -1;
        }
//----------------------------------------------------------------------------------------------------
// RECIPIENTS Call
//----------------------------------------------------------------------------------------------------
        
//----------------------------------------------------------------------------------------------------
// PD_TIME_STORE Call
//----------------------------------------------------------------------------------------------------
         classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
         KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
         System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);
         
         String COOKIE = "cookie";
         
         if (uoa.getCoveredText().contains(COOKIE))
         	Cookie_Flag = true;
         else
         	Cookie_Flag = false; 

         Set<String> PDTSs= new TreeSet<String>();
         PDTSs = predictPDTimeStored(classifierLabels, KWs);
         AllPDTSs.addAll(PDTSs);
         System.out.println("PDTSs: ");

         for (String cl : PDTSs) {
        	 System.out.println("\t"+ cl);
             String[] labels = metadataIndex.get(cl).split(";");
             newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
         }   
//----------------------------------------------------------------------------------------------------
// PD_TIME_STORE Call
//----------------------------------------------------------------------------------------------------
        
//----------------------------------------------------------------------------------------------------
// PD_PROVISION_OBLIGED Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);

        Set<String> PDPOs= new TreeSet<String>();
        PDPOs = predictPDProvisionObliged(classifierLabels, KWs);
        AllPDPOs.addAll(PDPOs);
        System.out.println("PDPOs: ");

        for (String cl : PDPOs) {
        	System.out.println("\t"+ cl);
        	String[] labels = metadataIndex.get(cl).split(";");
        	newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
        }  
//----------------------------------------------------------------------------------------------------
// PD_PROVISION_OBLIGED Call
//----------------------------------------------------------------------------------------------------
        
//----------------------------------------------------------------------------------------------------
// PROCESSING_PURPOSES Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);

        Set<String> PPs = new TreeSet<String>();
        String S1 = "follow";
        String S2 = "purpose";
        
        PPs = predictProcessingPurposes(classifierLabels, KWs);
        AllPPs.addAll(PPs);
        System.out.println("PPs: ");

        for (String cl : PPs) {
          System.out.println("\t"+ cl);
          String[] labels = metadataIndex.get(cl).split(";");
          newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
          if (uoa.getCoveredText().contains(S1) && uoa.getCoveredText().contains(S2)) {
                PP_flag = true;
                PP_context = 0;
            }          
          }
          
        PP_context++;
          if(PP_context==3) {
            PP_flag = false;
              PP_context = -1;
        }    
//----------------------------------------------------------------------------------------------------
// PROCESSING_PURPOSES Call
//----------------------------------------------------------------------------------------------------
        
//----------------------------------------------------------------------------------------------------
// PD_SECURITY Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);

        Set<String> PDSs = new TreeSet<String>();
        PDSs = predictPDSecurity(classifierLabels, KWs);
        //AllPDSs.addAll(PDSs);
        System.out.println("PDSs: ");

        for (String cl : PDSs) {
        	System.out.println("\t"+ cl);
        	String[] labels = metadataIndex.get(cl).split(";");
        	newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
        }   
//----------------------------------------------------------------------------------------------------
// PD_SECURITY Call
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
// AUTO_DECISION_MAKING Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);

        Set<String> ADMs = new TreeSet<String>();
        ADMs = predictAutoDecisionMaking(classifierLabels, KWs);
        //AllADMs.addAll(ADMs);
        System.out.println("ADMs: ");

        for (String cl : ADMs) {
        	System.out.println("\t"+ cl);
        	String[] labels = metadataIndex.get(cl).split(";");
        	newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
        }   
//----------------------------------------------------------------------------------------------------
// AUTO_DECISION_MAKING Call
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
// CHILDREN Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);

        Set<String> CHs = new TreeSet<String>();
        CHs = predictChildren(classifierLabels, KWs);
        //AllCHs.addAll(CHs);
        System.out.println("CHs: ");

        for (String cl : CHs) {
        	System.out.println("\t"+ cl);
        	String[] labels = metadataIndex.get(cl).split(";");
        	newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
        }   
//----------------------------------------------------------------------------------------------------
// CHILDREN Call
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
// DPO Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting DPOs from classification/clustering/keywords:" + classifierLabels + KWs);
                  
        Set<String> DPOs= new TreeSet<String>();
        String GDPR = "gdpr";
        String DPA = "data protection authority";   
        
        if (uoa.getCoveredText().contains(GDPR) || uoa.getCoveredText().contains(DPA))
        	Text_flag = true;
        else
        	Text_flag = false;
        
        DPOs = predictDPO(classifierLabels, KWs, DPO_flag);
        AllDPOs.addAll(DPOs);
        System.out.println("DPOs: ");

        for (String cl : DPOs) {
        	System.out.println("\t"+ cl);
            String[] labels = metadataIndex.get(cl).split(";");   
            newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
            DPO_flag = true;
            DPO_context = 0;
        }
                  
        DPO_context++;
        if(DPO_context==3) {
        	DPO_flag = false;
            DPO_context = -1;
        }     
//----------------------------------------------------------------------------------------------------
// DPO Call
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
// CONTROLLER Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
        System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);
        
        List<String> months = new ArrayList<String>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        Set<String> Cs= new TreeSet<String>();
        if (uoa.getCoveredText().contains(controllerName))
        	CName_flag = true;
        else
        	CName_flag = false;
        if (uoa.getCoveredText().contains(GDPR) || uoa.getCoveredText().contains(DPA))
        	Text_flag = true;
        else
        	Text_flag = false;
        for (int i=0;i<months.size();i++) {
        	if (uoa.getCoveredText().contains(months.get(i)))
        		Month_flag = true;
        }
        Cs = predictController(classifierLabels, KWs, C_flag);
        AllCs.addAll(Cs);
        Month_flag = false;
        System.out.println("Cs: ");

        for (String cl : Cs) {
        	System.out.println("\t"+ cl);
            String[] labels = metadataIndex.get(cl).split(";");
            newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
            C_flag = true;
            C_context = 0;
        }

        C_context++;
        if(C_context==3) {
          C_flag = false;
          C_context = -1;
        }
//----------------------------------------------------------------------------------------------------
// CONTROLLER Call
//----------------------------------------------------------------------------------------------------     

//----------------------------------------------------------------------------------------------------
// CONTROLLER_REPRESENTATIVE Call
//----------------------------------------------------------------------------------------------------
        classifierLabels = new ArrayList<String>(getClassificationResults(uoa));
        KWs = new ArrayList<String>(Arrays.asList(uoa.getContainedKWs().split(";")));      
             System.out.println("Predicting Cs from classification/clustering/keywords:" + classifierLabels + KWs);
             
        Set<String> CRs= new TreeSet<String>();
        if (uoa.getCoveredText().contains(controllerRepresentativeName))
        	CRName_flag = true;
        else
        	CRName_flag = false;
        CRs = predictControllerRepresentative(classifierLabels, KWs);
        AllCRs.addAll(CRs);
        System.out.println("CRs: ");

        for (String cl : CRs) {
             System.out.println("\t"+ cl);
             String[] labels = metadataIndex.get(cl).split(";");
             newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);
        }
//----------------------------------------------------------------------------------------------------
// CONTROLLER_REPRESENTATIVE Call
//----------------------------------------------------------------------------------------------------
        //filtering out the predictions out of context
        postprocessing(aJCas);
       //Any metadata?	
        if (DSRs.size()>0 || LBs.size()>0 || TOEs.contains("TRANSFER_OUTSIDE_EUROPE") || PDOs.size()>0 || PDCs.size()>0 || Rs.size()>0 || PDTSs.size()>0 || PDPOs.contains("PD_PROVISION_OBLIGED") || PPs.size()>0 || PDSs.size()>0 || ADMs.size()>0 || CHs.size()>0 || DPOs.size()>0 || Cs.size()>0 || CRs.size()>0) {	
          
          labeled = true;
            if (LBs.size()>0 && !(DSRs.size()>0) && !(TOEs.size()>0) && !(PDOs.size()>0) && !(PDCs.size()>0) && !(Rs.size()>0) && !(PDTSs.size()>0) && !(PDPOs.size()>0) && !(PPs.size()>0) && !(PDSs.size()>0) && !(ADMs.size()>0) && !(CHs.size()>0) && !(DPOs.size()>0) && !(Cs.size()>0) && !(CRs.size()>0))
              LB_flag = true;
            else
              LB_flag = false;
        }

        else if (R_flag || PP_flag) {   
          labeled = true;
          if (R_flag) {
            Rs.add("RECIPIENTS");
            for (String cl : Rs) {
                  System.out.println("\t"+ cl);
                  String[] labels = metadataIndex.get(cl).split(";");
                  newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);        
              }
           }
          
          if (PP_flag) {
              labeled = true;
            PPs.add("PROCESSING_PURPOSES");
            for (String cl : PPs) {
                  System.out.println("\t"+ cl);
                  String[] labels = metadataIndex.get(cl).split(";");
                  newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);         
              }
           }
          
        }
        
        if (LB_flag && PP_flag) {
          labeled = true;
          PPs.add("PROCESSING_PURPOSES");
            for (String cl : PPs) {
                System.out.println("\t"+ cl);
                String[] labels = metadataIndex.get(cl).split(";");
                newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), labels[3], labels[2], labels[1], labels[0]);         
            }
        }

        if (labeled) {
          char ch='"';
          log.append(ch);
          log.append(uoa.getCoveredText());
          log.append(ch);
          log.append(",");
          log.append(ch);
          
          if (DSRs.size()>0) {
            log.append("DATA_SUBJECT_RIGHT");
            log.append("|");
            if (DSRs.contains("ACCESS")) {
              log.append("ACCESS");
              log.append("|");
            }
            if (DSRs.contains("RECTIFICATION")) {
              log.append("RECTIFICATION");
              log.append("|");
            }
            if (DSRs.contains("RESTRICTION")) {
              log.append("RESTRICTION");
              log.append("|");
            }
            if (DSRs.contains("COMPLAINT")) {
              log.append("COMPLAINT");
              log.append("|");
            }
            if (DSRs.contains("COMPLAINT")){
                log.append("SA");
                log.append("|");
            }
            if (DSRs.contains("ERASURE")) {
              log.append("ERASURE");
              log.append("|");
            }
            if (DSRs.contains("OBJECT")) {
              log.append("OBJECT");
              log.append("|");
            }
            if (DSRs.contains("PORTABILITY")) {
              log.append("PORTABILITY");
              log.append("|");
            }
            if (DSRs.contains("WITHDRAW_CONSENT")) {
              log.append("WITHDRAW_CONSENT");
              log.append("|");
            }
          }
          if (LBs.size()>0) {
            log.append("LEGAL_BASIS");
            log.append("|");
            if (LBs.contains("CONTRACT")) {
              log.append("CONTRACT");
              log.append("|");
            }
            if (LBs.contains("CONTRACTUAL")) {
              log.append("CONTRACTUAL");
              log.append("|");
            }
            if (LBs.contains("TO_ENTER_CONTRACT")) {
              log.append("TO_ENTER_CONTRACT");
              log.append("|");
            }
            if (LBs.contains("STATUTORY")) {
              log.append("STATUTORY");
              log.append("|");
            }
            if (LBs.contains("PUBLIC_FUNCTION")) {
              log.append("PUBLIC_FUNCTION");
              log.append("|");
            }
            if (LBs.contains("LEGITIMATE_INTEREST")) {
              log.append("LEGITIMATE_INTEREST");
              log.append("|");
            }
            if (LBs.contains("VITAL_INTEREST")) {
              log.append("VITAL_INTEREST");
              log.append("|");
            }
            if (LBs.contains("LEGAL_OBLIGATION")) {
              log.append("LEGAL_OBLIGATION");
              log.append("|");
            }
            if (LBs.contains("CONSENT")) {
              log.append("CONSENT");
              log.append("|");
            }
          }
          //if (TOEs.size()>0) {
          if (TOEs.contains("TRANSFER_OUTSIDE_EUROPE")) {
            log.append("TRANSFER_OUTSIDE_EUROPE");
            log.append("|");
            if (TOEs.contains("SPECIFIC_DEROGATION")) {
              log.append("SPECIFIC_DEROGATION");
              log.append("|");
            }
            if (TOEs.contains("SAFEGUARDS")) {
              log.append("SAFEGUARDS");
              log.append("|");
            }
            if (TOEs.contains("ADEQUACY_DECISION")) {
              log.append("ADEQUACY_DECISION");
              log.append("|");
            }
            if (TOEs.contains("UNAMBIGUOUS_CONSENT")) {
              log.append("UNAMBIGUOUS_CONSENT");
              log.append("|");
            }
            if (TOEs.contains("BINDING_CORPORATE_RULES")) {
              log.append("BINDING_CORPORATE_RULES");
              log.append("|");
            }
            if (TOEs.contains("EU_MODEL_CLAUSES")) {
              log.append("EU_MODEL_CLAUSES");
              log.append("|");
            }
            if (TOEs.contains("TERRITORY")) {
              log.append("TERRITORY");
              log.append("|");
            }
            if (TOEs.contains("SECTOR")) {
              log.append("SECTOR");
              log.append("|");
            }
            if (TOEs.contains("COUNTRY")) {
              log.append("COUNTRY");
                log.append("|");
            }
          }
          if (PDOs.size()>0) {
            log.append("PD_ORIGIN");
            log.append("|");      
            if (PDOs.contains("INDIRECT")) {
              log.append("INDIRECT");
              log.append("|");
            }
            if (PDOs.contains("THIRD_PARTY")) {
              log.append("THIRD_PARTY");
              log.append("|");
            }
            if (PDOs.contains("PUBLICLY")) {
              log.append("PUBLICLY");
              log.append("|");
            }
          }
          if (PDCs.size()>0) {
            if (PDCs.contains("PD_CATEGORY")) {
              log.append("PD_CATEGORY");
              log.append("|");
            }
            if (PDCs.contains("SPECIAL")) {
              log.append("SPECIAL");
              log.append("|");
            }
            if (PDCs.contains("TYPE")) {
              log.append("TYPE");
              log.append("|");
            }
          }
          if (Rs.size()>0) {
            log.append("RECIPIENTS");
            log.append("|");
          }
          if (PDTSs.size()>0) {
            log.append("PD_TIME_STORED");
            log.append("|");
          }
          //if (PDPOs.size()>0) {
          if(PDPOs.contains("PD_PROVISION_OBLIGED")) {
            log.append("PD_PROVISION_OBLIGED");
            log.append("|");
          }
          if (PPs.size()>0) {
            log.append("PROCESSING_PURPOSES");
            log.append("|");
          }
          if (PDSs.size()>0) {
            log.append("PD_SECURITY");
            log.append("|");
          }
          if (ADMs.size()>0) {
            log.append("AUTO_DECISION_MAKING");
            log.append("|");
          }
          if (CHs.size()>0) {
            log.append("CHILDREN");
            log.append("|");
          }
          if (DPOs.size()>0) {
            log.append("DPO");
            log.append("|");
            if (DPOs.contains("CONTACT")) {
              log.append("D_CONTACT");
              log.append("|");
            } 
            if (DPOs.contains("EMAIL")) {
              log.append("D_EMAIL");
              log.append("|");      
            }
            if (DPOs.contains("LEGAL_ADDRESS")) {
              log.append("D_LEGAL_ADDRESS");
              log.append("|");
            }
            if (DPOs.contains("PHONE_NUMBER")) {
              log.append("D_PHONE_NUMBER");
              log.append("|");
            }
          }
          if (Cs.size()>0) {
          //if(Cs.contains("CONTROLLER")) {
            log.append("CONTROLLER");
            log.append("|");
            if (Cs.contains("IDENTITY")) {
              log.append("C_IDENTITY");
              log.append("|");
            }
            if (Cs.contains("LEGAL_NAME")) {
              log.append("C_LEGAL_NAME");
              log.append("|");
            } 
            if (Cs.contains("REGISTER_NUMBER")) {
              log.append("C_REGISTER_NUMBER");
              log.append("|");
            }
            if (Cs.contains("CONTACT")) {
              log.append("C_CONTACT");
              log.append("|");
            }           
            if (Cs.contains("EMAIL")) {
              log.append("C_EMAIL");
              log.append("|");
            }
            if (Cs.contains("LEGAL_ADDRESS")) {
              log.append("C_LEGAL_ADDRESS");
              log.append("|");
            }
            if (Cs.contains("PHONE_NUMBER")) {
              log.append("C_PHONE_NUMBER");
              log.append("|");
            }
          }
          if (CRs.size()>0) {
          log.append("CONTROLLER_REPRESENTATIVE");
          log.append("|");
            if (CRs.contains("IDENTITY")) {
              log.append("CR_IDENTITY");
              log.append("|");
            }
            if (CRs.contains("LEGAL_NAME")) {
              log.append("CR_LEGAL_NAME");
              log.append("|");
            } 
            if (CRs.contains("REGISTER_NUMBER")) {
              log.append("CR_REGISTER_NUMBER");
              log.append("|");
            }
            if (CRs.contains("CONTACT")) {
              log.append("CR_CONTACT");
              log.append("|");
            }
            if (CRs.contains("EMAIL")) {
              log.append("CR_EMAIL");
              log.append("|");
            }
            if (CRs.contains("LEGAL_ADDRESS")) {
              log.append("CR_LEGAL_ADDRESS");
              log.append("|");
            }
            if (CRs.contains("PHONE_NUMBER")) {
              log.append("CR_PHONE_NUMBER");
              log.append("|");
            }
          }
          log.deleteCharAt(log.length()-1);
          log.append(ch);
          FileHandler.writeToFile(TEST_OUTPUT + "Predictions_"+ DocumentMetaData.get(aJCas).getDocumentTitle() + ".csv", log.toString());
          log.append("\n");
          
        }

        else {
          //log.append(String.format("%nPredicted as:%s%n", "N/A"));
          newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), "N/A", "X", "X", "X");
          DPO_flag = false;
          DPO_context = -1;
        }
        
      }
    }
    
    //Completeness_Checking Analysis
    try {
    	log = new StringBuilder();
    	boolean flag_CC_DSR_LB = false, flag_CC_TOE = false, flag_CC_PDO = false, flag_CC_PDC = false, flag_CC_R = false, flag_CC_PDTS = false, flag_CC_PDPO = false, flag_CC_PP = false, flag_CC_DPO = false, flag_CC_C = false, flag_CC_CR = false;
    	
    	//if(CC_DSR_LB)
    		flag_CC_DSR_LB = CC_DSR_LB("DATA_SUBJECT_RIGHT", "LEGAL_BASIS", AllDSRs, AllLBs, CC_DOCS_DIR, documentTitle);
    	//if(CC_TOE)
    		flag_CC_TOE = CC_TOE("TRANSFER_OUTSIDE_EUROPE", AllTOEs, CC_DOCS_DIR, documentTitle);
    	//if(CC_PDO == 2 || CC_PDO == 3){
    		flag_CC_PDO = CC_PDO("PD_ORIGIN", AllPDOs, CC_DOCS_DIR, documentTitle);
    		flag_CC_PDC = CC_PDC("PD_CATEGORY", AllPDCs, AllPDOs, CC_DOCS_DIR, documentTitle);
    	//}
    	//if(CC_R)
    		flag_CC_R = CC_R("RECIPIENTS", AllRs, CC_DOCS_DIR, documentTitle);
    	//if(CC_PDTS)
    		flag_CC_PDTS = CC_PDTS("PD_TIME_STORED", AllPDTSs, CC_DOCS_DIR, documentTitle);
    	//if(CC_PDPO == 1 || CC_PDPO == 3)
    		flag_CC_PDPO = CC_PDPO("PD_PROVISION_OBLIGED", AllPDPOs, AllLBs, CC_DOCS_DIR, documentTitle);
    	//if(CC_PP)
    		flag_CC_PP = CC_PP("PROCESSING_PURPOSES", AllPPs, CC_DOCS_DIR, documentTitle);
    	//if(CC_DPO)
    		flag_CC_DPO = CC_DPO("DPO", AllDPOs, CC_DOCS_DIR, documentTitle);
    	//if(CC_C)
    		flag_CC_C = CC_C("CONTROLLER", AllCs, CC_DOCS_DIR, documentTitle);
    	//if(CC_CR)
    		flag_CC_CR = CC_CR("CONTROLLER_REPRESENTATIVE", AllCRs, CC_DOCS_DIR, documentTitle);
    	
    	if(!(flag_CC_DSR_LB || flag_CC_TOE || flag_CC_PDO || flag_CC_PDC || flag_CC_R || flag_CC_PDTS || flag_CC_PDPO || flag_CC_PP || flag_CC_DPO || flag_CC_C || flag_CC_CR))
    		log.append("\nNO CRITERION VIOLATED\n");
    	
    	FileHandler.writeToFile(CC_DOCS_DIR + "CC_" + DocumentMetaData.get(aJCas).getDocumentTitle() + ".txt", log.toString());
        } catch (Exception e) {
        e.printStackTrace();
      } 
    
  }

//----------------------------------------------------------------------------------------------------
//DATA_SUBJECT_RIGHT extraction			
//----------------------------------------------------------------------------------------------------
  private Set<String> predictDataSubjectRight(List<String> classifierLabels,
	      List<String> clustererLabels, List<String> KWs, UoA uoa) {
	  Set<String> labels = new TreeSet<String>();

	    List<String> data_subject_right = FileHandler.readFromFile(METADATA_MODEL + "DATA_SUBJECT_RIGHT");

	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    List<String> KWsL2 = new ArrayList<String>(metadataL2);
	    KWsL2.retainAll(KWs);
	    // Data_Subject_Right relevant KWs
	    KWsL2.retainAll(data_subject_right);
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    classification.retainAll(data_subject_right);

	    List<String> KWsL3 = new ArrayList<String>(metadataL3);
	    KWsL3.retainAll(KWs);

	    boolean firstCase = false;
	    boolean other = false;
	    int dsr = 0;

	    if (classifierLabels.contains("DATA_SUBJECT_RIGHT")) {
	      int currentSize = clustererLabels.size();
	      clustererLabels.retainAll(data_subject_right);

	      if (clustererLabels.isEmpty() && currentSize == 1)// Clustering contains one close option
	                                                        // other than DSR
	        other = true;

	      List<String> temp = new ArrayList<String>(KWsL2);
	      for (String clr : clustererLabels) {
	        if (!KWsL2.contains(clr))
	          continue;
	        firstCase = true;
	        labels.add(clr);
	        ++dsr;

	        temp.remove(clr);
	      }
	      // ADD extra labels? if KWs are not all covered?
	      if ((clustererLabels.isEmpty() && !other) || (firstCase && !temp.isEmpty())) {
	        clustererLabels = getClusteringResults(uoa, "DATA_SUBJECT_RIGHT", 0.1);
	        clustererLabels.retainAll(data_subject_right);

	        clustererLabels.retainAll(temp); // EXCLUDE WHAT HAS BEEN USED (TEMP?)

	        for (String clr : clustererLabels) {
	          ++dsr;
	          labels.add(clr);
	          temp.remove(clr);
	        }
	        if (!temp.isEmpty() && dsr > 2) {
	          for (String kw : temp) {
	            if (kw.equalsIgnoreCase("COMPLAINT") && KWsL3.contains("SA"))
	              kw = "SA";
	            labels.add(kw);
	          }
	        }
	        if (clustererLabels.isEmpty()) {
	          // KWs?
	          if (KWsL1.contains("DATA_SUBJECT_RIGHT") && KWsL2.size() == 1 && !KWsL3.contains("SA")) {
	            labels.add(KWsL2.get(0));
	          }
	        }
	      }
	      if (clustererLabels.isEmpty()
	          || (KWs.contains("COMPLAINT") && KWs.contains("SA") && KWs.size() == 2)) {

	        classification.retainAll(KWsL2);

	        if (!classification.isEmpty() && KWsL2.size() == 1) {
	          for (String cl : classification) {
	            labels.add(cl);
	          }
	        }
	        if (!classification.isEmpty() && KWsL2.size() == classification.size()) {
	          for (String cl : classification) {
	            labels.add(cl);
	          }
	        }
	      }
	    } // Not classified as DataSubjectRight
	    classification.retainAll(KWsL2);
	    if (!classification.isEmpty()) {
	      for (String cl : classification) {
	        labels.add(cl);
	      }
	    } else {

	      clustererLabels = getClusteringResults(uoa, "DATA_SUBJECT_RIGHT", 0.1);
	      classifierLabels.retainAll(data_subject_right);
	      clustererLabels.retainAll(KWsL2);
	      if (!clustererLabels.isEmpty() && KWsL2.size() == 1) {

	        for (String cl : clustererLabels) {
	          labels.add(cl);
	        }
	      }
	    }
	    if (KWsL2.contains("COMPLAINT") && KWsL2.size() == 1 && KWsL3.contains("SA")
	        && KWsL3.size() == 1){
	    		labels.add("SA");
		        labels.add("COMPLAINT");
		        labels.add("DATA_SUBJECT_RIGHT");
		      }
	    // }
	    if (!labels.contains("SA") && labels.contains("COMPLAINT") && KWsL3.contains("SA")){
		    	labels.add("SA");
		        labels.add("DATA_SUBJECT_RIGHT");
		    }
	    return labels;
	  }
//----------------------------------------------------------------------------------------------------
//DATA_SUBJECT_RIGHT extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//LEGAL_BASIS extraction
//---------------------------------------------------------------------------------------------------- 
  private Set<String> predictLegalBasis(List<String> classifierLabels,
		    List<String> clustererLabels, List<String> KWs) {
		    List<String> legal_basis = FileHandler.readFromFile(METADATA_MODEL + "LEGAL_BASIS");
	  
		    Set<String> labels = new TreeSet<String>();
		    
		    List<String> KWsL2 = new ArrayList<String>(metadataL2);
		    KWsL2.retainAll(KWs);
		    // Legel_Basis relevant KWs
		    List<String> KWsL3 = new ArrayList<String>(metadataL3);
		    KWsL3.retainAll(KWs);

		    List<String> classification = new ArrayList<String>(classifierLabels);
		    classification.retainAll(legal_basis);

		    List<String> clustering = new ArrayList<String>(clustererLabels);
		    clustering.retainAll(legal_basis);
		    
		    System.out.println("cluster:" + clustererLabels);
		    System.out.println("classifier: " + classifierLabels);
		    System.out.println("KWs: ");
		    System.out.println(KWsL2);
		    System.out.println(KWsL3);

		    if (classifierLabels.contains("LEGAL_BASIS")) {
		      clustering.retainAll(KWsL2);

		      if (!clustering.isEmpty()) {
		        for (String clr : clustering) {
		          labels.add(clr);
		        }
		      }
		      if (!classification.isEmpty()) {
		        List<String> intersection = new ArrayList<String>(classification);
		              intersection.retainAll(KWsL2);
		        for (String cl : intersection) {
		          labels.add(cl);
		        }
		      }
		      if(KWsL2.contains("CONTRACT") && KWsL2.size()==1 && KWsL3.contains("CONTRACTUAL") && KWsL3.contains("TO_ENTER_CONTRACT") && KWsL3.size()==2)
		    	  labels.add("CONTRACT");
            labels.add("LEGAL_BASIS");
		    }
		    
		    else {
		      List<String> intersection = new ArrayList<String>(classification);
		      intersection.retainAll(KWsL2);
		        for (String cl : intersection) {
		          labels.add(cl);
		        }
		    }
		    
		    if (labels.contains("CONTRACT") && KWsL3.contains("TO_ENTER_CONTRACT")) {
		    	labels.add("TO_ENTER_CONTRACT");
		    }
		    if (labels.contains("CONTRACT") && KWsL3.contains("CONTRACTUAL")) {
		    	labels.add("CONTRACTUAL");
		    }
		    if (labels.contains("CONTRACT") && KWsL3.contains("STATUTORY")) {
		    	labels.add("STATUTORY");
		    }
		    return labels;
		    
		  }
//----------------------------------------------------------------------------------------------------
//LEGAL_BASIS extraction
//----------------------------------------------------------------------------------------------------
  
//----------------------------------------------------------------------------------------------------
//TRANSFER_OUTSIDE_EUROPE Extraction
//---------------------------------------------------------------------------------------------------- 
  private Set<String> predictTransferOutsideEurope(List<String> classifierLabels, List<String> clustererLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> transfer_outside_europe = FileHandler.readFromFile(METADATA_MODEL + "TRANSFER_OUTSIDE_EUROPE");
	    classifierLabels.retainAll(transfer_outside_europe);
	    
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);    
	    List<String> KWsL2 = new ArrayList<String>(metadataL2);
	    KWsL2.retainAll(KWs);
	    KWsL2.retainAll(transfer_outside_europe);
	    List<String> KWsL3 = new ArrayList<String>(metadataL3);
	    KWsL3.retainAll(KWs);

	    if (classifierLabels.contains("TRANSFER_OUTSIDE_EUROPE")) {	
	    	for (String cl : classifierLabels)
	        	labels.add(cl);
	    }
	    
	    else {
	    	clustererLabels.retainAll(KWs);
	    	if (!clustererLabels.isEmpty()) {
	    		for (String clr : clustererLabels)
	    			labels.add(clr);      
	       }
	    }
	    
	    if (labels.contains("TRANSFER_OUTSIDE_EUROPE")) {
	    	if (KWsL2.contains("SAFEGUARDS"))
	    		labels.add("SAFEGUARDS");
	    	if (KWsL2.contains("SPECIFIC_DEROGATION"))
	    		labels.add("SPECIFIC_DEROGATION");
	    	if (KWsL2.contains("ADEQUACY_DECISION"))
	    		labels.add("ADEQUACY_DECISION"); 	    	
	    }

	    if (KWsL3.contains("UNAMBIGUOUS_CONSENT")) {
	    	labels.add("UNAMBIGUOUS_CONSENT");
	    	labels.add("SPECIFIC_DEROGATION");
	    	labels.add("TRANSFER_OUTSIDE_EUROPE");
	    }
				
	    if (KWsL3.contains("BINDING_CORPORATE_RULES")) {
	    	labels.add("BINDING_CORPORATE_RULES");
	    	labels.add("SAFEGUARDS");
	    	labels.add("TRANSFER_OUTSIDE_EUROPE");
	    }
	    
	    if (KWsL3.contains("EU_MODEL_CLAUSES")) {
	    	labels.add("EU_MODEL_CLAUSES");
	    	labels.add("SAFEGUARDS");
	    	labels.add("TRANSFER_OUTSIDE_EUROPE");
	    }
	    
	    if (KWsL3.contains("COUNTRY") && KWsL2.contains("ADEQUACY_DECISION")) {
	    	labels.add("COUNTRY");
	    	labels.add("ADEQUACY_DECISION");
	    	labels.add("TRANSFER_OUTSIDE_EUROPE");
	    }
	    
	    if(labels.isEmpty() && KWsL2.contains("SAFEGUARDS")) {
	    	labels.add("SAFEGUARDS");
	    	labels.add("TRANSFER_OUTSIDE_EUROPE");
	    }
	    
	    if(labels.isEmpty() && KWsL2.contains("ADEQUACY_DECISION")) {
	    	labels.add("ADEQUACY_DECISION");
	    	labels.add("TRANSFER_OUTSIDE_EUROPE");
	    }
	    
	    if(labels.isEmpty() && KWsL1.contains("TRANSFER_OUTSIDE_EUROPE")) {
	    	labels.add("TRANSFER_OUTSIDE_EUROPE");
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//TRANSFER_OUTSIDE_EUROPE Extraction
//---------------------------------------------------------------------------------------------------- 

//----------------------------------------------------------------------------------------------------
//PD_ORIGIN Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictPDOrigin(List<String> classifierLabels, List<String> clustererLabels, List<String> KWs){

	    Set<String> labels = new TreeSet<String>();    
	    
	    List<String> KWsL2 = new ArrayList<String>(metadataL2);
	    KWsL2.retainAll(KWs);
	    List<String> KWsL3 = new ArrayList<String>(metadataL3);
	    KWsL3.retainAll(KWs);
	    
	    if (classifierLabels.contains("PD_ORIGIN")) {	    	
	    	classifierLabels.retainAll(KWs);
		    if (!classifierLabels.isEmpty()) {
		    	for (String cl : classifierLabels)
		    		labels.add(cl);
		    }		    
	    	clustererLabels.retainAll(KWs);
	    	if (!clustererLabels.isEmpty()) {	    		
	    		for (String clr : clustererLabels)
	    			labels.add(clr);
	    	}	    	    			    
	    }
	    
    	if(KWsL3.contains("THIRD_PARTY")) {
			labels.add("THIRD_PARTY");
			labels.add("INDIRECT");
			labels.add("PD_ORIGIN");
		} 		
		if(KWsL3.contains("PUBLICLY")) {
			labels.add("PUBLICLY");
			labels.add("INDIRECT");
			labels.add("PD_ORIGIN");
		}		
		if(KWsL3.contains("COOKIE")) {
			labels.add("COOKIE");
			labels.add("INDIRECT");
			labels.add("PD_ORIGIN");
		}
		
		if(KWsL2.contains("DIRECT")) {
			labels.add("DIRECT");
			labels.add("PD_ORIGIN");
		}
	    return labels;	    
  }
//----------------------------------------------------------------------------------------------------
//PD_ORIGIN Extraction
//----------------------------------------------------------------------------------------------------
 
//----------------------------------------------------------------------------------------------------
//PD_CATEGORY Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictPDCategory(List<String> classifierLabels, List<String> KWs){
	    List<String> pd_category = FileHandler.readFromFile(METADATA_MODEL + "PD_CATEGORY");	// METADATA_MODEL
	    Set<String> labels = new TreeSet<String>();
	    
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    
	    // PD_Category relevant KWs
	    List<String> KWsL2 = new ArrayList<String>(metadataL2);
	    KWsL2.retainAll(KWs);	    
	    KWsL2.retainAll(pd_category);
	    
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    //classification.retainAll(pd_category);
	    	    	
	    if (classifierLabels.contains("PD_CATEGORY")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
		    if (!intersection.isEmpty()) {
		    	for (String cl : intersection)
		    		labels.add(cl);
		    }
	    }
	    
    	if(KWsL2.contains("SPECIAL")) {
    		labels.add("SPECIAL");
    		labels.add("PD_CATEGORY");
    	}        
	    
	    if(labels.isEmpty() && KWsL1.contains("PD_CATEGORY")) {
	    	labels.add("PD_CATEGORY");
	    }
	    
	    return labels; 	    
  }
//----------------------------------------------------------------------------------------------------
//PD_CATEGORY Extraction
//----------------------------------------------------------------------------------------------------
    
//----------------------------------------------------------------------------------------------------
//TYPE Extraction
//----------------------------------------------------------------------------------------------------
    private Set<String> predictTYPE(Set<String> PDOs, Set<String> PDCs){	//, boolean PDO_flag, boolean PDC_flag, int PDO_context, int PDC_context){
	    Set<String> labels = new TreeSet<String>();
	    //if((PDOs.contains("THIRD_PARTY") || PDOs.contains("PUBLICLY") || PDO_flag) && (PDCs.contains("PD_CATEGORY") || PDC_flag)) {
	    if((PDOs.contains("THIRD_PARTY") || PDOs.contains("PUBLICLY")) && PDCs.contains("PD_CATEGORY")) {
	    	labels.add("TYPE");
	    }
	    
	    return labels; 	    
  }
//----------------------------------------------------------------------------------------------------
//TYPE Extraction
//----------------------------------------------------------------------------------------------------
  
//----------------------------------------------------------------------------------------------------
//RECIPIENTS Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictRecipients(List<String> classifierLabels, List<String> clustererLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    // Recipients relevant KWs
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("RECIPIENTS")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}	
	    }
	    
	    else if (!clustererLabels.isEmpty()) {
    		for (String clr : clustererLabels) {
    			if (KWsL1.contains(clr))
    				labels.add(clr);
	      	}	      
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//RECIPIENTS Extraction
//----------------------------------------------------------------------------------------------------
  
//----------------------------------------------------------------------------------------------------
//PD_TIME_STORED Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictPDTimeStored(List<String> classifierLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    // Recipients relevant KWs
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("PD_TIME_STORED")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}
	    }
	    
	    else {
	    	if(KWsL1.contains("PD_TIME_STORED") && !Cookie_Flag)	    	
	    		labels.add("PD_TIME_STORED");
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//PD_TIME_STORED Extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//PD_PROVISION_OBLIGED Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictPDProvisionObliged(List<String> classifierLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    // Recipients relevant KWs
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("PD_PROVISION_OBLIGED")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}
	    }
	    
	    else {
	    	if(KWsL1.contains("PD_PROVISION_OBLIGED"))	    	
	    		labels.add("PD_PROVISION_OBLIGED");
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//PD_PROVISION_OBLIGED Extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//PROCESSING_PURPOSES Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictProcessingPurposes(List<String> classifierLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    // Recipients relevant KWs
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("PROCESSING_PURPOSES")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}
	    }
	    
	    else {
	    	if(KWsL1.contains("PROCESSING_PURPOSES"))	    	
	    		labels.add("PROCESSING_PURPOSES");
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//PROCESSING_PURPOSES Extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//PD_SECURITY Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictPDSecurity(List<String> classifierLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    // Recipients relevant KWs
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("PD_SECURITY")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//PD_SECURITY Extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//AUTO_DECISION_MAKING Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictAutoDecisionMaking(List<String> classifierLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    // Recipients relevant KWs
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("AUTO_DECISION_MAKING")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//AUTO_DECISION_MAKING Extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//CHILDREN Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictChildren(List<String> classifierLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    // Recipients relevant KWs
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("CHILDREN")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//CHILDREN Extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//DPO Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictDPO(List<String> classifierLabels, List<String> KWs, boolean DPO_flag) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    List<String> KWsL3 = new ArrayList<String>(metadataL3);
	    KWsL3.retainAll(KWs);
	    	    	    	
	    if (classifierLabels.contains("DPO")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		if (!CName_flag)
	    			labels.add(cl);
	    	}
	    }
	    
	    if(labels.isEmpty()) {
	    	if(KWsL1.contains("DPO"))
	    		labels.add("DPO");
	    }
	    
	    if(labels.contains("DPO") || DPO_flag) {
		    if (EM_Flag || KWsL3.contains("EMAIL")){
		    	labels.add("EMAIL");
		    	labels.add("CONTACT");
		    	labels.add("DPO");
		    }
		    if ((AD_Flag || KWsL3.contains("LEGAL_ADDRESS")) && !Text_flag){
		    	labels.add("LEGAL_ADDRESS");
		    	labels.add("CONTACT");
		    	labels.add("DPO");
		    }
		    if (PH_Flag || KWsL3.contains("PHONE_NUMBER")){
		    	labels.add("PHONE_NUMBER");
		   		labels.add("CONTACT");
		    	labels.add("DPO");
		    }
	    }
	    
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//DPO Extraction
//----------------------------------------------------------------------------------------------------

//----------------------------------------------------------------------------------------------------
//CONTROLLER Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictController(List<String> classifierLabels, List<String> KWs, boolean C_flag) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    List<String> KWsL2 = new ArrayList<String>(metadataL2);
	    KWsL2.retainAll(KWs);
	    List<String> KWsL3 = new ArrayList<String>(metadataL3);
	    KWsL3.retainAll(KWs); 
	    	    	    	
	    if (classifierLabels.contains("CONTROLLER")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    	}
	    }
	    
	    if(labels.contains("CONTROLLER") && NE_Flag) {
        labels.add("LEGAL_NAME");
        labels.add("IDENTITY");
      }    
      
      if (CName_flag){
        labels.add("LEGAL_NAME");
        labels.add("IDENTITY");
        labels.add("CONTROLLER");       
      }
      
      if (CName_flag && (EM_Flag || AD_Flag || PH_Flag)){
        labels.add("LEGAL_NAME");
        labels.add("IDENTITY");
        labels.add("CONTACT");                
        labels.add("CONTROLLER");       
      }     
      
      if((labels.contains("CONTACT") || (C_flag)) && !(KWs.contains("DPO"))) {  
        
        if (EM_Flag) {          
          labels.add("EMAIL");
          labels.add("CONTACT");
          labels.add("CONTROLLER");
        }
        
        if (AD_Flag && !Text_flag && !Month_flag) {
            labels.add("LEGAL_ADDRESS");
            labels.add("CONTACT");
            labels.add("CONTROLLER");
          }
        
          if (PH_Flag || KWsL3.contains("PHONE_NUMBER")) {
            labels.add("PHONE_NUMBER");
            labels.add("CONTACT");
            labels.add("CONTROLLER");
          }
          
      }
      
      if(KWsL3.contains("REGISTER_NUMBER")) {
          labels.add("REGISTER_NUMBER");
          labels.add("IDENTITY");
          labels.add("CONTROLLER");
      }
		
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//CONTROLLER Extraction
//---------------------------------------------------------------------------------------------------- 

//----------------------------------------------------------------------------------------------------
//CONTROLLER_REPRESENTATIVE Extraction
//---------------------------------------------------------------------------------------------------- 
    private Set<String> predictControllerRepresentative(List<String> classifierLabels, List<String> KWs) {

	    Set<String> labels = new TreeSet<String>();
	    List<String> classification = new ArrayList<String>(classifierLabels);
	    
	    List<String> KWsL1 = new ArrayList<String>(metadataL1);
	    KWsL1.retainAll(KWs);
	    List<String> KWsL3 = new ArrayList<String>(metadataL3);
	    KWsL3.retainAll(KWs);
	    
	    	    	    	
	    if (classifierLabels.contains("CONTROLLER_REPRESENTATIVE")) {
	    	List<String> intersection = new ArrayList<String>(classification);
	    	intersection.retainAll(KWsL1);
	    	for (String cl : intersection) {
	    		labels.add(cl);
	    		if (KWsL1.contains("CONTROLLER"))
	    			labels.remove("CONTROLLER_REPRESENTATIVE");
	    	}
	    }
	    
	    if (CRName_flag) {
	    	labels.add("LEGAL_NAME");
	      labels.add("IDENTITY");
	      labels.add("CONTROLLER_REPRESENTATIVE");
	    }
	    
	    if (CRName_flag || KWsL1.contains("CONTROLLER_REPRESENTATIVE")) {
     
        labels.add("CONTROLLER_REPRESENTATIVE");
        if (CRName_flag) {
          labels.add("LEGAL_NAME");
          labels.add("IDENTITY");
        }

        if (EM_Flag) {          
            labels.add("EMAIL");
            labels.add("CONTACT");
        }
        if (AD_Flag){
          	labels.add("LEGAL_ADDRESS");
          	labels.add("CONTACT");
          } 

          if (PH_Flag){
          	labels.add("PHONE_NUMBER");
          	labels.add("CONTACT");
          }
          if (KWsL3.contains("REGISTER_NUMBER")) {
            labels.add("REGISTER_NUMBER");
            labels.add("IDENTITY");
          }       
      }
		
	    return labels;	    	    
	  }
//----------------------------------------------------------------------------------------------------
//CONTROLLER_REPRESENTATIVE Extraction
//---------------------------------------------------------------------------------------------------- 

  private List<String> getClassificationResults(UoA uoa) {
	    List<String> classifierLabels = new ArrayList<String>();
	    for (ClassificationResult classification : selectCovered(ClassificationResult.class, uoa)) {
	      FSArray probabilities = classification.getProbabilitiesOfClasses();
	      ClassProbabilityPair prob;
	      for (int i = 0; i < probabilities.size(); ++i) {
	        prob = (ClassProbabilityPair) probabilities.get(i);
	        if (prob.getProbability() >= probThreshold) {
	          if (!prob.getClassLabel().startsWith("NOT")) {
	            classifierLabels.add(prob.getClassLabel());
	          } else if (prob.getClassLabel().equals("NOT_APPLICABLE")) {
	            classifierLabels.add("N/A");
	          }
	        }
	      }
	    }
	    return classifierLabels;
  }

  private List<String> getClusteringResults(UoA uoa, String metadata, double threshold) {
	    List<String> softLabels = new ArrayList<String>();
	    metadataDists = new TreeMap<Double, String>();
	    for (ClusteringResult clustering : selectCovered(ClusteringResult.class, uoa)) {
	      FSArray distances = clustering.getDistancesToClusters();
	      ClusterDistancePair dist;
	      for (int i = 0; i < distances.size(); ++i) {
	        dist = (ClusterDistancePair) distances.get(i);
	        // check whether the distance passes a threshold
	        double diff = dist.getDistance();
	        if (maximize) {
	          diff = 1 - dist.getDistance();
	        }
	        if (diff <= threshold)
	          if (metadataDists.get(diff) == null)
	            metadataDists.put(diff, dist.getClusterLabel());
	          else
	            metadataDists.put(diff, metadataDists.get(diff) + ";" + dist.getClusterLabel());
	      }
	    }

	    Iterator<Entry<Double, String>> entriesIterator = metadataDists.entrySet().iterator();
	    int k = 0;
	    while (entriesIterator.hasNext()) {
	      Entry<Double, String> entry = entriesIterator.next();
	      softLabels.add(k, entry.getValue());
	      k++;

	    }
	    return softLabels;
  }

  private void newPredictedMetadata(JCas aJCas, int begin, int end, String M1, String M2, String M3, String M4) {
    PredictedMetadata pm = new PredictedMetadata(aJCas);
    pm.setBegin(begin);
    pm.setEnd(end);
    pm.setMetadata1(M1);
    pm.setMetadata2(M2);
    pm.setMetadata3(M3);
    pm.setMetadata4(M4);
    pm.addToIndexes();
  }
  
  private void postprocessing(JCas aJCas) {
	  List<PPTuple> tuples = new ArrayList<PPTuple>(select(aJCas, PPTuple.class));
	    for (int i = 0; i < tuples.size(); ++i) {
	      PPTuple tuple = tuples.get(i);
	      int n = 5;

	      boolean fromSame = true;
	      if (selectCovered(PredictedMetadata.class, tuple).size() > 1) {
	        String pm0 = selectCovered(PredictedMetadata.class, tuple).get(0).getMetadata1();
	        for (int j = 1; j < selectCovered(PredictedMetadata.class, tuple).size(); ++j) {
	          PredictedMetadata pm = selectCovered(PredictedMetadata.class, tuple).get(j);
	          String pm1 = pm.getMetadata1();

	          if (!pm1.equalsIgnoreCase(pm0)) {
	            fromSame = false;
	            break;
	          }
	        }
	        if (fromSame) {
	          continue;
	        }
	      }
	      String m1 = "DATA_SUBJECT_RIGHT";
	      for (PredictedMetadata pm : selectCovered(PredictedMetadata.class, tuple)) {
	        if (pm.getMetadata1().equals(m1)) {
	          if (pm.getMetadata2().contains("WITHDRAW_CONSENT")) {
	            continue;
	          }
	          int fromIndex = (i - n < 0 ? 0 : i - n);
	          int toIndex = (i + n >= tuples.size() ? tuples.size() - 1 : i + n);
	          List<PPTuple> neighbors = tuples.subList(fromIndex, toIndex);
	          neighbors.remove(tuple);
	          if (!inContext(neighbors, m1, 4)) {
	            pm.removeFromIndexes();
	            newPredictedMetadata(aJCas, tuple.getBegin(), tuple.getEnd(), "N/A", "X", "X", "X");
	            DSR_PP_Flag = true;
	          }
	        }
	      }
	    }
	  }
  
  private boolean inContext(List<PPTuple> neighbors, String m1, int n) {
	    boolean in = false;
	    for (PPTuple tuple : neighbors) {
	      for (PredictedMetadata pm : selectCovered(PredictedMetadata.class, tuple)) {
	        if (pm.getMetadata1().equals(m1)) {
	          in = true;
	          break;
	        }
	      }
	      if (in)
	        break;
	    }
	    return in;
	  }
  
//----------------------------------------------------------------------------------------------------
//Completeness Checking
//----------------------------------------------------------------------------------------------------

  boolean CC_DSR_LB(String MetadataName1, String MetadataName2, Set<String> DSRs, Set<String> LBs, String CC_DOCS_DIR, String documentTitle) throws IOException{

	  boolean flag = false;
	  
    if(!DSRs.contains("ACCESS") || !DSRs.contains("COMPLAINT") || !DSRs.contains("RECTIFICATION") || !DSRs.contains("RESTRICTION"))
    {
        String criterionText = "\n[C1] Violation:";
        String criterion="";
        String violation = " is/are not identified.\n";
        if(!DSRs.contains("ACCESS")) {
          criterion += " ACCESS";
        }
        if(!DSRs.contains("COMPLAINT")) {
          criterion += " COMPLAINT";
          
        }
        if(!DSRs.contains("RECTIFICATION")) {
          criterion += " RECTIFICATION";
        }
        if(!DSRs.contains("RESTRICTION")) {
          criterion += " RESTRICTION";
        }
        if(!criterion.equals("")){
          criterionText += criterion + violation;
          log.append(criterionText);
          flag = true;
        }
    }
    
    
    if(DSRs.contains("COMPLAINT") && !DSRs.contains("SA")) {
		  log.append("\n[C2] Warning: COMPLAINT is partially identified because SA is missing.\n");
		  flag = true;
	  }

	  if(LBs.contains("CONTRACT") && !DSRs.contains("PORTABILITY")) {
	  log.append("\n[C3] Violation: PORTABILITY is not identified.\n");
	  flag = true;
	  }

	  if((LBs.contains("LEGITIMATE_INTEREST") || LBs.contains("PUBLIC_FUNCTION")) && !DSRs.contains("OBJECT")) {
	  log.append("\n[C4] Violation: OBJECT is not identified.\n");
	  flag = true;
	  }


    if(LBs.contains("CONSENT")){
        String criterionText = "\n[C5] Violation:";
        String criterion="";
        String violation = " is/are not identified.\n";
        if(!DSRs.contains("ERASURE")){
          criterion += " ERASURE";
        }
        if(!DSRs.contains("OBJECT")){
          criterion += " OBJECT";
        }
        if(!DSRs.contains("PORTABILITY")){
          criterion += " PORTABILITY";
        }
        if(!DSRs.contains("WITHDRAW_CONSENT")){
          criterion += " WITHDRAW_CONSENT";
        }
        if(!criterion.equals("")){
          criterionText += criterion + violation;
          log.append(criterionText);
          flag = true;
        }
    }

	  return flag;
	  }

	  boolean CC_TOE(String MetadataName, Set<String> TOEs, String CC_DOCS_DIR, String documentTitle) throws IOException{

	  boolean flag = false;

		if (TOEs.contains("TRANSFER_OUTSIDE_EUROPE")) {
			  if(!(TOEs.contains("SPECIFIC_DEROGATION") || TOEs.contains("SAFEGUARDS") || TOEs.contains("ADEQUACY_DECISION"))) {
				  String criterion= "\n[C7] Warning: TRANSFER_OUTSIDE_EUROPE is partially identified because";
				  int count=0;
				  //log.append("\n[C6] CRITERION VIOLATED: TRANSFER_OUTSIDE_EUROPE Incomplete\n");
				  if(!TOEs.contains("SPECIFIC_DEROGATION")){
					  criterion += " SPECIFIC_DEROGATION";
					  count += 1;
				  }
				  if(!TOEs.contains("SAFEGUARDS")) {
					  criterion += " SAFEGUARDS";
					  count += 1;
				  }
				  if(!TOEs.contains("ADEQUACY_DECISION")) {
					  criterion += " ADEQUACY_DECISION";
					  count += 1;
				  }
				  if(count >1) {
					  criterion += " are missing.\n";
				  }else
					  criterion += " is missing.\n";
				  log.append(criterion);
				  flag = true;
			  }

			  if(TOEs.contains("SPECIFIC_DEROGATION") && (!TOEs.contains("UNAMBIGUOUS_CONSENT"))) {
				  //log.append("\n[C7] CRITERION VIOLATED: SPECIFIC_DEROGATION Incomplete\n");
				  String criterion ="\n[C8] Warning: SPECIFIC_DEROGATION is partially identified because UNAMBIGUOUS_CONSENT is missing.\n";
				  log.append(criterion);
				  flag = true;
			  }

			  if(TOEs.contains("SAFEGUARDS") && (!(TOEs.contains("EU_MODEL_CLAUSES") || TOEs.contains("BINDING_CORPERATE_RULES")))) {
				  //log.append("\n[C8] CRITERION VIOLATED: SAFEGUARDS Incomplete\n");
				  String criterion ="\n[C9] Warning: SAFEGUARDS is partially identified because";
				  int count=0;
				  if(!TOEs.contains("EU_MODEL_CLAUSES")) {
					  criterion += " EU_MODEL_CLAUSES";
					  count += 1;
				  }
				  if(!TOEs.contains("BINDING_CORPERATE_RULES")) {
					  criterion += " BINDING_CORPERATE_RULES";
					  count += 1;
				  }
				  if(count >1) {
					  criterion += " are missing.\n";
				  }else
					  criterion += " is missing.\n";
				  log.append(criterion);
				  flag = true;
			  }

			  if(TOEs.contains("ADEQUACY_DECISION") && (!(TOEs.contains("COUNTRY") || TOEs.contains("SECTOR") || TOEs.contains("TERRITORY")))) {
				  //log.append("\n[C9] CRITERION VIOLATED: ADEQUACY_DECISION Incomplete\n");
				  String criterion ="\n[C10] Warning: ADEQUACY_DECISION is partially identified because";
				  int count=0;
				  if(!TOEs.contains("COUNTRY")) {
					  criterion += " COUNTRY";
					  count += 1;
				  }
				  if(!TOEs.contains("SECTOR")) {
					  criterion += " SECTOR";
					  count += 1;
				  }
				  if(!TOEs.contains("TERRITORY")) {
					  criterion += " TERRITORY";
					  count += 1;
				  }
				  if(count >1) {
					  criterion += " are missing.\n";
				  }else
					  criterion += " is missing.\n";
				  log.append(criterion);
				  flag = true;
			  }
	  }else{
	  		log.append("\n[C6] Violation: TRANSFER_OUTSIDE_EUROPE is not identified.\n");
        flag = true;
	  }

	  return flag;
	  }

	  boolean CC_PDO(String MetadataName, Set<String> PDOs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;

			  if(!PDOs.contains("INDIRECT")) {
				  log.append("\n[C11] Violation: INDIRECT is not identified.\n");
				  flag = true;
			  }
			  
			  if(PDOs.contains("INDIRECT")){
				 String criterion = "";
			  	
			  	if((!(PDOs.contains("THIRD_PARTY") || PDOs.contains("PUBLICLY")))){
					   criterion = "\n[C12] Warning: INDIRECT is partially identified because";
					   int count=0;
			  		 if(!PDOs.contains("THIRD_PARTY")){
			  			 	criterion += " THIRD_PARTY";
			  			 	count +=1;
			  		 }
			  		 if(!PDOs.contains("PUBLICLY")){
			  			 
			  				criterion += " PUBLICLY";
			  				count +=1;
			  		 }
			  		 if(count>1) {
			  			 criterion += " are missing.\n";
			  		 }else
			  			criterion += " is missing.\n";
			  		
			  		
			  		log.append(criterion);
			  		flag=true;
			  	}
			  }
			  
		  return flag;
	  }

	  boolean CC_PDC(String MetadataName, Set<String> PDCs, Set<String> PDOs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
	
		  if(!PDCs.contains("PD_CATEGORY")) {
		  log.append("\n[C13] Violation: PD_CATEGORY is not identified.\n");
		  flag = true;
		  }
		  
		  if(!PDCs.contains("TYPE") && (PDOs.contains("THIRD_PARTY") || PDOs.contains("PUBLICLY"))) {
			  log.append("\n[C14] Warning: PD_CATEGORY is partially identified because TYPE is missing.\n");
			  flag = true;
		  }


		  return flag;
	  }

	  boolean CC_R(String MetadataName, Set<String> Rs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
	
		  if(!Rs.contains("RECIPIENTS")) {
		  log.append("\n[C15] Violation: RECIPIENTS is not identified.\n");
		  flag = true;
		  }
	
		  return flag;
	  }

	  boolean CC_PDTS(String MetadataName, Set<String> PDTSs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
	
		  if(!PDTSs.contains("PD_TIME_STORED")) {
		  log.append("\n[C16] Violation: PD_TIME_STORED is not identified.\n");
		  flag = true;
		  }
	
		  return flag;
	  }

	  boolean CC_PDPO(String MetadataName, Set<String> PDPOs, Set<String> LBs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
	
		  if(!PDPOs.contains("PD_PROVISION_OBLIGED") && (LBs.contains("TO_ENTER_CONTRACT") || LBs.contains("LEGAL_OBLIGATION"))) {
			  log.append("\n[C17] Violation: PD_PROVISION_OBLIGED is not identified.\n");
			  flag = true;
		  }
	
		  return flag;
	  }

	  boolean CC_PP(String MetadataName, Set<String> PPs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
	
		  if(!PPs.contains("PROCESSING_PURPOSES")) {
			  log.append("\n[C18] Violation: PROCESSING_PURPOSES is not identified.\n");
			  flag = true;
		  }
	
		  return flag;
	  }

	  boolean CC_DPO(String MetadataName, Set<String> DPOs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
		  String criterion = "";
		  
		  
		  if(!(DPOs.contains("EMAIL") || DPOs.contains("LEGAL_ADDRESS") || DPOs.contains("PHONE_NUMBER"))) {
			  
			  	criterion += "\n[C19] Violation:";
		        if(!DPOs.contains("EMAIL")){
		          criterion+= " EMAIL";
		        }
		        if(!DPOs.contains("LEGAL_ADDRESS")){
		          criterion+= " LEGAL_ADDRESS";
		        }
		        if(!DPOs.contains("PHONE_NUMBER")){
		          criterion+= " PHONE_NUMBER";
		        }
					  criterion += " is/are not identified.\n";
					  log.append(criterion);
						flag = true;  	
		  }
	
		  return flag;
	  }

	  boolean CC_C(String MetadataName, Set<String> Cs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
	
		  if(!Cs.contains("IDENTITY")) {
		        String criterion="\n[C20] Violation:";
		        if(!Cs.contains("LEGAL_NAME")){
		            criterion += " LEGAL_NAME";
		        }
		        if(!Cs.contains("REGISTER_NUMBER")){
		            criterion += " REGISTER_NUMBER";
		        }
					  log.append(criterion+" is/are not identified.\n");
		
					  flag = true;
		  }
	
		  //String criterion = "\n[C21] Violation: LEGAL_ADDRESS not identified.\n";
		  //log.append(criterion);
      if(!(Cs.contains("EMAIL") || Cs.contains("LEGAL_ADDRESS") || Cs.contains("PHONE_NUMBER"))) {
		  //if(!Cs.contains("CONTACT")) {
			  String criterion = "";
			  
				  
					criterion += "\n[C21] Violation:";
			        if(!Cs.contains("EMAIL")){
			        	
			          criterion+= " EMAIL";
			        }
			        if(!Cs.contains("LEGAL_ADDRESS")){
			          criterion+= " LEGAL_ADDRESS";
			        }
			        if(!Cs.contains("PHONE_NUMBER")){
			          criterion+= " PHONE_NUMBER";
			        }
					criterion += " is/are not identified.\n";
					log.append(criterion);	  	//String criterion = "";
					flag = true;  	
			  
		  }
	
		  return flag;
	  }

	  boolean CC_CR(String MetadataName, Set<String> CRs, String CC_DOCS_DIR, String documentTitle) throws IOException{

		  boolean flag = false;
	
		if(!CRs.contains("IDENTITY")) {
			  String criterion="\n[C22] Violation:";
		        if(!CRs.contains("LEGAL_NAME")){
		            criterion += " LEGAL_NAME";
		        }
		        if(!CRs.contains("REGISTER_NUMBER")){
		            criterion += " REGISTER_NUMBER";
		        }
		        log.append(criterion+" is/are not identified.\n");
		
		        flag = true;
		 }
	
		  
		  if(!CRs.contains("CONTACT")) {
			  		String criterion = "";
			  
				  
					criterion += "\n[C23] Violation:";
			        if(!CRs.contains("EMAIL")){
			        	
			          criterion+= " EMAIL";
			        }
			        if(!CRs.contains("LEGAL_ADDRESS")){
			          criterion+= " LEGAL_ADDRESS";
			        }
			        if(!CRs.contains("PHONE_NUMBER")){
			          criterion+= " PHONE_NUMBER";
			        }
					criterion += " is/are not identified.\n";
					log.append(criterion);	  	//String criterion = "";
					flag = true;  	
			  
		  }
	
		  return flag;
	  }
  
//----------------------------------------------------------------------------------------------------
//Completeness Checking
//----------------------------------------------------------------------------------------------------

}
