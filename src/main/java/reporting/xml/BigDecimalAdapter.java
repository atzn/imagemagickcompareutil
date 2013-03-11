package reporting.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.math.BigDecimal;

public class BigDecimalAdapter extends XmlAdapter<BigDecimal, BigDecimal>
{

    @Override public BigDecimal unmarshal(final BigDecimal bigDecimal) throws Exception
    {
        return bigDecimal;
    }

    @Override public BigDecimal marshal(final BigDecimal bigDecimal) throws Exception
    {
        return bigDecimal.setScale(2);
    }
}
