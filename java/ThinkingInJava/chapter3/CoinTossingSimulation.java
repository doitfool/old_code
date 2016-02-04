package chapter3;
import java.util.*;
/**
 * 
 * @author ac
 * 第三章练习7(P46)
 * 编程模拟抛硬币
 */
public class CoinTossingSimulation {
	public StringBuilder getResult(){
		StringBuilder result = new StringBuilder();
		int i = new Random().nextInt(2);
		System.out.println("i="+i);
		if ( i==0 )
			result.append("the reverse side");  //硬币反面
		else if ( i==1 )
			result.append("the front side"); //硬币正面
		return result;
		
		/** 另一种方法是用String
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
