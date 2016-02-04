package chapter3;
/**
 * 
 * @author ac
 * 第三章练习11(P52)
 * 对数字进行有符号循环右移
 */
public class SignedNumRotateRight {
	public static void rotateRight(int shiftNum){
		int i=0;
		while ( shiftNum!=0 ){
			System.out.println(i+":  "+Integer.toBinaryString(shiftNum));
			shiftNum>>=1;
			i++;
		}
	}
	public static void main(String[] args){
		int shiftNum = 0x8000;
		SignedNumRotateRight.rotateRight(shiftNum);
	}
}
