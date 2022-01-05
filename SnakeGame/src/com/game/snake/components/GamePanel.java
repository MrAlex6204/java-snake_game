package com.game.snake.components;

import com.game.snake.components.Snake;
import com.game.snake.components.BaseLevel;

import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.AWTKeyStroke;
import java.util.ArrayList;
import java.lang.Runnable;
import java.lang.Thread;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;


//Graphics imports
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;

public class GamePanel extends JPanel implements Runnable, KeyListener, WindowListener, AutoCloseable {

//region Private variables

        private BaseLevel _crLevel;//Current Level
        private ArrayList<BaseLevel> _levels = new ArrayList<BaseLevel>();//Game Levels
        private Thread _gameThr = new Thread(this);
        private Snake _snake;
        private int _initialLenght;
        private Point _rLocation;//Relative location
        private Rectangle _snkeHeadRect;
        private Graphics2D _g2;
        private BufferedImage _img = null;
        private Dimension _rectSz,_boardSz;
        private volatile int _speed = 150;
        private volatile KeyEvent _prevKeyPressed = null;
        private volatile boolean    _pauseGame = true,
                                    _stop = false,
                                    _isGameOver = false;
        private final int   LEFT_KEY    = 37,
                            UP_KEY      = 38,
                            RIGHT_KEY   = 39,
                            DOWN_KEY    = 40;

//endregion

//region Class constructors

        public GamePanel(Dimension rectSz,Dimension boardSz,int initialLenght){
            _rectSz = rectSz;//Rectangle size
            _boardSz = boardSz;//Board size
            _initialLenght = initialLenght;           
        }

        public GamePanel(int rectWidth,int rectHeight,int width,int height,int initialLenght) throws Exception {
            if(width > 0 & height > 0){

                _rectSz = new Dimension(rectWidth,rectHeight);
                _boardSz = new Dimension(width,height);
                _initialLenght = initialLenght;
               
            }else{
                throw new Exception("Container board size with and height can not be less than cero!");
            }
        }

        @Override
        // protected void finalize(){
        public void close(){
            System.out.println("Finalizing class");
            _stop = true;
        }

//endregion

//region Public properties and functions

        public Dimension getRectSz(){
            return _rectSz;
        }

        public Dimension getBoardPXSz(){
            return new Dimension(
                (_rectSz.width * _boardSz.width)+3,
                (_rectSz.height * _boardSz.height)+28
            );
        }

        public Dimension getBoardSz(){
            return _boardSz;
        }

        public Rectangle getRectFromCoords(int x, int y){
            return new Rectangle(
                (x * _rectSz.width),
                (y * _rectSz.height),
                _rectSz.width,
                _rectSz.height
            );
        }

        public Rectangle getRectFromCoords(Point pos){
            return getRectFromCoords((int)pos.getX(), (int)pos.getY());
        }

        public Graphics2D getGraphic(){
            return _g2;
        }

        public Point getCenterLocation(){
            //Calculate center point from board
            Point pLocation = new Point();
            pLocation.setLocation(
                                    (int)(_boardSz.width/2),
                                    (int)(_boardSz.height/2)
                                );
            return pLocation;
        }

        public void start(){
            _initilizeResources();
            _gameThr.run();
        }

        public void pause(){
            _pauseGame = true;
        }

        public void stop(){
            System.out.println("Game has been stoped");
            //Stop running loops
            _pauseGame = true;
            _stop = true;
            //Stop running thread
            _gameThr.interrupt();

        }

        public void restart(){
            _snake =  _initilizeSnake(_g2,this.getCenterLocation(),_initialLenght);

            _rLocation = this.getCenterLocation();
            _snkeHeadRect = this.getRectFromCoords(_rLocation);

            _drawBoard(_g2);
            _snake.draw(Color.green,Color.red);
            _speed = 150;//Default speed
            _isGameOver = false;
            _pauseGame = false;
            _prevKeyPressed = null;

            
            if(_levels.size() > 0){
                //Reset all defined levels
                for(BaseLevel iLevel:this._levels){
                    iLevel.reset();    
                }

                //Set the fist level
                _crLevel = _levels.get(0);
                _crLevel.setNewObstacle(this._g2,_snake.getBody());//Draws the first obtacle
                _crLevel.notifyBeginOfLevel();
            }

            this.repaint();
        }

        public void setGameLevels(ArrayList<BaseLevel> levels){
            _levels =  levels;
        }

        public boolean hasLevels(){
            return (_levels.size() > 0);
        }

//endregion

//region Private functions

        private void _setNextLevel(){
            //Get the next level from the list            
            int level_idx = _levels.indexOf(_crLevel);//Get current level index
            int snake_size = _snake.getBody().size();



            level_idx++;

            if(level_idx >= _levels.size()){
                level_idx = 0;
            }

            _crLevel = _levels.get(level_idx);//Gets next level from index number
            _crLevel.reset();//Reset the level

            //Pause game
            _pauseGame = true;
            _prevKeyPressed = null;

            //Re-Draw board
            _drawBoard(_g2);

            //Draw re-draw snake at the center
            _snake.clear();
            _rLocation = this.getCenterLocation();//Gets center location
            _snake = _initilizeSnake(_g2,_rLocation,snake_size);
            
            _snake.draw(Color.green,Color.red);

            _crLevel.setNewObstacle(_g2,_snake.getBody());//Draw obstacle

        }

        private Snake _initilizeSnake(Graphics2D g,Point initialPos,int bodyLenght){
            //Initilize snake object
            Snake s = new Snake(g);

            for(int i = 0;i<bodyLenght;i++){
                int x = (int)initialPos.getX()-i,y  = (int)initialPos.getY();
                Rectangle r = getRectFromCoords(x,y);
                s.append(r);
            }

            return s;
        }

        private void _initilizeResources(){
            //Initilize _img Graphic resources

            _img = new BufferedImage(this.getBoardPXSz().width,this.getBoardPXSz().height,BufferedImage.TYPE_INT_ARGB);
            _g2 = _img.createGraphics();//Create an graphic object to draw
            _snake = _initilizeSnake(_g2,this.getCenterLocation(),_initialLenght);

            _drawBoard(_g2);//Draw board

            //Get center rectangle from position
            _rLocation = this.getCenterLocation();
            _snkeHeadRect = getRectFromCoords(_rLocation);//Get rectangle bounds from relative position

            if(this.hasLevels()){//Set the first level
                _crLevel = _levels.get(0);

                _crLevel.setNewObstacle(_g2,_snake.getBody());//Draw first obstacle
                _crLevel.notifyBeginOfLevel();
            }

            _snake.draw(Color.green,Color.red);
            _gameThr.setName("GameMainThr");
        }

        private void _fillRect(Graphics g,Rectangle rect,Color clr){
            //Draw fill rectangle
            g.setColor(clr);
            g.fillRect((int)rect.getX(),(int)rect.getY(),(int)rect.getWidth(),(int)rect.getHeight());
        }

        private void _drawRect(Graphics2D g,Rectangle rect){
            //Draw an empty rectangle
            g.setColor(new Color(255,255,255));
            g.draw(rect);
        }

        private void _drawBoard(Graphics2D g){
            //Draw game board

            for(int y = 0;y<_boardSz.height;y++){

                for(int x = 0;x<_boardSz.width;x++){
                    Rectangle rect  = new Rectangle(
                                                    (x * _rectSz.width),
                                                    (y * _rectSz.height),
                                                    _rectSz.width,
                                                    _rectSz.height
                                                );
                    _fillRect(g,rect,new Color(238,238,238));
                    _drawRect(g,rect);
                }

            }

        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            //Draw and Re-Draw the buffered image
            ((Graphics2D)g).drawImage(_img, 0, 0, _img.getWidth(), _img.getHeight(), null);

        }

//endregion

//region Keys Events

        //Overrides KeyeEvent Listener functions
        public void keyPressed(KeyEvent e){
            //Verify if the keypressed belongs to the navigation pad
            if(e.getKeyCode() == UP_KEY || e.getKeyCode()== RIGHT_KEY || e.getKeyCode() == DOWN_KEY || e.getKeyCode() == LEFT_KEY){

                if(_isGameOver){
                     //If was a game over then reset game board
                    this.restart();
                } else if(_prevKeyPressed == null){
                    if(e.getKeyCode() !=  LEFT_KEY){
                        _prevKeyPressed = e;
                        _pauseGame = false;
                    }
                } else {
                    //Prevents move to an opposite position
                    if(e.getKeyCode() ==  DOWN_KEY){
                        if(_prevKeyPressed.getKeyCode() != UP_KEY){
                            _prevKeyPressed = e;
                        }
                    }else if(e.getKeyCode() ==  LEFT_KEY){
                        if(_prevKeyPressed.getKeyCode() != RIGHT_KEY){
                            _prevKeyPressed = e;
                        }
                    }else if(e.getKeyCode() ==  UP_KEY){
                        if(_prevKeyPressed.getKeyCode() != DOWN_KEY){
                            _prevKeyPressed = e;
                        }
                    }else if(e.getKeyCode() ==  RIGHT_KEY){
                        if(_prevKeyPressed.getKeyCode() != LEFT_KEY){
                            _prevKeyPressed = e;
                        }
                    }
                }

                _speed -= (_speed*0.25);//Increment speed until key is pressed

                if(_speed<=30){
                    _speed = 30;//Limit the max speed;
                }

            }else if(e.getKeyCode() == KeyEvent.VK_P){//Key P as paused game

                if(_prevKeyPressed!=null & !_isGameOver){
                    _pauseGame = !_pauseGame;//Unpause/Pause game
                }

            }

         }

         public void keyTyped(KeyEvent e){
         }

         public void keyReleased(KeyEvent e){
             //Restore speed when key is pressed
             if(_crLevel!=null){
                 _speed = _crLevel.getSpeed();
             }else{
                 _speed = 150;
             }
         }



//endregion

//region Move engine

    public void run() {

        while(!_stop){//Runs while game is alive

            while(!_pauseGame){//Runs while game is not paused
                try{
                    _gameThr.sleep(_speed);//Set game speed
                    _moveSnake(_prevKeyPressed);//Move snake into the route selected
                }catch(Exception ex){

                }
            }

        }

    }

    private void _moveSnake(KeyEvent e){

            if(e.getKeyCode() == UP_KEY || e.getKeyCode()== RIGHT_KEY || e.getKeyCode() == DOWN_KEY || e.getKeyCode() == LEFT_KEY){
                int x = (int)_rLocation.getX(),y= (int)_rLocation.getY();

                if(e.getKeyCode() ==  UP_KEY){
                    // ^
                    y--;

                }else if(e.getKeyCode() ==  RIGHT_KEY){
                    // --->
                    x++;

                }else if(e.getKeyCode() ==  DOWN_KEY){
                    // v
                    y++;

                }else if(e.getKeyCode() ==  LEFT_KEY){
                    // <---
                    x--;
                }

                //verifying if the new coords are out of bounds
                if(x>_boardSz.width-1){
                    x = 0;
                }else if(x< 0 ){
                        x = _boardSz.width;
                }
                if (y>_boardSz.height-1){
                        y = 0;
                }else if(y< 0 ){
                        y = _boardSz.height-1;
                }

                _rLocation.setLocation(x,y);
                _snkeHeadRect = getRectFromCoords(_rLocation);//Get rectangle bounds relative position

                if(_snake.find(_snkeHeadRect)){
                        //Player loose :P she ate herself
                        _snake.draw(Color.red,Color.red);
                        _isGameOver = true; //Set is game over
                        _pauseGame = true;                         
                }else{

                    if(this.hasLevels()){

                        //Verify if require eat object at the current position
                        if(_crLevel.isRequiredEat(_snkeHeadRect)){

                            _snake.add(_snkeHeadRect);//Add body new item
                            _crLevel.setNewObstacle(_g2,_snake.getBody());//Draws next element to eat
                            _crLevel.updateScore();

                            if(_crLevel.levelEnds()){
                                
                                _crLevel.notifyEndOfLevel();
                                _crLevel.endLevel();//Notify end of level
                                _setNextLevel();//Set the next level
                                this.pause();
                                
                            }

                            _speed = _crLevel.getSpeed();//Update game speed;

                        }else{                            
                            _snake.push(_snkeHeadRect);//Move snake body to new position
                        }

                    }else{//Draw default snake with when levels are not defined

                            _snake.push(_snkeHeadRect);//Move snake body to new position

                    }

                    _snake.draw(Color.green,Color.red);//Draw snake body & head
                }

                repaint();
            }

        }

//endregion

//region Window Events
    public void windowActivated(WindowEvent e){

    }

    public void windowClosed(WindowEvent e){
    }

    public void windowClosing(WindowEvent e){
        this.stop();//Stop thread when window is closed
    }

    public void windowDeactivated(WindowEvent e){

    }

    public void windowDeiconified(WindowEvent e){

    }

    public void windowIconified(WindowEvent e){

    }

    public void windowOpened(WindowEvent e){

    }
//endregion

}