package kstreams.graphviz;

import java.io.*;
import java.nio.*;

import org.apache.kafka.streams.Topology;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.*;
import guru.nidi.graphviz.model.*;
import static guru.nidi.graphviz.model.Factory.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GraphvizTopologyTest {
    @Test
    void testDotAndPngRendering() throws Exception {
        Topology topology = TestKafkaStreamsTopology.get();
        Graph g = GraphvizTopology
            .describe(topology)
            .asGraphvizGraph();
        Graphviz
            .fromGraph(g)
            .render(Format.DOT)
            .toFile(new File("examples/ex1.dot"));

        Graphviz
            .fromGraph(g)
            .render(Format.PNG)
            .toFile(new File("examples/ex1.png"));
    }

    
    @Test
    void testSameRankInputTopics() throws Exception {
        Topology topology = TestKafkaStreamsTopology.get();
        Graph g = GraphvizTopology
            .describe(topology)
            .asGraphvizGraph();
        
        /**
         * This shows that one may be able to add some extra context to create some better 
         * looking renders. This all completely depends on logic provided by the user though,
         * and is not something we (can) get out of the Kafka Streams metadata.
         */
        Graph graphWithExtraInfo = g.with(
            graph()
                .graphAttr().with(Rank.inSubgraph(Rank.RankType.SAME))
                .with(node("input-topic-1"), node("input-topic-2"), node("input-topic-3"))
        );

        Graphviz
            .fromGraph(graphWithExtraInfo)
            .render(Format.DOT)
            .toFile(new File("examples/ex2.dot"));

        Graphviz
            .fromGraph(graphWithExtraInfo)
            .render(Format.PNG)
            .toFile(new File("examples/ex2.png"));
    }
}
