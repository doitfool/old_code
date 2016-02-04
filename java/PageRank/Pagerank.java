package pr;

import java.util.Random;

public class Pagerank {
	public void matrixInversion(double[][] transitionMatrix) { // 矩阵转置
		int n = transitionMatrix.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				double temp = transitionMatrix[i][j];
				transitionMatrix[i][j] = transitionMatrix[j][i];
				transitionMatrix[j][i] = temp;
			}
		}
	}

	public double[][] getTransitionMatrix(int[][] incidenceMatrix) { // 将邻接矩阵转换成转移矩阵(需要转置)
		int n = incidenceMatrix.length;
		double[][] transitionMatrix = new double[n][n];
		int[] notZeros = new int[n]; // 记录每个节点外链的个数
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (incidenceMatrix[i][j] != 0) {
					notZeros[i] += 1;
				}
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (notZeros[i] != 0) {
					transitionMatrix[i][j] = 1.0 * incidenceMatrix[i][j]
							/ notZeros[i];
					// System.out.print(transitionMatrix[i][j]+"              ");
				}
			}
			// System.out.println();
		}
		matrixInversion(transitionMatrix);
		/*
		 * System.out.println("转置后"); 
		 * for ( int i=0; i<n; i++ ){ 
		 *     for ( int j=0; j<n; j++ ){
		 * 	        System.out.print(transitionMatrix[i][j]+"                    "); 
		 * 	   }
		 *     System.out.println(); 
		 * }
		 */
		return transitionMatrix;
	}

	private boolean precisionMeet(double[] prevPR, double[] pr, double precision) { // 判断精度是否满足
		Boolean meet = true;
		int n = pr.length;
		for (int i = 0; i < n; i++) {
			if (Math.abs(pr[i] - prevPR[i]) > precision) { // 迭代过程中，连续两次的pr值差的绝对值大于要求的误差精度便继续迭代
				meet = false;
				break;
			}
		}
		return meet;
	}

	public double[] getSimplePR(double[] prevPR, double[][] transitionMatrix,
			double precision) { // 求节点的简化pr值（简化的pagerank算法依赖pr初始值）
		int n = transitionMatrix.length;
		double[] pr = new double[n];
		System.arraycopy(prevPR, 0, pr, 0, n);
		int iteration = 0; // 记录迭代次数
		do {
			System.arraycopy(pr, 0, prevPR, 0, n);
			for (int i = 0; i < n; i++) {
				double temp = 0.0;
				for (int j = 0; j < n; j++) {
					if (j != i) {
						temp += prevPR[j] * transitionMatrix[i][j];
					}
				}
				pr[i] = temp;
			}/*
			System.out.println("第" + (++iteration) + "次迭代结果：");
			for (double v : pr) {
				System.out.print(v + "   ");
			}
			System.out.println();*/
		} while (!precisionMeet(prevPR, pr, precision));
		return pr;
	}

	public double[] getPR(double[] prevPR, double[][] transitionMatrix,
			double precision, double weight) { // 求节点的优化pr值,weight表示跳转权重,优化后收敛结果与初始值无关,初始值只会影响迭代次数
		int n = transitionMatrix.length;
		double[] pr = new double[n];
		System.arraycopy(prevPR, 0, pr, 0, n);
		int iteration = 0; // 记录迭代次数
		do {
			System.arraycopy(pr, 0, prevPR, 0, n);
			for (int i = 0; i < n; i++) {
				double temp = 0.0;
				for (int j = 0; j < n; j++) {
					if (j != i) {
						temp += prevPR[j] * transitionMatrix[i][j];
					}
				}
				pr[i] = weight * temp + (1 - weight) / n;
			}
			System.out.println("第" + (++iteration) + "次迭代结果：");
			for (double v : pr) {
				System.out.print(v + "   ");
			}
			System.out.println();
		} while (!precisionMeet(prevPR, pr, precision));
		return pr;
	}
	
	public int[][] getRanIncidenceMatrix(int n){    //随机生成n*n的邻接矩阵
		int[][] incidenceMatrix = new int[n][n];
		Random r = new Random();
		for ( int i=0; i<n; i++ ){
			for ( int j=0; j<n; j++ ){
				incidenceMatrix[i][j] = r.nextInt(2);
			}
		}
		return incidenceMatrix;
	}
	
	public static void main(String[] args) {
		double precision = 0.0000000000000001, weight = 0.85; // 定义迭代精度和权值
		/*
		int[][] incidenceMatrix = { { 0, 1, 1, 1 }, 
									{ 0, 0, 0, 1 },
									{ 1, 1, 0, 1 }, 
									{ 0, 0, 1, 0 } };
									*/
		/*封闭问题测试邻接矩阵
		int[][] incidenceMatrix = { { 0, 1, 1, 1 }, 
									{ 0, 0, 0, 0 },
									{ 1, 1, 0, 1 }, 
									{ 0, 0, 1, 0 } };
		 */
		Pagerank pr = new Pagerank();
	//	随机生成n*n的测试邻接矩阵
		int[][] incidenceMatrix = pr.getRanIncidenceMatrix(100);
		int n = incidenceMatrix.length;
		System.out.println("邻接矩阵：");
		for ( int i=0; i<n; i++ ){
			for ( int j=0; j<n; j++ ){
				System.out.print(incidenceMatrix[i][j]+"   ");
			}
			System.out.println();
		}
		Random r = new Random();
		double[] initialPR = new double[n];    //存放最初pr值
		double[] finalPR = new double[n];      //存放最终pr值
		System.out.println("节点初始pr值为：");
		for (int i = 0; i < n; i++) {
			initialPR[i] = r.nextDouble();
			System.out.print(initialPR[i] + "   ");
		}
		System.out.println(); 

		double[][] transitionMatrix = pr.getTransitionMatrix(incidenceMatrix);
	//	finalPR = pr.getSimplePR(initialPR, transitionMatrix, precision);
		finalPR = pr.getPR(initialPR, transitionMatrix, precision, weight);
		System.out.println("迭代结果：");
		for (double v : finalPR) {
			System.out.print(v + "   ");
		}
		System.out.println();
		
	}
}