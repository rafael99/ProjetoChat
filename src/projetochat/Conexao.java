package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

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
            saida = new PrintStream(cliente.getOutputStream());
        } catch (IOException ex) {
            // Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scanner teclado = new Scanner(System.in);

        while (entrada.hasNextLine()) {
            String msg = entrada.nextLine();
            
            System.out.println("O cliente digitou: " + msg);
        }
    }

}
