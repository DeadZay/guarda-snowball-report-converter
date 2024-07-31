package com.github.deadzay.report;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.deadzay.report.firstam.Product;
import com.github.deadzay.report.guarda.GuardaReport;
import com.github.deadzay.report.mapping.GuardaSnowballMapper;
import com.github.deadzay.report.mapping.SourceReportType;
import com.github.deadzay.report.snowball.Event;
import com.github.deadzay.report.snowball.SnowballReport;
import org.apache.commons.cli.*;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.IOUtils;
import org.mapstruct.factory.Mappers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        Option sourceFileOption = Option.builder("s").required(true)
                .argName("Source")
                .longOpt("source")
                .desc("Source report file")
                .hasArg(true)
                .optionalArg(true)
                .numberOfArgs(1)
                .converter(Converter.FILE)
                .build();
        options.addOption(sourceFileOption);

        Option sourceReportTypeOption = Option.builder("t").required(true)
                .argName("Type")
                .longOpt("type")
                .desc("Source report type")
                .hasArg(true)
                .optionalArg(true)
                .numberOfArgs(1)
                .converter(SourceReportType.UNKNOWN)
                .build();
        options.addOption(sourceReportTypeOption);

        Option targetFileOption = Option.builder("o")
                .argName("Output")
                .longOpt("output")
                .desc("Output report file")
                .hasArg(true)
                .optionalArg(true)
                .numberOfArgs(1)
                .converter(Converter.FILE)
                .build();
        options.addOption(targetFileOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine commandLine = parser.parse(options, args);

            File sourceReportFile = commandLine.getParsedOptionValue(sourceFileOption);
            File snowballReportFile;

            if (commandLine.hasOption(targetFileOption))
                snowballReportFile = commandLine.getParsedOptionValue(targetFileOption);
            else
                snowballReportFile = new File("snowball-report.csv");

//            TODO Добавить автоопределение типа исходного отчёта на основе содержимого (например заголовков в .csv)
            SourceReportType sourceReportType;
            if (commandLine.hasOption(sourceReportTypeOption))
                sourceReportType = commandLine.getParsedOptionValue(sourceReportTypeOption);
            else
                sourceReportType = SourceReportType.GUARDA;

            CsvMapper mapper = new CsvMapper();
            List<SnowballReport> snowballReports = new ArrayList<>();

            switch (sourceReportType) {
                case GUARDA -> {
                    List<GuardaReport> guardaList = IteratorUtils.toList(mapper.readerFor(GuardaReport.class)
                            .with(CsvSchema.emptySchema().withHeader()).readValues(sourceReportFile));

                    GuardaSnowballMapper guardaSnowballMapper = Mappers.getMapper(GuardaSnowballMapper.class);

                    snowballReports = guardaList.stream()
                            .map(guardaSnowballMapper::guardaToSnowball)
                            .toList();

                }
                case FIRSTAM -> {
//                    TODO Более элегантное решение (парсинг данных с открытой вкладки?)
                    List<String> lines = IOUtils.readLines(new FileReader(sourceReportFile));
                    List<List<String>> linesBlocks = new ArrayList<>();

                    List<String> linesBlock = new ArrayList<>();
                    for (int i = 0; i < lines.size(); i++) {
                        if (i % 7 == 0 && i != 0) {
                            linesBlocks.add(linesBlock);
                            linesBlock = new ArrayList<>();
                        }
                        linesBlock.add(lines.get(i));
                    }

                    for (List<String> block : linesBlocks) {
                        SnowballReport snowballReport = new SnowballReport();
                        snowballReport.setEvent(Event.Dividend);
                        snowballReport.setSymbol(Product.fromDescription(block.get(1)).getTicker());
                        snowballReport.setCurrency("RUB");
                        snowballReport.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(block.get(6)));
                        snowballReport.setQuantity(Double.parseDouble(block.get(4)
                                .replaceAll("\\s+", "")));
                        snowballReport.setFeeCurrency("RUB");
                        snowballReport.setFeeTax(snowballReport.getQuantity() -
                                Double.parseDouble(block.get(5).replaceAll("\\s+", "")));
                        snowballReport.setExchange("CUSTOM_HOLDING");

                        snowballReports.add(snowballReport);
                    }
                }
            }

            mapper.writer(mapper.schemaFor(SnowballReport.class).withHeader())
                    .writeValue(snowballReportFile, snowballReports);

        } catch (ParseException | IOException exception) {
            exception.printStackTrace(System.err);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
