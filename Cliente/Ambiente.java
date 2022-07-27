package Cliente;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;

public class Ambiente extends JPanel{
    //classe que pinta o ambiente
        Image img;
        JogadorDesenho p1;
        JogadorDesenho p2;
        Desenho des = new Desenho();
        String placar1 = "0";
        String placar2 = "0";

        //pegar os jogadores
        Ambiente(JogoCliente jogo){
            new Desenho();
            p1 = new JogadorDesenho(jogo,true);
            p2 = new JogadorDesenho(jogo,false);
        }
        //classe que pinta o ambiente
        class Desenho extends JPanel {
            Desenho() {
                try{                //carregar a imagem
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
            g.drawImage(img, 0, 0, getSize().width, getSize().height, this);//desenha o fundo
            p1.desenhaPlayer(g);    //desenha o p1
            p2.desenhaPlayer(g);    //desenha o p2
            g.drawString(placar1, getWidth()/4, 100);
            g.drawString(placar2, 3*getWidth()/4 - 50, 100);
            }
        }
    }