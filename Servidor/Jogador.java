package Servidor;

//logica do jogador
public class Jogador {
    boolean subindo = false; //ver se o player quer estar subindo o pau
    boolean descendo = false; //váriavel pra ver se o player quer estar descendo
    int pontos = 0;
    int pVel = 10; //velocidade do player
    int y;

    void y(int y){
        this.y = y;
    }

    void estado(boolean taSubindo){
        if(taSubindo)   subindo = true;
        descendo = false;
    }

    void pontua(){
        this.pontos++;
    }
}
