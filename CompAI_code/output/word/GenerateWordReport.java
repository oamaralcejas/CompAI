package lu.svv.saa.linklaters.privacypolicies.output.word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import lu.svv.saa.linklaters.privacypolicies.interfaces.reportinterfaces.Report;
import lu.svv.saa.linklaters.privacypolicies.model.PrivacyPolicy;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;

import lu.svv.saa.linklaters.privacypolicies.model.CriterionUI;
import lu.svv.saa.linklaters.privacypolicies.Data.Enum.CriterionStatus;
import lu.svv.saa.linklaters.privacypolicies.model.Glossary;
import lu.svv.saa.linklaters.privacypolicies.utils.CriteriaManager;
import lu.svv.saa.linklaters.privacypolicies.utils.StringUtilities;

public class GenerateWordReport implements Report {

	private XWPFDocument wordDocument;
	private String fileName;
	private Map<String,CriterionUI> mapCriteria;

	private static final String GRAY = "808080";
	private static final String RED ="ff0000";
	private static final String ORANGE ="ffad33";
	private static final String GREEN ="40bf40";
	private static final String DARK ="000000";
	private static final String BLUE ="0000ff";
	private static final String heading1 = "My Heading 1";
    private static final String heading2 = "My Heading 2";
    private static final String heading3 = "My Heading 3";
    private static final String heading4 = "My Heading 4";

    
	
	
	
	
	public GenerateWordReport(String fileName, Map<String,CriterionUI> mapCriteria) {
		this.wordDocument = new XWPFDocument();
		createStyleForWordDocument();
		this.fileName = fileName;
		this.mapCriteria = mapCriteria;
	}

	@Override
	public void createReportPerPrivacyPolicy(String outputFolder, PrivacyPolicy privacyPolicy) {

		//runLogoSection();
		runTitleSection();
		runSummarySection();
		runDetailsSection();
		runGlossaryTermsSection();
		runPageNumbering();
		try {
			saveReport(outputFolder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void createFinalReport(String outputFolder, List<PrivacyPolicy> listOfPrivacyPolicies) {
		//throw NotImplementedException();
	}

	private void createStyleForWordDocument() {
		XWPFStyles styles = wordDocument.createStyles();
		addCustomHeadingStyle(wordDocument, styles, heading1, 1, 36, DARK);
		addCustomHeadingStyle(wordDocument, styles, heading2, 2, 28, DARK);
		addCustomHeadingStyle(wordDocument, styles, heading3, 3, 24, DARK);
		addCustomHeadingStyle(wordDocument, styles, heading4, 4, 20, DARK);
	}


	public void saveReport(String outputFolder) throws IOException {
		String reportFileName=Report.REPORT_EXTENSION+fileName;
		FileOutputStream out = new FileOutputStream(new File(outputFolder+reportFileName));
		wordDocument.write(out);
	    out.close();
	}
	
	
	
	
	private XWPFHyperlinkRun createHyperlinkRun(XWPFParagraph paragraph, String uri) {
		  String rId = paragraph.getDocument().getPackagePart().addExternalRelationship(
		    uri, 
		    XWPFRelation.HYPERLINK.getRelation()
		   ).getId();

		  CTHyperlink cthyperLink=paragraph.getCTP().addNewHyperlink();
		  cthyperLink.setId(rId);
		  cthyperLink.addNewR();

		  return new XWPFHyperlinkRun(
		    cthyperLink,
		    cthyperLink.getRArray(0),
		    paragraph
		   );
	}

	private void addCustomHeadingStyle(XWPFDocument wordDocument, XWPFStyles styles, String strStyleId, int headingLevel, int pointSize,
			String hexColor) {
		CTStyle ctStyle = CTStyle.Factory.newInstance();
	    ctStyle.setStyleId(strStyleId);
	    

	    CTString styleName = CTString.Factory.newInstance();
	    styleName.setVal(strStyleId);
	    ctStyle.setName(styleName);

	    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
	    indentNumber.setVal(BigInteger.valueOf(headingLevel));

	    // lower number > style is more prominent in the formats bar
	    ctStyle.setUiPriority(indentNumber);

	    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
	    ctStyle.setUnhideWhenUsed(onoffnull);

	    // style shows up in the formats bar
	    ctStyle.setQFormat(onoffnull);

	    // style defines a heading of the given level
	    CTPPr ppr = CTPPr.Factory.newInstance();
	    ppr.setOutlineLvl(indentNumber);
	    ctStyle.setPPr(ppr);

	    XWPFStyle style = new XWPFStyle(ctStyle);

	    CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
	    size.setVal(new BigInteger(String.valueOf(pointSize)));
	    CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
	    size2.setVal(new BigInteger("24"));
	    
	    CTFonts fonts = CTFonts.Factory.newInstance();
	    fonts.setAscii("Calibri");

	    CTRPr rpr = CTRPr.Factory.newInstance();
	    rpr.setRFonts(fonts);
	    rpr.setSz(size);
	    rpr.setSzCs(size2);

	    
	    CTColor color=CTColor.Factory.newInstance();
	    color.setVal(hexToBytes(hexColor));
	    rpr.setColor(color);
	    style.getCTStyle().setRPr(rpr);
	    // is a null op if already defined

	    style.setType(STStyleType.PARAGRAPH);
	    styles.addStyle(style);
		
	}
	
	public static byte[] hexToBytes(String hexString) {
	     HexBinaryAdapter adapter = new HexBinaryAdapter();
	     byte[] bytes = adapter.unmarshal(hexString);
	     return bytes;
	}

	
	
	
	public void runLogoSection() throws InvalidFormatException, IOException {
		XWPFParagraph logos = wordDocument.createParagraph();    
	    XWPFRun run = logos.createRun();
	    InputStream stream = getClass().getClassLoader().getResourceAsStream("/img/linklaters.png");
	    logos.setAlignment(ParagraphAlignment.LEFT);

	    String imgFile = "/img/linklaters.png";
	    //run.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(191.90), Units.toEMU(132.09));
	    //stream.close();
	    logos.setAlignment(ParagraphAlignment.RIGHT);
	    imgFile= "/img/snt_logo.png";
	    //stream = request.getServletContext().getResourceAsStream("/img/snt_logo.png");;
	    //run.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(261.07), Units.toEMU(126.99));
	    run.addBreak();
	    //stream.close();
	    
	}

	public void runGlossaryTermsSection() {
		XWPFParagraph title = wordDocument.createParagraph();
		title.setAlignment(ParagraphAlignment.LEFT);
		title.setStyle(heading1);
		XWPFRun titleRun = title.createRun();
		titleRun.setText("Glossary");
		titleRun.setBold(true);
		//titleRun.setFontSize(16);
		
		XWPFTable table = wordDocument.createTable();
		XWPFTableRow tableRowOne = table.getRow(0);
		
		XWPFParagraph header = tableRowOne.getCell(0).addParagraph();
		tableRowOne.getCell(0).removeParagraph(0);
        XWPFRun run = header.createRun();
        run.setText("Concept");
        run.setBold(true);
        
        header = tableRowOne.addNewTableCell().addParagraph();
		tableRowOne.getCell(1).removeParagraph(0);
        run = header.createRun();

        run.setText("Description");
        run.setBold(true);
        
        header = tableRowOne.addNewTableCell().addParagraph();
		tableRowOne.getCell(2).removeParagraph(0);
        run = header.createRun();

        run.setText("GDPR’s reference");
        run.setBold(true);
		
		
	    //tableRowOne.getCell(0).setText("Term");
	    //tableRowOne.addNewTableCell().setText("Description");
	    //tableRowOne.addNewTableCell().setText("Reference");
		
		Glossary glossary = new Glossary();
		Map<String,String[]> terms = glossary.getGlossaryTerms();
		
		for(String key : terms.keySet()) {
			String keyInReport= key.replaceAll("_", " ");
			String [] values = terms.get(key);
			XWPFTableRow row = table.createRow();
			row.getCell(0).setText(keyInReport);
			row.getCell(1).setText(values[0]);
			row.getCell(2).setText(values[1]);
			
			
			
		}
	}
	
	
	

	public void runSummaryFirstPart() {
		XWPFParagraph title = wordDocument.createParagraph();
		title.setAlignment(ParagraphAlignment.LEFT);
		
		title.setStyle(heading1);
		XWPFRun titleRun = title.createRun();
		//titleRun.addCarriageReturn();
		titleRun.setText("Summary");
		titleRun.setBold(true);
		//titleRun.setFontSize(16);
		
		if(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.VIOLATED).size() >0 && CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.WARNING).size() >0) {
			//violations and warnings
			
			buildSummaryFirstPart(StringUtilities.REPORT_SUMMARY_VIOLATION_LINE,
					StringUtilities.REPORT_SUMMARY_VIOLATION_WARNING_EXPLANATION.replace("$NUMBER_OF_VIOLATION", Integer.toString(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.VIOLATED).size()))
					.replace("$NUMBER_OF_WARNING", Integer.toString(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.WARNING).size())),RED);
			
			
		}else if(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.VIOLATED).size() >0 && CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.WARNING).size() ==0) {
			//only violations
			buildSummaryFirstPart(StringUtilities.REPORT_SUMMARY_VIOLATION_LINE,
					StringUtilities.REPORT_SUMMARY_VIOLATION_EXPLANATION_DE.replace("$NUMBER_OF_VIOLATION", Integer.toString(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.VIOLATED).size())),RED);
			
		}else if(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.WARNING).size() >0) {
			//only warnings
			buildSummaryFirstPart(StringUtilities.REPORT_SUMMARY_WARNING_LINE,
					StringUtilities.REPORT_SUMMARY_WARNING_EXPLANATION.replace("$NUMBER_OF_WARNING", Integer.toString(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.WARNING).size())),ORANGE);
			
		}else if(CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.WARNING).size() == 0 && CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.VIOLATED).size() == 0) {
			//pp passed
			buildSummaryFirstPart(StringUtilities.REPORT_SUMMARY_NO_VIOLATION_LINE, StringUtilities.REPORT_SUMMARY_NO_VIOLATION_EXPLANATION,GREEN);
			
		}
		
	
	}
	
	private void buildSummaryFirstPart(String line, String explanation, String color) {
		XWPFParagraph summaryLine = wordDocument.createParagraph();
		summaryLine.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun summaryLineRun = summaryLine.createRun();
		summaryLineRun.setBold(true);
		summaryLineRun.setText(line);
		summaryLineRun.setColor(color);
		summaryLineRun.addCarriageReturn();
		
		XWPFParagraph summaryExplanation = wordDocument.createParagraph();
		summaryExplanation.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun summaryExplanationRun = summaryExplanation.createRun();
		summaryExplanationRun.setText(explanation);
		summaryExplanationRun.setColor(BLUE);
		summaryLineRun.addCarriageReturn();
		
	}


	public void runSummarySection() {
		runSummaryFirstPart();
		runSummarySecondPart();

	}

	private void runSummarySecondPart() {
		// TODO Auto-generated method stub
		Map<String,CriterionUI> mapCriteriaViolated = CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.VIOLATED);
		Map<String,CriterionUI> mapCriteriaWarnings = CriteriaManager.filterMapByStatus(mapCriteria, CriterionStatus.WARNING);
		if(mapCriteriaViolated.size()> 0) {
			runViolationPart(mapCriteriaViolated);
		}
		if(mapCriteriaWarnings.size()> 0) {
			runWarningsPart(mapCriteriaWarnings);
		}
		wordDocument.createParagraph().createRun().addBreak(BreakType.PAGE);
	}


	/*private void runLastViolationPart(int countFinal) {
		if(countFinal==1){
			XWPFParagraph para1 = wordDocument.createParagraph();
			para1.setAlignment(ParagraphAlignment.BOTH);
			XWPFRun para1Run = para1.createRun();
			para1Run.setText("The remaining "+ countFinal +" criterion is met according to our analysis.");
			para1Run.addCarriageReturn();
    		
		}else{
			XWPFParagraph para1 = wordDocument.createParagraph();
			para1.setAlignment(ParagraphAlignment.BOTH);
			XWPFRun para1Run = para1.createRun();
			para1Run.setText("The remaining "+ countFinal +" criteria are met according to our analysis.");
			para1Run.addCarriageReturn();
			
			
		}
		
	}*/


	private void runWarningsPart(Map<String,CriterionUI> warnings) {
		List<Integer> order= new ArrayList<Integer>();
		for(String section : StringUtilities.SECTIONS){
			Map<String,CriterionUI> mapCriteriaSection= CriteriaManager.filterMapBySection(warnings, section);
			if(mapCriteriaSection.size() > 0){
				//out.println(BuildHtmlCode.buildSectionTitle(section));
				buildSectionTitle(section);
				order.clear();
				mapCriteriaSection.forEach((k,v)->order.add(v.getSectionOrder()));
				Collections.sort(order);
				int counter = 1;
				for(int ord : order) {
					for(String key : mapCriteriaSection.keySet()){
	        			if(mapCriteriaSection.get(key).getSection().equals(section) && mapCriteriaSection.get(key).getSectionOrder() == ord){
	        				buildLineSummary(counter,mapCriteriaSection.get(key).getSentenceSummary(),ORANGE);
	        				counter +=1;
		            		//contentWarnings.add(reportLine);
	        			}
	        		}
				}
				
				
			}
		}
		
		
		
	}
	
	private void runViolationPart(Map<String,CriterionUI> violations) {
		List<Integer> order= new ArrayList<Integer>();
		for(String section : StringUtilities.SECTIONS){
			Map<String,CriterionUI> mapCriteriaSection= CriteriaManager.filterMapBySection(violations, section);
			if(mapCriteriaSection.size() > 0){
				//out.println(BuildHtmlCode.buildSectionTitle(section));
				buildSectionTitle(section);
				order.clear();
				mapCriteriaSection.forEach((k,v)->order.add(v.getSectionOrder()));
				Collections.sort(order);
				int counter =1;
				for(int ord : order) {
					for(String key : mapCriteriaSection.keySet()){
	        			if(mapCriteriaSection.get(key).getSection().equals(section) && mapCriteriaSection.get(key).getSectionOrder() == ord){
	        				buildLineSummary(counter,mapCriteriaSection.get(key).getSentenceSummary(),RED);
	        				counter +=1;
		            		//contentWarnings.add(reportLine);
	        			}
	        		}
				}
				
				
			}
		}
		
	
	}

	private void buildSectionTitle(String section) {
		XWPFParagraph title = wordDocument.createParagraph();
		title.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun titleRun = title.createRun();
		titleRun.setText(section);
		titleRun.setBold(true);
	}

	private void buildLineSummary(int counter, String sencenceSummary, String color) {
		String[] split = sencenceSummary.split(StringUtilities.REASONING);
		buildSentenceToolResult(counter,split[0],color);
		buildReasoning(split[1]);
		
	}

	private void buildSentenceToolResult(int counter,String sentence,String color) {
		XWPFParagraph identParagrah = wordDocument.createParagraph();
		identParagrah.setIndentationLeft(720);
		XWPFRun identParagrahRun = identParagrah.createRun();
		identParagrahRun.setColor(color);
		identParagrahRun.setText(counter+") " + sentence);
		
	}


	private void buildReasoning(String reasoning) {
		XWPFParagraph identParagrah = wordDocument.createParagraph();
		identParagrah.setIndentationLeft(720);
		XWPFRun identParagrahRun = identParagrah.createRun();
		identParagrahRun.setItalic(true);
		identParagrahRun.setText(reasoning);
		
	}


	public void runTitleSection() {
		XWPFParagraph title = wordDocument.createParagraph();
		title.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun titleRun = title.createRun();
		titleRun.setText(StringUtilities.REPORT_TITLE.replace("$PRIVACY_POLICY_NAME", fileName));
		titleRun.setBold(true);
		titleRun.setFontSize(20);
		
		
		
		wordDocument.createParagraph().createRun().addBreak();
		XWPFParagraph statementPar = wordDocument.createParagraph();
		statementPar.setAlignment(ParagraphAlignment.BOTH);
		XWPFRun statementRun = statementPar.createRun();
		
		statementRun.setText(StringUtilities.REPORT_TITLE_STATEMENT);
		
		
		XWPFHyperlinkRun hyperlinkrun = createHyperlinkRun(statementPar, StringUtilities.TOOL_URL);
		  hyperlinkrun.setText(StringUtilities.TOOL_TITLE);
		  hyperlinkrun.setColor("0000FF");
		  hyperlinkrun.setUnderline(UnderlinePatterns.SINGLE);
		  
		  
		statementRun = statementPar.createRun();
		statementRun.setText(StringUtilities.REPORT_TITLE_STATEMENT_SECOND_PART.replace("$PRIVACY_POLICY_NAME", fileName).replace("$TOOL_NAME_EXPLANATION", StringUtilities.TOOL_TITLE_EXPLANATION)
				.replace("$TOOL_NAME", StringUtilities.TOOL_TITLE));
		
		//statementRun.setText(STATEMENT+" \""+fileName+"\".");
		statementRun.addCarriageReturn();
		//statementRun.setText(STATEMENT_THIRD_PART);
		statementRun.setItalic(true);
		
	}
	
	public void runTOCSection() {
		XWPFParagraph toc = wordDocument.createParagraph();
		wordDocument.createTOC();
	}
	
	public void runDetailsSection() {
		XWPFParagraph title = wordDocument.createParagraph();
		title.setAlignment(ParagraphAlignment.LEFT);
		title.setStyle(heading1);
		XWPFRun titleRun = title.createRun();
		titleRun.setText("Details");
		titleRun.setBold(true);
		
		XWPFParagraph detailsStatement = wordDocument.createParagraph();
		detailsStatement.setAlignment(ParagraphAlignment.BOTH);
		XWPFRun detailsStatementRun = detailsStatement.createRun();
		detailsStatementRun.setText(StringUtilities.DETAILS_STATEMENT.replace("$PRIVACY_POLICY_NAME", fileName));
		detailsStatementRun.setColor(BLUE);
		detailsStatementRun.addCarriageReturn();
		
		
		//section title
			//criterion definition
			//violation, no violation
		//table
		List<Integer> order = new ArrayList<Integer>();
		mapCriteria.forEach((k,v)->order.add(v.getSectionOrder()));
		Collections.sort(order);
		for(String section : StringUtilities.SECTIONS){
			buildSectionTitle(section);
			order.clear();
			wordDocument.createParagraph().createRun().addCarriageReturn();
			Map<String,CriterionUI> sectionCriteria = CriteriaManager.filterMapBySection(mapCriteria, section);
			sectionCriteria.forEach((k,v)->order.add(v.getSectionOrder()));
			Collections.sort(order);
			int counter =1;
			for(int ord : order) {
				for(String key : sectionCriteria.keySet()){
	    			if(sectionCriteria.get(key).getSection().equals(section) && sectionCriteria.get(key).getSectionOrder()==ord){
	    				buildDetailsCriterion(counter,sectionCriteria.get(key));
	    				wordDocument.createParagraph().createRun().addCarriageReturn();
	    				wordDocument.createParagraph().createRun().addCarriageReturn();
	    				counter +=1;
	    			}
	    		}
			}
			
		}
		

		wordDocument.createParagraph().createRun().addBreak(BreakType.PAGE);

	}
		
		
	

	private void buildDetailsCriterion(int counter, CriterionUI criterion) {
		// TODO Auto-generated method stub
		//criterion definition
		//violation, no violation
	//table
		buildDetailsCriterionDefinitionLine(counter,criterion.getId(),criterion.getDefinition());
		BuildReportTable reportTable = new BuildReportTable(mapCriteria);
			switch(criterion.getStatus()) {
				case NOT_APPLICABLE:
					buildDetailsResultLine(criterion.getSentenceLine(),GRAY);
					
					break;
				case SATISFIED:
					buildDetailsResultLine(criterion.getSentenceLine(),GREEN);
					
					displayTable(criterion.getId(),reportTable);
					break;
				case VIOLATED:
					buildDetailsResultLine(criterion.getSentenceLine(),RED);
					
					displayTable(criterion.getId(),reportTable);
					break;
				case WARNING:
					buildDetailsResultLine(criterion.getSentenceLine(),ORANGE);
					
					displayTable(criterion.getId(),reportTable);
					break;
			}
	}


	private void buildDetailsResultLine(String sentenceLine, String color) {
		XWPFParagraph paraResult = wordDocument.createParagraph();
		paraResult.setAlignment(ParagraphAlignment.BOTH);
		XWPFRun paraResultRun = paraResult.createRun();
		paraResultRun.setText(sentenceLine);
		paraResultRun.setColor(color);
	}


	private void buildDetailsCriterionDefinitionLine(int counter, String id, String definition) {
		XWPFParagraph identParagrah = wordDocument.createParagraph();
		identParagrah.setIndentationLeft(720);
		XWPFRun identParagrahRun = identParagrah.createRun();
		
		identParagrahRun.setText(counter+") " +id+": "+ definition);
		
	}


	public void runPageNumbering() {
		XWPFHeaderFooterPolicy headerFooterPolicy = wordDocument.getHeaderFooterPolicy();
		  if (headerFooterPolicy == null) 
			  headerFooterPolicy = wordDocument.createHeaderFooterPolicy();
		  
		  XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
		  //XWPFFooter footer = doc.createFooter(HeaderFooterType.DEFAULT);
		  
		  XWPFParagraph paragraph = footer.getParagraphArray(0);
		  
		  if (paragraph == null) 
			  paragraph = footer.createParagraph();
		  XWPFRun run=paragraph.createRun();
		  paragraph.setAlignment(ParagraphAlignment.CENTER);

		  run = paragraph.createRun();  
		  run.setText("Page ");
		  paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
		  run = paragraph.createRun();  
		  run.setText(" of ");
		  paragraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");
	}

	/*private String getColorText(String criterion) {
		String color=parseReport.getTextColorViolationLine(criterion);
		if(color.contains("red")) {
			return RED;
		}else if(color.contains("orange")) {
			return ORANGE;
		}else {
			return GREEN;
		}
		
	}*/

	private void displayTable(String criterion, BuildReportTable reportTable) {
		XWPFTable table = wordDocument.createTable();
		//table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(3259));
	    //table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(5777));
		XWPFTableRow tableRowOne = table.getRow(0);
		
		tableRowOne.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(3259));
		
		XWPFParagraph paragraph = tableRowOne.getCell(0).addParagraph();
		tableRowOne.getCell(0).removeParagraph(0);
        XWPFRun run = paragraph.createRun();
        //run.setColor(getColorText(criterion,originalNameOfMetadata,true));
        run.setText("Information Type (GDPR)");
        run.setBold(true);
	    //tableRowOne.getCell(0).setText("Metadata");
	    
	    
        
        //paragraph=tableRowOne.addNewTableCell().addParagraph();
        tableRowOne.addNewTableCell();
        tableRowOne.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(5777));
        paragraph=tableRowOne.getCell(1).addParagraph();
	    tableRowOne.getCell(1).removeParagraph(0);
	    run = paragraph.createRun();
        //run.setColor(getColorText(criterion,originalNameOfMetadata,true));
        run.setText("Corresponding text in the privacy policy");
        run.setBold(true);
	    
		//BuildReportTable.buildTable(criterion,table,csvContent,parseReport);
		reportTable.buildTable(criterion, table);
	}

}
