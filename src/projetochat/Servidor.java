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

    static Scanner entrada;
    static PrintStream saida;

    static ArrayList<String> lista_usuarios = new ArrayList<String>();

    public static void main(String[] args) throws UnknownHostException, IOException {
        ServerSocket servidor = new ServerSocket(2424);
        System.out.println("Porta 2424 aberta! Aguardando conexão!");

        while (true) {
            Socket cliente = servidor.accept();

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

            String msg = entrada.nextLine();
            String[] protocolo = msg.split(":");

            if (protocolo[0].equalsIgnoreCase("login")) {
                login(protocolo[1], cliente);
            }
        }

    }

    public static synchronized void login(String usuario, Socket cliente) {

        for (String user : lista_usuarios) {
            if (user.equalsIgnoreCase(usuario)) {
                saida.println("login:false");
                return;
            }
        }
        saida.println("login:true");
        new Conexao(cliente);
        atualizarListaUsuarios(usuario);
    }

    public static synchronized void atualizarListaUsuarios(String usuario) {
        String usuarios = "lista_usuarios:";
        lista_usuarios.add(usuario);

        for (String user : lista_usuarios) {
            usuarios += user + ";";
        }
        saida.println(usuarios.substring(0, usuarios.length() - 1));
    }
}
