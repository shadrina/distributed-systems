package ru.nsu.distributed;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.distributed.reader.BzippedXmlReader;
import ru.nsu.distributed.reader.JaxbXmlReader;
import ru.nsu.distributed.reader.SimpleXmlReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static void runReader(BzippedXmlReader reader) throws XMLStreamException {
        reader.read();
        reader.showResult();
    }

    public static void main(String[] args) throws ParseException {
        var options = new Options();
        options
                .addOption("f", "file", true, "Archive file absolute path")
                .addOption("j", "jaxb", false, "JAXB mode");
        var parser = new DefaultParser();
        var cmd = parser.parse(options, args);

        if (!cmd.hasOption("f")) {
            LOGGER.error("No archive file path provided");
            return;
        }
        var path = cmd.getOptionValue("f");
        var jaxbMode = cmd.hasOption("j");
        try (var reader = jaxbMode ? new JaxbXmlReader(path) : new SimpleXmlReader(path)) {
            runReader(reader);
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
