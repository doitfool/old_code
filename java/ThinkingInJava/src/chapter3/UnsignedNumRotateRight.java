package chapter3;
/**
 * 
 * @author ac
 * 第三章练习12(P52)
 * 对数字进行无符号循环右移
 * 
 */
public class UnsignedNumRotateRight {
	public static void rotateRight(int shiftNum){
		int i=0;
		while ( shiftNum!=0 ){
			System.out.println(i+":  "+Integer.toBinaryString(shiftNum));
			shiftNum>>>=1;
			i++;
		}
	}
	public static void main(String[] args){
		int shiftNum = 0xffff;
		shiftNum<<=1; //先左移
		UnsignedNumRotateRight.rotateRight(shiftNum);
	}
}
