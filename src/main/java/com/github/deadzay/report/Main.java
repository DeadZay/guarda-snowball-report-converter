package com.github.deadzay.report;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.deadzay.report.guarda.GuardaReport;
import com.github.deadzay.report.snowball.SnowballDAO;
import org.apache.commons.cli.*;
import org.apache.commons.collections4.IteratorUtils;
import org.mapstruct.factory.Mappers;

import java.io.File;
import java.io.IOException;
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
            CommandLine line = parser.parse(options, args);

            File guardaReportFile = line.getParsedOptionValue(sourceFileOption);
            File snowballReportFile;

            if (line.hasOption(targetFileOption))
                snowballReportFile = line.getParsedOptionValue(targetFileOption);
            else
                snowballReportFile = new File("snowball-report.csv");

            CsvMapper mapper = new CsvMapper();
            List<GuardaReport> guardaList = IteratorUtils.toList(mapper.readerFor(GuardaReport.class)
                    .with(CsvSchema.emptySchema().withHeader()).readValues(guardaReportFile));

            GuardaSnowballMapper guardaSnowballMapper = Mappers.getMapper(GuardaSnowballMapper.class);

            List<SnowballDAO> snowballList = guardaList.stream()
                    .map(guardaSnowballMapper::guardaToSnowball)
                    .toList();

            mapper.writer(mapper.schemaFor(SnowballDAO.class).withHeader())
                    .writeValue(snowballReportFile, snowballList);

        } catch (ParseException | IOException exception) {
            exception.printStackTrace(System.err);
        }
    }

}
