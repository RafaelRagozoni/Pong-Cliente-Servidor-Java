package Cliente;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class JogoCliente extends JFrame{
    //Rede rede = new Rede();
    Ambiente ambiente;

    JogoCliente(){
        //desenhar o ambiente e os players do jogo
        super("Pong");
        ambiente = new Ambiente(this);                            //instaciar o ambiente
        //rede.recebeDadosIniciais(ambiente);                       //????
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ambiente.add(ambiente.des);
        add(ambiente);                                            //add o ambiente no frame
        pack();
        setResizable(false);                                      //n pode mudar o tamanho
        setVisible(true);
        //colocar o key listener pra pegar os inputs do jogador 
        KeyAdapter kad = new KeyAdapter(){
            Timer t = new Timer(10,al);
            public void keyPressed(KeyEvent e){
                //Ver qual tecla foi pressionada
                switch (e.getKeyCode()){
                    //pau 2
                    case KeyEvent.VK_W:
                        subindop1=true;
                        repaint();
                        break;
                    case KeyEvent.VK_S:
                        descendop1=true;
                        repaint();
                        break;
                    //pau 1
                    case KeyEvent.VK_UP:
                        subindop2=true;
                        repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        descendop2 =true;
                        repaint();
                        break;
                    case KeyEvent.VK_SPACE:
                        if(start){
                            t.stop();
                            start = false;
                        } else {
                            t.start();
                            start = true;
                        }                  
                }     
            }
            public void keyReleased(KeyEvent e){
                switch (e.getKeyCode()){
                    //pau 2
                    case KeyEvent.VK_W:
                        subindop1=false;
                        repaint();
                        break;
                    case KeyEvent.VK_S:
                        descendop1=false;
                        repaint();
                        break;
                    //pau 1
                    case KeyEvent.VK_UP:
                        subindop2=false;
                        repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        descendop2 =false;
                        repaint();
                        break;
            }
        };
    };
    }
    
    static public void main(String[] args) {
        new JogoCliente();
      }
}
