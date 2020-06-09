package ru.nsu.distributed.reader;

import ru.nsu.distributed.schema.Node;
import ru.nsu.distributed.schema.Osm;
import ru.nsu.distributed.schema.Tag;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JaxbXmlReader extends BzippedXmlReader {
    Unmarshaller jaxbUnmarshaller;

    Collection<Node> nodes = new ArrayList<>();
    Collection<Tag> tags = new ArrayList<>();

    public JaxbXmlReader(String archivePath) throws IOException, XMLStreamException {
        super(archivePath);
        try {
            var jaxbContext = JAXBContext.newInstance(Osm.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    void processNodeElement() {
        try {
            var node = (Node) jaxbUnmarshaller.unmarshal(reader);
            nodes.add(node);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    void processTagElement() {
        try {
            var tag = (Tag) jaxbUnmarshaller.unmarshal(reader);
            tags.add(tag);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showResult() {

    }
}
