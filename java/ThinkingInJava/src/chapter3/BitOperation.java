package chapter3;
/**
 * 
 * @author ac
 * ��������ϰ10(P49) λ����
 * ����Integer.toBinaryString()��ʾ
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
