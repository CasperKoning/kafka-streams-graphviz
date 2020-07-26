package kstreams.graphviz;

import org.apache.kafka.streams.Topology;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    @Test
    void testPlainDescribeToStringImplementation() {
        Topology topology = TestKafkaStreamsTopology.get();
        String description = GraphvizTopologyDescription.describeGraphviz(topology);
        assertNotNull(description);
    }
}
