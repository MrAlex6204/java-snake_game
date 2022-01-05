package com.game.snake.levels;


import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.game.snake.components.BaseLevel;
import com.game.snake.events.GameLevelEventsListeners;


public class Level1 extends BaseLevel {

    public Level1(Dimension rectSz,Dimension boardSz){
        super(rectSz,boardSz);
         this.obstacleCounter = 10;
    }

    @Override
    public String getLevelName(){//Returns current level name
        return "Level #1";
    }
    
    public void setNewObstacle(Graphics2D g,ArrayList<Rectangle> snakeBody){//Draws next obstacle(s)
        Rectangle r;

        do{
            r = this.getRandomRect();

        }while(snakeBody.contains(r));//Repeat if got a rect in the same location of snake body

        this.currentRectangle = r;

        this.fillRect(g,r,Color.blue);
        this.drawRect(g,r);
       
    }

  @Override
    public void updateScore(){

        this.speed -= (this.speed*0.015); //Increment 1.5% the speed
        this.score += 10;//Increment current score value
        this.obstacleCounter--;//Decrement the total obstacles required
        this.notifyScore(); 
    }

}