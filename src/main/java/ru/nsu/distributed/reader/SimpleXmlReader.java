package ru.nsu.distributed.reader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleXmlReader extends BzippedXmlReader {
    private Map<String, Integer> modifications = new HashMap<>();
    private Map<String, Integer> keys = new HashMap<>();

    public SimpleXmlReader(String archiveName) throws IOException, XMLStreamException {
        super(archiveName);
    }

    @Override
    public void showResult() {
        modifications.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .forEachOrdered(e -> System.out.println(e.getKey() + " " + e.getValue()));
        System.out.println("Unique keys count: " + keys.size());
    }

    @Override
    void processNodeElement() {
        var user = reader.getAttributeValue(getAttributeIndex("user"));
        modifications.put(user, modifications.getOrDefault(user, 0) + 1);
    }

    @Override
    void processTagElement() {
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
}
