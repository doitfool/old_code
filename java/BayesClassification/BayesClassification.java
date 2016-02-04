import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * 
 * @author AC
 * 程序模拟 使用朴素贝叶斯分类预测类标号(P229)
 *
 */
public class BayesClassification {
	int buyCount=0,     //训练数据中类标号属性为yes的元组个数
		noBuyCount=0;   //训练数据中类标号属性为no的元组个数
	int ageCount=0,     //训练数据中与新数据的age属性值相同的元组个数 
		incomeCount=0,  //训练数据中与新数据的income属性值相同的元组个数 
		studentCount=0, //训练数据中与新数据的student属性值相同的元组个数 
		crCount=0;		//训练数据中与新数据的credit_rating属性值相同的元组个数 
	int ageBuyCount=0,  //与新数据的age属性值相同的前提下，训练数据中的类标号属性为yes的个数
		incomeBuyCount=0,  //与新数据的income属性值相同的前提下，训练数据中的类标号属性为yes的个数
		studentBuyCount=0,  //与新数据的student属性值相同的前提下，训练数据中的类标号属性为yes的个数
		crBuyCount=0;  //与新数据的credit_rating属性值相同的前提下，训练数据中的类标号属性为yes的个数
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
		   //存储训练数据到内存
		   for (int i=0; i<14; i++ ){
				Tuple tuple = new Tuple(data[i]);
				tuples.add(tuple);
		   }
		   /* 测试数据存储成功
			Iterator<Tuple> iterator = tuples.iterator();
			while ( iterator.hasNext() ){
				System.out.println(iterator.next());
			}
		   */
	}
	
	public void getCount(Tuple testTuple){
		//遍历tuples得到各个计数值
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
		/* 输出计数值
		System.out.println("ageCount="+ageCount+"  ageBuyCount="+ageBuyCount);
		System.out.println("incomeCount="+incomeCount+"  incomeBuyCount="+incomeBuyCount);
		System.out.println("studentCount="+studentCount+"  studentBuyCount="+studentBuyCount);
		System.out.println("crCount="+crCount+"  crBuyCount="+crBuyCount);
		*/
	}
	public void bayesPrediction(Tuple testTuple){
		//计算类标号属性为yes和no的概率
		double buyComputer=0.0, noBuyComputer=0.0;
		buyComputer=(1.0*ageBuyCount/buyCount)*(1.0*incomeBuyCount/buyCount)*(1.0*studentBuyCount/buyCount)*(1.0*crBuyCount/buyCount)*(1.0*buyCount/14);
		noBuyComputer=(1.0*(ageCount-ageBuyCount)/noBuyCount)*(1.0*(incomeCount-incomeBuyCount)/noBuyCount)*
				(1.0*(studentCount-studentBuyCount)/noBuyCount)*(1.0*(crCount-crBuyCount)/noBuyCount)*(1.0*noBuyCount/14);
		System.out.println("新数据 ("+testTuple+") 类标号属性为yes的概率为  "+buyComputer);
		System.out.println("新数据 ("+testTuple+") 类标号属性为no的概率为  "+noBuyComputer);
		if ( buyComputer>=noBuyComputer ){
			System.out.println("因此,新数据类标号属性为yes,即buys_computer=yes。");
		}else{
			System.out.println("因此,新数据类标号属性为no,即buys_computer=no。");
		}
	}
	
	public static void main(String[] args) throws IOException{
		BayesClassification bc = new BayesClassification();
		//输入新数据X(待分类数据) eg. X(age income student credit_rating buys_computer)=(youth medium yes fair "")或(youth low no fair "");
		String[] test = {""};
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		test = br.readLine().split(" ");
		Tuple testTuple = new Tuple(test);
		//System.out.println(testTuple);
		bc.getCount(testTuple);      //得到分类新数据所需要的各个计数值
		bc.bayesPrediction(testTuple);  //计算概率
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
