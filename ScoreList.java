import java.util.Arrays;
/**
 * Write a description of class ScoreList here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScoreList  
{
    Score [] score;

	public Score[] getScore() {
		return score;
	}

	public void setScore(Score[] score) {
		this.score = score;
	}

	public ScoreList(Score[] score) {
		super();
		this.score = score;
	}

	public ScoreList() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ScoreList [score=" + Arrays.toString(score) + "]";
	}
}
