import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * 
 * @author AC
 * ����ģ�� ʹ�����ر�Ҷ˹����Ԥ������(P229)
 *
 */
public class BayesClassification {
	int buyCount=0,     //ѵ����������������Ϊyes��Ԫ�����
		noBuyCount=0;   //ѵ����������������Ϊno��Ԫ�����
	int ageCount=0,     //ѵ���������������ݵ�age����ֵ��ͬ��Ԫ����� 
		incomeCount=0,  //ѵ���������������ݵ�income����ֵ��ͬ��Ԫ����� 
		studentCount=0, //ѵ���������������ݵ�student����ֵ��ͬ��Ԫ����� 
		crCount=0;		//ѵ���������������ݵ�credit_rating����ֵ��ͬ��Ԫ����� 
	int ageBuyCount=0,  //�������ݵ�age����ֵ��ͬ��ǰ���£�ѵ�������е���������Ϊyes�ĸ���
		incomeBuyCount=0,  //�������ݵ�income����ֵ��ͬ��ǰ���£�ѵ�������е���������Ϊyes�ĸ���
		studentBuyCount=0,  //�������ݵ�student����ֵ��ͬ��ǰ���£�ѵ�������е���������Ϊyes�ĸ���
		crBuyCount=0;  //�������ݵ�credit_rating����ֵ��ͬ��ǰ���£�ѵ�������е���������Ϊyes�ĸ���
	static ArrayList<Tuple> tuples = new ArrayList<Tuple>();  
	public BayesClassification(){
		   String[][] data = {{"youth",        "high",    "no",   "fair",       "no"},
				              {"youth",        "high",    "no",   "excellent",  "no"},
				              {"middle_aged",  "high",    "no",   "fair",       "yes"},
				              {"senior",       "medium",  "no",   "fair",       "yes"},
				              {"senior",       "low",     "yes",  "fair",       "yes"},
				              {"senior",       "low",     "yes",  "excellent",  "no"},
				              {"middle_aged",  "low",     "yes",  "excellent",  "yes"},
				              {"youth",        "medium",  "no",   "fair",       "no"},
				              {"youth",        "low",     "yes",  "fair",       "yes"},
				              {"senior",       "medium",  "yes",  "fair",       "yes"},
				              {"youth",        "medium",  "yes",  "excellent",  "yes"},
				              {"middle_aged",  "medium",  "no",   "excellent",  "yes"},
				              {"middle_aged",  "high",    "yes",  "fair",       "yes"},
				              {"senior",       "medium",  "no",   "excellent",  "no"}};
		   //�洢ѵ�����ݵ��ڴ�
		   for (int i=0; i<14; i++ ){
				Tuple tuple = new Tuple(data[i]);
				tuples.add(tuple);
		   }
		   /* �������ݴ洢�ɹ�
			Iterator<Tuple> iterator = tuples.iterator();
			while ( iterator.hasNext() ){
				System.out.println(iterator.next());
			}
		   */
	}
	
	public void getCount(Tuple testTuple){
		//����tuples�õ���������ֵ
		Iterator<Tuple> iterator = tuples.iterator();
		while ( iterator.hasNext() ){
			Tuple tempTuple = iterator.next();
			if ( tempTuple.buys_computer.equals("yes") ){
				buyCount++;
			}
			else if ( tempTuple.buys_computer.equals("no") ){
				noBuyCount++;
			}
			if ( tempTuple.age.equals(testTuple.age) ){
				ageCount++;
				if ( tempTuple.buys_computer.equals("yes") ){
					ageBuyCount++;
				}
			}
			if ( tempTuple.income.equals(testTuple.income) ){
				incomeCount++;
				if ( tempTuple.buys_computer.equals("yes") ){
					incomeBuyCount++;
				}
			}
			if ( tempTuple.student.equals(testTuple.student) ){
				studentCount++;
				if ( tempTuple.buys_computer.equals("yes") ){
					studentBuyCount++;
				}
			}
			if ( tempTuple.credit_rating.equals(testTuple.credit_rating) ){
				crCount++;
				if ( tempTuple.buys_computer.equals("yes") ){
					crBuyCount++;
				}
			}
		}
		/* �������ֵ
		System.out.println("ageCount="+ageCount+"  ageBuyCount="+ageBuyCount);
		System.out.println("incomeCount="+incomeCount+"  incomeBuyCount="+incomeBuyCount);
		System.out.println("studentCount="+studentCount+"  studentBuyCount="+studentBuyCount);
		System.out.println("crCount="+crCount+"  crBuyCount="+crBuyCount);
		*/
	}
	public void bayesPrediction(Tuple testTuple){
		//������������Ϊyes��no�ĸ���
		double buyComputer=0.0, noBuyComputer=0.0;
		buyComputer=(1.0*ageBuyCount/buyCount)*(1.0*incomeBuyCount/buyCount)*(1.0*studentBuyCount/buyCount)*(1.0*crBuyCount/buyCount)*(1.0*buyCount/14);
		noBuyComputer=(1.0*(ageCount-ageBuyCount)/noBuyCount)*(1.0*(incomeCount-incomeBuyCount)/noBuyCount)*
				(1.0*(studentCount-studentBuyCount)/noBuyCount)*(1.0*(crCount-crBuyCount)/noBuyCount)*(1.0*noBuyCount/14);
		System.out.println("������ ("+testTuple+") ��������Ϊyes�ĸ���Ϊ  "+buyComputer);
		System.out.println("������ ("+testTuple+") ��������Ϊno�ĸ���Ϊ  "+noBuyComputer);
		if ( buyComputer>=noBuyComputer ){
			System.out.println("���,��������������Ϊyes,��buys_computer=yes��");
		}else{
			System.out.println("���,��������������Ϊno,��buys_computer=no��");
		}
	}
	
	public static void main(String[] args) throws IOException{
		BayesClassification bc = new BayesClassification();
		//����������X(����������) eg. X(age income student credit_rating buys_computer)=(youth medium yes fair "")��(youth low no fair "");
		String[] test = {""};
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		test = br.readLine().split(" ");
		Tuple testTuple = new Tuple(test);
		//System.out.println(testTuple);
		bc.getCount(testTuple);      //�õ���������������Ҫ�ĸ�������ֵ
		bc.bayesPrediction(testTuple);  //�������
	}
}
class Tuple{
	String age, income, student, credit_rating, buys_computer;
	public Tuple(String... param){
		age=param[0];
		income=param[1];
		student=param[2];
		credit_rating=param[3];
		buys_computer=param[4];
	}
	public String toString(){
		return age+"   "+income+"   "+student+"   "+credit_rating+"   "+buys_computer;
	}
}
