package cn.itcast;

import cn.itcast.gossip.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TopKeyToRedis {
    public static void main(String[] args) {
        Jedis jedis = JedisUtil.getJedis();

        //向redis中存储热搜词语：sortedSet
        jedis.zadd("bigData:gossip:topkey",2772519,"冉莹颖怀三胎");
        jedis.zadd("bigData:gossip:topkey",1886901,"刘谦回应春晚换壶事件");
        jedis.zadd("bigData:gossip:topkey",1873564,"万屏FO以团");
        jedis.zadd("bigData:gossip:topkey",1820216,"考研成绩");
        jedis.zadd("bigData:gossip:topkey",1815190,"东宫真香");
        jedis.zadd("bigData:gossip:topkey",1811400,"杨洋乔欣方否认恋情");
        jedis.zadd("bigData:gossip:topkey",1775617,"翟天临吴秀波或被剧方索赔");
        jedis.zadd("bigData:gossip:topkey",664160,"李栋旭刘仁娜太甜了");
        jedis.zadd("bigData:gossip:topkey",579514,"特朗普将宣布国家紧急状态");
        jedis.zadd("bigData:gossip:topkey",493445,"赴韩植发成热潮");
        jedis.zadd("bigData:gossip:topkey",460183,"新泰坦尼克号轮廓初现");
        jedis.zadd("bigData:gossip:topkey",453785,"断眉新恋情");
        jedis.zadd("bigData:gossip:topkey",444045,"流浪地球票房超药神");
        jedis.zadd("bigData:gossip:topkey",431926,"卫计局回应超生三胎未缴费");
        jedis.zadd("bigData:gossip:topkey", 421710, "王思聪评论限定热狗");

        //释放资源
        jedis.close();

    }

}
