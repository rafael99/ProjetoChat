package projetochat;

import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import sun.util.logging.PlatformLogger;

public class Conexao extends Thread {

    Socket cliente;
    static Scanner entrada;
    static PrintStream saida;
    
    
    Conexao(Socket conexao) {
        this.cliente = conexao;
        start();
    }

    @Override
    public void run() {
        try {
            entrada = new Scanner(cliente.getInputStream());
        } catch (IOException ex) {
            /*Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);*/
        }

        try {
            saida = new PrintStream(cliente.getOutputStream());
        } catch (IOException ex) {
            /*Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);*/
        }

        Scanner teclado = new Scanner(System.in);

        while (entrada.hasNextLine()) {
            String msg = entrada.nextLine();
            saida.println("Você digitou: " + msg);
            // Pegando a ação do usuario
            String[] protocolo = msg.split(":");
            switch (protocolo[0]) {
                case "mensagem":
                    Servidor s = new Servidor();
                    // No lugar do "cliente" do segundo parâmetro, devemos informar o destinatario, porém não sei como capturar ainda =)
                    // protocolo[1] = msg de fato
                    s.enviarMensagem(cliente, cliente, protocolo[1]);
                    break;
                default:
                    saida.println("Protocolo não existe");
                    break;
            }

            System.out.println("O cliente digitou: " + msg);
            
        }       
    }

    
}
