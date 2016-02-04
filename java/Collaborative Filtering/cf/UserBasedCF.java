package cf;

import java.util.Random;
import java.util.Scanner;

import correlationIndex.CorrelationAnalysis;

/*模拟基于用户的协同过滤算法：计算矩阵行与行间的相似度
* 随机生成二维用户-物品矩阵，如下
* 			  itemID1	itemID2	  itemID3       。。。  
 * userID1      1/0       1/0       1/0
 * userID2      1/0       1/0       1/0
 * userID3      1/0       1/0       1/0
*    。
*    。
*    。
*/
public class UserBasedCF {
	//随机生成m*n的邻接矩阵
	public static int[][] getRanIncidenceMatrix(int m, int n){    
		int[][] incidenceMatrix = new int[m][n];
		Random r = new Random();
		for ( int i=0; i<m; i++ ){
			for ( int j=0; j<n; j++ ){
				incidenceMatrix[i][j] = r.nextInt(2);
			}
		}
		return incidenceMatrix;
	}
	//获得每个用户已有物品的个数总和
	public int[] itemSum(int[][] matrix, int m, int n){
		int[] itemSum = new int[m];
		for (int i=0; i<m; i++ ){
			for (int j=0; j<n; j++ ){
				if ( matrix[i][j]==1 ){
					itemSum[i]++;
				}
			}
		}
		return itemSum;
	}
	//执行推荐计算
	public int userBasedCF(int[][] matrix, int userId, int m, int n){
		CorrelationAnalysis ca = new CorrelationAnalysis();
		int[] itemSum = itemSum(matrix, m, n);
		int recomId=-1;
		double sim=0.0;
		for ( int i=0; i<m; i++ ){
			if ( itemSum[i]>=itemSum[userId] && i!=userId ){
				double tempSim = ca.sim(matrix[i], matrix[userId]);  //使用相似性表示相关度，还有其它计算方法
				if ( tempSim > sim ){
					recomId = i;
					sim = tempSim;
				}
			}
		}
		return recomId;
	}
	public static void main(String[] args){
		int m=100, n=20;   //100个用户，20件物品
		int[][] matrix = getRanIncidenceMatrix(m, n);
		System.out.println("User-Item matrix("+m+"*"+n+") as follow:");
		for ( int i=0; i<m; i++ ){
			for ( int j=0; j<n; j++ ){
				System.out.print(matrix[i][j]+"  ");
			}
			System.out.println();
		}
		
		//输入用户id，为该用户推荐物品
		System.out.print("Input user id for recommendation:");
		Scanner scanner = new Scanner(System.in);
		while ( true ){
			int userId = scanner.nextInt();
			if ( userId<0 || userId>=m ){
				System.out.println("Id not found, please input again(Range 0~"+(m-1)+").");
				continue;
			}
			int recomId = new UserBasedCF().userBasedCF(matrix, userId, m, n);
			if ( recomId==-1 ){
				System.out.print("Recommend items(0~+"+(n-1)+"):");
				for ( int j=0; j<n; j++ ){
					if ( matrix[userId][j]==0 )
						System.out.print(j+"  ");
				}
				System.out.println("");
			}else{
				System.out.println("user "+userId+" is the most similar with user "+recomId);
				System.out.print("Recommend items(0~+"+(n-1)+"):");
				for ( int j=0; j<n; j++ ){
					if ( matrix[recomId][j]==1 && matrix[userId][j]==0 ){
						System.out.print(j+" ");
					}
				}
				System.out.println();
			}
		}
	}
}
