package kstreams.graphviz;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import guru.nidi.graphviz.model.*;
import static guru.nidi.graphviz.model.Factory.*;

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

    public Graph asGraphvizGraph() {
        return graph("my-graph").directed()
            .with(topicSourceEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(topicSinkEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(storeSinkEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(subGraphs.stream().map(SubGraph::asGraphviz).collect(Collectors.toList()));
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
        private final Node start;
        private final Node end;

        public Edge(Node start, Node end) {
            this.start = start;
            this.end = end;
        }

        private guru.nidi.graphviz.model.Node asGraphviz() {
            return node(this.start.getIdentifier())
                .link(to(
                    node(this.end.getIdentifier())
                ));
        }
    }

    static class SubGraph {
        private final int id;
        private final List<Edge> edges;

        public SubGraph(int id, List<Edge> edges) {
            this.id = id;
            this.edges = edges;
        }

        private guru.nidi.graphviz.model.Graph asGraphviz() {
            return graph("subtopology-" + this.id)
                .with(edges.stream().map(Edge::asGraphviz).collect(Collectors.toList()));
        }
    }
}
