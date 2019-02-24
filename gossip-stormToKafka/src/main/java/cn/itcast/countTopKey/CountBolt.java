package cn.itcast.countTopKey;

import cn.itcast.utils.JedisUtil;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

//用来统计计算的bolt
public class CountBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //1.获取上游的数据
        String topKey = tuple.getStringByField("topKey");
        Integer score = tuple.getIntegerByField("score");

        //2.判断这个热搜词是否已经存在
        Jedis jedis = JedisUtil.getJedis();
        //bigData:gossip:topkey :  sortedSet
        //Double 默认值：null double默认值：0.0
        Double zscore = jedis.zscore("bigData:gossip:topkey", topKey);
        Double incrScore = 0.0;
        if (zscore != null) {
            //存在的数据，返回新增后的数值大小
            incrScore = jedis.zincrby("bigData:gossip:topkey", score, topKey);
        }else {
            jedis.zadd("bigData:gossip:topkey", score, topKey);
        }

        //3.如果点击量大于等于3，将这个元素发给下游（只发送一次）
        if (incrScore.intValue() >= 3) {
            if (!jedis.exists("gossip:" + topKey + ":1")) {
                basicOutputCollector.emit(Arrays.asList(topKey));
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("kafkaTopKey"));
    }
}
