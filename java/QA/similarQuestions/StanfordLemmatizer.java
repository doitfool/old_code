package similarQuestions;

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
import questionAnalysis.WordInfo;

public class StanfordLemmatizer {
	private StanfordCoreNLP pipeline = null;
	public StanfordLemmatizer(){
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
           
            	WordInfo wi = new WordInfo(word,pos,lemma,ne);
            	wiLemmas.add(wi);
//                	System.out.println(word+"\t"+pos+"\t"+lemma+"\t"+ne);
            }
		}
		return wiLemmas;
	}
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String text = null;
		StanfordLemmatizer sl = null;
		while ( (text=sc.nextLine())!=null ){
			sl = new StanfordLemmatizer();
			ArrayList<WordInfo> wiLemmas = sl.getLemmas(text);
			for ( WordInfo wiLemma : wiLemmas ){
				System.out.println(wiLemma.getWord()+"\t"+wiLemma.getPOS()+"\t"+wiLemma.getLemma()+"\t"+wiLemma.getNE());
			}
		}
	}
}
