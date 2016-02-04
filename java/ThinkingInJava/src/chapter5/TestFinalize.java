package chapter5;
/**
 * 
 * @author ac
 * ��������ϰ10(P89)
 * 
 */
public class TestFinalize {
	private int id;
	public TestFinalize(int id){
		this.id = id;
		System.out.println("Test "+id+" is created");
	}
	protected void finalize() throws Throwable{
		super.finalize();
		System.out.println("Test "+id+" is disposed");
	}
	public static void main(String[] args){
		TestFinalize tf1 = new TestFinalize(1);
		TestFinalize tf2 = new TestFinalize(2);
		TestFinalize tf3 = new TestFinalize(3);
		tf2 = tf3 = null;
		new TestFinalize(4);  //�ڴ���������������ʾ�ص�����  System.gc()������ʱ��finalize�������ܱ����á�
		System.gc();
	}
}
