package me.haogao.pintr.spout;

import backtype.storm.spout.ShellSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.IRichSpout;
import backtype.storm.tuple.Fields;
import java.util.Map;

public class PintrSpout extends ShellSpout implements IRichSpout {
    
	private static final long serialVersionUID = 7060865252243357618L;

	public PintrSpout() {
        super("python", "pintr_spout.py");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	declarer.declare(new Fields("pin_id", "orig_link", "orig_host", "pin_json", "category"));
    }
    
    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
