package me.haogao.pintr.topology;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.haogao.pintr.bolt.*;
import me.haogao.pintr.spout.*;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class PintrTopology {
    
    public static void main(String[] args) throws Exception {
		Logger logger = LogManager.getLogger(PintrTopology.class.getName());
    	logger.entry();
    	
    	
        TopologyBuilder builder = new TopologyBuilder();
        
        builder.setSpout("spout", new PintrSpout(), 1);
        
        builder.setBolt("bolt", new PintrCacheBolt("localhost", 6379), 12)
                 .fieldsGrouping("spout", new Fields("category"));

        Config conf = new Config();
        conf.setDebug(false);
        
        if(args!=null && args.length > 0) {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {        
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("pintr", conf, builder.createTopology());
            
            //Thread.sleep(10000);
            //cluster.killTopology("pintr");
            //cluster.shutdown();
        }  
		logger.exit();
    }
}