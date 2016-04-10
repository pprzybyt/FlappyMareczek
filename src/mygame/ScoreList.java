

package mygame;

import java.util.Collections;

public class ScoreList implements Comparable<ScoreList>
        {

    private int score;
    
    public ScoreList(int s, String n)
    {  
        score=s;
        name=n;
    }
    
    private final String name;
    
    public int getScore()
    {
        return this.score;
    }
    
    public String getName()
    {
        return this.name;
    }

   @Override
    public int compareTo(ScoreList s) 
    {
        int compareScore = s.getScore();
        return compareScore - this.score;
    }
    
    
}
