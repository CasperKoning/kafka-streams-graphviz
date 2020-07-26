# kafka-streams-graphviz

## Requirements
- Java toolchain (JVM, Gradle, etc.)
- Graphviz toolchain
- Kafka Streams app

## How to use
Here, we show how this library can be used to render a graph for your Kafka Streams topology.

### Build your Kafka Streams topology
First, one must have a Kafka Streams `Topology` available:
```java
StreamsBuilder builder = new StreamsBuilder();

... // This is where one would use builder to define a topology

Topology topology = builder.build();
```
(see [`TestKafkaStreamsTopology`](test/java/kstreams/graphviz/TestKafkaStreamsTopology) for a more complete example)

### Describe as graphviz graph
Using the Kafka Streams `Topology` one can then use this library to describe this as a `graphviz-java` `Graph`:
```java
Topology topology = builder.build();

Graph graph = GraphvizTopology
    .describe(topology)
    .asGraphvizGraph();
```

### Render as DOT, Image, ...
This `graphviz-java` `Graph` can then be rendered, for example, as a DOT file, or PNG file:
```java
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
```

#### Finetuning with graphviz-java
Further finetuning of the Graph can be done programmatically by extending the `Graph`, e.g.:
```java
Graph g = GraphvizTopology
    .describe(topology)
    .asGraphvizGraph();

Graph graphWithExtraInfo = g.with(
    graph()
        .graphAttr().with(Rank.inSubgraph(Rank.RankType.SAME))
        .with(node("input-topic-1"), node("input-topic-2"), node("input-topic-3"))
);

Graphviz
    .fromGraph(graphWithExtraInfo)
    .render(Format.DOT)
    .toFile(new File("examples/ex2.dot"));
```

See [graphviz-java](https://github.com/nidi3/graphviz-java) for more details.

## Acknowledgements
This project would never be able to exist without the following projects:
- [Kafka Streams](https://kafka.apache.org/documentation/streams/)
- [graphviz](https://graphviz.org)
- [graphviz-java](https://github.com/nidi3/graphviz-java)
