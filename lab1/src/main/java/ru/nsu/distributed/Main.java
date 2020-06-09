package ru.nsu.distributed;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static void printStatistics(BzippedXmlReader bzippedXmlReader) {
        bzippedXmlReader.getModifications().entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .forEachOrdered(e -> System.out.println(e.getKey() + " " + e.getValue()));
        System.out.println("Unique keys count: " + bzippedXmlReader.getKeys().size());
    }

    private static void parseArchive(String archivePath) {
        try (var bzippedXmlReader = new BzippedXmlReader(archivePath)) {
            bzippedXmlReader.read();
            printStatistics(bzippedXmlReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParseException {
        var options = new Options();
        options.addOption("f", "file", true, "Archive file absolute path");
        var parser = new DefaultParser();
        var cmd = parser.parse(options, args);

        if (cmd.hasOption("f")) {
            parseArchive(cmd.getOptionValue("f"));
        } else {
            LOGGER.error("No archive file path provided");
        }
    }
}
