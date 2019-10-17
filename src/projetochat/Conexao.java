package projetochat;

import com.sun.istack.internal.logging.Logger;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import sun.util.logging.PlatformLogger;

public class Conexao extends Thread {

    Socket cliente;
    static Scanner entrada;
    static PrintStream saida;
    static ArrayList<String> lista_usuarios = new ArrayList<String>();

    Conexao(Socket conexao) {
        this.cliente = conexao;
        start();
    }

    @Override
    public void run() {
        int aux = lista_usuarios.size();

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
                case "login":
                    if (login(protocolo[1])) {
                        saida.println("O nome de usuário foi registrado com sucesso");

                    } else {
                        saida.println("O nome do usuário não pôde ser "
                                + "registrado pois é inválido ou já está em uso");
                    }
                    break;
                case "lista_usuarios":
                    break;
                case "mensagem":
                    break;
                case "transmitir":
                    break;
                default:
                    System.out.println("Protocolo " + protocolo[0] + " não existe!");
                    break;
            }

            System.out.println("O cliente digitou: " + msg);
            if (lista_usuarios.size() > aux) {
                listarUsuarios();
            }
        }

        
    }

    public synchronized boolean login(String usuario) {
        for (String user : lista_usuarios) {
            if (user.equals(usuario)) {
                return false;
            }
        }
        lista_usuarios.add(usuario);
        return true;
    }

    public static void listarUsuarios() {
        String usuarios = "lista_usuarios:";
        for (String user : lista_usuarios) {
            usuarios += user + ";";
        }
        saida.println(usuarios);
    }
}
