package com.game.snake.components;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import com.game.snake.components.GamePanel;


import javax.swing.JFrame;

public class GameBoard extends JFrame  {

    private JLabel lblStatus = new JLabel("Score : | Level : ");
    private JButton btn = new JButton("Hello");
    private JPanel 
            pnlMain = new JPanel(),
            pnlControls = new JPanel(),
            pnlGameContainer = new JPanel();

    public GameBoard(String title){
        this.setTitle(title);
        initilizeComponents();
    }

    public GameBoard(){

        initilizeComponents();

    }

    public void initilizeComponents(){
        try{
            this.pnlControls.add(lblStatus);
            this.pnlGameContainer.setLayout(new BorderLayout(0,0));

            this.pnlMain.setLayout(new BoxLayout(this.pnlMain,BoxLayout.Y_AXIS));
            this.pnlMain.add(pnlControls);
            this.pnlMain.add(pnlGameContainer);
            this.getContentPane().add(pnlMain);

            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            this.setResizable(false);            
            this.pack();
            this.setLocationRelativeTo(null);
                        
        }catch(Exception e){

        }

    }

    public void setGamePanel(GamePanel panel){
        Dimension sz = new Dimension();
        double Height = panel.getBoardPXSz().getHeight()+pnlControls.getSize().getHeight();

        sz.setSize(panel.getBoardPXSz().getWidth(),Height);
        
        this.pnlGameContainer.removeAll();
        this.pnlGameContainer.add(panel);

        this.setPreferredSize(sz);//Auto resize the window
        this.addKeyListener(panel);//Set Key events
        this.addWindowListener(panel);//Set window events 
        this.setLocationRelativeTo(null);
        this.pack();

    }

    public JLabel getScoreLabel(){
        return lblStatus;
    }

}