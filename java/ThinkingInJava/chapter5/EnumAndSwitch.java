package chapter5;
/**
 * 
 * @author ac
 * 第五章练习21，22(P107)
 * enum特性  enum与switch组合使用
 */
public class EnumAndSwitch {
	public enum Note{
		OneYuan, TwoYuan, FiveYuan, TenYuan, FiftyYuan, OnehundredYuan
	}
	public static void main(String[] args){
		for ( Note n: Note.values() ){
			System.out.println(n+" ordinal "+n.ordinal());
		}
		Note note= Note.FiftyYuan;
		System.out.print("This is ");
		switch(note){
			case OneYuan:        System.out.println("one yuan");      	 break;
			case TwoYuan:        System.out.println("two yuan");     	 break;
			case FiveYuan:       System.out.println("five yuan"); 		 break;
			case TenYuan:        System.out.println("ten yuan");         break;
			case FiftyYuan:      System.out.println("fifty yuan");       break;
			case OnehundredYuan: System.out.println("one hundred yuan"); break;
		}
	}
}
