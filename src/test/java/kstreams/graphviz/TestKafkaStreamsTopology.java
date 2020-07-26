package kstreams.graphviz;

import java.util.stream.Stream;
import java.util.stream.Collectors; 
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.*;
import org.apache.kafka.common.utils.Bytes;

class TestKafkaStreamsTopology {
    public static Topology get() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> topic1 = builder.stream("input-topic-1");
        KStream<String, String> topic2 = builder.stream("input-topic-2");
        KStream<String, String> topic3 = builder.stream("input-topic-3");
        Materialized<String, String, KeyValueStore<Bytes,byte[]>> stateStore = 
            Materialized.as("state-store");
        
        GlobalKTable<String, String> globalKTable = builder.globalTable("global-topic");

        KStream<String, String> rekeyed1 = topic1
            .selectKey((key, value) -> value)
            .through("rekey-topic-1");
        
        KStream<String, String> rekeyed2 = (topic2.merge(topic3))
            .selectKey((key, value) -> value)
            .through("rekey-topic-2");

        KTable<String, String> aggregated = (rekeyed1.merge(rekeyed2))
            .groupByKey()
            .aggregate(
              () -> "",
              (key, value, agg) -> agg + value,
              stateStore
            );

        KStream<String, String> result = aggregated
            .toStream()
            .join(
                globalKTable,
                (key, value) -> value,
                (left, right) -> left + right
            );

        result
            .flatMapValues(value -> 
                Stream.of(value.toCharArray())
                    .map(c -> c.toString())
                    .collect(Collectors.toList())
            )
            .to("output-topic");
        return builder.build();
    }
}
