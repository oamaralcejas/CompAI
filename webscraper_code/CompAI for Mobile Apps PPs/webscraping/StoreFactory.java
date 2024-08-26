package lu.svv.saa.linklaters.privacypolicies.webscraping;

import lu.svv.saa.linklaters.privacypolicies.interfaces.storeinterfaces.Store;
import lu.svv.saa.linklaters.privacypolicies.webscraping.stores.apple.AppleStore;
import lu.svv.saa.linklaters.privacypolicies.webscraping.stores.google.GooglePlayStore;

public class StoreFactory {
    private String storeName;
    public Store getStore(String storeName){
        if(storeName ==null){
            return null;
        }
        if(storeName.equals("google-play-store")){
            return new GooglePlayStore();
        }
        if(storeName.equals("apple-store")){
            return new AppleStore();
        }
        //implement other stores here
        return null;
    }
}
