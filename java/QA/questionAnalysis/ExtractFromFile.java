package questionAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExtractFromFile {
	public static void main(String[] args) throws IOException{
		KeyWordExtracter kwe = new KeyWordExtracter();
		String qPath = "C:/Users/AC/Desktop/question.txt"; 
        File qFile = new File(qPath);
        BufferedReader br = new BufferedReader(new FileReader(qFile));
        String qString = null;
        String wsPath = "C:/Users/AC/Desktop/WordSegmentation.txt";
        String wePath = "C:/Users/AC/Desktop/WordExtension.txt";
        File wsFile = new File(wsPath);
        File weFile = new File(wePath);
        if ( !wsFile.exists() ){
        	wsFile.createNewFile();
        }
        if ( !weFile.exists() ){
        	weFile.createNewFile();
        }
        BufferedWriter bws = new BufferedWriter(new FileWriter(wsFile));
        BufferedWriter bwe = new BufferedWriter(new FileWriter(weFile));
        int num = 2;  //同义词扩展层数
        int j=0;
        while ( (qString=br.readLine()) != null ){
        	String[] lineStrings = qString.split("\t");
        	String qId = lineStrings[0];
        	String qBody = lineStrings[1];
        	String qDesc = lineStrings[2];
        	String qType = lineStrings[3]; 
        	String question = lineStrings[1]+lineStrings[2];
        	String[] result = kwe.extract(question, num);
        	if ( result[0].length()>10 ){
        		result = kwe.extract(qBody, num);
        	}
        	bws.write(qId+"\t");
        	bws.write(result[0]+"\n");
        	bwe.write(qId+"\t");
        	int i = 0;
        	for ( i=1; i<num; i++ )
        		bwe.write(result[i]+"\t");
        	bwe.write(result[i]+"\n");
//        	System.out.println(++j+qString);
//        	
        }
        br.close();
        bws.close();
        bwe.close();
        
	}
}
