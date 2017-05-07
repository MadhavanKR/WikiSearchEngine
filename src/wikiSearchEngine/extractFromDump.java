package wikiSearchEngine;


import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class extractFromDump {

	public static void main(String[] args) {
		//first we have to create a XML reader instance
		XMLReader read=null;
		try {
			read = XMLReaderFactory.createXMLReader();
		} catch (SAXException e1) 
		{
			System.out.println(e1.getMessage());
		}
		
		/*now since sax is an event driven parser, we have to register a content event handler 
		  for the reader - which can be done using setContentHandler(object of content handler class)
		   method*/ 
		eventHandler handle=new eventHandler();
		
		read.setContentHandler(handle);
		
		/*now that we have set the content/event handler, we can start parsing the xml file 
		using parse method or XMLreader*/
		try 
		{
			read.parse("C:\\Users\\m0k00eu\\Desktop\\final year project\\wikipedia-search-engine\\wiki-search-small.xml");
		} 
		catch (IOException | SAXException e) 
		{
			System.out.println(e.getMessage());
		} 
		
	}

}
