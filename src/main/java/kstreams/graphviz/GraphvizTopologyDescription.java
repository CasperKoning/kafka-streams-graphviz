package kstreams.graphviz;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import guru.nidi.graphviz.attribute.*;
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
        guru.nidi.graphviz.model.Node asGraphviz();
    }

    static class TopicNode implements Node {
        private final String identifier;

        TopicNode(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public guru.nidi.graphviz.model.Node asGraphviz() {
            return node(this.identifier).with(Shape.CYLINDER);
        }
    }

    static class ProcessNode implements Node {
        private final String identifier;

        ProcessNode(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public guru.nidi.graphviz.model.Node asGraphviz() {
            return node(this.identifier);
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
            return this.start.asGraphviz()
                .link(
                    to(this.end.asGraphviz())
                        .with(Arrow.NORMAL)
                );
                
        }
    }

    static class SubGraph {
        private final int id;
        private final List<Edge> edges;

        public SubGraph(int id, List<Edge> edges) {
            this.id = id;
            this.edges = edges;
        }

        /**
         * When changing the styling here, be aware that subgraph names need to start with cluster_ 
         * and need some other finnicky attribute combinations before they will get rendered as an 
         * actual cluster (with a filled rectangle).
         */
        private guru.nidi.graphviz.model.Graph asGraphviz() {
            return graph("cluster_topology_" + this.id).directed()
                .graphAttr().with(Style.FILLED, Label.of("Subtopology " + this.id).justify(Label.Justification.LEFT))
                .nodeAttr().with(Style.FILLED, Color.WHITE.fill())
                .with(edges.stream().map(Edge::asGraphviz).collect(Collectors.toList()));
        }
    }
}
