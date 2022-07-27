import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Cliente extends JFrame {
    int p1 = 250; //altura do pau1
    int p2 = 250; //altura do pau2
    int pVel = 10; //velocidade de deslocamento dos paus
    int bx = 490; //y da bola
    int by = 250; //x da bola
    int bYVel = -5; //velocidade de deslocamento Y da bola
    int bXVel = -5; //velocidade de deslocamento X da bola
    String placar1 = "0"; //placar
    String placar2 = "0"; //placar
    int pontos1 = 0; //pontos p1
    int pontos2 = 0; //pontos p2

    final int subindo = 0;
    final int descendo = 1;
    final int parado = 2;
  
  /** deslocamento até o centro da imagem, direcao da cabeca */
  DataOutputStream os = null;
  Socket socket = null;
  DataInputStream is = null;
  boolean jogoFuncionando = true;
  Thread t;

    Desenho des;
    Image img;
    //classe que pinta o ambiente
    class Desenho extends JPanel {
        Desenho() {
            try{
                setPreferredSize(new Dimension(1000, 600));
                setResizable(false);
                img = ImageIO.read(new File("FundoJogo.jpg"));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
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
      Toolkit.getDefaultToolkit().sync();
    }
  }

  Cliente() {
    super("Trabalho");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    conectaAoServidor();
    des = new Desenho();
    add(des);

    pack();
    setVisible(true);

    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          enviaComandosEDadosDoJogador(subindo);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          enviaComandosEDadosDoJogador(descendo);
        }
      }
      public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          enviaComandosEDadosDoJogador(parado);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          enviaComandosEDadosDoJogador(parado);
        }
      }
    });

    t = new Thread(new Runnable() {
      public void run() {
        while (jogoFuncionando) {
          recebeSituacao(); // Durante o jogo recebe dados
          recebePlacar();
          des.repaint();
        }
      }
    });
    t.start();
  }

  static public void main(String[] args) {
    new Cliente();
  }

  void conectaAoServidor() {
    try {
      socket = new Socket("127.0.0.1", 12345);
      os = new DataOutputStream(socket.getOutputStream());
      is = new DataInputStream(socket.getInputStream());
    } catch (UnknownHostException e) {
      erroFatalExcecao("Servidor desconhecido!", e);
    } catch (IOException e) {
      erroFatalExcecao("A conexao com o servidor não pode ser criada!", e);
    }
  }

  //manda o estado para o server (subindo ou descendo)
  void enviaComandosEDadosDoJogador(int estado) {
    try {
      os.writeInt(estado);
      os.flush();
    } catch (IOException e) {
      erroFatalExcecao("A imagem não pode ser enviada!", e);
    }
  }

  //recebe atualizações do servidor
  void recebeSituacao() {
    try {
        bx = is.readInt();
        by = is.readInt();
        p1 = is.readInt();
        p2 = is.readInt();
    } catch (IOException e) {
      erroFatalExcecao("Jogo terminado pelo servidor.", e);
    }
  }

  void recebePlacar() {
    try {
      pontos1 = is.readInt();
      pontos2 = is.readInt();
      placar1 = String.valueOf(pontos1);
      placar2 = String.valueOf(pontos2);
    } catch (IOException e) {
      erroFatalExcecao("Jogo terminado pelo servidor.", e);
    }
  }

  void erroFatalExcecao(String msg, Exception ex) {
    erroFatalExcecao(null, msg, ex);
  }

  void erroFatalExcecao(Exception ex) {
    erroFatalExcecao(null, null, ex);
  }

  void erroFatalExcecao(Component janela, String msg, Exception ex) {
    StringWriter str = new StringWriter();
    ex.printStackTrace(new PrintWriter(str));
    if (msg == null) {
      msg = str.toString();
    } else {
      msg += "\n" + str;
    }
    JOptionPane.showMessageDialog(janela, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    System.exit(10);
  }
}
