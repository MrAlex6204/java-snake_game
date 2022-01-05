package com.game.snake.events;

import com.game.snake.components.BaseLevel;

public interface GameLevelEventsListeners{
    
    public void onScoreUpdate(BaseLevel level);

    public void onEndLevel(BaseLevel level);    

    public void onBeginLevel(BaseLevel level);
    
}