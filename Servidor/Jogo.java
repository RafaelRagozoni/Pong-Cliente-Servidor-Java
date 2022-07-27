package Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Jogo implements IJogo{
    DataOutputStream[] os;
    DataInputStream[] is;
    Jogador[] jogadores;
    ILogica logica;
    int jogadoresConectados= 0;
    boolean continua = true;

    Jogo(int numMaximoJogadores) {  //pega num maxim de jogadores da classe JogoServidor
    os = new DataOutputStream[numMaximoJogadores];  //cria um vetor de os, com um os para cada jogador
    is = new DataInputStream[numMaximoJogadores]; //cria um vetor de is, com um is para cada jogador
    jogadores = new Jogador[numMaximoJogadores];  //cria uma instancia da parte logica de jogador pra cada
    }

    @Override
    public int numMaximoJogadores() {
        // TODO Auto-generated method stub
        return jogadores.length;    //retorna o tamanho do array de Jogador (obtido no construtor, 2)
    }

    @Override
    public void adicionaJogador(Socket clienteSocket) {
        // TODO Auto-generated method stub
        iniciaJogador(jogadoresConectados, clienteSocket);
        enviaDadosIniciais(jogadoresConectados);
        iniciaThreadJogadorRecebe(jogadoresConectados);
        jogadoresConectados++;
    }

    @Override
    public void iniciaLogica(ILogica logica) {
        // TODO Auto-generated method stub
        this.logica = logica;
    }

    @Override
    public void inicia() {
        // TODO Auto-generated method stub
        iniciaThreadJogoEnvio();
    }
    
    public void iniciaJogador(int numeroDeJogadores, Socket clienteSocket){
        try {
            os[numeroDeJogadores] = new DataOutputStream(clienteSocket.getOutputStream());//instancia o DOS[num]
            is[numeroDeJogadores] = new DataInputStream(clienteSocket.getInputStream());
            if (numeroDeJogadores == 0) {  //player 1
                jogadores[numeroDeJogadores] = new Jogador();
            } else {                       //player 2
                jogadores[numeroDeJogadores] = new Jogador();
                //jogadores[numeroDeJogadores].inverte(true);
            }
            } catch (IOException e) {
                e.printStackTrace();
                continua = false;
            }
    }


}
