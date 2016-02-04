package chapter4;
import java.awt.font.NumericShaper.Range;
import java.util.*;
/**
 * 
 * @author ac
 * 第四章练习3(P67)
 * 生成int类型随机数并与其后生成的随机数比较分类
 * 
 */
public class IfElse {
	public static void main(String[] args){
		Random random = new Random();
		int num1 = random.nextInt();
		int num2;
		while ( true ){
			num2 = random.nextInt();
			System.out.print("num1="+num1+"  num2="+num2);
			if ( num1 > num2 ){
				System.out.println("   >");
			}else if ( num1 < num2 ){
				System.out.println("   <");
			}else{
				System.out.println("   =");
			}
			num1 = num2;
		}
	}
}
