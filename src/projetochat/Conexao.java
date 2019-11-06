package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao extends Thread {

    Socket cliente;
    Scanner entrada;
    PrintStream saida;
    Servidor s = new Servidor();

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
            try {
                String msg = entrada.nextLine();
                s.recebeMensagem(cliente, msg);
//            System.out.println("O cliente digitou: " + msg);
            } catch (IOException ex) {
                Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
