package reporting.xml;

import reporting.ResultRow;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "resultRows")
public class ResultRows
{
    @XmlElement(name = "resultRow", type = ResultRow.class)
    private List<ResultRow> resultRows;

    public ResultRows() {}

    public ResultRows(List<ResultRow> resultRows)
    {
        this.resultRows = resultRows;
    }

    public List<ResultRow> getResultRows()
    {
        return resultRows;
    }

    public void setResultRows(final List<ResultRow> resultRows)
    {
        this.resultRows = resultRows;
    }

    public void addResultRow(ResultRow resultRow)
    {
        this.resultRows.add(resultRow);
    }
}
