package reporting;

public abstract class ReportBuilder
{
    protected String pathToReport;

    public ReportBuilder(String pathToReport)
    {
        this.pathToReport = pathToReport;
    }

    public abstract void build();

    public abstract void addResultRow(ResultRow resultRow);
}
