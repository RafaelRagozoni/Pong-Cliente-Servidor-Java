import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class Pong extends JFrame{
    //variaveis de desenho
    Image img;
    Desenho des = new Desenho();
    //variaveis de controle:
    int p1 = 250; //altura do pau1
    int p2 = 250; //altura do pau2
    int pVel = 10; //velocidade de deslocamento dos paus
    int bx = 490; //y da bola
    int by = 250; //x da bola
    int bYVel = -5; //velocidade de deslocamento Y da bola
    int bXVel = -5; //velocidade de deslocamento X da bola
    boolean start = false; //start do jogo
    String placar1 = "0"; //placar
    String placar2 = "0"; //placar
    int pontos1 = 0; //pontos p1
    int pontos2 = 0; //pontos p2
    boolean subindop1 = false;
    boolean descendop1 = false;
    boolean descendop2 = false ;
    boolean subindop2 = false; 

    //CONTROLE DA BOLA
    ActionListener al = new ActionListener(){
        public void actionPerformed(ActionEvent ae){
          bx+=bXVel;
          by+=bYVel;
          repaint();
          if(subindop1){
            p1-=pVel;
        } else if(descendop1){
            p1+=pVel;
        }
        if(subindop2){
            p2-=pVel;
        } else if(descendop2){
            p2+=pVel;
        }
        }
      };

    //ADPATADOR DE FUNÇÕES
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
    
    //classe que pinta o ambiente
    class Desenho extends JPanel {
        Desenho() {
            try{
                setPreferredSize(new Dimension(1000, 600));
                img = ImageIO.read(new File("FundoJogo.jpg"));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }  

        //função que desenha as parte gráficas
        public void paintComponent(Graphics g) {
          super.paintComponent(g);
          //limites
          //p1
          if(p1>getHeight() - 150)    p1 = getHeight() - 150;
          else if(p1<0)    p1 = 0; 
          //p2
          if(p2>getHeight() -150)    p2 = getHeight() - 150;
          else if(p2<0)    p2 = 0;
          //bola
          if(by>getHeight() - 20){
              by = getHeight()-20;
              bYVel = bYVel*-1;
          } else if(by<0){
            by = 0;
            bYVel = bYVel*-1;
          } else
          if(bx<=60){
            if(by>=p1 && by<=p1+160){
              bXVel = bXVel*-1;
            } else if(bx<25){
                bx = 500;
                by = 250;
                pontos2++;
                placar2 = String.valueOf(pontos2);
            }
          } else if(bx >= getWidth()-60){
              if(by>=p2 && by<=p2+160){
                bXVel = bXVel*-1;
              } else if(bx>getWidth() - 25){
                bx = 500;
                by = 250;
                pontos1++;
                placar1 = String.valueOf(pontos1);
              }
          }
          //fundo
          g.drawImage(img, 0, 0, getSize().width, getSize().height, this);
          //pau 1
          g.setColor(Color.white);
          g.fillRect(25, p1, 25 , 150);
          //pau 2
          g.fillRect(getWidth()-50,p2,25,150);
          Toolkit.getDefaultToolkit().sync();
          //Bolinha
          g.fillOval(bx, by, 20, 20);
          //risco no meio
          g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight()-1);
          //placar
          Font f = new Font("Texto", 1, 50);
          g.setFont(f);
          g.drawString(placar1, getWidth()/4, 100);
          g.drawString(placar2, 3*getWidth()/4 - 50, 100);
          
        }
    }

    Pong() {
        super("Trabalho");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(des);
        pack();
        setVisible(true);

        addKeyListener(kad);
    };

    static public void main(String[] args) {
        new Pong();
    }  
}

