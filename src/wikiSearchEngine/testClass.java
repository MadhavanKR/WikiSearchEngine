package wikiSearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class testClass {

	public static void main(String[] args) throws IOException {
		BufferedReader curReader = new BufferedReader(new FileReader(new File("rawFile0")));
		System.out.println(curReader.readLine());
		System.out.println(curReader.readLine());
		System.out.println(curReader.readLine());
		System.out.println(curReader.readLine());
		System.out.println(curReader.readLine());
		System.out.println(curReader.readLine());
		System.out.println(curReader.readLine());
	}

}
