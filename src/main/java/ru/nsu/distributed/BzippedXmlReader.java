package ru.nsu.distributed;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BzippedXmlReader implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();
    private final XMLStreamReader reader;

    private Map<String, Integer> modifications = new HashMap<>();
    private Map<String, Integer> keys = new HashMap<>();

    public Map<String, Integer> getModifications() {
        return modifications;
    }

    public Map<String, Integer> getKeys() {
        return keys;
    }

    public BzippedXmlReader(String archivePath) throws XMLStreamException, IOException {
        var compressorInputStream = new BZip2CompressorInputStream(new FileInputStream(archivePath));
        reader = FACTORY.createXMLStreamReader(compressorInputStream);
    }

    void read() throws XMLStreamException {
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

    private void processNodeElement() {
        var user = reader.getAttributeValue(getAttributeIndex("user"));
        modifications.put(user, modifications.getOrDefault(user, 0) + 1);
    }

    private void processTagElement() {
        var k = reader.getAttributeValue(getAttributeIndex("k"));
        keys.put(k, modifications.getOrDefault(k, 0) + 1);
    }

    private int getAttributeIndex(String name) {
        for (var i = 0; i < reader.getAttributeCount(); i++) {
            if (reader.getAttributeLocalName(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }
}
