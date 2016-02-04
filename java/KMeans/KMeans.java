package com.ac.kmeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class KMeans {
	private ArrayList<Tuple> data;  //���۴ص�Ԫ��
	private int k;   //�۴���Ŀ
	private int k1;  //��ʾ���γɵľ۴�����Ԫ��
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
	public ArrayList<Tuple> getRanCenters(){  //���ѡ��k��Ԫ����Ϊ��ʼ�۴�����
		ArrayList<Tuple> Centers = new ArrayList<Tuple>();   //������ѡ���k������Ԫ��
		ArrayList<Integer> ranIds = new ArrayList<Integer>();   //�����id����ʽ���ѡ��k��Ԫ��
		Random r = new Random();
		for ( int i=0; i<k; i++ ){
			int id = r.nextInt(data.size());  //����0`(n-1)���������nΪ���ݼ�data��Ԫ��ĸ���
			//������ɵ�id��֮ǰһ���Ļ�������ranIds��
			if ( !isExist(ranIds,id) ){
				ranIds.add(id);
			}
		}
		for ( int i=0; i<ranIds.size(); i++ ){
			Centers.add(data.get(ranIds.get(i)));
		}
		return Centers;
	}
	public Tuple getNewCenter(ArrayList<Tuple> cluster){  //�õ�һ���۴ص�����Ԫ��
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
	public ArrayList<Tuple> getCenters(ArrayList<ArrayList<Tuple>> clusters){      //�������¾۴�����Ԫ��
		ArrayList<Tuple> centers = new ArrayList<Tuple>();   //��ż������ɵ�k������Ԫ��
		for ( int i=0; i<clusters.size(); i++ ){
			ArrayList<Tuple> cluster = clusters.get(i);    //ȡ�õ�i���۴������е�Ԫ��
			Tuple newCenter = getNewCenter(cluster);
			centers.add(newCenter);
		}
		return centers;
	}
	public ArrayList<ArrayList<Tuple>> getClusters(ArrayList<Tuple> Centers){ //���ɾ۴أ��������е�����Ԫ��
		ArrayList<ArrayList<Tuple>> clusters = new ArrayList<ArrayList<Tuple>>();  //�洢k���۴������е�Ԫ��
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
			ArrayList<Tuple> cluster = new ArrayList<Tuple>();   //�洢�����۴ص�Ԫ��
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
		//�������ݼ�
		int tupleNum=100, clusterNum=5;
		int clusterCounts=10;  //�ܵľ������
		int clusterCount=0;     //��ǰ�������
		Iterator<Tuple> it = null;
		ArrayList<Tuple> data = new Tuple().init(tupleNum);       //������ݼ�
		
		System.out.println("--------------------���ݼ�--------------------");
		it = data.iterator();
		while ( it.hasNext() ){
			Tuple temp = it.next();
			System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
		}
		System.out.println("--------------------���ݼ�--------------------");
		
		//ʵ����KMeans�࣬׼�������ݼ�����k-means����
		KMeans KMeans = new KMeans(data, clusterNum);
		//���ѡ��k��Ԫ����Ϊ��ʼ�۴�����
		ArrayList<Tuple> ranCenters = KMeans.getRanCenters();
		
		System.out.println("-----------------���ѡ��ĳ�ʼ�۴�����-----------------");
		it = ranCenters.iterator();
		while ( it.hasNext() ){
			Tuple temp = it.next();
			System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
		}
		System.out.println("-----------------���ѡ��ĳ�ʼ�۴�����-----------------");
		
		//�������ѡ���k������Ԫ����о���
		ArrayList<ArrayList<Tuple>> clusters = KMeans.getClusters(ranCenters);
		System.out.println("--------------------��"+(++clusterCount)+"�ξ���--------------------");
		for ( int i=0; i<clusters.size(); i++ ){
			ArrayList<Tuple> cluster = clusters.get(i);
			System.out.println("��"+(i+1)+"���۴�,��"+cluster.size()+"��Ԫ��");
			it = cluster.iterator();
			while ( it.hasNext() ){
				Tuple temp = it.next();
				System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
			}
		}
		
		//���¾۴ص�����Ԫ��
		ArrayList<Tuple> centers = KMeans.getCenters(clusters);
		System.out.println("     ----------�۴�����----------     ");
		for ( int i=0; i<clusterNum; i++ ){
			Tuple temp = centers.get(i);
			System.out.println("��"+(i+1)+"���۴�����:");
			System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
		}
		System.out.println("     ----------�۴�����----------     ");
		System.out.println("--------------------��"+(clusterCount)+"�ξ���--------------------");
		
		//�������޴ξ������
		while ( clusterCount < clusterCounts ){
			clusters = KMeans.getClusters(centers);
			System.out.println("--------------------��"+(++clusterCount)+"�ξ���--------------------");
			for ( int i=0; i<clusters.size(); i++ ){
				ArrayList<Tuple> cluster = clusters.get(i);
				System.out.println("��"+(i+1)+"���۴�,��"+cluster.size()+"��Ԫ��");
				it = cluster.iterator();
				while ( it.hasNext() ){
					Tuple temp = it.next();
					System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
				}
			}
			
			centers = KMeans.getCenters(clusters);
			System.out.println("     ----------�۴�����----------     ");
			for ( int i=0; i<clusterNum; i++ ){
				Tuple temp = centers.get(i);
				System.out.println("��"+(i+1)+"���۴�����:");
				System.out.println(temp.tupleId+": ("+temp.x+", "+temp.y+")");
			}
			System.out.println("     ----------�۴�����----------     ");
			System.out.println("--------------------��"+(clusterCount)+"�ξ���--------------------");
		}
	
	}
}

class Tuple{
	double x, y;
	int tupleId;
	Tuple centralTuple;  //�۴�����Ԫ��
	public ArrayList<Tuple> init(int tupleNum){  //�Զ��������ݼ�
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
	