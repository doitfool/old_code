package chapter3;
import java.util.*;
/**
 * 
 * @author ac
 * ��������ϰ7(P46)
 * ���ģ����Ӳ��
 */
public class CoinTossingSimulation {
	public StringBuilder getResult(){
		StringBuilder result = new StringBuilder();
		int i = new Random().nextInt(2);
		System.out.println("i="+i);
		if ( i==0 )
			result.append("the reverse side");  //Ӳ�ҷ���
		else if ( i==1 )
			result.append("the front side"); //Ӳ������
		return result;
		
		/** ��һ�ַ�������String
		  String result = "";
		  int i = new Random().nextInt(2);
		  System.out.println("i="+i);
		  if ( i==0 )
		  	   result = "the reverse side";
		  else if ( i==1 )
		      result = "the front side";
		  return result;
		 */
	}
	
	public static void main(String[] args){
		CoinTossingSimulation cts = new CoinTossingSimulation();
		while ( true ){
			System.out.println(cts.getResult().toString());
		}
	}
}
