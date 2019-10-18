package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author Marcelo
 */
public class Mensagem extends Thread{
    
    Socket remetente;
    Socket destinatario;
    String msg;
    
    Mensagem(Socket remetente, Socket destinatario, String msg) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.msg = msg;
        start();
    }
    
    @Override
    public void run(){
        try {
            // Instânciando a saída para o destinatario da msg
            PrintStream saida = new PrintStream(this.destinatario.getOutputStream());
            // Conseguir relacionar o nome do cliente com o Socket, para conseguir enviar a msg, ex:
            // remetente.toString() + ";" + destinatario.toString() + ";" + msg
            saida.println();
        } catch(IOException io) {
            System.out.println("msg erro: " + io.getMessage());
        }
        
        
    }
}
