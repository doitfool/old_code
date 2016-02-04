package com.ac.storm.wordcount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class LineSplitBolt implements IBasicBolt {

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("word"));
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
		String line = (String) input.getValue(0);
//		System.out.println(collector.toString()+"\tLineSplitBolt get line: "+line);
		String[] words = line.split("[^0-9a-zA-Z]"); // 以非字母和数字分割line,但当line中有连续空格时，words中会有为null的string
//		String[] words = line.split("\\s"); //  \\s表示 空格,回车,换行等空白符
//		System.out.print(collector.toString()+"\tLineSplitBolt send word");
		for ( String word : words ){
//			System.out.print("\t"+word);
			if ( !word.isEmpty()){
				collector.emit(new Values(word));
			}
		}
//		System.out.println();
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
