package chapter3;

import java.util.Scanner;

/**
 * 
 * @author ac
 * ��������ϰ13(P52)
 * ��������ʽ��ʾchar���͵�ֵ
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
