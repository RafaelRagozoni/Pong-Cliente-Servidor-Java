package Cliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Rede {
    DataOutputStream os = null;
    Socket socket = null;
    DataInputStream is = null;

    //conecta a rede
    Rede(){
        try{
            socket = new Socket("127.0.0.1", 12345);
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException e){
            
        } catch (IOException e){

        }
    }

    //Enviar os dados enviados pelo cliente para o servidor
    public void enviaComandosEDadosDoJogador(int estado){
        try{
            os.writeInt(estado);        //manda o estado pra rede
            os.flush();
        } catch (IOException e){

        }
    }

    //Receber a situação processada pelo servidor para atualizar o jogo
    public void recebeContexto(Jogador jogador){
        try {
            jogador.y(is.readInt());            //atualiza a posição vertical do player
            jogador.estado(is.readBoolean());   //checa se o player ta subindo ou descendo
        } catch (IOException e) {
            
        }
    }
    //Receber o placar 
    public void recebePlacar(){
        try {
            jogador.y(is.readInt());            //atualiza a posição vertical do player
            jogador.estado(is.readBoolean());   //checa se o player ta subindo ou descendo
        } catch (IOException e) {
            
        }
    }

}
