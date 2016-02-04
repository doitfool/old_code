package chapter3;
/**
 * 
 * @author ac
 * µÚÈýÕÂÁ·Ï°5ºÍÁ·Ï°6(P45)
 */
public class Dog {
	String name;
	String says;
	public Dog(String name, String says){
		this.name = name;
		this.says = says;
	}
	public static void main(String[] args){
		Dog dog1 = new Dog("spot", "Ruff");
		Dog dog2 = new Dog("scruffy", "Wurf");
		System.out.println("dog1 name:"+dog1.name+"  dog1 says:"+dog1.says);
		System.out.println("dog2 name:"+dog2.name+"  dog2 says:"+dog2.says);
		Dog newDog = dog1;
		System.out.println(newDog==dog1);
		System.out.println(newDog!=dog1);
		System.out.println(newDog.equals(dog1));
	}
}
