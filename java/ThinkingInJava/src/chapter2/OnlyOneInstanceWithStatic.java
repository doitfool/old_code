/**
 * @author ac
 * 第二章练习8
 * static单实例
 * 无论创建某个特定类的多少个对象，该类中的某个特定的static域只有一个实例
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
