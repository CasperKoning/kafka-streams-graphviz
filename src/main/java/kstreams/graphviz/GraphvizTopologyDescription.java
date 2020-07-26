package kstreams.graphviz;

import java.util.*;

public class GraphvizTopologyDescription {
    private final List<Edge> topicSourceEdges;
    private final List<Edge> topicSinkEdges;
    private final List<Edge> storeSinkEdges;
    private final List<SubGraph> subGraphs;

    GraphvizTopologyDescription(
        List<Edge> topicSourceEdges, 
        List<Edge> topicSinkEdges, 
        List<Edge> storeSinkEdges, 
        List<SubGraph> subGraphs
    ) {
        this.topicSourceEdges = topicSourceEdges;
        this.topicSinkEdges = topicSinkEdges;
        this.storeSinkEdges = storeSinkEdges;
        this.subGraphs = subGraphs;
    }

    interface Node {
        String getIdentifier();
    }

    static class TopicNode implements Node {
        private final String identifier;

        TopicNode(String identifier) {
            this.identifier = identifier;
        }

        @Override()
        public String getIdentifier() {
            return this.identifier;
        }
    }

    static class ProcessNode implements Node {
        private final String identifier;

        ProcessNode(String identifier) {
            this.identifier = identifier;
        }

        @Override()
        public String getIdentifier() {
            return this.identifier;
        }
    }

    static class Edge {
        final Node start;
        final Node end;

        public Edge(Node start, Node end) {
            this.start = start;
            this.end = end;
        }
    }

    static class SubGraph {
        private final int id;
        private final List<Edge> edges;

        public SubGraph(int id, List<Edge> edges) {
            this.id = id;
            this.edges = edges;
        }
    }
}
