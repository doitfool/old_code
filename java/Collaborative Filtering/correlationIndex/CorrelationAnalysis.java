package correlationIndex;

import java.nio.channels.AcceptPendingException;
import java.util.Random;

//一些相关性计算方法
public class CorrelationAnalysis {
	//函数参数double[] x与double[] y分别表示待计算相似性的两个向量
	//相似性计算
	public double sim(int[] x, int[] y){
		if ( x.length != y.length){
			System.out.println("属性不同，无法比较相似性");
			return -1;
		}
		int n = x.length;
		double dis = 0.0, sim = 0.0;
		for ( int i=0; i<n; i++ ){
			dis += Math.pow(x[i]-y[i],2);
		}
		dis = Math.sqrt(dis);
		sim = 1.0/(1+dis);
		return sim;
	}
	public double sim(double[] x, double[] y){
		if ( x.length != y.length){
			System.out.println("属性不同，无法比较相似性");
			return -1;
		}
		int n = x.length;
		double dis = 0.0, sim = 0.0;
		for ( int i=0; i<n; i++ ){
			dis += Math.pow(x[i]-y[i],2);
		}
		dis = Math.sqrt(dis);
		sim = 1.0/(1+dis);
		return sim;
	}
	
	//皮尔逊相关系数
	public double pearsonCorrelationCoefficient(double[] x, double[] y){
		if ( x.length != y.length){
			System.out.println("属性不同，无法计算皮尔逊相关系数");
			return -1;
		}
		int n = x.length;
		double xSum=0.0, ySum=0.0, xxSum=0.0, yySum=0.0, xySum=0.0;
		for ( int i=0; i<n; i++ ){
			xSum += x[i];
			ySum += y[i];
			xxSum += x[i]*x[i];
			yySum += y[i]*y[i];
			xySum += x[i]*y[i];
		}
		double pearson = (n*xySum-xSum*ySum)/(Math.sqrt(n*xxSum-xSum*xSum)*Math.sqrt(n*yySum-ySum*ySum));
		return pearson;
	}
	
	//余弦相似性
	public double cosine(double[] x, double[] y){
		if ( x.length != y.length){
			System.out.println("属性不同，无法计算皮尔逊相关系数");
			return -1;
		}
		int n = x.length;
		double xxSum=0.0, yySum=0.0, xySum=0.0;
		for ( int i=0; i<n; i++ ){
			xxSum += x[i]*x[i];
			yySum += y[i]*y[i];
			xySum += x[i]*y[i];
		}
		double cosine = xySum/(Math.sqrt(xxSum)*Math.sqrt(yySum));
		return cosine;
	}
	
	//Tanimoto系数
	public double tanimoto(double[] x, double[] y){
		if ( x.length != y.length){
			System.out.println("属性不同，无法计算皮尔逊相关系数");
			return -1;
		}
		int n = x.length;
		double xxSum=0.0, yySum=0.0, xySum=0.0;
		for ( int i=0; i<n; i++ ){
			xxSum += x[i]*x[i];
			yySum += y[i]*y[i];
			xySum += x[i]*y[i];
		}
		double tanimoto = xySum/(Math.sqrt(xxSum)+Math.sqrt(yySum)-xySum);
		return tanimoto;
	}
	public static void main(String[] args){
		double[] x={1,0,0,1,1,0,1,1,1,0,0,1,0,1,0};
		double[] y={0,0,0,1,0,1,1,0,1,0,1,1,1,1,0};
		CorrelationAnalysis ca = new CorrelationAnalysis();
		
		System.out.println("相似度sim(x,y)="+ca.sim(x, y));
		System.out.println("皮尔逊相关系数p(x,y)="+ca.pearsonCorrelationCoefficient(x, y));
		System.out.println("余弦相似度c(x,y)="+ca.cosine(x, y));
		System.out.println("Tanimoto系数t(x,y)="+ca.tanimoto(x, y));
		
		Random r = new Random();
		double[] z = new double[x.length];
		for ( int i=0; i<x.length; i++ ){
			System.out.print(x[i]+" ");
		}
		System.out.println();
		for ( int i=0; i<y.length; i++ ){
			System.out.print(y[i]+" ");
		}
		System.out.println();
		
		for ( int i=0; i<x.length; i++ ){
			z[i] = r.nextInt(2);
			System.out.print(z[i]+" ");
		}
		System.out.println();
		
		System.out.println("相似度sim(x,z)="+ca.sim(x, z));
		System.out.println("皮尔逊相关系数p(x,z)="+ca.pearsonCorrelationCoefficient(x, z));
		System.out.println("余弦相似度c(x,z)="+ca.cosine(x, z));
		System.out.println("Tanimoto系数t(x,z)="+ca.tanimoto(x, z));
		
		System.out.println("相似度sim(z,y)="+ca.sim(z, y));
		System.out.println("皮尔逊相关系数p(z,y)="+ca.pearsonCorrelationCoefficient(z, y));
		System.out.println("余弦相似度c(z,y)="+ca.cosine(z, y));
		System.out.println("Tanimoto系数t(z,y)="+ca.tanimoto(z, y));
		
		
	}
}
