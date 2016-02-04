package com.ac.storm.wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.config__init;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class WordCountTopo{
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException{
		Config conf = new Config(); 
		conf.setDebug(false);  // 设置为true时storm会记录下每个组件所发射的每条消息。这在本地环境调试topology很有用, 但是在线上会影响性能
//		conf.setNumAckers(0);  // 设置关闭storm中acker executor的数目，默认每个工作进程worker process设置一个acker executor,当spout可靠传播时设置的越多效率越高
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("LineSpout", new LineSpout());         
		builder.setBolt("LineSplitBolt", new LineSplitBolt(), 3).shuffleGrouping("LineSpout");
		builder.setBolt("WordCountBolt", new WordCountBolt()).shuffleGrouping("LineSplitBolt");
//		builder.setBolt("WordCountBolt", new WordCountBolt(), 3).fieldsGrouping("LineSplitBolt", new Fields("word"));
		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);  // 设置工作进程worker process的数目
			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());  
		} else {   
			LocalCluster cluster = new LocalCluster();  
			cluster.submitTopology("wordcountTopo", conf, builder.createTopology());  
			Utils.sleep(10000000);
			cluster.killTopology("wordcountTopo"); 
			cluster.shutdown();
		}  
	}

}
