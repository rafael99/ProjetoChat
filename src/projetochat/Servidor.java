package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Servidor {

    static String remetente;
    static String destinatario;

    static Scanner entrada;
    static PrintStream saida;

//    static ArrayList<String> lista_usuarios = new ArrayList<String>();
    static final Map<String,Socket>lista_usuarios = new HashMap<String,Socket>();

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

        for (String user : lista_usuarios.keySet()) {
            if (user.equalsIgnoreCase(usuario)) {
                saida.println("login:false");
                return;
            }
        }
        saida.println("login:true");
        new Conexao(cliente);
        atualizarListaUsuarios(usuario, cliente);
    }

    public static synchronized String atualizarListaUsuarios(String usuario, Socket cliente) {
        String usuarios = "lista_usuarios:";
        lista_usuarios.put(usuario, cliente);

        for (String user : lista_usuarios.keySet()) {
            usuarios += user + ";";
        }
        String lista = usuarios.substring(0, usuarios.length() - 1);
        enviarMensagem(cliente, cliente, lista);
        return lista;
    }

    // Envia a msg para thread responsável por transmitir
    public static void enviarMensagem(Socket remetente, Socket destinatario, String msg) {
        // Testar se o usuário destinatario existe
        for (String user : lista_usuarios.keySet()) {
            // Obs: não sei se o destinatario.toString() vai retornar o nome do usuário destinatário, isso é somente um exemplo!
            if (user.equalsIgnoreCase(destinatario.toString())) {
                // Se sim:
                Mensagem m = new Mensagem(remetente, destinatario, msg);
                return;
            }
        }        
    }

}
