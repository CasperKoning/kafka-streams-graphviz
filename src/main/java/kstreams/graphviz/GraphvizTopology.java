package kstreams.graphviz;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors; 

import org.apache.kafka.streams.*;
import org.apache.kafka.streams.TopologyDescription.Source;
import org.apache.kafka.streams.TopologyDescription.Sink;
import org.apache.kafka.streams.TopologyDescription.Processor;
import kstreams.graphviz.GraphvizTopologyDescription.*;

public class GraphvizTopology {
    public static GraphvizTopologyDescription describe(Topology topology) {
        TopologyDescription description = topology.describe();

        List<Edge> topicSourceEdges = new ArrayList<>();
        List<Edge> topicSinkEdges = new ArrayList<>();
        List<Edge> storeSinkEdges = new ArrayList<>();
        List<SubGraph> subGraphs = new ArrayList<>();
        List<Edge> globalStoreSourceEdges = new ArrayList<>();
        List<Edge> globalStoreSinkEdges = new ArrayList<>();
        List<SubGraph> globalStoreSubGraphs = new ArrayList<>();

        description
            .subtopologies()
            .stream()
            .forEach(subtopology -> {
                int id = subtopology.id();
                List<Edge> subGraphEdges = new ArrayList<>();

                subtopology
                    .nodes()
                    .forEach(node -> {
                        if (node instanceof Source) {
                            Source source = (Source) node;
                            topicSourceEdges.addAll(exractTopicEdges(source));
                            subGraphEdges.addAll(extractSubGraphEdges(source));
                        } else if (node instanceof Sink) {
                            Sink sink = (Sink) node;
                            topicSinkEdges.addAll(exractTopicEdges(sink));
                            subGraphEdges.addAll(extractSubGraphEdges(sink));
                        } else if (node instanceof Processor) {
                            Processor processor = (Processor) node;
                            storeSinkEdges.addAll(extractTopicEdges(processor));
                            subGraphEdges.addAll(extractSubGraphEdges(processor));
                        } else {
                          // At time of writing, there are no other cases, but we skip them in case they get added to the API
                        }
                    });
                
                SubGraph subGraph = new SubGraph(id, subGraphEdges);
                subGraphs.add(subGraph);
            });

        description.globalStores()
            .stream()
            .forEach(store -> {
                List<Edge> storeEdges = new ArrayList<>();
                
                Source source = store.source();
                Processor processor = store.processor();
                
                List<Edge> sourceTopicEdges = exractTopicEdges(source);
                List<Edge> processorStoreEdges = extractTopicEdges(processor);
                
                List<Edge> sourceProcessorEdge = extractSubGraphEdges(source);
                List<Edge> processorEdges = extractSubGraphEdges(processor);

                globalStoreSourceEdges.addAll(sourceTopicEdges);
                globalStoreSinkEdges.addAll(processorStoreEdges);
                
                storeEdges.addAll(sourceProcessorEdge);
                storeEdges.addAll(processorEdges);

                globalStoreSubGraphs.add(new SubGraph(store.id(), storeEdges));
            });

        return new GraphvizTopologyDescription(
            topicSourceEdges,
            topicSinkEdges,
            storeSinkEdges,
            subGraphs,
            globalStoreSourceEdges,
            globalStoreSinkEdges,
            globalStoreSubGraphs
        );
    }

    private static List<Edge> exractTopicEdges(Source source) {
        return source
            .topicSet()
            .stream()
            .map(topic -> new Edge(
                new TopicNode(topic),
                new ProcessNode(source.name())
            ))
            .collect(Collectors.toList());
    }

    private static List<Edge> exractTopicEdges(Sink sink) {
        return Collections.singletonList(
            new Edge(
                new ProcessNode(sink.name()), new TopicNode(sink.topic())
            )
        );
    }

    private static List<Edge> extractTopicEdges(Processor processor) {
        return processor
            .stores()
            .stream()
            .map(store -> new Edge(
                new ProcessNode(processor.name()),
                new TopicNode(store)
            ))
            .collect(Collectors.toList());
    }

    private static List<Edge> extractSubGraphEdges(Source source) {
        return source
            .successors()
            .stream()
            .map(node -> new Edge(
                new ProcessNode(source.name()),
                new ProcessNode(node.name())
            ))
            .collect(Collectors.toList());
    }

    private static List<Edge> extractSubGraphEdges(Sink sink) {
        return sink
            .predecessors()
            .stream()
            .map(node -> new Edge(
                new ProcessNode(node.name()),
                new ProcessNode(sink.name())
            ))
            .collect(Collectors.toList());
    }

    private static List<Edge> extractSubGraphEdges(Processor processor) {
        Stream<Edge> predecessorEdges = processor
            .predecessors()
            .stream()
            .map(node -> new Edge(
                new ProcessNode(node.name()),
                new ProcessNode(processor.name())
            ));

        Stream<Edge> successorEdges = processor
            .successors()
            .stream()
            .map(node -> new Edge(
                new ProcessNode(processor.name()),
                new ProcessNode(node.name())
            ));

        return Stream.concat(predecessorEdges, successorEdges)
            .collect(Collectors.toList());
    }
}
