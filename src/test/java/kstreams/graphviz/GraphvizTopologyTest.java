package kstreams.graphviz;

import org.apache.kafka.streams.Topology;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GraphvizTopologyTest {
    @Test
    void testPlainDescribeToStringImplementation() {
        Topology topology = TestKafkaStreamsTopology.get();
        GraphvizTopologyDescription description = GraphvizTopology.describe(topology);
        assertNotNull(description);
    }
}
