package similarQuestions;
import java.util.Scanner;
import java.util.TreeMap;

import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.JiangAndConrath;
import edu.sussex.nlp.jws.Lin;

public class WordSimilarityOffline {
	private final String dir = "C:/Program Files (x86)/WordNet";
	private JWS jws = null;
	public static final int TYPE_ADAPTED_LESK = 0;
	public static final int TYPE_ADAPTED_LESK_TANIMOTO = 1;
	public static final int TYPE_ADAPTED_LESK_TANIMOTO_NO_HYPONYMS = 2;
	public static final int TYPE_HIRST_AND_ST_ONGE = 3;
	public static final int TYPE_JIANG_AND_CONRATH = 4;
	public static final int TYPE_LEACOCK_AND_CHODOROW = 5;
	public static final int TYPE_LIN = 6;
	public static final int TYPE_PATH = 7;
	public static final int TYPE_RESNIK = 8;
	public static final int TYPE_WU_AND_PALMER = 9;
	public WordSimilarityOffline(){
		jws = new JWS(dir, "2.1");
	}
	public JWS getWS(){
		return jws;
	}
	
	public double WordSimilarity(String word1, String word2, String pos, int method, boolean selectBest){
		double wsvalue = 0.0;
		//method = 0,1,2时报错，没有该方法
		switch(method) {
//			case TYPE_ADAPTED_LESK: if(selectBest) { wsvalue = jws.getAdaptedLesk().max(word1, word2, pos); } else { wsvalue = jws.getAdaptedLesk().lesk(word1, 1, word2, 1, pos); } break;
//			case TYPE_ADAPTED_LESK_TANIMOTO: if(selectBest) { wsvalue = jws.getAdaptedLeskTanimoto().max(word1, word2, pos); } else { wsvalue = jws.getAdaptedLeskTanimoto().lesk(word1, 1, word2, 1, pos); } break;
//			case TYPE_ADAPTED_LESK_TANIMOTO_NO_HYPONYMS: if(selectBest) { wsvalue = jws.getAdaptedLeskTanimotoNoHyponyms().max(word1, word2, pos); } else { wsvalue = jws.getAdaptedLeskTanimotoNoHyponyms().lesk(word1, 1, word2, 1, pos); } break;
			case TYPE_HIRST_AND_ST_ONGE: if(selectBest) { wsvalue = jws.getHirstAndStOnge().max(word1, word2, pos); } else { wsvalue = jws.getHirstAndStOnge().hso(word1, 1, word2, 1, pos); } break;
			case TYPE_JIANG_AND_CONRATH: if(selectBest) { wsvalue = jws.getJiangAndConrath().max(word1, word2, pos); } else { wsvalue = jws.getJiangAndConrath().jcn(word1, 1, word2, 1, pos); } break;
			case TYPE_LEACOCK_AND_CHODOROW: if(selectBest) { wsvalue = jws.getLeacockAndChodorow().max(word1, word2, pos); } else { wsvalue = jws.getLeacockAndChodorow().lch(word1, 1, word2, 1, pos); } break;
			case TYPE_LIN: if(selectBest) { wsvalue = jws.getLin().max(word1, word2, pos); } else { wsvalue = jws.getLin().lin(word1, 1, word2, 1, pos); } break;
			case TYPE_PATH: if(selectBest) { wsvalue = jws.getPath().max(word1, word2, pos); } else { wsvalue = jws.getPath().path(word1, 1, word2, 1, pos); } break;
			case TYPE_RESNIK: if(selectBest) { wsvalue = jws.getResnik().max(word1, word2, pos); } else { wsvalue = jws.getResnik().res(word1, 1, word2, 1, pos); } break;
			case TYPE_WU_AND_PALMER: if(selectBest) { wsvalue = jws.getWuAndPalmer().max(word1, word2, pos); } else { wsvalue = jws.getWuAndPalmer().wup(word1, 1, word2, 1, pos); } break;
		}
		return wsvalue;
	}
	
	
	public static void main(String[] args){
		WordSimilarityOffline wsoff = new WordSimilarityOffline();
		double result = 0.0;
		Scanner sc = new Scanner(System.in);
		while ( true ){
			String word1 = sc.next();
			String word2 = sc.next();
			int i=3;
			System.out.println(word1+"\t"+word2);
			while( i<10 ){
				result = wsoff.WordSimilarity(word1, word2, "n", i++, true);
				System.out.println(result);
			}
		}
	}
}
