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
	StringBuilder pageContent;
	
	public eventHandler(){
		try {
			titleWriter = new BufferedWriter(new FileWriter(new File("title.txt")));
		} 
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		
		pageContent = new StringBuilder("");
		
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
		pageContent.append(temp.toLowerCase());
	}
}
