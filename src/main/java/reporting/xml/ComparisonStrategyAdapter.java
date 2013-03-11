package reporting.xml;

import reporting.ComparisonStrategy;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ComparisonStrategyAdapter extends XmlAdapter<String, ComparisonStrategy>
{

    @Override public ComparisonStrategy unmarshal(final String comparisonStrategy) throws Exception
    {
        return ComparisonStrategy.getStrategyFromDescription(comparisonStrategy);
    }

    @Override public String marshal(final ComparisonStrategy comparisonStrategy) throws Exception
    {
        return comparisonStrategy.getValue();
    }
}
