package ru.nsu.distributed;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;

public class BzippedXmlReader implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();
    private final XMLStreamReader reader;

    public BzippedXmlReader(String archivePath) throws XMLStreamException, IOException {
        var compressorInputStream = new BZip2CompressorInputStream(new FileInputStream(archivePath));
        reader = FACTORY.createXMLStreamReader(compressorInputStream);
    }

    void read() throws XMLStreamException {
        while (reader.hasNext()) {
            var event = reader.next();
            if (event == XMLEvent.START_ELEMENT && "node".equals(reader.getLocalName())) {
                System.out.println("Start Element: " + reader.getName());
                for(int i = 0, n = reader.getAttributeCount(); i < n; ++i) {
                    var name = reader.getAttributeName(i);
                    var value = reader.getAttributeValue(i);
                    System.out.println("Attribute: " + name + "=" + value);
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
