package reporting;

import reporting.csv.CSVReportBuilder;
import reporting.html.HTMLReportBuilder;
import reporting.xml.XMLReportBuilder;

public enum ReportType
{
    XML, HTML, CSV;

    public static ReportBuilder getReportBuilder(ReportType type, String pathToReport) {
        ReportBuilder reportBuilder = null;
        switch(type)
        {
            case XML:
                reportBuilder = new XMLReportBuilder(pathToReport);
                break;
            case CSV:
                reportBuilder = new CSVReportBuilder(pathToReport);
                break;
            case HTML:
                reportBuilder = new HTMLReportBuilder(pathToReport);
                break;
        }
        return reportBuilder;
    }
}
