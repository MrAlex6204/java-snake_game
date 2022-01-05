package com.game.snake;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.util.ArrayList;

import java.awt.Dimension;
import com.game.snake.levels.*;
import com.game.snake.components.GameBoard;
import com.game.snake.components.GamePanel;
import com.game.snake.components.Snake;
import com.game.snake.components.BaseLevel;
import com.game.snake.events.GameLevelEventsListeners;



public class Launcher{

    public static void main(String[] args){
        try{
            int snakeLength = 4;//Sanke body length
            Dimension 
                        boardSz = new Dimension(40,30),
                        rectSz = new Dimension(20,20);

            ArrayList<BaseLevel> levels = new ArrayList<BaseLevel>();
            GameBoard gameBoard = new GameBoard(".::Snake~Game::. | Developed by: @MrAlex6204");
            GamePanel gamePanel  = new GamePanel(rectSz,boardSz,snakeLength);//Set game size
            GameLevelEventsListeners scoreEvent  = new GameLevelEventsListeners(){//Define score event

                //Update score event in the game board
                @Override
                public void onScoreUpdate(BaseLevel level){
                    gameBoard.getScoreLabel().setText(level.getLevelName()+" | Score : "+level.getScore());
                }

                //When Level Ends
                @Override
                public void onEndLevel(BaseLevel level){
                    gameBoard.getScoreLabel().setText("** "+level.getLevelName()+" Finished! **");
                }

                //When Level Begins
                @Override
                public void onBeginLevel(BaseLevel level){
                    gameBoard.getScoreLabel().setText("** Press any navigation key to start **");
                }

            };

            //Adds game levels
            levels.add(
                        new Level1(//Create a level object
                                        rectSz,//Rect size
                                        boardSz//Board size
                                   ).setLevelEvent(scoreEvent) //Adds Score event listener
                      );      

            //Set game levels
            gamePanel.setGameLevels(levels);

            gameBoard.setGamePanel(gamePanel);//set the game panel
            gameBoard.setVisible(true);

            gamePanel.start();//Start game
            
        }catch(Exception e){
            System.err.println(e);
        }

    }

}