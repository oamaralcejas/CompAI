package com.snt.mainServlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
//import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
//import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

//import model.Document;


import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;





/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*1, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)   	// 100 MB
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletFileUpload uploader = null;
	private static final Logger LOGGER = Logger.getLogger( MainServlet.class.getName() );
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init()throws ServletException{
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
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

			if(!ServletFileUpload.isMultipartContent(request)){
				throw new ServletException("Content type is not multipart/form-data");
			}
			
			LOGGER.log( Level.INFO, "session id " +request.getSession().getId());
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-store");
			LOGGER.log(Level.INFO, "cache control: ", response.getHeader("Cache-Control"));
			
			Cookie cookies[]=request.getCookies(); 
			LOGGER.log( Level.INFO, "Cookies: ");
			//c.length gives the cookie count 
			for(int i=0;i<cookies.length;i++){  
			 LOGGER.log(Level.INFO,"Name: "+cookies[i].getName()+" & Value: "+cookies[i].getValue());
			}
			
			if(request.getSession().getAttribute("fileName") != null) {
				request.getSession().setAttribute("fileName",null);
			}
			if(request.getSession().getAttribute("fileAbsolutePath") != null) {
				request.getSession().setAttribute("fileAbsolutePath",null);
			}
			
			
			//PrintWriter out = response.getWriter();
			try {
				checkIfUIDirecttoryExist();
				String uploadFormat = request.getParameter("format");
				LOGGER.log( Level.INFO, "uploadFormat: {0}", uploadFormat );
				//System.out.println("uploadFormat: "+uploadFormat);
				if(uploadFormat.equals("url")) {
					String url = request.getParameter("url-value");
					LOGGER.log( Level.INFO, "url: {0}", url );
					File fileDocx =createWordFromUrl(url);
					request.getSession().setAttribute("fileName", fileDocx.getName());
					request.getSession().setAttribute("fileAbsolutePath", fileDocx.getAbsolutePath());
					getServletContext().getRequestDispatcher("/questionnaire.jsp").forward(request, response);
				}else if(uploadFormat.equals("word")) {
					Part filePart = request.getPart("fileToUpload"); // Retrieves <input type="file" name="file">
				    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
				    String extension =getFileExtension(fileName);
				    LOGGER.log( Level.INFO, "fileName: {0}", fileName );
				    LOGGER.log( Level.INFO, "extension: {0}", extension  );
				    
				    if(extension.contains(".doc")){
				    	File file = new File(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+fileName);
						
						LOGGER.log( Level.INFO, "Absolute Path at server: {0}", file.getAbsolutePath()  );
						InputStream fileContent = filePart.getInputStream();
						java.nio.file.Files.copy(
								fileContent, 
								file.toPath(), 
							    StandardCopyOption.REPLACE_EXISTING);
						request.getSession().setAttribute("fileName", fileName);
						request.getSession().setAttribute("fileAbsolutePath", file.getAbsolutePath());
						//session.setAttribute("fileName", fileName);
						//session.setAttribute("fileAbsolutePath", file.getAbsolutePath());
						//response.sendRedirect(response.encodeRedirectURL("questionnaire.jsp"));
						getServletContext().getRequestDispatcher("/questionnaire.jsp").forward(request, response);
				    }else {
				    	 request.setAttribute("error", "Please upload a file with a correct format according to your choice.");
				    	 request.setAttribute("goBackPage", "uploadinput.html");
				    	 getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
				     }
				}
				/*else if(uploadFormat.equals("pdf")) {
					Part filePart = request.getPart("fileToUpload"); // Retrieves <input type="file" name="file">
				    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
				    String extension =getFileExtension(fileName);
				    LOGGER.log( Level.INFO, "fileName: {0}", fileName );
				    LOGGER.log( Level.INFO, "extension: {0}", extension  );
				    if(extension.contains(".pdf")){
				    	File fileDocx =createWordFromPdFFile(filePart);
				    	request.getSession().setAttribute("fileName", fileDocx.getName());
						request.getSession().setAttribute("fileAbsolutePath", fileDocx.getAbsolutePath());

						getServletContext().getRequestDispatcher("/questionnaire.jsp").forward(request, response);
				    }*/else {
				    	request.setAttribute("error", "Please upload a file with a correct format according to your choice.");
				    	request.setAttribute("goBackPage", "uploadinput.html");
				    	getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
				    }
				}
				
				
			} catch (FileUploadException e) {
				LOGGER.log( Level.SEVERE, e.toString(), e );
				request.setAttribute("error", e.toString());
		    	request.setAttribute("goBackPage", "uploadinput.html");
		    	getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
			} catch (Exception e) {
				LOGGER.log( Level.SEVERE, e.toString(), e );
				request.setAttribute("error", e.toString());
		    	request.setAttribute("goBackPage", "uploadinput.html");
		    	getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
			}
			
		//}
		
		
		

	}
	
	private void checkIfUIDirecttoryExist() {
		if (!Files.isDirectory(Paths.get(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator))) {
	    	//File fileDir = new File(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator);
			File fileDir = new File(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator);
	        //Creating the directory
	        boolean bool = fileDir.mkdir();
	        if(bool){
	           //System.out.println("Directory created successfully");
	        	LOGGER.log( Level.INFO, "Directory created successfully");
	        }
	    }
		
	}

	public  String getTitleFromUrl(String urlString) throws URISyntaxException {

		
		String domain="";
		try {
	      URL url = new URL(urlString);
	      String nullFragment = null;
	      URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);
	      domain = uri.getHost();
		  
		  
	    } catch (MalformedURLException e) {
	      
	      LOGGER.log( Level.SEVERE, e.toString(), e );
	    } catch (URISyntaxException e) {
	    	LOGGER.log( Level.SEVERE, e.toString(), e );
	    }
		
		
		return domain.replace(".com", "").replace("www.", "").replace(".", "_");
	
	    
	}
	
	
	private File createWordFromUrl(String url) throws IOException, URISyntaxException {
		Document docJsoup = Jsoup.connect(url).get();
		XWPFDocument doc = new XWPFDocument();
		String documentTitle = getTitleFromUrl(url);
		//String documentTitle = "PP_from_Url";
		
	    String textTitle=docJsoup.title();
	    Elements resultParagraphs = docJsoup.select("p");
	    // Create a new paragraph in the word document, adding the extracted text
	    XWPFParagraph p = doc.createParagraph();
	    XWPFRun run = p.createRun();
	    run.setText(textTitle);
	    
	    for(Element pTag : resultParagraphs) {
	    	XWPFParagraph paragraph = doc.createParagraph();
		    XWPFRun runParagraph = paragraph.createRun();
		    runParagraph.setText(pTag.text());
		    runParagraph.addBreak();
	    }
	    // Adding a page break
	    //run.addBreak(BreakType.PAGE);


		// Write the word document
		File fileDocx = new File(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+documentTitle+".docx");
		FileOutputStream outDoc = new FileOutputStream(fileDocx.getAbsolutePath());
		doc.write(outDoc);
		
		// Close all open files
		outDoc.close();
		
		doc.close();
		return fileDocx;
	}

	private File  createWordFromPdFFile(Part fileItem) throws Exception {
		//File filePdf = new File(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+fileItem.getName());
		File filePdf = new File(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+Paths.get(fileItem.getSubmittedFileName()).getFileName().toString());
		
		InputStream fileContent = fileItem.getInputStream();
		java.nio.file.Files.copy(
				fileContent, 
				filePdf.toPath(), 
			    StandardCopyOption.REPLACE_EXISTING);
		
		//fileItem.write(filePdf);
		//text extraction with itext7
		XWPFDocument doc = new XWPFDocument();

		// Open the pdf file
		String pdf = filePdf.getAbsolutePath();
		PdfReader reader = new PdfReader(pdf);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);

		// Read the PDF page by page
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
		    TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
		    // Extract the text
		    String text=strategy.getResultantText();
		    // Create a new paragraph in the word document, adding the extracted text
		    XWPFParagraph p = doc.createParagraph();
		    XWPFRun run = p.createRun();
		    run.setText(text);
		    // Adding a page break
		    run.addBreak(BreakType.PAGE);
		}
		// Write the word document
		File fileDocx = new File(System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+FilenameUtils.removeExtension(fileItem.getName())+".docx");
		FileOutputStream outDoc = new FileOutputStream(fileDocx.getAbsolutePath());
		doc.write(outDoc);
		
		// Close all open files
		outDoc.close();
		reader.close();
		doc.close();
		return fileDocx;
	}

	private String getFileExtension(String fileName) {

	    int lastIndexOf = fileName.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return fileName.substring(lastIndexOf);
	}


}
