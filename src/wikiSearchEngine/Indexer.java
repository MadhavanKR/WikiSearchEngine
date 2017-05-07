package wikiSearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public void Indexer(String fileName,int start,int end){
		this.start = start;
		this.end = end;
		this.rawFileName = fileName;
		pageCount=5000*start;
	}
	
	public void stemmer(){
		
	}
	
	public void process(String line,char type){
		String[] words;
		words = line.split("\\P{Alnum}");
		for(String curWord:words){
			Stemmer stem = new Stemmer();
			stem.add(curWord.toCharArray(), curWord.toCharArray().length);
			stem.stem();
			curWord=stem.toString();
		}
	}
	
	public void run(){
		//HashMap<String,HashMap<String,>>
		for(int i=start;i<end;i++)
		{
			String curRawFile = rawFileName+i;
			String line;
			try
			{
				BufferedReader curReader = new BufferedReader(new FileReader(new File(curRawFile)));
				while((line=curReader.readLine())!=null){
					if(line.compareTo("~~~~~DELIMITER~~~~~")==0)
					{
						pageCount++;
					}
					else
					{
						
					}
				}
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
