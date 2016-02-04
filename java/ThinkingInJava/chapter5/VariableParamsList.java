package chapter5;
/**
 * 
 * @author ac
 * 第五章练习20(P105)
 * 练习可变参数列表
 * 添加main函数参数(Run As->Run Configurations...)
 * 
 */
public class VariableParamsList {
	public static void main(String... args){
		for ( String arg: args){
			System.out.println(arg);
		}
	}
}
