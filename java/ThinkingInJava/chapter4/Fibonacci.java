package chapter4;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * 
 * @author ac
 * ��������ϰ9(P75)
 * ��ӡ쳲�������������
 * 
 */
public class Fibonacci {
	public static void print(int num){
		int n1 = 1, n2 = 1;
		if ( num==1 ){
			System.out.println("1");
		}else if ( num==2 ){
			System.out.println("1 1");
		}else{
			System.out.print("1 1 ");
			while ( num-->2 ){
				int temp = n1+n2;
				/* ���ڼӷ����   ���г���>=47ʱ���
				if ( temp<0 ){
					
				}
				*/
				n1 = n2;
				n2 = temp;
				System.out.print(temp+" ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		while ( scanner.hasNext() ){
			Fibonacci.print(scanner.nextInt());
		}
	}
}
