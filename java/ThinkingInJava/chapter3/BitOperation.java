package chapter3;
/**
 * 
 * @author ac
 * 第三章练习10(P49) 位操作
 * 并用Integer.toBinaryString()显示
 * 
 */
public class BitOperation {
	public static void main(String[] args){
		
		int num1 = 0xAAAA;
		int num2 = 0x5555;
		System.out.println(Integer.toHexString(num1)+"H="+Integer.toBinaryString(num1));
		System.out.println(Integer.toHexString(num2)+"H="+Integer.toBinaryString(num2));
		System.out.println(Integer.toHexString(num1)+"H & "+Integer.toHexString(num2)+"H="+Integer.toBinaryString(num1&num2));
		System.out.println(Integer.toHexString(num1)+"H | "+Integer.toHexString(num2)+"H="+Integer.toBinaryString(num1|num2));
		System.out.println(Integer.toHexString(num1)+"H ^ "+Integer.toHexString(num2)+"H="+Integer.toBinaryString(num1^num2));
	}
}
