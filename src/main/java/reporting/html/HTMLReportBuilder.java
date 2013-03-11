package reporting.html;

import freemarker.template.Configuration;
import freemarker.template.Template;
import reporting.ReportBuilder;
import reporting.ResultRow;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTMLReportBuilder extends ReportBuilder
{
    private List<ResultRow> resultRows = new ArrayList<ResultRow>();
    private List<String> columnHeaders = new ArrayList<String>();

    public HTMLReportBuilder(String pathToHtmlReport)
    {
        super(pathToHtmlReport);
    }

    @Override
    public void build()
    {
        Writer file = null;
        Configuration configuration = new Configuration();

        try {
            configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates/"));
            Template template = configuration.getTemplate("htmlreport.ftl");
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("columnHeaders", columnHeaders);
            input.put("resultRows", resultRows);
            file = new FileWriter(new File(pathToReport));
            template.process(input, file);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception fileException) {
                }
            }
        }
    }

    @Override
    public void addResultRow(final ResultRow resultRow)
    {
        resultRows.add(resultRow);
        Map<String, String> columnValues = resultRow.getResultsAsMap();
        if(columnHeaders.size() == 0) {
            for(String headerValue : columnValues.keySet()) {
                columnHeaders.add(headerValue);
            }
        }
    }
}
