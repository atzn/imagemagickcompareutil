package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Properties;

import constants.PropertyValues;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.lang3.StringUtils;

import reporting.ComparisonStrategy;
import reporting.ReportBuilder;
import reporting.ReportType;
import reporting.ResultRow;

public class ImageMagickCompareUtil {
    private final String SETTINGS_PROPERTIES_PATH = "src/main/resources/settings.properties";
    private String actualScreensPath;
    private String expectedScreensPath;
    private String diffScreenshotPath;
    private String pathToImCompareBinary;
    private String resultsFilePath;
    private ReportBuilder reportBuilder;

    public ImageMagickCompareUtil() {
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(SETTINGS_PROPERTIES_PATH));
            actualScreensPath = properties.getProperty(PropertyValues.ACTUAL_SCREENSHOT_PATH);
            expectedScreensPath = properties.getProperty(PropertyValues.EXPECTED_SCREENSHOT_PATH);
            diffScreenshotPath = properties.getProperty(PropertyValues.DIFF_SCREENSHOT_PATH);
        	pathToImCompareBinary = System.getProperty("pathToIMCompareBinary");
            if(StringUtils.isEmpty(pathToImCompareBinary)) {
            	pathToImCompareBinary = properties.getProperty(PropertyValues.PATH_TO_IM_COMPARE_BINARY);
            }
            ReportType reportType = ReportType.valueOf(properties.getProperty(PropertyValues.RESULTS_FILE_TYPE));
            resultsFilePath = getResultsFilePath(properties, reportType);
            reportBuilder = ReportType.getReportBuilder(reportType, resultsFilePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getResultsFilePath(Properties properties, ReportType reportType) {
        String extension = ReportType.getFileExtension(reportType);
        StringBuilder resultsFilePath = new StringBuilder();
        resultsFilePath.append(properties.getProperty(PropertyValues.RESULTS_FILE_PATH));
        resultsFilePath.append(properties.getProperty(PropertyValues.RESULTS_FILE_NAME));
        resultsFilePath.append(".");
        resultsFilePath.append(extension);
        return resultsFilePath.toString();
    }

    private File[] getActualScreenshotFiles() {
        return new File(actualScreensPath).listFiles();
    }

    private File[] getExpectedScreenshotFiles() {
        return new File(expectedScreensPath).listFiles();
    }

    public void compareAndCaptureResults() {
    	checkIfDiffFolderExists();
        File[] actualFiles = getActualScreenshotFiles();
        Arrays.sort(actualFiles, NameFileComparator.NAME_COMPARATOR);
        File[] expectedFiles = getExpectedScreenshotFiles();
        Arrays.sort(expectedFiles, NameFileComparator.NAME_COMPARATOR);
        try {
            for(int i = 0; i < expectedFiles.length; i++) {
                Image expectedImage = new Image(expectedScreensPath + expectedFiles[i].getName());
                Image actualImage = new Image(actualScreensPath + actualFiles[i].getName());
                CommandBuilder commandBuilder = buildCommand(actualFiles[i], expectedFiles[i], expectedImage, actualImage);
                String commandOutput = executeCommandAndGetOutput(commandBuilder.getCommandAsArray());
                ResultRow resultRow = getResultRow(actualFiles[i], expectedFiles[i],
                                                    expectedImage, actualImage,
                                                    commandBuilder, commandOutput);
                reportBuilder.addResultRow(resultRow);
            }
            reportBuilder.build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	private void checkIfDiffFolderExists() {
		File diffFolder = new File(diffScreenshotPath);
    	try {
	    	if(!diffFolder.isDirectory()) {
	    		FileUtils.forceMkdir(diffFolder);
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}

    private ResultRow getResultRow(File actualFile, File expectedFile, Image expectedImage, Image actualImage,
                                   CommandBuilder commandBuilder, String commandOutput) throws IOException {
        ResultRow resultRow = new ResultRow();
        resultRow.setExpectedTotalPixels(expectedImage.getTotalPixels());
        resultRow.setActualTotalPixels(actualImage.getTotalPixels());
        resultRow.setExpectedFileName(expectedFile.getName());
        resultRow.setActualFileName(actualFile.getName());
        resultRow.setCommandExecuted(commandBuilder.toString());
        resultRow.setStrategyUsed(ComparisonStrategy.getStrategy(commandOutput));
        captureAndStoreResults(resultRow, commandOutput);
        return resultRow;
    }

    private CommandBuilder buildCommand(File actualFile, File expectedFile, Image expectedImage, Image actualImage) {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.setPathToImageMagickCompareBinary(pathToImCompareBinary);
        commandBuilder.setFirstImagePixels(expectedImage.getTotalPixels());
        commandBuilder.setSecondImagePixels(actualImage.getTotalPixels());
        commandBuilder.setFilePaths(expectedScreensPath + expectedFile.getName(),
                actualScreensPath + actualFile.getName(),
                diffScreenshotPath + expectedFile.getName());
        commandBuilder.build();
        return commandBuilder;
    }

	private String executeCommandAndGetOutput(String[] command) throws IOException,
			InterruptedException {
		Process process = Runtime.getRuntime().exec(command);
		StreamGobbler errorGobbler = gobbleStream(process);
		System.out.println("Exit Value: " + process.waitFor());
        return errorGobbler.getOutputLine();
	}

    private void captureAndStoreResults(ResultRow resultRow, String output) throws IOException {
        output = getFinalOutput(resultRow.getStrategyUsed(), output);
        BigDecimal percentageDeviation;
        if(resultRow.getExpectedTotalPixels().longValue() > resultRow.getActualTotalPixels().longValue()) {
        	percentageDeviation = calculatePercentageDeviation(resultRow.getExpectedTotalPixels(), output);
        } else {
        	percentageDeviation = calculatePercentageDeviation(resultRow.getActualTotalPixels(), output); 
        }
        resultRow.setPercentageDeviation(percentageDeviation);
        resultRow.setOutput(output);
    }

    private String getFinalOutput(ComparisonStrategy strategy, String output) {
        if(strategy.equals(ComparisonStrategy.SUB_IMAGE)) {
            output = output.replace(output.substring(output.indexOf("@"), output.length()), "").trim();
        }
        return output;
    }

    private BigDecimal calculatePercentageDeviation(BigDecimal totalImagePixels, String output) throws IOException {
        BigDecimal percentageDeviation;
        try {
            BigDecimal pixelDifference = new BigDecimal(output);
            percentageDeviation = pixelDifference.divide(totalImagePixels, 4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            percentageDeviation = BigDecimal.valueOf(-1);
        }
        return percentageDeviation.multiply(new BigDecimal(100));
    }

    private StreamGobbler gobbleStream(Process process) {
        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error Stream");
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Input Stream");
        errorGobbler.start();
        outputGobbler.start();
        return errorGobbler;
    }

    public static void main(String[] args) {
        new ImageMagickCompareUtil().compareAndCaptureResults();
    }
}
