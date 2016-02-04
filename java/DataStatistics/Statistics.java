package com.ac.dataStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author AC
 * 
 */
//�������ھ��е�����ͳ������װ����Statistics
public class Statistics {
	//ͳ�����ݼ���ƽ��ֵ����λ�����������ķ�֮һλ�����ķ�֮��λ�����ķ�λ�������Сֵ�����ֵ���������������׼��
	private double mean, median, Q1, Q3, IQR, min, max, midrange, variance, standard_deviation;
	private List<Double> mode;
	private double[] data;
	public Statistics(double... dataSet){
		data = dataSet;
		Arrays.sort(data);
		/*������������ݼ�
		for ( double i:data){
			System.out.print(i+"  ");
		}
		System.out.println();
		*/
	}
	//����ƽ��ֵ
	public double mean(){
		double sum=0.0;
		for (double temp : data){
			sum += temp;
		}
		mean = sum/data.length;
		return mean;
	}
	//������λ��
	public double median(){
		if ( data.length % 2 == 1){
			median = data[data.length/2];
		}else{
			median = (data[data.length/2]+data[(data.length+1)/2])/2; //���ݼ���ż������ʱͨ��ȡ���м����������ƽ��ֵ��Ϊ��λ��
		}
		return median;
	}
	//��������(���ݼ��г��ִ���������,���ܲ�ֹһ������)                             
	public List<Double> mode(){
		//ʹ��map��¼��ͬdoubleֵ���ֵĴ���
		HashMap<Double, Integer> map = new HashMap<Double, Integer>();
		for ( double temp:data ){
			if ( map.containsKey(temp) ){
				map.put(temp, map.get(temp)+1);
			}else{
				map.put(temp, 1);
			}
		}
		//����map�ҵ���������doubleֵ
		Iterator it = map.keySet().iterator();
		int max = 0;
		while  ( it.hasNext() ){
			Object key = it.next();	
			if ( max < map.get(key) ){
				max = map.get(key);
			}
		}
		if ( 1 == max ){
			System.out.println("�����ݼ�������������");
			return null;
		}
		mode = new ArrayList<Double>();  //ʵ����List<Double>,����NullPointerException
		it = map.keySet().iterator();
		while ( it.hasNext() ){
			Double key = (Double)it.next();
			if ( max == map.get(key) ){
		//		System.out.println("key="+key);
				mode.add(key);
			}
		}
		return mode;
	}
/* �����ķ�λ��
 * (n+1)��4�ı���ʱ��Q1=data[(n+1)/4-1], Q3=data[(n+1)*3/4-1]  ע�⣺�����data[0]��ʼ�������
 * (n+1)����4�ı���ʱ����������ķ�λ��λ�ô���С������ʱ�ķ�λ�������С�����ڵ���������λ���ϵı�־ֵ�Ĵ�Ȩƽ������Ȩ����Сȡ������������λ�þ��С���ľ���
 * ����n=10����11/4=2.75����Q1=0.25*data[2-1]+0.75*data[3-1];  
 *        11*3/4=8.25,��Q1=0��75*data[8-1]+0.25*data[9-1];
 * */
	//�����ķ�֮һλ��                                           
	public double Q1(){
		int n = data.length;
		if ( n == 0 ){
			System.out.println("����Ϊ�գ��޷�����Q1");
			Q1 = -1;
		}else{
			if ( (n+1)%4 == 0 ){
				Q1 = data[(n+1)/4-1];
			}else{   //����������С�����֣��õ�Ȩֵ                
				int integerPart = (int)Math.floor(1.0*(n+1)/4);
				double factionalPart = 1.0*(n+1)/4-integerPart;
				Q1 = (1-factionalPart)*data[integerPart-1] + factionalPart*data[integerPart];
			}
		}
		return Q1;
	}
	//�����ķ�֮��λ��
	public double Q3(){
		int n = data.length;
		if ( n == 0 ){
			System.out.println("����Ϊ�գ��޷�����Q3");
			Q3 = -1;
		}else{
			if ( (n+1)%4 == 0 ){
				Q3 = data[(n+1)*3/4-1];
			}else{
				int integerPart = (int)Math.floor(1.0*(n+1)*3/4);
				double factionalPart = 1.0*(n+1)*3/4-integerPart;
				Q3 = (1-factionalPart)*data[integerPart-1] + factionalPart*data[integerPart];
			}
		}
		return Q3;
	}
	//�����ķ�λ������
	public double IQR(){
		IQR = Q3()-Q1();
		return IQR;
	}
	//������Сֵ
	public double min(){
		min = data[0];
		return min;
	}
	//�������ֵ
	public double max(){
		max = data[data.length-1];
		return max;
	}
	//����������(�����������ֵ����Сֵ��ƽ��ֵ)
	public double midrange(){
		midrange = (max()+min())/2;
		return midrange;
	}
	//���ط���
	public double variance(){
		double ave = mean();
		double sum = 0.0;
		for ( double temp:data ){
			sum += Math.pow(temp-ave,2);
		}
		variance = sum/data.length;
		return variance;
	}
	//���ر�׼��
	public double standard_deviation(){
		standard_deviation = Math.sqrt(variance());
		return standard_deviation;
	}

	//����������ݼ���ƽ��ֵ����λ�����������ķ�֮һλ�����ķ�֮��λ�����ķ�λ�������Сֵ�����ֵ���������������׼��
	public static void main(String[] args){
		double[] dataSet = {1.2, 2.4, 5.2, 4.7, 0.9, 1.4, 8.8, 143, 45.3, 99.4, 1.2, 1.2, 2.4, 2.4};
		//	{13, 13.5, 13.8, 13.9, 14, 14.6, 14.8, 15, 15.2, 15.4, 15.7};  11
		//  {13, 13.5, 13.8, 13.9, 14, 14.6, 14.8, 15, 15.2, 15.4};   10
		//	{1.2, 2.4, 5.2, 4.7, 0.9, 1.4, 8.8, 143, 45.3, 99.4, 1.2, 1.2, 2.4, 2.4};  14
		Statistics test = new Statistics(dataSet);
		System.out.println("mean="+test.mean());
		System.out.println("median="+test.median());
		System.out.println("mode="+test.mode());
		System.out.println("Q1="+test.Q1());
		System.out.println("Q3="+test.Q3());
		System.out.println("IQR="+test.IQR());
		System.out.println("min="+test.min());
		System.out.println("max="+test.max());
		System.out.println("midrange="+test.midrange());
		System.out.println("variance="+test.variance());
		System.out.println("standard_deviation="+test.standard_deviation());
	}
}
