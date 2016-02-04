package similarQuestions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.time.SUTime.Time;

public class Evaluation {
	public ArrayList<String> getQuestions(String filename){
		ArrayList<String> questions = new ArrayList<>();
		File file = new File(filename);	
		try {
			String question = "";
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ( (question=br.readLine()) != null ){
				if ( !question.equals("\n") ){
					questions.add(question);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return questions;
	}
	
	public static void main(String[] args) throws Exception{
		Evaluation eval = new Evaluation();
		long start = System.currentTimeMillis();
		String qInfoQuestionsFile = "C:\\Users\\AC\\Desktop\\qInfo questions.txt";
		String similarQuestionsFile = "C:\\Users\\AC\\Desktop\\similar questions.txt";
		String evaluationFile = "C:\\Users\\AC\\Desktop\\evaluationFile.txt";
		File file = new File(evaluationFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		ArrayList<String> qInfoQuestions = eval.getQuestions(qInfoQuestionsFile);
		ArrayList<String> similarQuestions = eval.getQuestions(similarQuestionsFile);
		SentenceSim ss = new SentenceSim();
		for ( int i=0; i < qInfoQuestions.size(); i++ ){
			String qInfoQuestion = qInfoQuestions.get(i);
			String similarQuestion = similarQuestions.get(i);
			if ( !qInfoQuestion.equals("\n") ){
				if ( !similarQuestion.equals("null") ){
					double score = ss.sentenceSimilarityValue(qInfoQuestion, similarQuestion);
					bw.write(qInfoQuestion+"\t"+similarQuestion+"\t"+score+"\n");
				}else{
					bw.write(qInfoQuestion+"\t"+similarQuestion+"\t"+"similar question is null.\n");
				}
			}else{
				bw.write("\n");
			}
		}
		bw.close();
		long end = System.currentTimeMillis();
		System.out.println("Time used:"+(end-start));
	}
}
