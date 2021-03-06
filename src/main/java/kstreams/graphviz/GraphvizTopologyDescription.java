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
    private final List<Edge> globalStoreSourceEdges;
    private final List<Edge> globalStoreSinkEdges;
    private final List<SubGraph> globalStoreSubGraphs;

    GraphvizTopologyDescription(
        List<Edge> topicSourceEdges, 
        List<Edge> topicSinkEdges, 
        List<Edge> storeSinkEdges, 
        List<SubGraph> subGraphs,
        List<Edge> globalStoreSourceEdges,
        List<Edge> globalStoreSinkEdges,
        List<SubGraph> globalStoreSubGraphs
    ) {
        this.topicSourceEdges = topicSourceEdges;
        this.topicSinkEdges = topicSinkEdges;
        this.storeSinkEdges = storeSinkEdges;
        this.subGraphs = subGraphs;
        this.globalStoreSourceEdges = globalStoreSourceEdges;
        this.globalStoreSinkEdges = globalStoreSinkEdges;
        this.globalStoreSubGraphs = globalStoreSubGraphs;
    }

    public Graph asGraphvizGraph() {
        return graph("kafka-streams-topology").directed()
            .with(topicSourceEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(topicSinkEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(storeSinkEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(subGraphs.stream().map(SubGraph::asGraphviz).collect(Collectors.toList()))
            .with(globalStoreSourceEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(globalStoreSinkEdges.stream().map(Edge::asGraphviz).collect(Collectors.toList()))
            .with(globalStoreSubGraphs.stream().map(SubGraph::asGraphviz).collect(Collectors.toList()));
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

        private guru.nidi.graphviz.model.Graph asGraphviz() {
            return graph("topology_" + this.id)
                .directed()
                .cluster()
                .graphAttr().with(Style.FILLED, Label.of("Subtopology " + this.id).justify(Label.Justification.LEFT))
                .nodeAttr().with(Style.FILLED, Color.WHITE.fill())
                .with(edges.stream().map(Edge::asGraphviz).collect(Collectors.toList()));
        }
    }
}
