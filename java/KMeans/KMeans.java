package com.ac.kmeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class KMeans {
	private ArrayList<Tuple> data;  //待聚簇的元组
	private int k;   //聚簇数目
	private int k1;  //标示新形成的聚簇中心元组
	public KMeans(ArrayList<Tuple> data, int k){
		this.data = data;
		this.k = k;
		k1 = data.size()-1;
	}
	public boolean isExist(ArrayList<Integer> ranIds, int id){
		Boolean result = false;
		Iterator it = ranIds.iterator();
		while ( it.hasNext() ){
			int tempId = (int)it.next();
			if ( id == tempId ){
				result = true;
				break;
			}
		}
		return result;
	}
	public ArrayList<Tuple> getRanCenters(){  //随机选择k个元组作为初始聚簇中心
		ArrayList<Tuple> Centers = new ArrayList<Tuple>();   //存放随机选择的k个中心元组
		ArrayList<Integer> ranIds = new ArrayList<Integer>();   //以随机id的形式随机选择k个元组
		Random r = new Random();
		for ( int i=0; i<k; i++ ){
			int id = r.nextInt(data.size());  //生成0`(n-1)的随机数，n为数据集data中元组的个数
			//随机生成的id与之前一样的话不加入ranIds中
			if ( !isExist(ranIds,id) ){
				ranIds.add(id);
			}
		}
		for ( int i=0; i<ranIds.size(); i++ ){
			Centers.add(data.get(ranIds.get(i)));
		}
		return Centers;
	}
	public Tuple getNewCenter(ArrayList<Tuple> cluster){  //得到一个聚簇的中心元组
		Tuple newCenter = new Tuple();
		double xSum=0.0, ySum=0.0;
		for ( int i=0; i<cluster.size(); i++ ){
			xSum += cluster.get(i).x;
			ySum += cluster.get(i).y;
		}
		if ( cluster.size() > 0 ){
			newCenter.x = xSum/cluster.size();
			newCenter.y = ySum/cluster.size();
		}
		newCenter.tupleId = ++k1;
		return newCenter;
	}
	public ArrayList<Tuple> getCenters(ArrayList<ArrayList<Tuple>> clusters){      //迭代更新聚簇中心元组
		ArrayList<Tuple> centers = new ArrayList<Tuple>();   //存放计算生成的k个中心元组
		for ( int i=0; i<clusters.size(); i++ ){
			ArrayList<Tuple> cluster = clusters.get(i);    //取得第i个聚簇中所有的元组
			Tuple newCenter = getNewCenter(cluster);
			centers.add(newCenter);
		}
		return centers;
	}
	public ArrayList<ArrayList<Tuple>> getClusters(ArrayList<Tuple> Centers){ //生成聚簇，返回其中的所有元组
		ArrayList<ArrayList<Tuple>> clusters = new ArrayList<ArrayList<Tuple>>();  //存储k个聚簇中所有的元组
		for ( int i=0; i<data.size(); i++ ){
			Tuple tupleFromData = data.get(i);
			double minDis = Double.MAX_VALUE;
			for ( int j=0; j<Centers.size(); j++ ){
				Tuple tupleFromCenters = Centers.get(j);
				double dis = dist(tupleFromData, tupleFromCenters);
				if ( dis < minDis ){
					minDis = dis;
					tupleFromData.centralTuple = tupleFromCenters;
				}
			}
		}
		for ( int i=0; i<Centers.size(); i++ ){
			ArrayList<Tuple> cluster = new ArrayList<Tuple>();   //存储单个聚簇的元组
		//	cluster.add(Centers.get(i));
			for ( int j=0; j<data.size(); j++ ){
				if ( data.get(j).centralTuple == Centers.get(i) ){
					cluster.add(data.get(j));
				}
			}
			clusters.add(cluster);
		}
		return clusters;
	}
	public double dist(Tuple t1, Tuple t2){
		return Math.sqrt(Math.pow(t1.x-t2.x, 2)+Math.pow(t1.y-t2.y, 2));
	}
	public static void main(String[] args){
		//生成数据集
		int tupleNum=100, clusterNum=5;
		int clusterCounts=10;  //总的聚类次数
		int clusterCount=0;     //当前聚类次数
		Iterator<Tuple> it = null;
		ArrayList<Tuple> data = new Tuple().init(tupleNum);       //存放数据集
		
		System.out.println("--------------------数据集--------------------");
		it = data.iterator();
		while ( it.hasNext() ){
			Tuple temp = it.next();
			System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
		}
		System.out.println("--------------------数据集--------------------");
		
		//实例化KMeans类，准备对数据集进行k-means聚类
		KMeans KMeans = new KMeans(data, clusterNum);
		//随机选择k个元组作为初始聚簇中心
		ArrayList<Tuple> ranCenters = KMeans.getRanCenters();
		
		System.out.println("-----------------随机选择的初始聚簇中心-----------------");
		it = ranCenters.iterator();
		while ( it.hasNext() ){
			Tuple temp = it.next();
			System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
		}
		System.out.println("-----------------随机选择的初始聚簇中心-----------------");
		
		//根据随机选择的k个中心元组进行聚类
		ArrayList<ArrayList<Tuple>> clusters = KMeans.getClusters(ranCenters);
		System.out.println("--------------------第"+(++clusterCount)+"次聚类--------------------");
		for ( int i=0; i<clusters.size(); i++ ){
			ArrayList<Tuple> cluster = clusters.get(i);
			System.out.println("第"+(i+1)+"个聚簇,共"+cluster.size()+"个元组");
			it = cluster.iterator();
			while ( it.hasNext() ){
				Tuple temp = it.next();
				System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
			}
		}
		
		//更新聚簇的中心元组
		ArrayList<Tuple> centers = KMeans.getCenters(clusters);
		System.out.println("     ----------聚簇中心----------     ");
		for ( int i=0; i<clusterNum; i++ ){
			Tuple temp = centers.get(i);
			System.out.println("第"+(i+1)+"个聚簇中心:");
			System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
		}
		System.out.println("     ----------聚簇中心----------     ");
		System.out.println("--------------------第"+(clusterCount)+"次聚类--------------------");
		
		//进行有限次聚类操作
		while ( clusterCount < clusterCounts ){
			clusters = KMeans.getClusters(centers);
			System.out.println("--------------------第"+(++clusterCount)+"次聚类--------------------");
			for ( int i=0; i<clusters.size(); i++ ){
				ArrayList<Tuple> cluster = clusters.get(i);
				System.out.println("第"+(i+1)+"个聚簇,共"+cluster.size()+"个元组");
				it = cluster.iterator();
				while ( it.hasNext() ){
					Tuple temp = it.next();
					System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
				}
			}
			
			centers = KMeans.getCenters(clusters);
			System.out.println("     ----------聚簇中心----------     ");
			for ( int i=0; i<clusterNum; i++ ){
				Tuple temp = centers.get(i);
				System.out.println("第"+(i+1)+"个聚簇中心:");
				System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
			}
			System.out.println("     ----------聚簇中心----------     ");
			System.out.println("--------------------第"+(clusterCount)+"次聚类--------------------");
		}
	
	}
}

class Tuple{
	double x, y;
	int tupleId;
	Tuple centralTuple;  //聚簇中心元组
	public ArrayList<Tuple> init(int tupleNum){  //自动生成数据集
		ArrayList<Tuple> data = new ArrayList<Tuple>();
		Random r = new Random();
		for ( int id=0; id<tupleNum; id++ ){
			Tuple t = new Tuple();
			t.x = r.nextDouble();
			t.y = r.nextDouble();
			t.tupleId = id;
			data.add(t);
		}
		return data;
	}
}
	