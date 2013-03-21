package reporting;

import reporting.csv.CSVReportBuilder;
import reporting.html.HTMLReportBuilder;
import reporting.xml.XMLReportBuilder;

public enum ReportType
{
    XML, HTML, CSV;

    public static String getFileExtension(ReportType type) {
        String extension = "";
        switch(type)
        {
            case XML:
                extension = "xml";
                break;
            case CSV:
                extension = "csv";
                break;
            case HTML:
                extension = "html";
                break;
        }
        return extension;
    }

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
