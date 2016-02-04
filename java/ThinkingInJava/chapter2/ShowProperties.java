package chapter2;
public class ShowProperties {
	public static void main(String[] args){
		System.getProperties().list(System.out);  //获得系统属性，提供环境信息
		System.out.println(System.getProperty("user.name"));  
		System.out.println(System.getProperty("java.library.path"));
	}
}
