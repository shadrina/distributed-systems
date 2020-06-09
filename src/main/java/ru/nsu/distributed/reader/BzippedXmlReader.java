package ru.nsu.distributed.reader;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class BzippedXmlReader implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();
    protected final XMLStreamReader reader;

    public BzippedXmlReader(String archivePath) throws XMLStreamException, IOException {
        var compressorInputStream = new BZip2CompressorInputStream(new FileInputStream(archivePath));
        reader = FACTORY.createXMLStreamReader(compressorInputStream);
    }

    public void read() throws XMLStreamException {
        while (reader.hasNext()) {
            var event = reader.next();
            if (event != XMLEvent.START_ELEMENT) {
                continue;
            }
            if (reader.getLocalName().equals("node")) {
                processNodeElement();
            } else if (reader.getLocalName().equals("tag")) {
                processTagElement();
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }

    public abstract void showResult();

    abstract void processNodeElement();

    abstract void processTagElement();
}
