import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


public class RestClient extends Actor {

      public void act() 
    {
        // Add your action code here.
    }    
    
  public void getQuiz(String key) {
    try {

        Client client = Client.create();

        WebResource webResource = client
           .resource("http://localhost:8080/location/ms/rest/getgame/"+key);

        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        if (response.getStatus() != 201) {
           throw new RuntimeException("Failed : HTTP error code : "
            + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Output from Server .... \n");
        System.out.println(output);

      } catch (Exception e) {

        e.printStackTrace();

      }

    }
    
    
    public void postData(String gameName, String playerName) {

    try {

        Client client = Client.create();

        WebResource webResource = client
           .resource("http://35.165.44.176:8080/location/ms/rest/postgame");

        String input = "{\"gameName\":\""   +gameName+"\",\"playerName\":\""+playerName+"\"}";
        //String input1 = "{\"gameName\":\"Game123\",\"playerName\":\"Neha\"}";
         System.out.println(input);
        ClientResponse response = webResource.type("application/json")
           .post(ClientResponse.class, input);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                 + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

     } catch (Exception e) {
        e.printStackTrace();
     }

}


public void postCurrentQuesData(String gameName, String questionName) {

    try {

        Client client = Client.create();

        WebResource webResource = client
           .resource("http://35.165.44.176:8080/location/ms/rest/postques");

        String input = "{\"gameID\":\""   +gameName+"\",\"ques\":\""+questionName+"\"}";
        //String input1 = "{\"gameName\":\"Game123\",\"playerName\":\"Neha\"}";
         System.out.println(input);
        ClientResponse response = webResource.type("application/json")
           .post(ClientResponse.class, input);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                 + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

     } catch (Exception e) {
        e.printStackTrace();
     }

}

public void postCurrentQuesScore(String gameID, String score, String time, String playerName, String quesId) {

    try {

        Client client = Client.create();

        WebResource webResource = client
           .resource("http://35.165.44.176:8080/location/ms/rest/postscore");

        String input = "{\"gameID\":\""+gameID+"\",\"score\":\""+score+"\",\"time\":\""+time+"\",\"playerName\":\""+playerName+"\",\"quesId\":\""+quesId+"\"}";
        System.out.println(input);
        ClientResponse response = webResource.type("application/json")
           .post(ClientResponse.class, input);
           
           //System.out.println("RC: postCurrentQuesScore posting score 1");

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                 + response.getStatus());
        }
        
        //System.out.println("RC: postCurrentQuesScore posting score 2");

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);
        //System.out.println("RC: postCurrentQuesScore posting score 3");

     } catch (Exception e) {
        e.printStackTrace();
     }

}

public String getCurrentQues(String gameID){
		
		Client client = Client.create();
		
		//System.out.println("RC: getCurrentQues: Values of current game: " +gameID);
		
		WebResource webResource = client
				.resource("http://35.165.44.176:8080/location/ms/rest/getques/"+gameID);
		
		ClientResponse response = webResource.type("application/json")
				.get(ClientResponse.class);

		if(response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		ObjectMapper mapper = new ObjectMapper();
		
		Questions questions = new Questions();
		
		try {
			questions = mapper.readValue(response.getEntity(String.class), Questions.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("RC: Current Question Game "+ questions.getGameID());
		//System.out.println("RC: Current Question "+ questions.getQues());
		
		return questions.getQues();
		
	}
    
	public Game[] getCurrentGame(String gameID){
		
		Client client = Client.create();
		
		WebResource webResource = client
				.resource("http://35.165.44.176:8080/location/ms/rest/getgame/"+gameID);
		
		ClientResponse response = webResource.type("application/json")
				.get(ClientResponse.class);

		if(response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		ObjectMapper mapper = new ObjectMapper();
		
		GamesList game = new GamesList();
		
		try {
			game = mapper.readValue(response.getEntity(String.class), GamesList.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("Current Game "+ game.getGame()[0]);
		System.out.println("Total number of players "+ game.getGame().length);
		
		return game.getGame();
		
	}
	
	public Score getCurrentQuestionScore(String quesId){
		
		Client client = Client.create();
		//System.out.println("RC: getCurrentQuestionScore: 1 "+quesId);
		WebResource webResource = client
				.resource("http://35.165.44.176:8080/location/ms/rest/getquestscore/"+quesId);
		//System.out.println("RC: getCurrentQuestionScore: 2 "+quesId);
		ClientResponse response = webResource.type("application/json")
				.get(ClientResponse.class);
				
				//System.out.println("RC: getCurrentQuestionScore: 3 "+quesId);

		if(response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		//System.out.println("RC: getCurrentQuestionScore: 4 "+quesId);
		ObjectMapper mapper = new ObjectMapper();
		
		Score scoreList = new Score();
		//System.out.println("RC: getCurrentQuestionScore: 5 "+quesId);
		try {
			scoreList = mapper.readValue(response.getEntity(String.class), Score.class);
			//System.out.println("RC: getCurrentQuestionScore: 6 "+quesId);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("7 getCurrentQuestionScore: Current Game "+ scoreList.getScore());
		//System.out.println("8 RC: getCurrentQuestionScore: Total number of players "+ scoreList.getScore());
		//scoreList.getGame()[0];
		return scoreList;
		
	}

    
}

