package chapter4;
/**
 * 
 * @author ac
 * 第四章练习10(P75)
 * 找出所有4位数的吸血鬼数字(位数为偶数，可由一对数字相乘得到，且该对数字各包含乘积的一半位数，其中从最初的数字中选取的数字可任意排序。以两个0结尾的数字是不允许的。)
 * eg：	1260=21×60， 	1827=21×87， 2187=27×81		
 * 
 */
public class VampireNumber {
	//求4位数的所有吸血鬼数字
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
			//求出该四位数的每一位数字
			int a[] = new int[4];
			a[0]=i/1000;  	//最高位数字a 
			a[1]=i/100%10; //次高位数字b
			a[2]=i/10%10;  //次低位数字c
			a[3]=i%10;     //最低位数字d
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
