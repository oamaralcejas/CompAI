package lu.svv.saa.linklaters.privacypolicies.interfaces.storeinterfaces;

import java.io.IOException;
import java.util.Map;

public interface Store {

    Map<String,String> getPrivacyPolicy(String category) throws IOException;




}
