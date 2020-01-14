import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class WebpageWordCount {
	
	/**
	 * -main gets all text from webpage (including HTML tags)
	 * 1. research ways to get without tags
	 * 2. add try/catch
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		List<String> wordsWithHTML = new ArrayList<>();
		URL macbeth = new URL("http://shakespeare.mit.edu/macbeth/full.html");
        BufferedReader in = new BufferedReader(new InputStreamReader(macbeth.openStream()));

        String inputLine;
        
        while ((inputLine = in.readLine()) != null) {
            wordsWithHTML.add(inputLine);
        }
        
        
        
        in.close();
	}

}
