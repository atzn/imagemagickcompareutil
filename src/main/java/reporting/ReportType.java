package reporting;

public enum ReportType
{
    HTML, CSV;

    public static ReportBuilder getReportBuilder(ReportType type, String pathToReport) {
        ReportBuilder reportBuilder = null;
        switch(type)
        {
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
