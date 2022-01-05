package com.game.snake.components;

import java.util.ArrayList;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class Snake {

//region Private variables    
    private ArrayList<Rectangle> _snke = new ArrayList<Rectangle>();
    private Graphics2D _g;
//endregion

//region Class constructor
    public Snake(Graphics2D g){             
        _g = g;
    }
//endregion

//region Private functions
    private void _fillRect(Graphics g,Rectangle rect,Color clr){        
            g.setColor(clr);
            g.fillRect((int)rect.getX(),(int)rect.getY(),(int)rect.getWidth(),(int)rect.getHeight());
    }

    private void _drawRectBorder(Graphics2D g,Rectangle rect){
            g.setColor(new Color(255,255,255));
            g.draw(rect);
    }

    private void _clearRect(Rectangle rect){
        _fillRect(_g,rect,new Color(238,238,238));
        _drawRectBorder(_g,rect);        
    }

//endregion

//region public Properties and functions

    public Rectangle getHead(){
        return _snke.get(0);
    }

    public Rectangle getTail(){
        int lastIdx = _snke.size()-1;
        return _snke.get(lastIdx);
    }

    public void push(Rectangle r){
        Rectangle tailRect = getTail();

        _snke.add(0,r);//Add head 
        _snke.remove(tailRect);//Remove tail
        _clearRect(tailRect);//Undo tail paint
    }

    public void add(Rectangle r){

        _snke.add(0,r);//Add item at the end of the list (FIFO)
        
    }

    public void append(Rectangle r){
        _snke.add(r);//Append item at the end of the list
    }

    public void draw(Color bodyClr,Color headClr){

        //Draw snake body
        for(int idx = 0;idx<_snke.size();idx++){
            Rectangle r = _snke.get(idx);

            _fillRect(_g,r,bodyClr);
            _drawRectBorder(_g,r);

        }

        //Draw head        
        _fillRect(_g,this.getHead(),headClr);
        _drawRectBorder(_g,this.getHead());

    }

    public boolean find(Rectangle r){
        return (_snke.indexOf(r) > -1);
    }

    public void clear(){
        _snke.clear();
    }

    public ArrayList<Rectangle> getBody(){
        return _snke;
    }

//endregion

}