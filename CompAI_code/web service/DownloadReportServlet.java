package com.snt.mainServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;



/**
 * Servlet implementation class DownloadReportServlet
 */
@WebServlet("/DownloadReportServlet")
public class DownloadReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger( DownloadReportServlet.class.getName() );   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Cache-Control", "no-store");
		LOGGER.log(Level.INFO, "cache control: ", response.getHeader("Cache-Control"));
		LOGGER.log( Level.INFO, "sessionID " +request.getSession().getId());
		LOGGER.log( Level.INFO, "fileName from session " +request.getSession().getAttribute("fileName"));
		
		LOGGER.log( Level.INFO, "fileName from request " +request.getAttribute("fileName"));
		String fileName =(String)request.getSession().getAttribute("fileName");
		fileName = FilenameUtils.removeExtension(fileName);
		
		String filePath = System.getProperty("user.home")+File.separator+"UI_Compliance_Test"+File.separator+fileName+"_report.docx";
		LOGGER.log( Level.INFO, "fileName " +fileName);
		LOGGER.log( Level.INFO, "filePath " +filePath);
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);
         
       
         
        // obtains ServletContext
        ServletContext context = getServletContext();
         
        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {        
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
         
        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
         
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
         
        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();
         
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
         
        inStream.close();
        outStream.close();   
		
		
		
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
