package wikiSearchEngine;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class eventHandler extends DefaultHandler {
	
	public static int pageCount = 0;
	public static String fileName = new String("rawFile");
	public static int fileNum = 0;
	public static BufferedWriter currentWriter=null;
	public BufferedWriter titleWriter;
	public static String[] stopWordArray = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the","ref","reflist","www"};
	public static HashMap stopWords;
	StringBuilder pageContent;
	
	public eventHandler(){
		try {
			titleWriter = new BufferedWriter(new FileWriter(new File("title.txt")));
		} 
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		stopWords = new HashMap();
		pageContent = new StringBuilder("");
		for(String st:stopWordArray)
			stopWords.put(st, true);
		System.out.println(stopWords);
	}
	
	
	public void startElement(String nameSpaceURI,String localName,String qName,Attributes att){
		pageContent = new StringBuilder("");
	}
	
	public void endDocument(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("fileCount.dat")));
			String out = new String(fileNum+"\n"+pageCount);
			writer.write(out);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	public void endElement(String nameSpaceURI,String localName,String qName){
		if(qName.compareTo("title")==0)
		{
			try{
				titleWriter.write(pageContent.toString());
				titleWriter.append("\n");
			}
			catch(IOException e)
			{
				System.out.println(e.getMessage());
			}
		}
		else if(qName.compareTo("text")==0)
		{
			if(pageCount%5000 == 0){
				try {
					currentWriter = new BufferedWriter(new FileWriter(new File(fileName+fileNum)));
					currentWriter.append(pageContent);
					currentWriter.append("~~~~~DELIMITER~~~~~");
					} 
				catch (IOException e) {
				e.printStackTrace();
				}
				fileNum++;
			}
			try {
				currentWriter.append(pageContent);
				currentWriter.append("\n~~~~~DELIMITER~~~~~\n");
			} 
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
			//System.out.println(pageCount);
			pageCount++;
		}
	}
	
	public void characters(char[] ch,int start,int length){
		String temp = new String(ch,start,length);
			if(stopWords.containsKey(temp.trim().toLowerCase())){
				//System.out.println(temp);
				return;
			}
			pageContent.append(temp.toLowerCase());
			//else
				//System.out.println(temp);
	}
}
