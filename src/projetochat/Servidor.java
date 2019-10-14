package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor {

    static ArrayList<String> lista_usuarios = new ArrayList<String>();
    ;
    static String remetente;
    static String destinatario;

    public static void main(String[] args) throws UnknownHostException, IOException {
        ServerSocket servidor = new ServerSocket(2424);

        System.out.println("Porta 2424 aberta! Aguardando conexão!");

        while (true) {

            Socket cliente = servidor.accept();
            Scanner usuario = new Scanner(cliente.getInputStream());
            
            // Saídas para o cliente
            PrintStream saida = new PrintStream(cliente.getOutputStream());
            PrintStream saidaListaUsuarios = new PrintStream(cliente.getOutputStream());
            
            // Pegando a ação do usuario
            String[] protocolo = usuario.nextLine().split(":");
            
            switch (protocolo[0]) {
                case "login":
                    if (login(protocolo[1])) {
                        Conexao cnx = new Conexao(cliente);
                        cnx.start();
                        System.out.println("Usuário "+ protocolo[1] + " conectado!");
                        saida.println("Você está conectado!");
                    } else {
                        System.out.println("Nome já existente!");
                    }
                    break;
                case "lista_usuarios":
                    listarUsuarios();
                    break;
                case "mensagem":
                    break;
                case "transmitir":
                    break;
                default:
                    System.out.println("Protocolo " + protocolo[0] + " não existe!");
                    break;
            }
        }

    }

    public static boolean login(String usuario) {
        for (String user : lista_usuarios) {
            if (user == usuario) {
                return false;
            }
        }
        lista_usuarios.add(usuario);
        return true;
    }
    
    public static String listarUsuarios() {
        String usuarios = null;
        for (String user : lista_usuarios) {
            usuarios += user + ";";
        }
        return usuarios;
    }
}
