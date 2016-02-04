package questionAnalysis;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by AC on 2015/7/28.
 * 抽取句子中的关键词：包含1个原句分词序列和num个同义词扩展序列
 */
public class KeyWordExtracter {
	public String[] extract(String sentence, int num){
		String[] result = new String[num+1];
		for ( int i=0; i<=num; i++ ){
			result[i] = "";
		}
		ArrayList<WordInfo> wiLemmas = new CoreNLPProcess().getLemmas(sentence);
		ArrayList<ArrayList<String>> lemmaSyns = new ArrayList<>();
		//wordnet扩展
		WordNet wn = new WordNet();
		for ( WordInfo wiLemma : wiLemmas ){
//			System.out.println(wiLemma.getWord()+"\t"+wiLemma.getPOS()+"\t"+wiLemma.getLemma()+"\t"+wiLemma.getNE());
			String[][] weMatrix = wn.wordExtend(wiLemma);
			int all = num;
			ArrayList<String> lemmaSyn = new ArrayList<>();
			for ( int col=0; col<weMatrix[0].length && all!=0; col++ ){
				for ( int row=0; row<weMatrix.length && all!=0; row++ ){
					if ( weMatrix[row][col]!=null && !weMatrix[row][col].equals(wiLemma.getLemma()) ){
						lemmaSyn.add(weMatrix[row][col]);
						all--;
					}
				}
			}
			if ( lemmaSyn.size()==0 ){
				for ( int i=0; i<num; i++ )
					lemmaSyn.add(wiLemma.getLemma());
			}else if ( lemmaSyn.size()<num ){
				for ( int i=lemmaSyn.size(); i<num; i++ )
					lemmaSyn.add(lemmaSyn.get(0));
			}
			lemmaSyns.add(lemmaSyn);
			result[0] += wiLemma.getWord()+" ";
		}
		for ( ArrayList<String> lemmaSyn : lemmaSyns ){
			for ( int i=1; i<=num; i++ ){
				result[i] += (lemmaSyn.get(i-1)+" ");
			}
		}
		return result;
	}
	
    public static void main(String[] args){
		KeyWordExtracter kwe = new KeyWordExtracter();
		int num=2;
		String sentence=null;
		Scanner sc = new Scanner(System.in);
		while ( (sentence=sc.nextLine())!=null ){
			String[] result = kwe.extract(sentence,num);
//			System.out.println(sentence+result.length);
			for ( int i=0; i<=num; i++ )
				System.out.println(i+"\t"+result[i]);
		}
	}
}
