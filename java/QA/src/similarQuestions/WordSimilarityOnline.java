package similarQuestions;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordSimilarityOnline {
	public WordSimilarityOnline(){};
	public String getHTML(String urlToRead) {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
	   }
	
	public double getSimilarity(String w1, String w2, String pos1, String pos2){
		
		//just for noun and verb
		//if(pos1.startsWith("NN))
		//TODO
		 //String url = "http://maraca.d.umn.edu/cgi-bin/similarity/similarity.cgi?word1=keep%23v&senses1=all&word2=use%23v&senses2=all&measure=path&rootnode=yes";
		 String url = "http://maraca.d.umn.edu/cgi-bin/similarity/similarity.cgi?word1="
		 +w1+"%23"+pos1+"&senses1=all&word2="+w2+"%23"+pos2+"&senses2=all&measure=lin&rootnode=yes";
		// System.out.println(url);
	     String result = getHTML(url);
//	     System.out.println(result);
	     Pattern pattern = Pattern.compile("using lin is (\\d+\\.\\d+)");
	     Matcher matcher = pattern.matcher(result);
	     
	     while(matcher.find()){
	    	 result = matcher.group();
	    	System.out.println(matcher.group());
	    	 
	    	 pattern = Pattern.compile("(\\d+\\.\\d+)");
	    	 matcher = pattern.matcher(result);
	    	 
	    	 while(matcher.find()){
	    		 return Double.parseDouble(matcher.group());
	    	 }
	    	 
	     }
		
		return 0;
	}

	   public static void main(String args[])
	   {
		   WordSimilarityOnline ws = new WordSimilarityOnline();
		   System.out.println(ws.getSimilarity("goes", "go","v","v"));
		   
	     
	   }
}
