package chapter5;
/**
 * 
 * @author ac
 * ��������ϰ20(P105)
 * ��ϰ�ɱ�����б�
 * ���main��������(Run As->Run Configurations...)
 * 
 */
public class VariableParamsList {
	public static void main(String... args){
		for ( String arg: args){
			System.out.println(arg);
		}
	}
}
