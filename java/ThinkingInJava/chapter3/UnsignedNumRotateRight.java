package chapter3;
/**
 * 
 * @author ac
 * ��������ϰ12(P52)
 * �����ֽ����޷���ѭ������
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
		shiftNum<<=1; //������
		UnsignedNumRotateRight.rotateRight(shiftNum);
	}
}
