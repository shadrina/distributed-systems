package ru.nsu.distributed.reader;

import ru.nsu.distributed.DatabaseInitializer;
import ru.nsu.distributed.dao.NodeDAO;
import ru.nsu.distributed.schema.Node;
import ru.nsu.distributed.schema.Osm;
import ru.nsu.distributed.schema.Tag;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
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
        try (var databaseInitilizer = new DatabaseInitializer()) {
            var nodeDAO = new NodeDAO(databaseInitilizer.getConnection());
            collectTimeStatistics("SQL", () -> nodeDAO.insertSQL(nodes), nodes.size());
            nodeDAO.deleteAll();
            collectTimeStatistics("Prepared statement", () -> nodeDAO.insertPreparedStatement(nodes), nodes.size());
            nodeDAO.deleteAll();
            collectTimeStatistics("Batch", () -> nodeDAO.insertBatch(nodes), nodes.size());
            nodeDAO.deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void collectTimeStatistics(String task, Runnable runnable, int recordAmount) {
        System.out.print(task + ": ");
        var start = System.currentTimeMillis();
        runnable.run();
        var diff = System.currentTimeMillis() - start;
        var result = recordAmount * 1. / (diff / 1000.);
        System.out.println((int) result);
    }
}
