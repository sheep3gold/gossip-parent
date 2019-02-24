package cn.itcast.countTopKey;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Arrays;

//进行数据切割的bolt
public class SplitBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        System.out.println(tuple.toString());

        String message = tuple.getString(4);
        //进行数据切割
        int index = message.lastIndexOf("#CS#");
        if (message.length()>index+4) {
            String topKey = message.substring(index + 4);
            basicOutputCollector.emit(Arrays.asList(topKey, 1));
        }else {
            basicOutputCollector.emit(Arrays.asList("", 1));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("topKey", "score"));
    }
}
