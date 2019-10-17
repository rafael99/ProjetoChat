package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    String usuario;
    private String mensagem;
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket cliente = new Socket("127.0.0.1", 2424);
        
        System.out.println("O cliente se conectou ao servidor!");
        
        Scanner teclado = new Scanner(System.in);
        
//        Scanner usuario = new Scanner(cliente.getInputStream());
        PrintStream saida = new PrintStream(cliente.getOutputStream());
        Scanner entrada = new Scanner(cliente.getInputStream());
        
        while(teclado.hasNextLine()) {
            saida.println(teclado.nextLine());
            System.out.println(entrada.nextLine());
        }
        
        saida.close();
        teclado.close();
    }
}
