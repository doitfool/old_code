package chapter4;
/**
 * 
 * @author ac
 * ��������ϰ10(P75)
 * �ҳ�����4λ������Ѫ������(λ��Ϊż��������һ��������˵õ����Ҹö����ָ������˻���һ��λ�������д������������ѡȡ�����ֿ���������������0��β�������ǲ�����ġ�)
 * eg��	1260=21��60�� 	1827=21��87�� 2187=27��81		
 * 
 */
public class VampireNumber {
	//��4λ����������Ѫ������
	Boolean judgeVampireNum(int a[], int num){
		boolean result=false;
		for ( int i=0; i<4; i++ ){
			for ( int j=0; j<4; j++ ){
				if ( j!=i ){
					for ( int x=0; x<4; x++ ){
						if ( x!=i && x!=j ){
							for ( int y=0; y<4; y++ ){
								if ( y!=i && y!=j && y!=x ){
									int n1=10*a[i]+a[j], n2=10*a[x]+a[y], n3=10*a[j]+a[i], n4=10*a[y]+a[x];
									if ( n1*n2==num || n1*n4==num || n3*n2==num || n3*n4==num ){
										result = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		return  result;
	}
	public void getVampireNum(){
		for ( int i=1001; i<10000; i++ ){
			if ( i%100==0 )
				continue;
			//�������λ����ÿһλ����
			int a[] = new int[4];
			a[0]=i/1000;  	//���λ����a 
			a[1]=i/100%10; //�θ�λ����b
			a[2]=i/10%10;  //�ε�λ����c
			a[3]=i%10;     //���λ����d
			if ( judgeVampireNum(a,i) ){
				System.out.println(i);
			}
		}
	}
	public static void main(String[] args){
		VampireNumber vn = new VampireNumber();
		vn.getVampireNum();
	}
}
