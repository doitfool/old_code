package chapter2;
/**
 * 
 * @author ac
 * �ڶ�����ϰ7
 * static���͵ı����ͷ�����ͨ������ֱ��ʹ�ã�����ʵ��������
 */
public class Incrementable {
	static void increment(){
		StaticTest.i++;
	}
	public static void main(String[] args){
		System.out.println(StaticTest.i);  //static���ͱ�����ͨ������ֱ������
		Incrementable.increment(); //static���ͷ�����ͨ������ֱ�ӵ���
		System.out.println(StaticTest.i);
	}
}

class StaticTest{
	static int i=47;
}