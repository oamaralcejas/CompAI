package lu.svv.saa.linklaters.privacypolicies.output.csv;

import com.opencsv.CSVWriter;
import lu.svv.saa.linklaters.privacypolicies.Data.Enum.CriterionStatus;
import lu.svv.saa.linklaters.privacypolicies.interfaces.reportinterfaces.Report;
import lu.svv.saa.linklaters.privacypolicies.model.CriterionUI;
import lu.svv.saa.linklaters.privacypolicies.model.PrivacyPolicy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GenerateCsvReport implements Report {
    private final String[] HEADER_PER_PRIVACY_POLICY={"criterion-name","definition","status"};
    private final String[] HEADER_FINAL={"criterion-name","definition","satisfied","violated","warning","not applicable"};

    private final String CSV_EXTENSION=".csv";
    @Override
    public void createReportPerPrivacyPolicy(String outputFolder, PrivacyPolicy privacyPolicy) {
        FileWriter writer = null;
        try {
            writer=new FileWriter(outputFolder+"/"+Report.REPORT_EXTENSION+privacyPolicy.getName()+"_"+privacyPolicy.getStatus().name()+CSV_EXTENSION);
            // create CSVWriter object filewriter object as parameter
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeNext(HEADER_PER_PRIVACY_POLICY);
            for(String criterionName :privacyPolicy.getMapCriteria().keySet()) {
                CriterionUI criterionUI = privacyPolicy.getMapCriteria().get(criterionName);
                String [] line={criterionUI.getId(),criterionUI.getDefinition(),criterionUI.getStatus().name()};
                csvWriter.writeNext(line);
            }
            csvWriter.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createFinalReport(String outputFolder, List<PrivacyPolicy> listOfPrivacyPolicies) {
        FileWriter writer = null;
        CSVWriter csvWriter = null;
        /*try {
            writer=new FileWriter(outputFolder+"/"+Report.REPORT_FINAL_EXTENSION+CSV_EXTENSION);
            // create CSVWriter object filewriter object as parameter
            csvWriter = new CSVWriter(writer);
            csvWriter.writeNext(HEADER_PER_PRIVACY_POLICY);
            Map<String, Map<String,Integer>> criteriaStatusMap= new HashMap<>();
            List<Map<String,CriterionUI>> listCriteria= new ArrayList<>();
            final Set<String> listCriteriaNames= new HashSet<>();
            listOfPrivacyPolicies.forEach(privacyPolicy -> listCriteria.add(privacyPolicy.getMapCriteria()));
            listCriteria.stream().
                    filter(
                    //map->map.get("test").getStatus().equals(CriterionStatus.SATISFIED)
                    map-> map.containsKey("test") && map.get("test").getStatus().equals(CriterionStatus.SATISFIED)
            ).count();

            listOfPrivacyPolicies.forEach(privacyPolicy -> privacyPolicy.getMapCriteria().forEach((criterionName,criterionUI) -> listCriteriaNames.add(criterionName)));
            for (PrivacyPolicy privacyPolicy : listOfPrivacyPolicies){
                for(String criterionName :privacyPolicy.getMapCriteria().keySet()) {
                    CriterionUI criterionUI = privacyPolicy.getMapCriteria().get(criterionName);
                    if(criteriaStatusMap.containsKey(criterionName)){
                        switch (criterionUI.getStatus()){
                            case SATISFIED:
                                if(criteriaStatusMap.get(criterionName).containsKey("satisfied")){
                                    criteriaStatusMap.get(criterionName).put("satisfied", criteriaStatusMap.get(criterionName).get("satisfied")+1);
                                }else {
                                    criteriaStatusMap.get(criterionName).put("satisfied", 1);
                                }
                                break;
                            case WARNING:
                                if(criteriaStatusMap.get(criterionName).containsKey("warning")){
                                    criteriaStatusMap.get(criterionName).put("warning", criteriaStatusMap.get(criterionName).get("warning")+1);
                                }else {
                                    criteriaStatusMap.get(criterionName).put("warning", 1);
                                }
                                break;
                            case VIOLATED:
                                if(criteriaStatusMap.get(criterionName).containsKey("violated")) {
                                    criteriaStatusMap.get(criterionName).put("violated", criteriaStatusMap.get(criterionName).get("violated") + 1);
                                }else {
                                    criteriaStatusMap.get(criterionName).put("violated", 1);
                                }
                                break;
                            case NOT_APPLICABLE:
                                if(criteriaStatusMap.get(criterionName).containsKey("not applicable")) {
                                    criteriaStatusMap.get(criterionName).put("not applicable", criteriaStatusMap.get(criterionName).get("not applicable") + 1);
                                }else {
                                    criteriaStatusMap.get(criterionName).put("not applicable", 1);
                                }
                                break;
                        }
                    }else{
                        Map<String,Integer> statusMap=new HashMap<>();
                        switch (criterionUI.getStatus()){
                            case SATISFIED:
                                statusMap.put("satisfied", 1);
                                statusMap.put("warning", 0);
                                statusMap.put("violated", 0);
                                statusMap.put("not applicable", 0);
                                criteriaStatusMap.put(criterionName,statusMap);
                                break;
                            case WARNING:
                                statusMap.put("warning", 1);
                                statusMap.put("satisfied", 0);
                                statusMap.put("violated", 0);
                                statusMap.put("not applicable", 0);
                                criteriaStatusMap.put(criterionName,statusMap);
                                break;
                            case VIOLATED:
                                statusMap.put("violated", 1);
                                statusMap.put("warning", 0);
                                statusMap.put("satisfied", 0);
                                statusMap.put("not applicable", 0);
                                criteriaStatusMap.put(criterionName,statusMap);
                                break;
                            case NOT_APPLICABLE:
                                statusMap.put("not applicable", 1);
                                statusMap.put("warning", 0);
                                statusMap.put("violated", 0);
                                statusMap.put("satisfied", 0);
                                criteriaStatusMap.put(criterionName,statusMap);
                                break;
                        }
                    }
                }
            }

            csvWriter.close();

        }catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
