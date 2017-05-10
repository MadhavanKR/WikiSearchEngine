package wikiSearchEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Indexer extends Thread {
	String rawFileName;
	int start,end,pageCount;
	Pattern refpat=Pattern.compile("== ?references ?==(.*?)==", Pattern.DOTALL);
	Matcher refmatcher=null;
	Pattern catpat=Pattern.compile("[\\[][\\[]category:(.*?)[\\]][\\]]", Pattern.DOTALL);
	Matcher catmatcher=null;
	Pattern linkpat=Pattern.compile("== ?external links ?==(.*?)\n\n", Pattern.DOTALL);
	Matcher linkmatcher=null;
	Pattern http=Pattern.compile("\\[http(.*?)\\]",Pattern.DOTALL);
	Matcher httpmatcher=null;
	Pattern infopat=Pattern.compile("[{][{]infobox(.*?)\n\n",Pattern.DOTALL);
	Matcher infomatcher=null;
	Pattern garbage=Pattern.compile("[{][{](.*?)[}][}]",Pattern.DOTALL);
	Matcher garbagematcher=null;
	public static String[] stopWordArray = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the","ref","reflist","www"};
	public static HashMap<String,Integer> stopWords;
	public HashMap<String,HashMap<Integer,HashMap<String,Integer>>> index;
	
	public final static Logger log = Logger.getLogger(Logger.class);

	
	public Indexer(String fileName,int start,int end){
		
		log.info("Initializing the indexer variables");
		
		this.start = start;
		this.end = end;
		this.rawFileName = fileName;
		pageCount=1000*start;
		
		stopWords = new HashMap<String,Integer>();
		for(String st:stopWordArray)
			stopWords.put(st,1);
		System.out.println(stopWords);
		
		index = new HashMap<String,HashMap<Integer,HashMap<String,Integer>>>();
		
		log.info("completed initializing!");
	}
	
	
	public String[] stopWordsRemover(String line){
		String[] wordList = line.split("\\P{Alnum}");
		StringBuilder words = new StringBuilder("");
		for(String curWord:wordList){
			if(!stopWords.containsKey(curWord))
				words.append(curWord+"-");
		}
		return words.toString().split("-");
	}
	
	public void indexTheWord(String word,int pageCount,String type){
		
		if(index.containsKey(word)){
			HashMap<Integer,HashMap<String,Integer>> pageToTypeMap = index.get(word);
			if(pageToTypeMap!=null && pageToTypeMap.containsKey(pageCount)){
				HashMap<String,Integer> typeToFreqMap = pageToTypeMap.get(pageCount);
				if(typeToFreqMap!=null && typeToFreqMap.containsKey(type))
				{
					int freq = typeToFreqMap.get(type);
					freq++;
					typeToFreqMap.put(type, freq);
				}
				else
				{
					typeToFreqMap.put(type, 1);
				}
			}
			else{
				HashMap<String,Integer> mapTypeToFreq = new HashMap<String,Integer>();
				mapTypeToFreq.put(type, 1);
				pageToTypeMap.put(pageCount, mapTypeToFreq);
			}
		}
		else{
			HashMap<Integer,HashMap<String,Integer>> mapPageToType = new HashMap<Integer,HashMap<String,Integer>>();
			HashMap<String,Integer> mapTypeToFreq = new HashMap<String,Integer>();
			mapTypeToFreq.put(type, 1);
			mapPageToType.put(pageCount, mapTypeToFreq);
			index.put(word, mapPageToType);
		}	
	}
	
	public void process(String line,String type,int pageCount){
		String[] words;
		words = stopWordsRemover(line);
		for(String curWord:words){
			Stemmer stem = new Stemmer();
			stem.add(curWord.toCharArray(), curWord.toCharArray().length);
			stem.stem();
			curWord=stem.toString();
			indexTheWord(curWord,pageCount,type);
		}
	}
	
	public void run(){
		//HashMap<String,HashMap<String,>>
		
		log.info("starting the thread");
		
		for(int i=start;i<end;i++)
		{
			log.info("processing rawFile"+i);
			String curRawFile = rawFileName+i;
			String line;
			try
			{
				BufferedReader curReader = new BufferedReader(new FileReader(new File(curRawFile)));
				StringBuilder pageContentBuilder = new StringBuilder();
				String pageContent;
				log.info("starting the processing..");
				int lineNum=0;
				while((line=curReader.readLine())!=null)
				{
					//System.out.println("in here!!");
					//lineNum++;
					//if(lineNum%1000 == 0)
						//log.info("line number + "+lineNum);
					if(line.compareTo("~~~~~DELIMITER~~~~~")==0)
					{
						pageCount++;
						pageContent = pageContentBuilder.toString();
						refmatcher=refpat.matcher(pageContent);
						if(refmatcher.find()){
							String referenceStrings = refmatcher.group(0);
							int x=0;
							for(String referenceString:referenceStrings.split(" "))
							{
								if(x==0)
								{
									x++;
									continue;
								}
								x++;
								process(referenceString,"r",i*1000+pageCount);
								pageContent = refmatcher.replaceAll(" ");
							}
						}
						catmatcher=catpat.matcher(pageContent);
						if(catmatcher.find())
						{
							String[] catStrings = catmatcher.group(0).split(" ");
							int x=0;
							for(x=1;x<catStrings.length;x++)
							{
								process(catStrings[x],"c",i*1000+pageCount);
								pageContent = catmatcher.replaceAll(" ");
							}
						}
						linkmatcher=linkpat.matcher(pageContent);
						if(linkmatcher.find())
						{
							String links = linkmatcher.group(0);
							httpmatcher=http.matcher(links);
							while(httpmatcher.find())
							{
								String linkStrings = httpmatcher.group(0);
								int x=0;
								for(String linkString:linkStrings.split(" "))
								{
									if(x==0)
									{
										x++;
										continue;
									}
									x++;								
									process(linkString,"l",i*1000+pageCount);
									pageContent = linkmatcher.replaceAll(" ");
								}
							}
						}
						infomatcher = infopat.matcher(pageContent);
						if(infomatcher.find())
						{
							String infoStrings = infomatcher.group(0);
							int x=0;
							for(String infoString:infoStrings.split(" "))
							{
								if(x==0)
								{
									x++;
									continue;
								}
								x++;								
								process(infoString,"i",i*1000+pageCount);
								pageContent = infomatcher.replaceAll(" ");
							}
						}
						garbagematcher = garbage.matcher(pageContent);
						pageContent = garbagematcher.replaceAll(" ");
						process(pageContent,"b",i*1000+pageCount);
					}
					else
					{
						pageContentBuilder.append(line);
					}
				}
				log.info("completed processing rawFile"+i);
				log.info("calling the Writer");
				writeIndexFile(i);				
			}
			catch(IOException e)
			{
				log.info(e.getMessage());
			}
		}
	}


	private void writeIndexFile(int i) {
		try 
		{
			log.info("starting to write the index file"+i);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("indexFile"+i)));
			StringBuilder curLine = new StringBuilder();
			Set<String> words = index.keySet();
			Iterator<String> iter = words.iterator();
			while(iter.hasNext())
			{
				String curWord = iter.next();
				curLine.append(curWord+"-");
				HashMap<Integer,HashMap<String,Integer>> pageIdToType = index.get(curWord) ;
				Set<Integer> docIds = pageIdToType.keySet();
				Iterator<Integer> pageIter = docIds.iterator();
				while(pageIter.hasNext()){
					Integer curPage = pageIter.next();
					curLine.append(curPage+" ");
					HashMap<String,Integer> typeToFreq = pageIdToType.get(curPage); 
					Set<String> types = typeToFreq.keySet();
					Iterator<String> typeIterator = types.iterator();
					while(typeIterator.hasNext()){
						String curType = typeIterator.next();
						curLine.append(curType+typeToFreq.get(curType)+" ");
					}
					curLine.append("|");
				}
				curLine.append("\n");
			}
			writer.write(curLine.toString());
			writer.close();
			index.clear();
			log.info("completed writing the index file"+i);
		}
		catch (IOException e) 
		{
			log.info(e.getMessage());
		}
	}
	
}
