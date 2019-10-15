package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor {

    static String remetente;
    static String destinatario;

    public static void main(String[] args) throws UnknownHostException, IOException {
        ServerSocket servidor = new ServerSocket(2424);        
        System.out.println("Porta 2424 aberta! Aguardando conexão!");

        while (true) {

            Socket cliente = servidor.accept();            
            new Conexao(cliente);
        }

    }
}
