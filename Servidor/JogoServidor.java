package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JogoServidor {
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        int porto = 12345;;

        //conectando ao porto
        try{
            serverSocket = new ServerSocket(porto);
        } catch(IOException e) {
            System.out.println("Nao pode escutaro o porto: " + porto + ".\n" + e);
            System.exit(1);
        }

        while(true){
            IJogo jogo = new Jogo(2);
            int numMaximoJogadores = jogo.numMaximoJogadores();   
            for(int i =0;i< numMaximoJogadores;i++){
                Socket clienteSocket = null;
                try {
                    System.out.println("Esperando conexao de um jogador.");
                    clienteSocket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("Accept falhou: " + porto + ".\n" + e);
                    System.exit(1);
                }
                    System.out.println("Accept Funcionou!");
                    jogo.adicionaJogador(clienteSocket);
            }
            try{
                serverSocket.close();
            } catch(IOException e) {
                System.out.println("Nao pode escutaro o porto: " + porto + ".\n" + e);
                System.exit(1);
            }
        }
        
    }
}
