package com.game.snake.events;

import com.game.snake.components.GamePanel;

public interface GameEventsListeners{

    public void onGamePause(GamePanel g);
    public void onGameOver(GamePanel g);
    public void onNewLevel(GamePanel g);
    
}