package ru.nsu.distributed;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void parseArchive(String archivePath) {
        try (var bzippedXmlReader = new BzippedXmlReader(archivePath)) {
            bzippedXmlReader.read();
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
