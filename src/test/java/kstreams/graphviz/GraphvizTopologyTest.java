package kstreams.graphviz;

import java.io.*;
import java.nio.*;

import org.apache.kafka.streams.Topology;

import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GraphvizTopologyTest {
    @Test
    void testPlainDescribeToStringImplementation() throws Exception {
        Topology topology = TestKafkaStreamsTopology.get();
        Graph g = GraphvizTopology
            .describe(topology)
            .asGraphvizGraph();
        Graphviz
            .fromGraph(g)
            .render(Format.DOT)
            .toFile(new File("ex1.dot"));

        Graphviz
            .fromGraph(g)
            .render(Format.PNG)
            .toFile(new File("ex1.png"));
    }
}
