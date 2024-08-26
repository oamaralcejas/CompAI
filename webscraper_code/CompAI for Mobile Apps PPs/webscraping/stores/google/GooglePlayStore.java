package lu.svv.saa.linklaters.privacypolicies.webscraping.stores.google;

import lu.svv.saa.linklaters.privacypolicies.interfaces.storeinterfaces.Store;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GooglePlayStore implements Store {

    private static final Logger LOGGER = Logger.getLogger( GooglePlayStore.class.getName() );
//    private final String GOOGLE_PLAY_STORE_ENDPOINT="https://play.google.com/store/apps/collection/topgrossing?";
//    private final String GOOGLE_PLAY_STORE_ENDPOINT="https://play.google.com/store/apps";
//    private final String GOOGLE_PLAY_STORE_ENDPOINT="https://play.google.com/store/apps/category/FAMILY";	//https://play.google.com/store/apps/category/FAMILY?age=AGE_RANGE1, AGE_RANGE2, AGE_RANGE3

//    private final String GOOGLE_PLAY_STORE_ENDPOINT="https://play.google.com/store/apps?device=tablet";
    private final String GOOGLE_PLAY_STORE_ENDPOINT="https://play.google.com/store/games?device=tablet";
    
//    private final String CLASS_ATTRIBUTE_LIST_APPS="Si6A0c ZD8Cqc";	//For phone
    private final String CLASS_ATTRIBUTE_LIST_APPS="Si6A0c Gy4nib";		//For tablet
    
    private final String DATASAFETY_LINK="https://play.google.com/store/apps/datasafety?id=";

    private final String PRIVACY_POLICY_HTML_CLASS_ATTRIBUTE="GO2pB";
//    private final String PRIVACY_POLICY_HTML_CLASS_ATTRIBUTE="wkMJlb";

    @Override
    public Map<String,String> getPrivacyPolicy(String category) throws IOException {
        Document documentEndPoint=getEndpoint(category);
        Map<String,String> mapAppsPrivacyPolicies=new HashMap<>();
        Elements listOfApps=documentEndPoint.getElementsByClass(CLASS_ATTRIBUTE_LIST_APPS);
        LOGGER.log(Level.INFO, "size list of apps: "+ listOfApps.size());
        for (Element appElement : listOfApps){
            String hrefLink =appElement.attr("href");
            String appName=hrefLink.substring(hrefLink.indexOf("=")+1);
            LOGGER.log(Level.INFO, "app name: "+ appName);
            String dataSafetyAppWebPage=DATASAFETY_LINK+appName;
            LOGGER.log(Level.INFO, "link datasafety: "+ dataSafetyAppWebPage);
            Document dataSafetyDocument = Jsoup.connect(dataSafetyAppWebPage).get();
            Element elementToPrivacyPolicy = dataSafetyDocument.getElementsByClass(PRIVACY_POLICY_HTML_CLASS_ATTRIBUTE).get(0);
            if(dataSafetyDocument.getElementsByClass(PRIVACY_POLICY_HTML_CLASS_ATTRIBUTE).size()>1) {
            	elementToPrivacyPolicy=dataSafetyDocument.getElementsByClass(PRIVACY_POLICY_HTML_CLASS_ATTRIBUTE).get(1);
            }
            String linkToPrivacyPolicy= elementToPrivacyPolicy.attr("href");
            LOGGER.log(Level.INFO, "link to privacy policy website: "+ linkToPrivacyPolicy);
            mapAppsPrivacyPolicies.put(appName,linkToPrivacyPolicy);

        }
        return mapAppsPrivacyPolicies;
    }

    private Document getEndpoint(String category) throws IOException {
        Document documentEndPoint= null;
        if (category==null){
            documentEndPoint= Jsoup.connect(GOOGLE_PLAY_STORE_ENDPOINT).get();
        }
        return documentEndPoint;
        //implement other category
    }
}
