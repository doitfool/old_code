package chapter3;
/**
 * 
 * @author ac
 * ��������ϰ11(P52)
 * �����ֽ����з���ѭ������
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
