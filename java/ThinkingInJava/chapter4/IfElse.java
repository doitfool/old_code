package chapter4;
import java.awt.font.NumericShaper.Range;
import java.util.*;
/**
 * 
 * @author ac
 * ��������ϰ3(P67)
 * ����int�������������������ɵ�������ȽϷ���
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
