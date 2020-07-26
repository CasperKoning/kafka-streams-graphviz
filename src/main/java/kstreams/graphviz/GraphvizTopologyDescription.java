package kstreams.graphviz;

import org.apache.kafka.streams.Topology;

public class GraphvizTopologyDescription {
    public static String describeGraphviz(Topology topology) {
        return topology.describe().toString();
    }
}
