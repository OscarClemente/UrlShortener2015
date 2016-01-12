package urlshortener2015.goldenbrown.hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import rita.wordnet.RiWordnet;

@Controller
public class GreetingController {
	
	RiWordnet wordnet = new RiWordnet();

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		if (message.getName()!=null && !message.getName().equals("")) {
			String poss = wordnet.getBestPos(message.getName());
			if (poss==null) {
				poss = "n";
			}
			String[] synonyms = null;
			try {
				synonyms = wordnet.getStartsWith(message.getName(), poss);
			}
			catch(Exception e) { }
			if (synonyms != null && synonyms.length > 0) {
				String synonym = synonyms[0];
				if (synonym.equals(message.getName()) && synonyms.length > 1) {
					synonym = synonyms[1];
				}
				else {
					return new Greeting("No hay sugerencias");
				}
				return new Greeting("Nuestra sugerencia es: " + synonym);
			}
			else {
				return new Greeting("No hay sugerencias");
			}
		}
		else {
			return new Greeting("");
		}
    }

}