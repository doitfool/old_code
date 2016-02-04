package questionAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StopWord {
	private ArrayList<String> swList = new ArrayList<>();
	private String swFile = "resource/stoplist.txt";
	public StopWord(){
		File file = new File(swFile);
        BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String temp = null;
        try {
			while ( (temp=br.readLine())!=null ){
				swList.add(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public StopWord(String filePath){
		File file = new File(filePath);
        BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String temp = null;
        try {
			while ( (temp=br.readLine())!=null ){
				swList.add(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getSWList(){
		return swList;
	}
}
