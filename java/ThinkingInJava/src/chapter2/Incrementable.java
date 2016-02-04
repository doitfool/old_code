package chapter2;
/**
 * 
 * @author ac
 * 第二章练习7
 * static类型的变量和方法可通过类名直接使用，不需实例化该类
 */
public class Incrementable {
	static void increment(){
		StaticTest.i++;
	}
	public static void main(String[] args){
		System.out.println(StaticTest.i);  //static类型变量可通过类名直接饮用
		Incrementable.increment(); //static类型方法可通过类名直接调用
		System.out.println(StaticTest.i);
	}
}

class StaticTest{
	static int i=47;
}