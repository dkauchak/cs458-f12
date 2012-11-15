package search.data;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A reader for reading the text documents from the Cranfield test corpus.
 * 
 * 
 * @author dkauchak
 */
public class CranfieldReader extends BasicDocumentReader {
	
	public CranfieldReader(String filename){
		super(filename);
	}
	
	protected TextIDPair readNextDocText(BufferedReader docIn) throws IOException{
		String line = docIn.readLine();
		
		// find the beginning of the document
		while( line != null &&
			   !line.startsWith(".I") ){
			line = docIn.readLine();
		}
		
		if( line == null ){
			return null;
		}else{
			// figure out what the docID is
			String[] parts = line.split("\\s+");
			
			if( parts.length != 2 ){
				throw new RuntimeException("CranfieldReader::Problems finding docID: " + line);
			}
			
			int docID = Integer.parseInt(parts[1]);
			
			line = docIn.readLine();
			
			// find the beginning of the text
			while( line != null &&
				   !line.startsWith(".W") ){
				line = docIn.readLine();
			}
			
			if( line == null ){
				return null;
			}else{
				StringBuffer buffer = new StringBuffer();
		
				line = docIn.readLine();
			
				// grab all the text between <DOC> and </DOC>
				while( line != null &&
					   !line.equals("<END_DOC>") ){
					buffer.append(" " + line);
					line = docIn.readLine();
				}

				return new TextIDPair(buffer.toString(), docID);
			}
		}
	}
}
