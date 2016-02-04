package com.ac.storm.wordcount;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WordCountBolt implements IBasicBolt {
	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public void prepare(Map stormConf, TopologyContext context) {
		// TODO Auto-generated method stub

	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		String word = ((String) input.getValue(0)).trim();
//		System.out.println(collector.toString()+"\tWordCountBolt get word: "+word);
		
		if ( map.containsKey(word) ){
			map.put(word, map.get(word)+1);
		}else{
			map.put(word, 1);
		}
		System.out.print("当前单词个数统计结果("+map.size()+"):");
		Iterator iter = map.entrySet().iterator();
		while ( iter.hasNext() ){
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			int value = (Integer) entry.getValue();
			System.out.print("\t"+key+":"+value);
		}
		System.out.println();
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
