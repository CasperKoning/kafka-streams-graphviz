package kstreams.graphviz;

import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

class TestKafkaStreamsTopology {
    public static Topology get() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> topic1 = builder.stream("input-topic-1");
        topic1.to("output-topic");
        return builder.build();
    }
}
