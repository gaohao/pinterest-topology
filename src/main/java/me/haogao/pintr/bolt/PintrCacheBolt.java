package me.haogao.pintr.bolt;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class PintrCacheBolt extends BaseRichBolt{

	private static final long serialVersionUID = 5161826509198496313L;
	private String host;
	private int port;
	private JedisPool jedisPool = null;
	private Jedis jedis= null;
	private static Logger logger;
	
	public PintrCacheBolt(String host, int port) {
		this.host = host;
		this.port = port;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.jedisPool = new JedisPool(this.host, this.port);
		this.jedis = jedisPool.getResource();
		logger = LogManager.getLogger(PintrCacheBolt.class.getName());
	}

	@Override
	public void execute(Tuple input) {
		String pin_id = (String) input.getValue(0);
		String orig_link = (String) input.getValue(1);
		String orig_host = (String) input.getValue(2);
		String pin_json = (String) input.getValue(3);
		
		String allPinsKey = "pintr:pins";
		String origHostKey = "pintr:orig:host";

		if (!this.jedis.hexists(allPinsKey, pin_id)) {
			this.jedis.hset(allPinsKey, pin_id, pin_json);

			this.jedis.zadd(origHostKey, 1, orig_host);
		} else {
			this.jedis.zincrby(origHostKey, 1, orig_host);
		}
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}
	
	@Override
	public void cleanup() {
		if (this.jedisPool != null ) {
			this.jedisPool.returnResource(this.jedis);
			this.jedisPool.destroy();
		}
	}
	
}

