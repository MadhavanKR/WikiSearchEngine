package wikiSearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class invokeIndexer {

	public static void main(String[] args) {
		try 
		{
			BufferedReader read = new BufferedReader(new FileReader(new File("fileCount.dat")));
			int rawFileCount = Integer.parseInt(read.readLine());
			int pageCount = Integer.parseInt(read.readLine());
			System.out.println("rawFileCount = "+rawFileCount+"\n pageCount = "+pageCount);
			int fileCount = 0;
			//new Indexer("rawFile",1,2).start();
			while(fileCount<rawFileCount){
				if(fileCount + 10 > rawFileCount)
				{
					new Indexer("rawFile",fileCount,fileCount+rawFileCount).start();
					System.out.println("calling the thread for rawFile"+fileCount+" to "+fileCount+rawFileCount);
					fileCount+=rawFileCount;
				}
				else
				{
					new Indexer("rawFile",fileCount,fileCount+10).start();
					System.out.println("calling the thread for rawFile"+fileCount+" to "+fileCount+10);
					fileCount+=10;
				}
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
