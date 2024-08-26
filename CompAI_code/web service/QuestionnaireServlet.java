package com.snt.mainServlet;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.uima.UIMAException;
import org.javatuples.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pipelines.RunProcess; 
import type.Criterion;
import type.IncompletenessType;
import model.CriterionUI;
import model.CriterionUI.CriterionStatus;
import model.Questionnaire;
import utilities.CriteriaManager;
import utilities.GenerateReport;
import utilities.StringUtilities;

/**
 * Servlet implementation class QuestionnaireServlet
 */
@WebServlet("/QuestionnaireServlet")
public class QuestionnaireServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger( QuestionnaireServlet.class.getName() );
	//private static final String NOT_APPLICABLE= "Not applicable because";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionnaireServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.setHeader("Cache-Control", "no-store");
		LOGGER.log(Level.INFO, "cache control: ", response.getHeader("Cache-Control"));
		String reportFileName =null;
		String docPath = null;
		
		
		
		
		if(request.getSession().getAttribute("mapCriteria") != null) {
			request.getSession().setAttribute("mapCriteria",null);
			LOGGER.log( Level.INFO, "mapCriteria not null ");
		}
		if(request.getSession().getAttribute("criterionConf") != null) {
			request.getSession().setAttribute("criterionConf",null);
		}
		
		//HttpSession session = request.getSession();
		
		LOGGER.log( Level.INFO, "session id " +request.getSession().getId());
		LOGGER.log( Level.INFO, "fileName " +request.getSession().getAttribute("fileName"));
		LOGGER.log( Level.INFO, "fileAbsolutePath " +request.getSession().getAttribute("fileAbsolutePath"));
		reportFileName= (String)request.getSession().getAttribute("fileName");
		docPath = (String)request.getSession().getAttribute("fileAbsolutePath");

		

		Questionnaire questionnaire = new Questionnaire();
		String controllerName = StringUtilities.DUMMY_CONTROLLER_NAME;
		
		String controllerKnown = request.getParameter("radio-controller-name");

		if(controllerKnown.equals("Yes") && request.getParameter("controller-name") != null) {
			controllerName= request.getParameter("controller-name");//controller name
		}
		LOGGER.log( Level.INFO, "controllerName " +controllerName);
	    String transferPersonalData = request.getParameter("radio-transfer-outside-eu");//plan transfer outside eu
	    String otherRecipients = request.getParameter("radio-recipients");
	    LOGGER.log( Level.INFO, "otherRecipients " +otherRecipients);
		String controllerInEea = request.getParameter("radio-controller-in-eea");
		String []dpo = request.getParameterValues("radio-dpo");//checkboxes

		String howCollectData = request.getParameter("radio-data-collected");
		String place = request.getParameter("places-list").toString();
	    questionnaire.setControllerName(controllerName);
	    questionnaire.setTransferPersonalData(transferPersonalData);
	    questionnaire.setOtherRecipients(otherRecipients);
	    questionnaire.setDPO(dpo);
	    questionnaire.setHowToCollectData(howCollectData);
	    questionnaire.setControllerInEea(controllerInEea);
	    
	    


	    String controllerRepresentativeName=StringUtilities.DUMMY_CONTROLLER_REPRESENTATIVE_NAME;
	    questionnaire.setControllerRepresentativeName(controllerRepresentativeName);
	    
	    //controllerName="CONTROLLER_NAME";
	    if(!questionnaire.getControllerInEea()) {
	    	if(place.equals("Outside European Economic Area")) {
		    	questionnaire.setOutside(true);

		    }else{
		    	controllerRepresentativeName = request.getParameter("controller-representative-name");//name of controller reprenstative name
				questionnaire.setControllerRepresentativeName(controllerRepresentativeName);
		    }
	    }

	    URL resourceUrl = request.getServletContext().getResource("/WEB-INF/resource");
	    //reading json file
	    JSONParser jsonParser = new JSONParser();
	    JSONObject criterionConf= null;
        
        try (FileReader reader = new FileReader(resourceUrl.getPath().toString()+"configuration.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            criterionConf = (JSONObject) obj;
           
            System.out.println(criterionConf);
             
            
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
	    
	    
	    Map<String,CriterionUI> mapCriteria= CriteriaManager.initMapCriteria(criterionConf);
	    mapCriteria= CriteriaManager.updateStatusToNotApplicableBeforeProcessing(mapCriteria, questionnaire);

		RunProcess run = new RunProcess();

    	reportFileName = FilenameUtils.removeExtension(reportFileName);
    	run.setDocPath(docPath);
		run.setResourcesPath(resourceUrl.getPath().toString());
		try {
			Map<String,Criterion> criteriaFromBackend =run.metaDataExtraction(questionnaire.getControllerName(), questionnaire.getControllerRepresentativeName(),
					questionnaire.getControllerInEea(),questionnaire.getTransferPersonalData(), questionnaire.getOtherRecipients(), questionnaire.getDPO(),
					questionnaire.getOutside(), questionnaire.getHowToCollectData(),criterionConf);
			LOGGER.log( Level.INFO, "process done!" );
			FileWriter myWriterBackend = new FileWriter(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+"debugBackend"+reportFileName+".txt");
			StringBuilder builderBackend = new StringBuilder();
			CriterionStatus status;
			int counter =1;
			for (String criterionName : criteriaFromBackend.keySet()) {
				LOGGER.log( Level.INFO, "criterionName: "+ criterionName );
				
				Criterion c =criteriaFromBackend.get(criterionName);

				builderBackend.append(counter+") criterion name: "+ c.getTitle() + "\n" );
				builderBackend.append("status: "+ c.getStatus() + "\n" );
				builderBackend.append("incompleteness: "+ c.getIncompletenessType() + "\n" );
				if(c.getStatus().equals(CriterionStatus.VIOLATED.name())) {
					String incompletenessType = c.getIncompletenessType();//check if it's violated
					if(incompletenessType.equals(IncompletenessType.VIOLATION.name())) {//c.getIncompletenessType() //Violated or warning
						status = CriterionStatus.VIOLATED;
					}else {
						status = CriterionStatus.WARNING;
						
					}
				}else if (c.getStatus().equals(CriterionStatus.NOT_APPLICABLE.name())){
					status = CriterionStatus.valueOf(c.getStatus());
				}else
					status = CriterionStatus.valueOf(c.getStatus());

				mapCriteria.get(criterionName).setStatus(status);

				
				LOGGER.log( Level.INFO, "status "+ status );
				//String metadataSentences = "";
				builderBackend.append("list of metadata: " + "\n" );
	            for(int i=0; i<c.getSentencesPerMetadata().size();++i) {
	            	
	            	LOGGER.log( Level.INFO, "metadata "+ c.getSentencesPerMetadata(i).getMetadata() );
	            	builderBackend.append("metadata "+ c.getSentencesPerMetadata(i).getMetadata() + "\n" );
	            	builderBackend.append("sentences "+ c.getSentencesPerMetadata(i).getSentences()+ "\n" );
	            	//c.getSentencesPerMetadata().
	            	LOGGER.log( Level.INFO, "sentences "+ c.getSentencesPerMetadata(i).getSentences() );
	            	mapCriteria.get(criterionName).setMetadatasPerSentences(c.getSentencesPerMetadata(i).getMetadata(), c.getSentencesPerMetadata(i).getSentences());
	            }
	          
	            mapCriteria.get(criterionName).setSentenceLine(2);
	           
	            builderBackend.append("\n\n\n" );
				
				counter +=1;
			}
			mapCriteria = CriteriaManager.updateStatusToNotApplicableAfterProcessing(mapCriteria);
			if(questionnaire.getTransferPersonalData() == false) {
				mapCriteria = CriteriaManager.writeNoteForTransferOutsideEuropeCriteria(mapCriteria);
			}
			if(questionnaire.getHowToCollectData() == 1) {
				mapCriteria= CriteriaManager.writeNoteForDataCollectionCriteria(mapCriteria);
			}
			myWriterBackend.write(builderBackend.toString());
			myWriterBackend.close();
			LOGGER.log( Level.INFO, "map done!" );
	    	writeDebugFile(mapCriteria,reportFileName);
	    	
	    	
	    	
	    	GenerateReport report = new GenerateReport(request,reportFileName,mapCriteria);
            report.runLogoSection();
            report.runTitleSection();
            report.runQuestionnaireSection(controllerName,controllerInEea, place,controllerRepresentativeName, transferPersonalData, otherRecipients, dpo, howCollectData);
            report.runSummarySection();
            report.runDetailsSection();
            report.runGlossaryTermsSection();
            report.runPageNumbering();
            report.saveReport();
            LOGGER.log( Level.INFO, "report done!" );

	    	
            request.getSession().setAttribute("mapCriteria", mapCriteria);
            request.getSession().setAttribute("criterionConf",criterionConf);
           
			getServletContext().getRequestDispatcher("/results.jsp").forward(request, response);

		//mapCriteria=run.getMapCriteria();

		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			LOGGER.log( Level.SEVERE, e.toString(), e );
			request.setAttribute("error", "Compai has encountered a problem in analyzing the privacy policy. Please send us an email to report the issue by clicking \"contact us\" on the link below.");
	    	request.setAttribute("goBackPage", "questionnaire.jsp");
	    	getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);

		} catch (InvalidFormatException e) {
			LOGGER.log( Level.SEVERE, e.toString(), e );
			request.setAttribute("error", "Compai has encountered a problem in analyzing the privacy policy. Please send us an email to report the issue by clicking \"contact us\" on the link below.");
	    	request.setAttribute("goBackPage", "questionnaire.jsp");
	    	getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
		}catch (Exception e) {
			LOGGER.log( Level.SEVERE, e.toString(), e );
			request.setAttribute("error", "Compai has encountered a problem in analyzing the privacy policy. Please send us an email to report the issue by clicking \"contact us\" on the link below.");
	    	request.setAttribute("goBackPage", "questionnaire.jsp");
	    	getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
		}



	}

	private void writeDebugFile(Map<String, CriterionUI> mapCriteria,String fileName) throws IOException {
		
		FileWriter myWriter = new FileWriter(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+"debug"+fileName+".txt");
		StringBuilder builder = new StringBuilder();
		int counter =1;
		for(CriterionUI c : mapCriteria.values()) {
			builder.append(counter+") criterion name: "+ c.getId() + "\n" );
			builder.append("criterion definition: "+ c.getDefinition() + "\n" );
			builder.append("status: "+ c.getStatus() + "\n" );
			builder.append("list of metadata: " + "\n" );
			for(String metadata : c.getMetadatasPerSentences().keySet()) {
				Pair<List<String>,CriterionStatus> pair = c.getMetadatasPerSentences().get(metadata);
				builder.append("metadata name: "+ metadata + " metadata status: " +  pair.getValue1().name() + " sentences found: \n");
				for(String sentence : pair.getValue0()) {
					builder.append(sentence+ "\n");
				}
			}
			builder.append("\n\n\n" );
			counter +=1;
		}
		myWriter.write(builder.toString());
		myWriter.close();
	}

}
