package mygame;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.util.Collections;
import javax.swing.ImageIcon;


public final class MyGame implements ActionListener, KeyListener//, Comparable
{      
    public final int width=800, height=600;
    
    public static MyGame game;
    
    public Renderer renderer;
    
    public Rectangle bird;
    
    public ArrayList<Rectangle> columns;
    
    public Random rand; 
    
    public int tick, yMotion, score=0;
    
    public boolean gameOver, started = false, isWritten= false;
    
    public ArrayList<ScoreList> scoreList;
    
    public Image obraz1 = new ImageIcon("marek.png").getImage();
  
    public MyGame () 
    {
        JFrame frame = new JFrame();
        Timer timer = new Timer(20, this);
    
        renderer = new Renderer();
        rand = new Random();
        
        frame.add(renderer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
      bird = new Rectangle(width/2 - 10,height / 2 - 10,40,40);
      
      columns = new ArrayList<Rectangle>();
      addColumn(true);
      addColumn(true);
      addColumn(true);
      addColumn(true);
      
      scoreList = new ArrayList<ScoreList>(); 
      
      timer.start();
    }
  
    public void addName() 
{
    
    String name;
    name = JOptionPane.showInputDialog("Name:");
    scoreList.add(new ScoreList(score,name));
    Collections.sort(scoreList);
}
   
    
public void addColumn(boolean start)
{
    int space = 250;
    int WIDTH = 100;
    int HEIGHT = 50 + rand.nextInt(300);
    
    if(start)
    {
        columns.add(new Rectangle(width + WIDTH + (columns.size())*300, height-120-HEIGHT,WIDTH,HEIGHT));
        columns.add(new Rectangle(width + WIDTH + (columns.size()-1)*300, 0,WIDTH,height-HEIGHT-space));
    }
    else
    {
        columns.add(new Rectangle(columns.get(columns.size()-1).x+600, height-120-HEIGHT,WIDTH,HEIGHT));
        columns.add(new Rectangle(columns.get(columns.size()-2).x+600, 0,WIDTH,height-HEIGHT-space));
    }
}

public void jump()
{
    if(gameOver)
    {
      bird = new Rectangle(width/2 - 10,height / 2 - 10,40,40);
     
      columns.clear();
      yMotion=0;
      score=0;
      
      addColumn(true);
      addColumn(true);
      addColumn(true);
      addColumn(true);
      
      gameOver=false;
      isWritten = false;
    }
    
    if(!started)
    {
        started=true;      
    }
    else if(!gameOver)
    {
       if(yMotion>0)
           yMotion=0;
       
       yMotion-=7;
    }
}


     @Override
     
public void actionPerformed(ActionEvent e) 
{
    int speed = 8;
      // tick++;
       
    if(started)
    {
        for(int i=0; i<columns.size(); i++)
        {
            Rectangle column = columns.get(i);
            column.x -=speed;
        }
       
        if(yMotion<15)
            yMotion++;
       
        for(int i=0; i<columns.size(); i++)
        {
            Rectangle column = columns.get(i);
            if(column.x+column.width<0)
            {
                columns.remove(column);
                if(column.y==0)
                    addColumn(false);
            }
        }
       
            bird.y+=yMotion;
            
       
            for(Rectangle column : columns)
            {
                if(column.y==0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 4 && bird.x + bird.width / 2 < column.x + column.width / 2 + 5)
                    score++;
                
                if (column.intersects(bird))
                { 
                    gameOver=true;
                    
                    if(bird.x<= column.x)
                    bird.x = column.x-bird.width;
                    
                else
                {
                    if(column.y !=0)
                        bird.y= column.y-bird.height;
                    else if (bird.y<column.height)
                        bird.y=column.height;
                }
                
            }
            
            if(bird.y>height-120-bird.height || bird.y<0)
            {
                gameOver=true;
            }
            
             if(bird.y+ yMotion >= height - 120-bird.height)
            {
                bird.y = height-120-bird.height;
            }
            renderer.repaint();
        
    }
   }
}
   public void paintColumn(Graphics g, Rectangle column)
   {
       g.setColor(Color.green.darker());
       g.fillRect(column.x, column.y, column.width, column.height);
   }
    
   void repaint(Graphics g)
   {
       g.setColor(Color.cyan);
       g.fillRect(0, 0, width, height);
       
       g.setColor(Color.orange);
       g.fillRect(0,height-120,width,120);
       
       g.setColor(Color.green);
       g.fillRect(0,height-120,width,20);
         
       
       g.setColor(Color.red);
       g.fillRect(bird.x, bird.y, bird.width, bird.height);
       
      
       g.drawImage(obraz1, bird.x, bird.y, renderer);
       
 
       for(Rectangle column : columns)
       {
           paintColumn(g, column);
       }
       
       g.setColor(Color.white);
       g.setFont(new Font("Calibri",9,100));
       if(!started)
           g.drawString("Click to START",width/7,height/2-50);
            
       if(gameOver)
       {
           g.drawString("GAME OVER",width/5,height/2-200);
           g.setFont(new Font("Calibri",5,20));
           g.drawString("score: " + score,width/5,height/2-180);
           if(isWritten)
           {
                g.setFont(new Font("Calibri",9,30));
                int counter=0;
                g.drawString("HIGHSCORES: ", width/5, height/2 - 150);
                
                    for(ScoreList s : scoreList)
                    {
                        g.drawString(s.getName()+": "+s.getScore(),width/5,height/2-100+32*counter);
                        counter++;
                        if(counter==9)
                            break;
                    }
               
           }
       }
       
       if(gameOver && !isWritten )
       {
           isWritten = true;
           addName();
       }
       
      if(!gameOver && started)
      {
          g.drawString(String.valueOf(score), width/2-25, 100);
      }
               
   }

      public static void main(String[] args) //throws FileNotFoundException 
    {
        game = new MyGame();
        
    }

     @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
          jump();
           
        
    }

 
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
  
}
