package chapter3;

import java.util.Scanner;

/**
 * 
 * @author ac
 * 第三章练习13(P52)
 * 二进制形式显示char类型的值
 * 
 */
public class CharToBinaryForm {
	public static void getBinaryString(char c){
		System.out.println(Integer.toBinaryString(c));
	}
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		while ( scanner.hasNext() ){
			String input = scanner.next();
			for ( int i=0; i<input.length(); i++ ){
				System.out.println(input.charAt(i)+": "+Integer.toBinaryString(input.charAt(i)));
			}
		}
	}
}
