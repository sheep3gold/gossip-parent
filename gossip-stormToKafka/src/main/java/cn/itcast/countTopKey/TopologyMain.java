package cn.itcast.countTopKey;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

public class TopologyMain {
    public static void main(String[] args) {
        KafkaSpoutConfig<String, String> build = KafkaSpoutConfig
                .builder("192.168.78.141:9092,192.168.78.142:9092,192.168.78.143:9092", "gossip-logs")
                .setGroupId("keyCount")
                .setKey(StringDeserializer.class)
                .setValue(StringDeserializer.class)
                .setOffsetCommitPeriodMs(1000)
                .build();

        //1.创建Topology构建类对象
        TopologyBuilder builder = new TopologyBuilder();
        //1.1添加spout类
        builder.setSpout("kafka-reader", new KafkaSpout<String, String>(build));
        //1.2添加bolt类
        builder.setBolt("splitBolt", new SplitBolt()).shuffleGrouping("kafka-reader");
        //1.3添加Bolt类
        builder.setBolt("countBolt", new CountBolt()).shuffleGrouping("splitBolt");

        builder.setBolt("kafka-writer", new KafkaWriterBolt()).shuffleGrouping("countBolt");

        LocalCluster cluster = new LocalCluster();

        Config config = new Config();

        cluster.submitTopology("topKeyCountTopology", config, builder.createTopology());
    }
}
