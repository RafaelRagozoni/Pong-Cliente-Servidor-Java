import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    int porto = 12345;

    try {
      serverSocket = new ServerSocket(porto);
    } catch (IOException e) {
      System.out.println("Nao pode escutaro o porto: " + porto + ".\n" + e);
      System.exit(1);
    }

    while (true) {
      IJogo jogo = new Jogo();
      int numMaximoJogadores = jogo.numMaximoJogadores();
      for (int i = 0; i < numMaximoJogadores; i++) {
          
        Socket clientSocket = null;
        try {
          System.out.println("Esperando conexao de um jogador.");
          clientSocket = serverSocket.accept();
        } catch (IOException e) {
          System.out.println("Accept falhou: " + porto + ".\n" + e);
          System.exit(1);
        }
        System.out.println("Accept Funcionou!");
        jogo.adicionaJogador(clientSocket);
      }
      
      jogo.iniciaLogica(new Logica(jogo));
      jogo.inicia();
    }
  }
}

class Jogo extends Thread implements IJogo {
  Socket clientSocket;
  DataOutputStream[] os = new DataOutputStream[2];
  int pontos1 = 0;
  int pontos2 = 0;
  /** se false para a repetição das threads e consequentemente o jogo */
  boolean continua = true;
  ILogica logica;

  //variaveis de controle:
  int p1 = 250; //altura do pau1
  int p2 = 250; //altura do pau2
  int pVel = 10; //velocidade de deslocamento dos paus
  int bx = 490; //y da bola
  int by = 250; //x da bola
  int bYVel = -10; //velocidade de deslocamento Y da bola
  int bXVel = -10; //velocidade de deslocamento X da bola

  int contaJogadoresConectados = 0;
  int[] estadoJogador = {2,2}; //p1 e p2 parados
  // Unica Thread envia dados para os dois clientes
  public void run() {
    int numDoJogador = 0;

    try {
      while (continua) {
        logica.executa();
        enviaSituacao();
        enviaPlacar();

        forcaEnvio();

        try {
          Thread.sleep(10);//a cada 1000/10 = 100 vezes por segundo
        } catch (InterruptedException e) {
          continua = false;
          e.printStackTrace();
        }
      }
      os[numDoJogador].close();
      clientSocket.close();

    } catch (IOException e) {
      continua = false;
      e.printStackTrace();
    }
  }

  public void adicionaJogador(Socket clientSocket) {
    this.clientSocket = clientSocket;
    int numDoJogador = contaJogadoresConectados++;
    try {
      os[numDoJogador] = new DataOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
      continua = false;
      e.printStackTrace();
    }
    new Thread() {
      // Duas Threads, uma por cliente esperando dados
      public void run() {
        try {
          DataInputStream is = new DataInputStream(clientSocket.getInputStream());
          while (continua) {
            estadoJogador[numDoJogador] = is.readInt();//recebe os inputs
          }
          is.close();
        } catch (IOException e) {
          continua = false;
          e.printStackTrace();
        }
      }
    }.start();
  }

  public int numMaximoJogadores() {
    return 2;
  }

  public void iniciaLogica(ILogica logica) {
    this.logica = logica;
  }

  public void inicia() {
    start();
  }

  public void enviaSituacao() {
    try {
      os[0].writeInt(bx);
      os[0].writeInt(by);
      os[0].writeInt(p1);
      os[0].writeInt(p2);

      os[1].writeInt(bx);
      os[1].writeInt(by);
      os[1].writeInt(p1);
      os[1].writeInt(p2);
    } catch (IOException e) {
      e.printStackTrace();
      continua = false;
    }
  }

  public void enviaPlacar() {
    try {
      os[0].writeInt(pontos1);
      os[0].writeInt(pontos2);
      os[1].writeInt(pontos1);
      os[1].writeInt(pontos2);
    } catch (IOException e) {
      e.printStackTrace();
      continua = false;
    }
  }

  void forcaEnvio() {
    try {
      os[0].flush();
      os[1].flush();
    } catch (IOException e) {
    }
  }
}

class Logica implements ILogica {
  Jogo jogo;

  Rectangle[] rectColisao = new Rectangle[2];

  Logica(IJogo jogo) {
    this.jogo = (Jogo) jogo;
  }

  public void executa() {
    movimentoJogador();
    limites();
    movimentoBola();
  }

  //fazer subir e descer
  boolean subindop1 = false;
  boolean subindop2=false;
  boolean descendop1 = false;
  boolean descendop2 = false;
  void movimentoJogador(){
    //p1
    switch (jogo.estadoJogador[0]){
      case 0: //subindo
        jogo.p1 -= jogo.pVel; //diminui altura de p1 por pVel
        break;
      case 1: //descendo
        jogo.p1 += jogo.pVel;
        break;
    }
    //p2
    switch (jogo.estadoJogador[1]){
      case 0: //subindo
        jogo.p2 -= jogo.pVel; //diminui altura de p2 por pVel
        break;
      case 1: //descendo
        jogo.p2 += jogo.pVel;
        break;
    }
  }

  void limites(){
    //p1
    if(jogo.p1> 450) jogo.p1 = 450;
    else if(jogo.p1<0) jogo.p1 =0;
    //p2
    if(jogo.p2> 450) jogo.p2 = 450;
    else if(jogo.p2<0) jogo.p2 =0;
    //bola
    if(jogo.by>580){
      jogo.by = 580;
      jogo.bYVel = jogo.bYVel*-1;
    } else if(jogo.by<0){
      jogo.by = 0;
      jogo.bYVel = jogo.bYVel*-1;
    } else
    if(jogo.bx<=60){
      if(jogo.by>=jogo.p1 && jogo.by<=jogo.p1+160){
        jogo.bXVel = jogo.bXVel*-1;
      } else if(jogo.bx<25){
          jogo.bx = 500;
          jogo.by = 250;
          jogo.pontos2++;
          //jogo.placar2 = String.valueOf(jogo.pontos2);
      }
    } else if(jogo.bx >= 940){
        if(jogo.by>=jogo.p2 && jogo.by<=jogo.p2+160){
          jogo.bXVel = jogo.bXVel*-1;
        } else if(jogo.bx>975){
          jogo.bx = 500;
          jogo.by = 250;
          jogo.pontos1++;
          //jogo.placar1 = String.valueOf(jogo.pontos1);
        }
    }
  }

  void movimentoBola(){
    //bola
    jogo.bx += jogo.bXVel;
    jogo.by += jogo.bYVel;
  }

}
