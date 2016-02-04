package chapter6;
/**
 * 
 * @author ac
 * 第六章练习5(P120)
 * 调用public,protected,private和包访问权限作用域的大小
 * 
 */
public class TestActionScope {
	protected char c='x';
	public int i=10;
	float f=8.28f;
	double d=10.29d;
	public static void main(String[] args){
		TestActionScope tas = new TestActionScope();
		System.out.println("Character c="+tas.c+"   Integer i="+tas.i+"   Float f="+tas.f+"   Double d="+tas.d);
	}
}

class Test{
	Test(TestActionScope tas){
		System.out.println("Character c="+tas.c+"   Integer i="+tas.i+"   Float f="+tas.f+"   Double d="+tas.d);
	}
}
