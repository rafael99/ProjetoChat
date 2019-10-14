package projetochat;

import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import sun.util.logging.PlatformLogger;

public class Conexao extends Thread{
    Socket cliente;
    
    Conexao(Socket conexao) {
        this.cliente = conexao;
    }
    
    @Override
    public void run() {
        // System.out.println("Nova conexão com o cliente "+ cliente.getInetAddress().getHostAddress());
        
        Scanner entrada = null;
        
        try {
            entrada = new Scanner(cliente.getInputStream());
        } catch(IOException ex) {
            /*Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);*/
        }
        
        PrintStream saida = null;
        try {
            saida = new PrintStream(cliente.getOutputStream());
        } catch(IOException ex) {
            /*Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);*/
        }
        
        Scanner teclado = new Scanner(System.in);
        
        while(entrada.hasNextLine()) {
            String msg = entrada.nextLine();
            System.out.println("O cliente digitou: " + msg);
            
            saida.println("Você digitou: " + msg);
        }
    }
}
