package questionAnalysis;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by AC on 2015/7/27.
 */
public class WordNet {
    private IDictionary dict = null;
    private final int maxSynNum = 100;
    public WordNet(){
        String wnhome = System.getenv("WNHOME");
//        System.out.println(wnhome);
        String path = wnhome + File.separator+ "dict-stanford40k";
        URL url;
        try {
            url = new URL("file", null, path);
            dict=new Dictionary(url);
            dict.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public IDictionary getDict(){
        return dict;
    }
    //Õ¨“Â¥ æÿ’Û
    public String[][] wordExtend(WordInfo wiLemma){
    	String[][] weList = new String[maxSynNum][maxSynNum];
    	String lemma = wiLemma.getLemma();
    	IIndexWord idxWord = null;
    	if ( wiLemma.getPOS().startsWith("NN") ){
    		idxWord = dict.getIndexWord(lemma, POS.NOUN);
    	}else if ( wiLemma.getPOS().startsWith("VB") ){
    		idxWord = dict.getIndexWord(lemma, POS.VERB);
    	}else if ( wiLemma.getPOS().startsWith("JJ") ){
    		idxWord = dict.getIndexWord(lemma, POS.ADJECTIVE);
    	}
        try{
        	int len = idxWord.getWordIDs().size();
//        	System.out.println("len:"+len);
            //get all lemma
        	for(int row=0; row<len; row++){
        		IWordID wordID = idxWord.getWordIDs().get(row); 
        		IWord iword = dict.getWord(wordID);
        		ISynset synset = iword.getSynset();
        		int col = 0;
        		for( IWord w : synset.getWords() ){
        			
        			weList[row][col] = w.getLemma();
        			col++;
        		}
              }
        }catch(NullPointerException e){
        	System.out.println(lemma+" has no "+wiLemma.getPOS()+" synonyms.");
        }
//        for ( int m=0; m<30; m++ ){
//        	for ( int n=0; n<30; n++ ){
//        		System.out.print(weList[m][n]+"   ");
//        	}
//        	System.out.println();
//        }
    	return weList;
    }
    
    public static void main(String[] args){
    	WordInfo wi = new WordInfo("kill", "VB", "kill", "0");
    	WordNet wn = new WordNet();
    	IDictionary dict = wn.getDict();
    	String[][] weList = wn.wordExtend(wi);
    	for ( int i=0; i<weList.length; i++ ){
    		for ( int j=0; j<weList.length; j++ ){
    			System.out.print(weList[i][j]+"\t");    			
    		}
    		System.out.println();
    	}
    }
}
