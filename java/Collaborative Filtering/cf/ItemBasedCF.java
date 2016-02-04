package cf;

import java.util.Random;
import java.util.Scanner;

import correlationIndex.*;
/*模拟基于物品的协同过滤算法:计算矩阵的列与列间的相似度
 * 随机生成二维用户-物品矩阵，如下
 * 			  itemID1	itemID2	  itemID3       。。。  
 * userID1      1/0       1/0       1/0
 * userID2      1/0       1/0       1/0
 * userID3      1/0       1/0       1/0
 *    。
 *    。
 *    。
 */
public class ItemBasedCF {
	public int[][] getRanIncidenceMatrix(int m, int n){    //随机生成m*n的邻接矩阵
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
