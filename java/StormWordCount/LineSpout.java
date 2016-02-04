package com.ac.storm.wordcount;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class LineSpout implements IRichSpout {
	private SpoutOutputCollector collector;

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {  // spout task初始化时执行
		// TODO Auto-generated method stub
		this.collector = collector;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void activate() {
		// TODO Auto-generated method stub

	}

	public void deactivate() {
		// TODO Auto-generated method stub

	}

	public void nextTuple() {
		// TODO Auto-generated method stub
		String logDir = "C:\\Users\\AC\\Desktop\\logs"; // 本地测试目录，集群运行需要更改目录
//		String logDir = "/home/lab681/tempDir/logs"; // 集群测试目录
		Collection<File> logFiles = FileUtils.listFiles(new File(logDir), new String[]{"txt", "xml"}, true);
		
		if ( logFiles!=null && logFiles.size()>0 ){  // 保证从测试目录下读取到指定类型文件,首先判断是否为空是为了避免NullPointerException
//			System.out.print(collector.toString()+"\tLineSpout send lines");
			for ( File logFile : logFiles ){
				try {
					List<String> lines = FileUtils.readLines(logFile);
					for(String line : lines){
//						System.out.print("\t"+line);
                        collector.emit(new Values(line));
//                      Person person = new Person(id, name, age);
//                      collector.emit(new Values(person.id, person.name, person.age));
                    }
//					System.out.println();
					///修改操作完的文件（这里是修改后缀） 这样nextTuple方法就不会再重新处理该文件  
					FileUtils.moveFile(logFile, new File(logFile.getAbsolutePath() + "." + System.currentTimeMillis()));  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				
			}
		}else{
			collector.emit(new Values(""));
		}
	}

	public void ack(Object msgId) {
		// TODO Auto-generated method stub

	}

	public void fail(Object msgId) {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
		declarer.declare(new Fields("line"));
//		declarer.declare(new Fields( "id", "name", "age" ));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
