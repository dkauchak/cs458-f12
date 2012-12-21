package search;

import java.util.ArrayList;
import java.util.Hashtable;

import search.data.WebDocumentReader;

public class Processor {

  WebDocumentReader reader, reader2;
	ImprovedTokenizer tokenizer;
	TokenProcessor processor;
	Index index;
	Hashtable<Integer, WebResultData> id2web;

	public Processor() {

		String web_data_file = "/Users/billyLee/Desktop/web_data.txt";
		
		tokenizer = new ImprovedTokenizer();
		processor = new TokenProcessor();				
		processor.setLowercase(true);

		reader = new WebDocumentReader(web_data_file);
		reader.setTokenizer(tokenizer);
		reader.setTokenProcessor(processor);

		reader2 = new WebDocumentReader(web_data_file);
		reader2.setTokenizer(tokenizer);
		reader2.setTokenProcessor(processor);

		index = new Index(reader, Index.TERM_MODIFIER.l, Index.DOC_MODIFIER.t, Index.LENGTH_MODIFIER.n );
		id2web = new Hashtable<Integer, WebResultData>();
		while( reader2.hasNext() ){
			WebDocument next = (WebDocument)reader2.next();
			id2web.put(next.getDocID(), new WebResultData(next.getTitle(), next.getUrl(), next.getText()));
		}
	}

	public ArrayList<WebResultData> processResults(String query) {

		ArrayList<WebResultData> searchResults = new ArrayList<WebResultData>();
		VectorResult rankedResult = index.rankedQuery(new Query(processor.process(tokenizer.tokenize(query))));
		int[] IDs = rankedResult.getIDs();
		double[] scores = rankedResult.getScores();

		for(int i = 0; i < scores.length; i++) {
			searchResults.add(id2web.get(IDs[i]));	
		}
		return searchResults;
	}
}
