package reporting.xml;

import reporting.ResultRow;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ResultRowXMLHandler
{
    public static void marshal(List<ResultRow> resultRows, File outputFile)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            JAXBContext context = JAXBContext.newInstance(ResultRows.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(new ResultRows(resultRows), writer);
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(JAXBException e)
        {
            e.printStackTrace();
        }
    }
}
