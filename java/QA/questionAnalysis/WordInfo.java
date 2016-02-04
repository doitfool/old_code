package questionAnalysis;

public class WordInfo {
    private String word;
    private String pos;
    private String lemma;
    private String ne;
    public WordInfo(String word, String pos, String lemma, String ne){
    	this.word = word;
    	this.pos = pos;
    	this.lemma = lemma;
    	this.ne = ne;
    }
    public void setWord(String word){
    	this.word = word;
    }
    public void setPOS(String pos){
    	this.pos = pos;
    }
    public void setLemma(String lemma){
    	this.lemma = lemma;
    }
    public void setNE(String ne){
    	this.ne = ne;
    }
    public String getWord(){
        return word;
    }
    public String getPOS(){
    	return pos;
    }
    public String getLemma(){
    	return lemma;
    }
    public String getNE(){
    	return ne;
    }
}
