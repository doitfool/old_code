package cf;

import java.util.Random;
import java.util.Scanner;

import correlationIndex.*;
/*ģ�������Ʒ��Эͬ�����㷨:�������������м�����ƶ�
 * ������ɶ�ά�û�-��Ʒ��������
 * 			  itemID1	itemID2	  itemID3       ������  
 * userID1      1/0       1/0       1/0
 * userID2      1/0       1/0       1/0
 * userID3      1/0       1/0       1/0
 *    ��
 *    ��
 *    ��
 */
public class ItemBasedCF {
	public int[][] getRanIncidenceMatrix(int m, int n){    //�������m*n���ڽӾ���
		int[][] incidenceMatrix = new int[m][n];
		Random r = new Random();
		for ( int i=0; i<m; i++ ){
			for ( int j=0; j<n; j++ ){
				incidenceMatrix[i][j] = r.nextInt(2);
			}
		}
		return incidenceMatrix;
	}
	
}
