package Cliente;
import java.awt.*;
import javax.swing.*;

//desenha os jogadores na tela 
public class JogadorDesenho {
    //variaveis pra desenhar
    JFrame janela;
    int y;  //posição vertical de y
    boolean ehP1;   //Bollean pra saber se é o player 1
    
    //Váriaveis de controle do player
    boolean subindo = false; //ver se o player quer estar subindo o pau
    boolean descendo = false; //váriavel pra ver se o player quer estar descendo
    int pontos = 0; 
    int pVel = 10; //velocidade do player

    //Essa função vai pegar as informações necessárias para se add o player.
    JogadorDesenho(JFrame janela, boolean ehP1){
        this.janela = janela;   //pega a janela que o jogo estará rodando
        this.ehP1 = ehP1;       //diz se é player 1 ou nao
        //this.y = y;             //mostra a posição y do negocio
    }

    void desenhaPlayer(Graphics g){
        g.setColor(Color.white);
        //pau 1
        if(ehP1){    
            g.fillRect(25, y, 25 , 150);    //cria o p1 na direita
        }
        //pau 2
        else{
            g.fillRect(janela.getWidth()-50,y,25,150);            
        }
        Toolkit.getDefaultToolkit().sync();
        
    }

}
