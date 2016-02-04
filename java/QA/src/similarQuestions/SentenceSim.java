package similarQuestions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import questionAnalysis.WordInfo;
import Jama.Matrix;
import Jama.SingularValueDecomposition;


public class SentenceSim {
	protected LexicalizedParser lp;
	public SentenceSim(){
		lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	}
	public Matrix setSentenceMatrix(String str, int n){
	    TokenizerFactory<CoreLabel> tokenizerFactory =
	        PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    Tokenizer<CoreLabel> tok =
	        tokenizerFactory.getTokenizer(new StringReader(str));
	    List<CoreLabel> rawWords2 = tok.tokenize();
	    Tree parse = lp.apply(rawWords2);
	    
	    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	    
	    //int n = tdl.size();
		double[][] ma = new double[n][n];
		
		for(int i=0;i<tdl.size();i++){
			//gov->dep
			 TypedDependency td = tdl.get(i);
			 TreeGraphNode gov = new TreeGraphNode(td.gov());
			 TreeGraphNode dep = new TreeGraphNode(td.dep());
			 String gsn = td.reln().getShortName();
			 
			 if(gov.index()!=0){
				 // -1 eliminer le root
				 if(gsn.equals("det")||gsn.equals("cc")){
					 ma[gov.label().index()-1][dep.label().index()-1] = 0.1;
				 }else if(gsn.equals("aux")||gsn.equals("auxpass")||gsn.equals("prep")||gsn.equals("pcomp")||gsn.equals("pobj")){
					 ma[gov.label().index()-1][dep.label().index()-1] = 0.3;
				 }else if(gsn.equals("nsubj")||gsn.equals("nsubjpass")){
					 ma[gov.label().index()-1][dep.label().index()-1] = 2;			 
				 }else if(gsn.equals("dobj")||gsn.equals("conj")){
					 ma[gov.label().index()-1][dep.label().index()-1] = 2;				 
				 }else {
					 ma[gov.label().index()-1][dep.label().index()-1] = 1;				 
				 }
			 }
		}
		return new Matrix(ma);
	}
	
	public Matrix sentenceSimMatrix(String sen1, String sen2){
		StanfordLemmatizer sl = new StanfordLemmatizer();
		ArrayList<WordInfo> lemma1 = sl.getLemmas(sen1);
		ArrayList<WordInfo> lemma2 = sl.getLemmas(sen2);
		double[][] simmat = new double[lemma1.size()][lemma2.size()];
		WordSimilarityOffline wsoff = new WordSimilarityOffline();
    	for(int i=0;i<lemma1.size();i++){
    		for(int j=0;j<lemma2.size();j++){
    			WordInfo wi1 = lemma1.get(i);
    			WordInfo wi2 = lemma2.get(j);
    			if(wi1.getPOS().startsWith(".") && wi2.getPOS().startsWith(".")){}
    			else if(wi1.getLemma().equals(wi2.getLemma())){
    				simmat[i][j] = 1;
    			}else if(wi1.getPOS().startsWith("NN") && wi2.getPOS().startsWith("NN")){  //权重设置
    				simmat[i][j] = wsoff.WordSimilarity(wi1.getLemma(), wi2.getLemma(), "n", 9, true);
    			}else if(wi1.getPOS().startsWith("VB") && wi2.getPOS().startsWith("VB")){  
    				simmat[i][j] = wsoff.WordSimilarity(wi1.getLemma(), wi2.getLemma(), "v", 9, true);
    			} 
    			else{
    				simmat[i][j] = 0;
    			}
			}
    		
    	}
    	return new Matrix(simmat);
	}
	
	public double twoSS(String sen1, String sen2){
		StanfordLemmatizer sl = new StanfordLemmatizer();
		ArrayList<WordInfo> lemma1 = sl.getLemmas(sen1);
		ArrayList<WordInfo> lemma2 = sl.getLemmas(sen2);
		//http://stackoverflow.com/questions/2087198/java-jama-matrix-problem
		if ( lemma1.size() < lemma2.size() ){
			String temp = sen1;
			sen1 = sen2;
			sen2 = temp;
		}
//		System.out.println("similarity : "+ sen1+" & " + sen2);
		
		Matrix simMatRes = sentenceSimMatrix(sen1, sen2);
		int m = simMatRes.getRowDimension();
		int n = simMatRes.getColumnDimension();
		
		Matrix matrix1 = setSentenceMatrix(sen1,m);
		Matrix matrix2 = setSentenceMatrix(sen2,n);
		//A*X*t(B) + t(A)*X*B
		simMatRes = matrix1.times(simMatRes).times(matrix2.transpose()).plus(matrix1.transpose().times(simMatRes).times(matrix2));
		
//		System.out.println("calcul S=A*X*t(B) + t(A)*X*B, resultat norm茅 S");
//		simMatRes.print(m, 3);
		
		//normaliser
		//double normf = simMatRes.normF(); 
		//System.out.println(normf);
		//simMatRes.arrayRightDivideEquals(new Matrix(m,n,normf)) ;
	
		SingularValueDecomposition svd = new SingularValueDecomposition(simMatRes);
		//获得奇异值矩阵
		Matrix resultMat = svd.getS();
		
//		System.out.println("Matrice diagonale SVD");
//		resultMat.print(resultMat.getColumnDimension(), 4);
		
		//信息去噪
//		double produit = resultMat.get(0, 0);
//		int i=1;
//		while(i<n&&resultMat.get(i-1, i-1)/resultMat.get(i, i)<4){
//			produit *= resultMat.get(i,i);
//			i++;
//		}
		double sum = 0;
		for(int i=0;i<resultMat.getRowDimension();i++){
		//TODO	
			sum += resultMat.get(i,i);
//			System.out.print(resultMat.get(i,i));
//			System.out.print(" ");
			/*
			if(resultMat.get(i,i)>0.1)
				produit *= resultMat.get(i,i);*/
		}
//		System.out.println();
//		
//		System.out.println("norm2 : "+svd.norm2());
//		System.out.println("sum : "+sum);
//		System.out.println("produit : "+produit);
//		//System.out.println("sim value : "+sum*25/n);		
//		//System.out.println("produit : "+produit);
//		//System.out.println("sum/(m+n-2) valeur : "+sum/(m+n-2));
//		System.out.printf("%s & %s sentence similarity: %f\n", sen1, sen2, sum);
		return sum;
	}
	
	public double sentenceSimilarityValue(String sen1, String sen2){
		double p1 = twoSS(sen1, sen2);
		double p2 = twoSS(sen1, sen1);
		double p3 = twoSS(sen2, sen2);
		double value = p1*p1/p2/p3;
		return value;
	}
	
	public static void main(String[] args) throws Exception{
		//par default m>n
		
		SentenceSim ss = new SentenceSim();
		Scanner sc = new Scanner(System.in);
		String s1 = "";
		String s2 =	"";
		String s = "";
		while ( (s=sc.nextLine()) != null ){
			long startTime = System.currentTimeMillis();
			System.out.println("Start time:"+startTime);
			s1 = s.split("\t")[0];
			s2 = s.split("\t")[1];
			
			double score = ss.sentenceSimilarityValue(s1, s2);
			System.out.println("score="+score);
			long endTime = System.currentTimeMillis();
			System.out.println("end time:"+endTime);
			System.out.println("Time cost:"+Long.toString(endTime-startTime));
		}
		
		
//		String rpath = "E:/CQA/MSR Paraphrase Corpus/msr_paraphrase_test.txt";
//		String wpath = "C:/Users/AC/Desktop/score.txt";
//		File rfile = new File(rpath);
//		File wfile = new File(wpath);
//		BufferedReader br = new BufferedReader(new FileReader(rfile));
//		BufferedWriter bw = new BufferedWriter(new FileWriter(wfile, true));
//	
//		String line = null;
//	
//		int i=1;
//		while ( (line=br.readLine()) != null ){
//			String[] str = line.split("\t");
//			double result = ss.twoSS(str[3], str[4])/(ss.twoSS(str[3], str[3])*ss.twoSS(str[4], str[4]));
//			System.out.println(i+++"----------------------------------------"+result);
//			bw.write(result+"\t");
//		}
//		bw.write("\n");
//		br.close();
//		bw.close();
	}
}
