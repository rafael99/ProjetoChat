package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author Marcelo
 */
public class Transmitir extends Thread {

    static ListaUsuarios lu = new ListaUsuarios();

    public Transmitir() {
        start();
    }

    public void sendToOne(Socket remetente, Socket destinatario, String msg) {

    }

    public void sendToAll() {

    }

    @Override
    public void run() {
        try {
            for (Socket usuario : lu.lista_usuarios.values()) {
        System.out.println("Buuuu");
                PrintStream saida = new PrintStream(usuario.getOutputStream());
                saida.println(lu.exibirUsuarios());
            }
        } catch (IOException io) {
            System.out.println("msg erro: " + io.getMessage());
        }

    }
}
