package lu.svv.saa.linklaters.privacypolicies.output.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lu.svv.saa.linklaters.privacypolicies.Data.Enum.CriterionStatus;
import lu.svv.saa.linklaters.privacypolicies.Data.Enum.PrivacyPolicyStatus;
import lu.svv.saa.linklaters.privacypolicies.interfaces.reportinterfaces.Report;
import lu.svv.saa.linklaters.privacypolicies.model.CriterionUI;
import lu.svv.saa.linklaters.privacypolicies.model.PrivacyPolicy;
import lu.svv.saa.linklaters.privacypolicies.utils.CriteriaManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class GenerateJsonReport implements Report {
    private final String JSON_EXTENSION=".json";
    @Override
    public void createReportPerPrivacyPolicy(String outputFolder, PrivacyPolicy privacyPolicy) {
        FileWriter writer = null;

        // Try block to check for exceptions
        try {
            writer = new FileWriter(outputFolder+"/"+Report.REPORT_EXTENSION+privacyPolicy.getName()+"_"+privacyPolicy.getStatus().name()+JSON_EXTENSION);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("privacy-policy-name", privacyPolicy.getName());
            jsonObject.put("status",privacyPolicy.getStatus().name());
            jsonObject.put("number-of-criteria-violated",privacyPolicy.getNumberOfCriteriaViolated());
            jsonObject.put("number-of-criteria-in-warnings",privacyPolicy.getNumberOfCriteriaInWarnings());
            jsonObject.put("number-of-criteria-satisfied",privacyPolicy.getNumberOfCriteriaSatisfied());
            jsonObject.put("number-of-criteria-not-applicable",privacyPolicy.getNumberOfCriteriaNotApplicable());
            jsonObject.put("number-of-criteria-not-required",privacyPolicy.getNumberOfCriteriaNotRequired());
            JSONArray jsonArray = new JSONArray();
            for(String criterionName :privacyPolicy.getMapCriteria().keySet()){
                CriterionUI criterionUI = privacyPolicy.getMapCriteria().get(criterionName);
                JSONObject jsonCriterion = new JSONObject();
                jsonCriterion.put("criterion-name",criterionUI.getId());
                jsonCriterion.put("definition",criterionUI.getDefinition());
                jsonCriterion.put("status",criterionUI.getStatus().name());
                jsonArray.add(jsonCriterion);
            }
            jsonObject.put("criteria",jsonArray);
            String prettyJsonString = prettyFy(jsonObject);
            writer.write(prettyJsonString);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String prettyFy(JSONObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonObject.toJSONString());
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    @Override
    public void createFinalReport(String outputFolder, List<PrivacyPolicy> listOfPrivacyPolicies) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFolder+"/"+Report.REPORT_FINAL_EXTENSION+JSON_EXTENSION);
            JSONObject jsonObject = new JSONObject();
            int counterPrivacyPoliciesViolated=(int)listOfPrivacyPolicies.stream().filter(
                    pp->pp.getStatus().equals(PrivacyPolicyStatus.VIOLATED)
            ).count();
            jsonObject.put("number-of-privacy-policies-violated",counterPrivacyPoliciesViolated);
            int counterPrivacyPoliciesSatisfied=(int)listOfPrivacyPolicies.stream().filter(
                    pp->pp.getStatus().equals(PrivacyPolicyStatus.SATISFIED)
            ).count();
            jsonObject.put("number-of-privacy-policies-satisfied",counterPrivacyPoliciesSatisfied);
            int counterCriteriaViolated=0;
            int counterCriteriaWarnings=0;
            int counterCriteriaSatisfied=0;
            int counterCriteriaNotApplicable=0;
            int counterCriteriaNotRequired=0;
            Map<String,JSONObject> jsonMap=new HashMap<>();
            JSONArray jsonArray = new JSONArray();
            for(PrivacyPolicy privacyPolicy: listOfPrivacyPolicies){
                Map<String,CriterionUI> mapCriteria=privacyPolicy.getMapCriteria();
                for(String criterion : mapCriteria.keySet()){
                    if(jsonMap.containsKey(criterion)){
                        switch (mapCriteria.get(criterion).getStatus()){
                            case SATISFIED:
                                if(jsonMap.get(criterion).containsKey("satisfied")){
                                    jsonMap.get(criterion).put("satisfied", (int)jsonMap.get(criterion).get("satisfied")+1);
                                }else {
                                    jsonMap.get(criterion).put("satisfied", 1);
                                }
                                break;
                            case WARNING:
                                if(jsonMap.get(criterion).containsKey("warning")){
                                    jsonMap.get(criterion).put("warning", (int)jsonMap.get(criterion).get("warning")+1);
                                }else {
                                    jsonMap.get(criterion).put("warning", 1);
                                }
                                break;
                            case VIOLATED:
                                if(jsonMap.get(criterion).containsKey("violated")) {
                                    jsonMap.get(criterion).put("violated", (int) jsonMap.get(criterion).get("violated") + 1);
                                }else {
                                    jsonMap.get(criterion).put("violated", 1);
                                }
                                break;
                            case NOT_APPLICABLE:
                                if(jsonMap.get(criterion).containsKey("not applicable")) {
                                    jsonMap.get(criterion).put("not applicable", (int) jsonMap.get(criterion).get("not applicable") + 1);
                                }else {
                                    jsonMap.get(criterion).put("not applicable", 1);
                                }
                                break;
                        }
                    }else {
                        JSONObject jsonNewCriterion = new JSONObject();
                        jsonNewCriterion.put("criterion-name",criterion);
                        jsonNewCriterion.put("definition",mapCriteria.get(criterion).getDefinition());
                        switch (mapCriteria.get(criterion).getStatus()){
                            case SATISFIED:
                                jsonNewCriterion.put("satisfied", 1);
                                jsonNewCriterion.put("warning", 0);
                                jsonNewCriterion.put("violated", 0);
                                jsonNewCriterion.put("not applicable", 0);
                                jsonMap.put(criterion,jsonNewCriterion);
                                break;
                            case WARNING:
                                jsonNewCriterion.put("warning", 1);
                                jsonNewCriterion.put("satisfied", 0);
                                jsonNewCriterion.put("violated", 0);
                                jsonNewCriterion.put("not applicable", 0);
                                jsonMap.put(criterion,jsonNewCriterion);
                                break;
                            case VIOLATED:
                                jsonNewCriterion.put("violated", 1);
                                jsonNewCriterion.put("warning", 0);
                                jsonNewCriterion.put("satisfied", 0);
                                jsonNewCriterion.put("not applicable", 0);
                                jsonMap.put(criterion,jsonNewCriterion);
                                break;
                            case NOT_APPLICABLE:
                                jsonNewCriterion.put("not applicable", 1);
                                jsonNewCriterion.put("warning", 0);
                                jsonNewCriterion.put("violated", 0);
                                jsonNewCriterion.put("satisfied", 0);
                                jsonMap.put(criterion,jsonNewCriterion);
                                break;
                        }
                        jsonMap.put(criterion,jsonNewCriterion);
                    }
                }
                counterCriteriaViolated += CriteriaManager.filterMapByStatus(mapCriteria,CriterionStatus.VIOLATED).size();
                counterCriteriaWarnings += CriteriaManager.filterMapByStatus(mapCriteria,CriterionStatus.WARNING).size();
                counterCriteriaNotApplicable += CriteriaManager.filterMapByStatus(mapCriteria,CriterionStatus.NOT_APPLICABLE).size();
                counterCriteriaSatisfied += CriteriaManager.filterMapByStatus(mapCriteria,CriterionStatus.SATISFIED).size();
                counterCriteriaNotRequired += CriteriaManager.filterMapByStatus(mapCriteria,CriterionStatus.NOT_REQUIRED).size();
            }
            jsonObject.put("number-of-criteria-satisfied",counterCriteriaSatisfied);
            jsonObject.put("number-of-criteria-violated",counterCriteriaViolated);
            jsonObject.put("number-of-criteria-in-warnings",counterCriteriaWarnings);
            jsonObject.put("number-of-criteria-not-applicable",counterCriteriaNotApplicable);
            jsonObject.put("number-of-criteria-not-required",counterCriteriaNotRequired);
            jsonMap.forEach((key,value)->jsonArray.add(value));
            jsonObject.put("criteria",jsonArray);
            String prettyJsonString = prettyFy(jsonObject);
            writer.write(prettyJsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
