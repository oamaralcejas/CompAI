package lu.svv.saa.linklaters.privacypolicies.webscraping;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lu.svv.saa.linklaters.privacypolicies.interfaces.storeinterfaces.Store;

public class WebScraping {
    private static final Logger LOGGER = Logger.getLogger( WebScraping.class.getName() );
    public static void getPrivacyPolicyFromStore(Store store,String category,String folder) throws IOException, IndexOutOfBoundsException, HttpStatusException {
    	Map<String,String> mapAppPrivacyPolicy=store.getPrivacyPolicy(category);
        for (String app : mapAppPrivacyPolicy.keySet()){
            try {
                Connection connection = Jsoup.connect(mapAppPrivacyPolicy.get(app));
                Document privacyPolicyDocument=connection.get();
                String privacyPolicyText=privacyPolicyDocument.body().text();
                LOGGER.log(Level.FINE,"This is the info: " + connection.response().statusCode());
                if(connection.response().statusCode() != 200) {
                	LOGGER.log(Level.SEVERE,"Here is a problem");
                	continue;
                }
                if(!privacyPolicyText.isEmpty()){
                    XWPFDocument document = new XWPFDocument();
                    FileOutputStream out = new FileOutputStream(folder+"/"+app+".docx");
                    XWPFParagraph paragraph = document.createParagraph();
                    XWPFRun run = paragraph.createRun();
                    run.setText(privacyPolicyText);
                    document.write(out);
                    //Close document
                    out.close();
                }

            }
            catch (HttpStatusException exception){
                LOGGER.log(Level.SEVERE,"httpEx"+exception.getMessage());
                continue;
            }
            catch (SocketException exception) {
            	LOGGER.log(Level.SEVERE,"SocketEx"+exception.getMessage());
                continue;
            }
            catch (Exception exception){
                LOGGER.log(Level.SEVERE,"GeneralEx"+exception.getMessage());
                continue;
            }
        }
    }




}
