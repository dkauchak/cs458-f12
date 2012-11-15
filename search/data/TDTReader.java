package search.data;

import java.io.*;

import search.data.BasicDocumentReader.TextIDPair;

public class TDTReader extends BasicDocumentReader{	
	public TDTReader(String documentFile){
		super(documentFile);
	}
	
	/**
	 * Read through the file and extract the text between the next <DOC> and </DOC> tags
	 * @return The text between the next DOC tags or null if no more documents exist
	 * @throws IOException
	 */
	protected TextIDPair readNextDocText(BufferedReader docIn) throws IOException{
		String line = docIn.readLine();
		
		// find the beginning of the document
		while( line != null &&
			   !line.equals("<DOC>") ){
			line = docIn.readLine();
		}
		
		if( line == null ){
			return null;
		}else{
			StringBuffer buffer = new StringBuffer();
		
			line = docIn.readLine();
			
			// grab all the text between <DOC> and </DOC>
			while( line != null &&
				   !line.equals("<\\DOC>") ){
				buffer.append(" " + line);
				line = docIn.readLine();
			}

			docID++;
			return new TextIDPair(buffer.toString(), docID);
		}
	}	
}