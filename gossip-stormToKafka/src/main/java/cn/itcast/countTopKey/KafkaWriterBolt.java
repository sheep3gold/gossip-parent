package cn.itcast.countTopKey;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Properties;

//用来向kafka生产数据的bolt
public class KafkaWriterBolt extends BaseBasicBolt {

    private static KafkaProducer<String, String> producer;

    static {
        //1.创建kafka的生产者对象
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.78.141:9092,192.168.78.142:9092,192.168.78.143:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(props);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //1.获取需要生产到kafka中的数据
        String kafkaTopKey = tuple.getStringByField("kafkaTopKey");

        //2.将数据生产到kafka即可
        producer.send(new ProducerRecord<String, String>("topKeywords", kafkaTopKey));
        producer.flush();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
