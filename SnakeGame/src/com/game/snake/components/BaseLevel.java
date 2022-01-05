package com.game.snake.components;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

import com.game.snake.events.GameLevelEventsListeners;


public abstract class BaseLevel{

    protected int obstacleCounter = 100;
    protected GameLevelEventsListeners levelEvent = null;
    protected Rectangle currentRectangle = null;
    protected Dimension rectSz = null,boardSz = null;
    protected int   speed = 150,
                    score = 0;

    public BaseLevel(Dimension rectSz,Dimension boardSz){
        this.rectSz =  rectSz;
        this.boardSz = boardSz;
        this.currentRectangle = this.getRandomRect();
    }

    public int getSpeed(){//Get current speed
        return speed;
    }

    public BaseLevel setLevelEvent(GameLevelEventsListeners levelEvent){//Event listener to notify socore change
        this.levelEvent = levelEvent;
        return this;
    }

    public int getScore(){//Get the current score
        return score;    
    }

    public boolean isRequiredEat(Rectangle r){//Verify if require eat current obstacle
        return currentRectangle.equals(r);
    }
    
    public void endLevel(){
         if(levelEvent!=null){
            levelEvent.onEndLevel(this);//Notify score if event is not null
        }
    }

    protected Rectangle getRandomRect(){//Gets random rect from the obstacles list
        Random rnd = new Random();
        int x = rnd.nextInt(boardSz.width);
        int y = rnd.nextInt(boardSz.height);

        return new Rectangle(
                                (x * this.rectSz.width),
                                (y * this.rectSz.height),
                                this.rectSz.width,
                                this.rectSz.height
                            );
    }

    protected void fillRect(Graphics g,Rectangle rect,Color clr){
            //Draw fill rectangle
            g.setColor(clr);
            g.fillRect((int)rect.getX(),(int)rect.getY(),(int)rect.getWidth(),(int)rect.getHeight());
    }

    protected void drawRect(Graphics2D g,Rectangle rect){
            //Draw an empty rectangle
            g.setColor(new Color(255,255,255));
            g.draw(rect);
    }

    public void notifyScore(){
        if(levelEvent!=null){
            levelEvent.onScoreUpdate(this);//Notify score if event is not null
        }
    }

    public void notifyEndOfLevel(){
        if(levelEvent!=null){
            levelEvent.onEndLevel(this);
        }
    }

    public  void notifyBeginOfLevel(){
        if(levelEvent!=null){
            levelEvent.onBeginLevel(this);
        }
    }
  
    public void reset(){//Reset current level
        this.speed = 150;
        this.score = 0;
        this.obstacleCounter = 10;
        this.notifyScore();
    }

    public boolean levelEnds(){
        return (obstacleCounter <= 0);//Returns game ends when obstacles counter is finished;
    }

    public abstract String getLevelName();//Returns current level name

    public abstract void setNewObstacle(Graphics2D g,ArrayList<Rectangle> snakeBody);//Draws next obstacle(s)
    
    public abstract void updateScore();
}