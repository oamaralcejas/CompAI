package lu.svv.saa.linklaters.privacypolicies.output.word;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import lu.svv.saa.linklaters.privacypolicies.model.CriterionUI;
import lu.svv.saa.linklaters.privacypolicies.Data.Enum.CriterionStatus;
import lu.svv.saa.linklaters.privacypolicies.utils.StringUtilities;

public class BuildReportTable {

	//private static String GRAY = "808080";
	private static String RED ="ff0000";
	private static String ORANGE ="ffad33";
	//private static String GREEN ="40bf40";
	private static String DARK ="000000";
	//private CSVUtility csvContent;
	private Map<String,CriterionUI> mapCriteria;

	public BuildReportTable(Map<String,CriterionUI> mapCriteria) {
		//this.csvContent= csvContent;
		this.mapCriteria = mapCriteria;
	}
	
	public void buildTable(String criterion, XWPFTable table) {
		switch(criterion) {
			case StringUtilities.CRITERION_MANDATORY_RIGHTS:
				buildTableMandatoryRights(criterion,table);
				break;
			case StringUtilities.CRITERION_SUPERVISORY_AUTHORITY:
				buildTableSupervisoryAuthority(criterion,table);
				break;
			case StringUtilities.CRITERION_PORTABILITY_RIGHT:
				buildTablePortabilityRight(criterion,table);
				break;
			case StringUtilities.CRITERION_OBJECT_RIGHT:
				buildTableObjectRight(criterion,table);
				break;
			case StringUtilities.CRITERION_CONSENT_RELATED_RIGHTS:
				buildTableConsentRelatedRights(criterion,table);
				break;
			case StringUtilities.CRITERION_TRANSFER_OUTSIDE_EU:
				buildTableTransferOutsideEU(criterion,table);
				break;
			case StringUtilities.CRITERION_TRANSFER_OUTSIDE_EU_DETAILS:
				buildTableToeDetails(criterion,table);
				break;
			case StringUtilities.CRITERION_EXPLICIT_CONSENT:
				buildTableExplicitConsent(criterion,table);
				break;
			case StringUtilities.CRITERION_SAFEGUARDS_DETAILS:
				buildTableSafeguardsDetails(criterion,table);
				break;
			case StringUtilities.CRITERION_ADEQUACY_DECISION_DETAILS:
				buildTableAdequacyDecisionDetails(criterion,table);
				break;
			case StringUtilities.CRITERION_INDIRECT_DATA_COLLECTION:
				buildTableIndirectDataCollection(criterion,table);
				break;
			case StringUtilities.CRITERION_INDIRECT_COLLECTION_DETAILS:
				buildTableIndirectCollectionDetails(criterion,table);
				break;
			case StringUtilities.CRITERION_CATEGORY:
				buildTableCategory(criterion,table);
				break;
			case StringUtilities.CRITERION_CATEGORY_TYPE_COLLECTED_INDIRECTLY:
				buildTableCategoryTypeCollectedIndirectly(criterion,table);
				break;
			case StringUtilities.CRITERION_RECIPIENTS:
				buildTableRecipients(criterion,table);
				break;
			case StringUtilities.CRITERION_RETENTION_PERIOD:
				buildTableRetentionPeriod(criterion,table);
				break;
			case StringUtilities.CRITERION_FAILURE_TO_PROVIDE_DATA:
				buildTableFailureToProvideData(criterion,table);
				break;
			case StringUtilities.CRITERION_PROCESSING_PORPUSES:
				buildTableProcessingPurposes(criterion,table);
				break;
			case StringUtilities.CRITERION_DPO_CONTACT:
				buildTableDPO(criterion,table);
				break;
			case StringUtilities.CRITERION_CONTROLLER_IDENTITY:
				buildTableControllerIdentity(criterion,table);
				break;
			case StringUtilities.CRITERION_CONTROLLER_CONTACT:
				buildTableControllerContact(criterion,table);
				break;
			case StringUtilities.CRITERION_CONTROLLER_REPRESENTATIVE_IDENTITY:
				buildTableControllerRepresentativeIdentity(criterion,table);
				break;
			case StringUtilities.CRITERION_CONTROLLER_REPRESENTATIVE_CONTACT:
				buildTableControllerRepresentativeContact(criterion,table);
				break;
		}
	}

	private void buildTableDPO(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"EMAIL","DATA PROTECTION OFFICER.CONTACT.EMAIL ADDRESS");

		
		row = table.createRow();
		buildRow(row,criterion,"LEGAL_ADDRESS","DATA PROTECTION OFFICER.CONTACT.POSTAL ADDRESS");

		
		row = table.createRow();
		buildRow(row,criterion,"PHONE_NUMBER","DATA PROTECTION OFFICER.CONTACT.PHONE NUMBER");

		
	}

	private  void buildTableProcessingPurposes(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"PROCESSING_PURPOSES","PROCESSING PURPOSES");
		
		
	}

	private  void buildTableFailureToProvideData(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"TO_ENTER_CONTRACT","LEGAL BASIS.CONTRACT.ENTERING A CONTRACT");

		
		row = table.createRow();
		buildRow(row,criterion,"LEGAL_OBLIGATION","LEGAL BASIS.LEGAL OBLIGATION");

		
		row = table.createRow();
		buildRow(row,criterion,"PD_PROVISION_OBLIGED","PROVISION OF DATA");

		
	}

	private  void buildTableRetentionPeriod(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"PD_TIME_STORED","RETENTION PERIOD");
		
		
	}

	private  void buildTableRecipients(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"RECIPIENTS","RECIPIENTS");
		
	}

	private  void buildTableCategoryTypeCollectedIndirectly(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"THIRD_PARTY","DATA ORIGIN.INDIRECT.THIRD PARTIES");

		
		row = table.createRow();
		buildRow(row,criterion,"PUBLICLY","DATA ORIGIN.INDIRECT.PUBLIC SOURCES");

		
		row = table.createRow();
		buildRow(row,criterion,"TYPE","CATEGORY.TYPE OF PERSONAL DATA");

	}

	private  void buildTableCategory(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"PD_CATEGORY","CATEGORY");
		
		
	}

	private  void buildTableIndirectCollectionDetails(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"THIRD_PARTY","DATA ORIGIN.INDIRECT.THIRD PARTIES");

		
		row = table.createRow();
		buildRow(row,criterion,"PUBLICLY","DATA ORIGIN.INDIRECT.PUBLIC SOURCES");
		
		
	}

	private  void buildTableControllerIdentity(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"LEGAL_NAME","CONTROLLER.IDENTITY.LEGAL NAME");

		
		row = table.createRow();
		buildRow(row,criterion,"REGISTER_NUMBER","CONTROLLER.IDENTITY.REGISTER NUMBER");

		
	}

	private  void buildTableControllerContact(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"EMAIL","CONTROLLER.CONTACT.EMAIL ADDRESS");

		
		row = table.createRow();
		buildRow(row,criterion,"LEGAL_ADDRESS","CONTROLLER.CONTACT.POSTAL ADDRESS");

		
		row = table.createRow();
		buildRow(row,criterion,"PHONE_NUMBER","CONTROLLER.CONTACT.PHONE NUMBER");

		
	}

	private  void buildTableControllerRepresentativeIdentity(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"LEGAL_NAME","CONTROLLER REPRESENTATIVE.IDENTITY.LEGAL NAME");
  
		
		row = table.createRow();
		buildRow(row,criterion,"REGISTER_NUMBER","CONTROLLER REPRESENTATIVE.IDENTITY.REGISTER NUMBER");

		
	}

	private  void buildTableIndirectDataCollection(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"INDIRECT","DATA ORIGIN.INDIRECT");
		//row.getCell(0).setText("PD ORIGIN.INDIRECT");
		//row.getCell(1).setText(csvContent.getSentences("INDIRECT",""));
		
	}

	private  void buildTableAdequacyDecisionDetails(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"COUNTRY","TRANSFER OUTSIDE EU.ADEQUACY DECISION.COUNTRY");

		
		row = table.createRow();
		buildRow(row,criterion,"TERRITORY","TRANSFER OUTSIDE EU.ADEQUACY DECISION.TERRITORY");

		
		row = table.createRow();
		buildRow(row,criterion,"SECTOR","TRANSFER OUTSIDE EU.ADEQUACY DECISION.SECTOR");

		
	}

	private void buildTableSafeguardsDetails(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"EU_MODEL_CLAUSES","TRANSFER OUTSIDE EU.SAFEGUARDS.EU MODEL CLAUSES");

		
		row = table.createRow();
		buildRow(row,criterion,"BINDING_CORPORATE_RULES","TRANSFER OUTSIDE EU.SAFEGUARDS.BINDING CORPORATE RULES");

		
	}

	private void buildTableExplicitConsent(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"UNAMBIGUOUS_CONSENT","TRANSFER OUTSIDE EU.SPECIFIC DEROGATION.UNAMBIGUOS CONSENT");

		
	}

	private void buildTableToeDetails(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"ADEQUACY_DECISION","TRANSFER OUTSIDE EU.ADEQUACY DECISION");

		
		row = table.createRow();
		buildRow(row,criterion,"SAFEGUARDS","TRANSFER OUTSIDE EU.SAFEGUARDS");

		
		row = table.createRow();
		buildRow(row,criterion,"SPECIFIC_DEROGATION","TRANSFER OUTSIDE EU.SPECIFIC DEROGATION");

		
	}

	private void buildTableControllerRepresentativeContact(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"EMAIL","CONTROLLER REPRESENTATIVE.CONTACT.EMAIL ADDRESS");
	
		row = table.createRow();
		buildRow(row,criterion,"LEGAL_ADDRESS","CONTROLLER REPRESENTATIVE.CONTACT.POSTAL ADDRESS");

		
		row = table.createRow();
		buildRow(row,criterion,"PHONE_NUMBER","CONTROLLER REPRESENTATIVE.CONTACT.PHONE NUMBER");
		
		
	}

	private void buildTableTransferOutsideEU(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"TRANSFER_OUTSIDE_EUROPE","TRANSFER OUTSIDE EU");
		//row.getCell(0).setText("TRANSFER OUTSIDE EUROPE");
		//row.getCell(1).setText(csvContent.getSentences("TRANSFER_OUTSIDE_EUROPE",""));
		
	}

	private void buildTableConsentRelatedRights(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"CONSENT","LEGAL BASIS.CONSENT");
		
		
		row = table.createRow();
		buildRow(row,criterion,"ERASURE","DATA SUBJECT RIGHT.ERASURE");
		
		
		row = table.createRow();
		buildRow(row,criterion,"OBJECT","DATA SUBJECT RIGHT.OBJECT");
		
		row = table.createRow();
		buildRow(row,criterion,"PORTABILITY","DATA SUBJECT RIGHT.PORTABILITY");
		
		
		row = table.createRow();
		buildRow(row,criterion,"WITHDRAW_CONSENT","DATA SUBJECT RIGHT.WITHDRAWING CONSENT");

		
	}

	private  void buildTableObjectRight(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"LEGITIMATE_INTEREST","LEGAL BASIS.LEGITIMATE INTEREST");

		
		row = table.createRow();
		buildRow(row,criterion,"PUBLIC_FUNCTION","LEGAL BASIS.PUBLIC FUNCTION");

		
		row = table.createRow();
		buildRow(row,criterion,"OBJECT","DATA SUBJECT RIGHT.OBJECT");

		
	}

	private  void buildTablePortabilityRight(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"CONTRACT","LEGAL BASIS.CONTRACT");
		//row.getCell(0).setText("LEGAL BASIS.CONTRACT");
		//row.getCell(1).setText(csvContent.getSentences("CONTRACT",""));
		
		row = table.createRow();
		buildRow(row,criterion,"PORTABILITY","DATA SUBJECT RIGHT.PORTABILITY");
		//row.getCell(0).setText("DATA SUBJECT RIGHT.PORTABILITY");
		//row.getCell(1).setText(csvContent.getSentences("PORTABILITY",""));
		
	}

	private  void buildTableSupervisoryAuthority(String criterion, XWPFTable table) {
		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"SA","DATA SUBJECT RIGHT.COMPLAINT.SUPERVISORY AUTHORITY");

		
	}

	private  void buildTableMandatoryRights(String criterion, XWPFTable table) {
		

		XWPFTableRow row = table.createRow();
		buildRow(row,criterion,"ACCESS","DATA SUBJECT RIGHT.ACCESS");

		
		row = table.createRow();
		buildRow(row,criterion,"RECTIFICATION","DATA SUBJECT RIGHT.RECTIFICATION");
		

		
		row = table.createRow();
		buildRow(row,criterion,"RESTRICTION","DATA SUBJECT RIGHT.RESTRICTION");

		
		row = table.createRow();
		buildRow(row,criterion,"COMPLAINT","DATA SUBJECT RIGHT.COMPLAINT");


	}

	private void buildRow(XWPFTableRow row, String criterion, String metadata, String headerRow) {
		row.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(3259));
		row.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(5777));
		
		
		XWPFParagraph paragraph = row.getCell(0).addParagraph();
		row.getCell(0).removeParagraph(0);
        XWPFRun run = paragraph.createRun();
        //run.setColor(getColorText(criterion,originalNameOfMetadata,true));
        run.setText(headerRow);
        
        
        CriterionUI criterionUI=  mapCriteria.get(criterion);
        String colorSentence = getColorText(criterionUI.getMetadataStatus(metadata));
      

        //List<String> sentences=new ArrayList<String>(csvContent.getSentencesForWordReport(metadata,criterion));
        List<String> sentences = new ArrayList<String>(criterionUI.getSentences(metadata));
        //run.setColor(colorSentence);
        String firstSentence= sentences.get(0);
        if(firstSentence.equals("NOT FOUND") || firstSentence.equals("NOT REQUIRED")) {
        	paragraph = row.getCell(1).addParagraph();
        	row.getCell(1).removeParagraph(0);
        	run = paragraph.createRun();
        	run.setText(firstSentence);
        	run.setColor(colorSentence);
        }else {

        	if(sentences.size()>1) {
        		paragraph = row.getCell(1).addParagraph();
        		row.getCell(1).removeParagraph(0);
        		run = paragraph.createRun();
        		run.setText("1) "+firstSentence);
        		run.addCarriageReturn();
        		for(int i=1; i< sentences.size(); i++) {
        			paragraph = row.getCell(1).addParagraph();
        			run = paragraph.createRun();
        			run.setColor(colorSentence);
        			run.setText(i+1+") "+sentences.get(i));
        			run.addCarriageReturn();
        		}
        	}else {
        		paragraph = row.getCell(1).addParagraph();
        		row.getCell(1).removeParagraph(0);
        		run = paragraph.createRun();
        		run.setText(firstSentence);
        		run.setColor(colorSentence);
        	}

        }
        
        	
	}
	
	
	
	private String getColorText(CriterionStatus status) {

		switch(status) {
			case SATISFIED:
				return DARK;
			case VIOLATED:
				return RED;
			case WARNING:
				return ORANGE;
			default:
				return DARK;
				
		}
		
		
	}
}
