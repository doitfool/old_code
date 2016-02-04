package chapter2;
/**
 * 
 * @author ac
 * 第二章练习1
 * 验证java中int和char基本类型的默认初始化
 * 
 */
public class DefaultInitialization {
	int intTemp;
	char charTemp;
	public static void main(String[] args){
		DefaultInitialization di = new DefaultInitialization();
		System.out.println("Integer is initialized "+di.intTemp+".");
		System.out.println("Character is initialized "+di.charTemp+".");
	}

}
