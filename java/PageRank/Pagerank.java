package pr;

import java.util.Random;

public class Pagerank {
	public void matrixInversion(double[][] transitionMatrix) { // ����ת��
		int n = transitionMatrix.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				double temp = transitionMatrix[i][j];
				transitionMatrix[i][j] = transitionMatrix[j][i];
				transitionMatrix[j][i] = temp;
			}
		}
	}

	public double[][] getTransitionMatrix(int[][] incidenceMatrix) { // ���ڽӾ���ת����ת�ƾ���(��Ҫת��)
		int n = incidenceMatrix.length;
		double[][] transitionMatrix = new double[n][n];
		int[] notZeros = new int[n]; // ��¼ÿ���ڵ������ĸ���
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
		 * System.out.println("ת�ú�"); 
		 * for ( int i=0; i<n; i++ ){ 
		 *     for ( int j=0; j<n; j++ ){
		 * 	        System.out.print(transitionMatrix[i][j]+"                    "); 
		 * 	   }
		 *     System.out.println(); 
		 * }
		 */
		return transitionMatrix;
	}

	private boolean precisionMeet(double[] prevPR, double[] pr, double precision) { // �жϾ����Ƿ�����
		Boolean meet = true;
		int n = pr.length;
		for (int i = 0; i < n; i++) {
			if (Math.abs(pr[i] - prevPR[i]) > precision) { // ���������У��������ε�prֵ��ľ���ֵ����Ҫ������ȱ��������
				meet = false;
				break;
			}
		}
		return meet;
	}

	public double[] getSimplePR(double[] prevPR, double[][] transitionMatrix,
			double precision) { // ��ڵ�ļ�prֵ���򻯵�pagerank�㷨����pr��ʼֵ��
		int n = transitionMatrix.length;
		double[] pr = new double[n];
		System.arraycopy(prevPR, 0, pr, 0, n);
		int iteration = 0; // ��¼��������
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
			System.out.println("��" + (++iteration) + "�ε��������");
			for (double v : pr) {
				System.out.print(v + "   ");
			}
			System.out.println();*/
		} while (!precisionMeet(prevPR, pr, precision));
		return pr;
	}

	public double[] getPR(double[] prevPR, double[][] transitionMatrix,
			double precision, double weight) { // ��ڵ���Ż�prֵ,weight��ʾ��תȨ��,�Ż�������������ʼֵ�޹�,��ʼֵֻ��Ӱ���������
		int n = transitionMatrix.length;
		double[] pr = new double[n];
		System.arraycopy(prevPR, 0, pr, 0, n);
		int iteration = 0; // ��¼��������
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
			System.out.println("��" + (++iteration) + "�ε��������");
			for (double v : pr) {
				System.out.print(v + "   ");
			}
			System.out.println();
		} while (!precisionMeet(prevPR, pr, precision));
		return pr;
	}
	
	public int[][] getRanIncidenceMatrix(int n){    //�������n*n���ڽӾ���
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
		double precision = 0.0000000000000001, weight = 0.85; // ����������Ⱥ�Ȩֵ
		/*
		int[][] incidenceMatrix = { { 0, 1, 1, 1 }, 
									{ 0, 0, 0, 1 },
									{ 1, 1, 0, 1 }, 
									{ 0, 0, 1, 0 } };
									*/
		/*�����������ڽӾ���
		int[][] incidenceMatrix = { { 0, 1, 1, 1 }, 
									{ 0, 0, 0, 0 },
									{ 1, 1, 0, 1 }, 
									{ 0, 0, 1, 0 } };
		 */
		Pagerank pr = new Pagerank();
	//	�������n*n�Ĳ����ڽӾ���
		int[][] incidenceMatrix = pr.getRanIncidenceMatrix(100);
		int n = incidenceMatrix.length;
		System.out.println("�ڽӾ���");
		for ( int i=0; i<n; i++ ){
			for ( int j=0; j<n; j++ ){
				System.out.print(incidenceMatrix[i][j]+"   ");
			}
			System.out.println();
		}
		Random r = new Random();
		double[] initialPR = new double[n];    //������prֵ
		double[] finalPR = new double[n];      //�������prֵ
		System.out.println("�ڵ��ʼprֵΪ��");
		for (int i = 0; i < n; i++) {
			initialPR[i] = r.nextDouble();
			System.out.print(initialPR[i] + "   ");
		}
		System.out.println(); 

		double[][] transitionMatrix = pr.getTransitionMatrix(incidenceMatrix);
	//	finalPR = pr.getSimplePR(initialPR, transitionMatrix, precision);
		finalPR = pr.getPR(initialPR, transitionMatrix, precision, weight);
		System.out.println("���������");
		for (double v : finalPR) {
			System.out.print(v + "   ");
		}
		System.out.println();
		
	}
}