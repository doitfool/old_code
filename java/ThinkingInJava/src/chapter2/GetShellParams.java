package chapter2;
/**
 * 
 * @author ac
 * �ڶ�����ϰ10
 * ��ӡ���������л�õ���������
 * ��������(Run->Run Configrations...)Ϊparam1 param2 param3
 * args[0]=param1, args[1]=param2, args[2]=param3
 */
public class GetShellParams {
	public static void main(String[] args){
		System.out.println("The first parameter from commandline is "+args[0]);
		System.out.println("The second parameter from commandline is "+args[1]);
		System.out.println("The third parameter from commandline is "+args[2]);
	}
}
