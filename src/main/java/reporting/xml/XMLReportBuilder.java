package reporting.xml;

import reporting.ReportBuilder;
import reporting.ResultRow;

import java.io.File;
import java.util.ArrayList;

public class XMLReportBuilder extends ReportBuilder
{
    private ResultRows resultRows;

    public XMLReportBuilder(String pathToXmlFile)
    {
        super(pathToXmlFile);
        resultRows = new ResultRows(new ArrayList<ResultRow>());
    }

    @Override public void build()
    {
        File xmlReportFile = new File(pathToReport);
        ResultRowXMLHandler.marshal(resultRows.getResultRows(), xmlReportFile);
    }

    @Override public void addResultRow(final ResultRow resultRow)
    {
        resultRows.addResultRow(resultRow);
    }
}
