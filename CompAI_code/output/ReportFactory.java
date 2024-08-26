package lu.svv.saa.linklaters.privacypolicies.output;

import  lu.svv.saa.linklaters.privacypolicies.interfaces.reportinterfaces.Report;
import lu.svv.saa.linklaters.privacypolicies.output.csv.GenerateCsvReport;
import lu.svv.saa.linklaters.privacypolicies.output.json.GenerateJsonReport;
import lu.svv.saa.linklaters.privacypolicies.output.word.GenerateWordReport;

public class ReportFactory {
    private String reportFormat;
    public Report getReportMethod(String reportFormat){
        if(reportFormat ==null){
            return null;
        }
        //if(reportFormat.equals("docx")){
            //return new GenerateWordReport();
        //}
        if(reportFormat.equals("json")){
            return new GenerateJsonReport();
        }
        if(reportFormat.equals("csv")){
            return new GenerateCsvReport();
        }
        //implements other format
        return null;
    }


}
