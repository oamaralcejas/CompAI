package lu.svv.saa.linklaters.privacypolicies.interfaces.reportinterfaces;

import lu.svv.saa.linklaters.privacypolicies.model.PrivacyPolicy;
import java.util.List;

public interface Report {
    String REPORT_EXTENSION ="report_";
    String REPORT_FINAL_EXTENSION ="report_final";

    void createReportPerPrivacyPolicy(String outputFolder, PrivacyPolicy privacyPolicy);
    void createFinalReport(String outputFolder, List<PrivacyPolicy> listOfPrivacyPolicies);
}
