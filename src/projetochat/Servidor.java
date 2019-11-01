package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Servidor {

    static String remetente;
    static String destinatario;

    static Scanner entrada;
    static PrintStream saida;

    static Map<String,Socket> lista_usuarios = new HashMap<String, Socket>();

    public static void main(String[] args) throws UnknownHostException, IOException {
        ServerSocket servidor = new ServerSocket(2424);
        System.out.println("Porta 2424 aberta! Aguardando conexão!");

        while (true) {
            Socket cliente = servidor.accept();

            try {
                entrada = new Scanner(cliente.getInputStream());
                saida = new PrintStream(cliente.getOutputStream());
            } catch (IOException ex) {
                // Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            }

            String msg = entrada.nextLine();
            String[] protocolo = msg.split(":");

            if (protocolo[0].equalsIgnoreCase("login")) {
                login(protocolo[1], cliente);
            }
        }

    }

    public static synchronized void login(String usuario, Socket cliente) throws IOException {

        for (String user : lista_usuarios.keySet()) {
            if (user.equalsIgnoreCase(usuario)) {
                saida.println("login:false");
                return;
            }
        }
        saida.println("login:true");
        new Conexao(cliente);
        sendToAll(atualizarListaUsuarios(usuario,cliente));
    }

    public static String atualizarListaUsuarios(String usuario, Socket endereco) {
        lista_usuarios.put(usuario, endereco);
        
        String usuarios = "lista_usuarios:";

        for (String user : lista_usuarios.keySet()) {
            
            usuarios += user + ";";
        }
        return usuarios.substring(0, usuarios.length() - 1);
    }
    
    public static void sendToAll(String msg) throws IOException{
        for (Socket user : lista_usuarios.values()) {
            saida = new PrintStream(user.getOutputStream());
            saida.println(msg);
        }
    }
}
