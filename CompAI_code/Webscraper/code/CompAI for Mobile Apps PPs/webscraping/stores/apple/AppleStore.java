package lu.svv.saa.linklaters.privacypolicies.webscraping.stores.apple;

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

public class AppleStore implements Store {
    private final String APPLE_STORE_ENDPOINT="https://apps.apple.com/gb/charts/iphone";
    private static final Logger LOGGER = Logger.getLogger( AppleStore.class.getName() );
    private static final String CLASS_ATTRIBUTE_LIST_APPS = "we-lockup  targeted-link";
    private static final String A_TEXT_PRIVACY_POLICY="developer’s privacy policy";

    @Override
    public Map<String,String> getPrivacyPolicy(String category) throws IOException {
        Document documentEndPoint = getEndpoint(category);
        Map<String,String> mapAppsPrivacyPolicies=new HashMap<>();
        Elements listOfApps=documentEndPoint.getElementsByClass(CLASS_ATTRIBUTE_LIST_APPS);
        LOGGER.log(Level.INFO, "size list of apps: "+ listOfApps.size());
        for (Element appElement : listOfApps){
            String dataSafetyAppWebPage =appElement.attr("href");
            String appName=getAppName(appElement.attr("aria-label"));
            LOGGER.log(Level.INFO, "app name: "+ appName);
            LOGGER.log(Level.INFO, "link datasafety: "+ dataSafetyAppWebPage);
            Document dataSafetyDocument = Jsoup.connect(dataSafetyAppWebPage).get();
            String linkToPrivacyPolicy= getLinkToPrivacyPolicy(dataSafetyDocument);
            LOGGER.log(Level.INFO, "link to privacy policy website: "+ linkToPrivacyPolicy);
            mapAppsPrivacyPolicies.put(appName,linkToPrivacyPolicy);
        }
        return  mapAppsPrivacyPolicies;
    }

    private Document getEndpoint(String category) throws IOException {
        Document documentEndPoint= null;
        if (category==null){
            documentEndPoint= Jsoup.connect(APPLE_STORE_ENDPOINT).get();
        }else {
            String categoryName=category.split("-")[0];
            String subcategoryName=category.split("-")[1];
            LOGGER.log(Level.INFO, "categoryName: "+ categoryName);
            LOGGER.log(Level.INFO, "subcategoryName: "+ subcategoryName);
            if(categoryName.equals("app")){//app category
                documentEndPoint=Jsoup.connect(AppCategory.getSubCategoryEndPoint(subcategoryName)).get();
            }else if(categoryName.equals("games")) {//games category
                documentEndPoint= Jsoup.connect(GameCategory.getSubCategoryEndPoint(subcategoryName)).get();
            }
            //implement other categories here
        }
        return documentEndPoint;
    }

    private String getAppName(String attrValue){
        String appName="";
        //Number n. appName
        int index=attrValue.indexOf(".");
        appName=attrValue.substring(index+1);
        appName=appName.replaceAll("\\s+","");
        return appName;
    }

    private String getLinkToPrivacyPolicy(Document appDocument){
        Elements elements = appDocument.select("p > a");
        for (Element element : elements){
            if(element.text().contains(A_TEXT_PRIVACY_POLICY)){
                return element.attr("href");
            }
        }
        return null;
    }
}
