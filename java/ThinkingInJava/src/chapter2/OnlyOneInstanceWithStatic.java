/**
 * @author ac
 * �ڶ�����ϰ8
 * static��ʵ��
 * ���۴���ĳ���ض���Ķ��ٸ����󣬸����е�ĳ���ض���static��ֻ��һ��ʵ��
 */
package chapter2;

public class OnlyOneInstanceWithStatic {
	
	static int testNum=10;
	public static void main(String[] args){
		OnlyOneInstanceWithStatic o1 = new OnlyOneInstanceWithStatic();
		OnlyOneInstanceWithStatic o2 = new OnlyOneInstanceWithStatic();
		
		System.out.println("Before Change.");
		System.out.println(o1.testNum);
		System.out.println(o2.testNum);
		
		System.out.println("After change the testNum of o1 instance");
		o1.testNum = 12;
		System.out.println(o1.testNum);
		System.out.println(o2.testNum);
	}
}
