package lu.svv.saa.linklaters.privacypolicies;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.uima.UIMAException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import lu.svv.saa.linklaters.privacypolicies.interfaces.storeinterfaces.Store;
import lu.svv.saa.linklaters.privacypolicies.webscraping.StoreFactory;
import lu.svv.saa.linklaters.privacypolicies.webscraping.WebScraping;

public class Main {

    String inputFolder;

    String outputFolder;

    String questionnaireFolder;

    String outputFormat;

    JSONObject defaulQuestionnaire;

    JSONObject store;

    String storeName;

    String category=null;

    @Parameter(names = {"--settings-file","-s"},description = "Settings file to be provided to run the experiment.",required = true)
    String settingsFile;


    private static final Logger LOGGER = Logger.getLogger( Main.class.getName() );
    public static void main(String[] args) throws UIMAException, IOException, ParseException {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();

    }

    public void run() throws UIMAException, IOException, ParseException {
        LOGGER.log(Level.INFO, "settings file "+ settingsFile);
        Object objectSettings = new JSONParser().parse(new FileReader(settingsFile));
        JSONObject jsonSettings = (JSONObject) objectSettings;
        inputFolder=(String) jsonSettings.get("input-folder");
        questionnaireFolder=(String) jsonSettings.get("questionnaire-folder");
        outputFolder=(String) jsonSettings.get("output-folder");
        outputFormat=(String) jsonSettings.get("output-format");
        defaulQuestionnaire=(JSONObject) jsonSettings.get("default-questionnaire");
        LOGGER.log(Level.INFO, "input folder: "+ inputFolder);
        LOGGER.log(Level.INFO, "output folder  "+ outputFolder);
        LOGGER.log(Level.INFO, "questionnaire folder  "+ questionnaireFolder);
        LOGGER.log(Level.INFO, "output format "+ outputFormat);
        if (jsonSettings.containsKey("store")){ //go to the store and download the privacy policies, otherwise just consider the input folder
            store=(JSONObject) jsonSettings.get("store");
            storeName=((String) store.get("name"));
            category=((String) store.get("category"));
            LOGGER.log(Level.INFO, "store name "+ storeName);
            LOGGER.log(Level.INFO, "category name "+ category);
            StoreFactory storeFactory = new StoreFactory();
            Store store=storeFactory.getStore(storeName);
            WebScraping.getPrivacyPolicyFromStore(store,category,inputFolder);
        }
    }
}
