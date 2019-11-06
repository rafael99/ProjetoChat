package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    static String remetente;
    static String destinatario;

    static Scanner entrada;
    static PrintStream saida;
    static Conexao cnx;

    static Map<String, Socket> lista_usuarios = new HashMap<String, Socket>();

    public static void main(String[] args) throws UnknownHostException, IOException {
        ServerSocket servidor = new ServerSocket(2424);
        System.out.println("Porta 2424 aberta! Aguardando conexão!");

        while (true) {
            Socket cliente = servidor.accept();

            try {
                entrada = new Scanner(cliente.getInputStream());
                saida = new PrintStream(cliente.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String msg = entrada.nextLine();
            String[] protocolo = msg.split(":");
            System.out.println("Servidor: " + msg);
            if (protocolo[0].equalsIgnoreCase("login")) {
                login(protocolo[1], cliente);
            } else {
                saida.println("Protocolo não existe!");
            }
            
        }
    }

    public static synchronized boolean login(String usuario, Socket cliente) throws IOException {

        for (String user : lista_usuarios.keySet()) {
            if (user.equalsIgnoreCase(usuario)) {
                saida.println("login:false");
                return false;
            }
        }
        saida.println("login:true");
        new Conexao(cliente);
        sendToAll(atualizarListaUsuarios(usuario, cliente));
        return true;
    }

//    public static Thread conexao(Socket cliente) {
//        return new Thread() {
//            @Override
//            public void run() {
//
//                try {
//                    entrada = new Scanner(cliente.getInputStream());
//                    saida = new PrintStream(cliente.getOutputStream());
//                } catch (IOException ex) {
//                    // Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                Scanner teclado = new Scanner(System.in);
//
//                while (entrada.hasNextLine()) {
//                    String msg = entrada.nextLine();
//
//                    System.out.println("O cliente digitou: " + msg);
//                }
//            }
//        };
//    }
    public static String atualizarListaUsuarios(String usuario, Socket endereco) {
        lista_usuarios.put(usuario, endereco);

        String usuarios = "lista_usuarios:";

        for (String user : lista_usuarios.keySet()) {
            usuarios += user + ";";
        }
        return usuarios.substring(0, usuarios.length() - 1);
    }

    public void recebeMensagem(Socket cliente, String mensa) throws IOException {
        if (mensa.equalsIgnoreCase("sair")) {
            cliente.close();
        } else {
            String[] protocolo = mensa.split(":");
            if (protocolo[0].equalsIgnoreCase("mensagem")) {
                if (protocolo[1].contains(";")) {
                    System.out.println("SEND TO MANY");

                } else if(protocolo[1].equalsIgnoreCase("*")) {
                    sendToAll("transmitir:*:"+protocolo[2]);
                } else {
                    if (lista_usuarios.get(protocolo[1]) != null) {
                        sendToOne(cliente, lista_usuarios.get(protocolo[1]), protocolo[2]);
                    } else {
                        saida.println("Destinatário não existe!");
                    }
                }
            }
        }
    }

    public static void sendToOne(Socket remetente, Socket destinatario, String msg) throws IOException {
        saida = new PrintStream(destinatario.getOutputStream());
        lista_usuarios.keySet().stream().filter((remet) -> (lista_usuarios.get(remet) == remetente)).forEachOrdered((remet) -> {
            String r = remet;

            lista_usuarios.keySet().stream().filter((dest) -> (lista_usuarios.get(dest) == destinatario)).forEachOrdered((dest) -> {

                saida.println("transmitir:" + r + ":" + dest + ":" + msg);
            });

        });
    }
    
    public static void sendToMany(String clientes, String msg){
        String[] remententes = clientes.split(";");
        
        
    }

    public static void sendToAll(String msg) throws IOException {
        for (Socket user : lista_usuarios.values()) {
            saida = new PrintStream(user.getOutputStream());
            saida.println(msg);
        }
    }

}
