/**
 * Write a description of class Questions here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Questions  
{
   String gameID;
	String ques;
	public String getGameID() {
		return gameID;
	}
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	public String getQues() {
		return ques;
	}
	public void setQues(String ques) {
		this.ques = ques;
	}
	public Questions() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Questions(String gameID, String ques) {
		super();
		this.gameID = gameID;
		this.ques = ques;
	}
	@Override
	public String toString() {
		return "Question [gameID=" + gameID + ", ques=" + ques + "]";
	}
}
