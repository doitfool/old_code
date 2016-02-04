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
//将数据挖掘中的数据统计量封装成类Statistics
public class Statistics {
	//统计数据集的平均值，中位数，众数，四分之一位数，四分之三位数，四分位数极差，最小值，最大值，中列数，方差，标准差
	private double mean, median, Q1, Q3, IQR, min, max, midrange, variance, standard_deviation;
	private List<Double> mode;
	private double[] data;
	public Statistics(double... dataSet){
		data = dataSet;
		Arrays.sort(data);
		/*输出排序后的数据集
		for ( double i:data){
			System.out.print(i+"  ");
		}
		System.out.println();
		*/
	}
	//返回平均值
	public double mean(){
		double sum=0.0;
		for (double temp : data){
			sum += temp;
		}
		mean = sum/data.length;
		return mean;
	}
	//返回中位数
	public double median(){
		if ( data.length % 2 == 1){
			median = data[data.length/2];
		}else{
			median = (data[data.length/2]+data[(data.length+1)/2])/2; //数据集有偶数个数时通常取最中间的两个数的平均值作为中位数
		}
		return median;
	}
	//返回众数(数据集中出现次数最多的数,可能不止一个众数)                             
	public List<Double> mode(){
		//使用map记录不同double值出现的次数
		HashMap<Double, Integer> map = new HashMap<Double, Integer>();
		for ( double temp:data ){
			if ( map.containsKey(temp) ){
				map.put(temp, map.get(temp)+1);
			}else{
				map.put(temp, 1);
			}
		}
		//遍历map找到次数最多的double值
		Iterator it = map.keySet().iterator();
		int max = 0;
		while  ( it.hasNext() ){
			Object key = it.next();	
			if ( max < map.get(key) ){
				max = map.get(key);
			}
		}
		if ( 1 == max ){
			System.out.println("该数据集不存在众数。");
			return null;
		}
		mode = new ArrayList<Double>();  //实例化List<Double>,否则报NullPointerException
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
/* 计算四分位数
 * (n+1)是4的倍数时，Q1=data[(n+1)/4-1], Q3=data[(n+1)*3/4-1]  注意：数组从data[0]开始存放数据
 * (n+1)不是4的倍数时，计算出的四分位数位置带有小数，此时四分位数是与该小数相邻的两个整数位置上的标志值的带权平均数，权数大小取决于两个整数位置距该小数的距离
 * 比如n=10，则11/4=2.75，则Q1=0.25*data[2-1]+0.75*data[3-1];  
 *        11*3/4=8.25,则Q1=0。75*data[8-1]+0.25*data[9-1];
 * */
	//返回四分之一位数                                           
	public double Q1(){
		int n = data.length;
		if ( n == 0 ){
			System.out.println("数据为空，无法计算Q1");
			Q1 = -1;
		}else{
			if ( (n+1)%4 == 0 ){
				Q1 = data[(n+1)/4-1];
			}else{   //分离整数和小数部分，得到权值                
				int integerPart = (int)Math.floor(1.0*(n+1)/4);
				double factionalPart = 1.0*(n+1)/4-integerPart;
				Q1 = (1-factionalPart)*data[integerPart-1] + factionalPart*data[integerPart];
			}
		}
		return Q1;
	}
	//返回四分之三位数
	public double Q3(){
		int n = data.length;
		if ( n == 0 ){
			System.out.println("数据为空，无法计算Q3");
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
	//返回四分位数极差
	public double IQR(){
		IQR = Q3()-Q1();
		return IQR;
	}
	//返回最小值
	public double min(){
		min = data[0];
		return min;
	}
	//返回最大值
	public double max(){
		max = data[data.length-1];
		return max;
	}
	//返回中列数(中列数是最大值和最小值的平均值)
	public double midrange(){
		midrange = (max()+min())/2;
		return midrange;
	}
	//返回方差
	public double variance(){
		double ave = mean();
		double sum = 0.0;
		for ( double temp:data ){
			sum += Math.pow(temp-ave,2);
		}
		variance = sum/data.length;
		return variance;
	}
	//返回标准差
	public double standard_deviation(){
		standard_deviation = Math.sqrt(variance());
		return standard_deviation;
	}

	//测试输出数据集的平均值，中位数，众数，四分之一位数，四分之三位数，四分位数极差，最小值，最大值，中列数，方差，标准差
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
