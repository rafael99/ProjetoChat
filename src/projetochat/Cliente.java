package projetochat;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    String usuario;
    private String mensagem;
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket cliente = new Socket("10.10.111.44", 2424);
        
        System.out.println("O cliente se conectou ao servidor!");
    }
}
