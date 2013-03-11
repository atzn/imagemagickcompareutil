<!DOCTYPE html>
<title>Image Comparison report</title>
<body>
    <table border="1px">
        <thead>
            <tr>
                <#list columnHeaders as columnHeader>
                    <th>${columnHeader}</th>
                </#list>
            </tr>
        </thead>
        <tbody>
            <#list resultRows as resultRow>
                <tr>
                    <td>${resultRow.getExpectedFileName()}</td>
                    <td>${resultRow.getActualFileName()}</td>
                    <td>${resultRow.getExpectedTotalPixels()}</td>
                    <td>${resultRow.getActualTotalPixels()}</td>
                    <td>${resultRow.getOutput()}</td>
                    <td>${resultRow.getPercentageDeviation()}</td>
                    <td>${resultRow.getStrategyUsed().getValue()}</td>
                    <td>${resultRow.getNotes()!""}</td>
                    <td>${resultRow.getCommandExecuted()}</td>
                </tr>
            </#list>
        </tbody>
    </table>
</body>