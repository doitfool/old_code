package questionAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.MentionsAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class CoreNLPProcess {
	private StanfordCoreNLP pipeline = null;
	public CoreNLPProcess(){
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,entitymentions");
        pipeline = new StanfordCoreNLP(props);
        // create an empty Annotation just with the given text
	}

	public ArrayList<String> getEntityMentions(String text){
		Annotation document = new Annotation(text);
        pipeline.annotate(document);
		ArrayList<String> entitymentions = new ArrayList<>();
		List<CoreMap> mentions = document.get(MentionsAnnotation.class);
		System.out.println("-------entitymentions-------");
        for ( CoreMap mention : mentions ){
        	System.out.println(mention.toString());
        	entitymentions.add(mention.toString());
        }
        System.out.println("-------entitymentions-------");
        return entitymentions;
	}
	
	public ArrayList<WordInfo> getLemmas(String text){
		Annotation document = new Annotation(text);
        pipeline.annotate(document);
		ArrayList<WordInfo> wiLemmas = new ArrayList<>();
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                // this is the lemma of the token
                String lemma = token.get(LemmaAnnotation.class);
                if ( pos.startsWith("NN") || pos.startsWith("VB") || pos.startsWith("JJ") ){
                	WordInfo wi = new WordInfo(word,pos,lemma,ne);
                	wiLemmas.add(wi);
                }
                	
//                	System.out.println(word+"\t"+pos+"\t"+lemma+"\t"+ne);
            }
		}
//		
////		ArrayList<String> entitymentions = getEntityMentions();
//		ArrayList<String> finalLemmas = new ArrayList<>();
//		for ( String lemma : lemmas ){
//			WordInfo wi = new WordInfo(lemma);
//			String word = wi.getWord();
//			String pos = wi.getLemma();
//			String lem = wi.getLemma();
//			String ne = wi.getNE();
//			boolean flag = true;
//        	for ( String entitymention : entitymentions ){
//        		if ( entitymention.contains(word) ){
//        			entitymention+="\t"+pos+"\t"+lem+"\t"+ne;
//        			flag = false;
//        			break;
//        		}
//        	}
//        	if ( flag ){
//        		finalLemmas.add(lemma);
//        	}
//        }
//        for ( String entitymention : entitymentions ){
//        	finalLemmas.add(entitymention);
//        }
//     
        ArrayList<String> swList = new StopWord().getSWList();
        ArrayList<WordInfo> finalWiLemmas = new ArrayList<>();
        
        //È¥³ýÍ£ÓÃ´Ê
    	for ( WordInfo wiLemma : wiLemmas ){
    		boolean flag = true;
    		for ( String stopword : swList ){
    			if ( wiLemma.getWord().equals(stopword) ){
    				flag = false;
    				break;
    			}
    		}
        	if ( flag ){
        		finalWiLemmas.add(wiLemma);
    		}
    		
        }
		return finalWiLemmas;
	}
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String text = null;
		CoreNLPProcess cp = null;
		while ( (text=sc.nextLine())!=null ){
			cp = new CoreNLPProcess();
			ArrayList<WordInfo> wiLemmas = cp.getLemmas(text);
			for ( WordInfo wiLemma : wiLemmas ){
				System.out.println(wiLemma.getWord()+"\t"+wiLemma.getPOS()+"\t"+wiLemma.getLemma()+"\t"+wiLemma.getNE());
			}
		}
	}
}
