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
            
            boolean flag = entrada.hasNextLine();

            while (flag) {
                String msg = entrada.nextLine();
                String[] protocolo = msg.split(":");
                System.out.println("Servidor: " + msg);
                if (protocolo[0].equalsIgnoreCase("login")) {
                    login(protocolo[1], cliente);
                    flag = false;
                } else {
                    saida.println("Protocolo não existe!");
                }
            }
        }
    }

    public static synchronized void login(String usuario, Socket cliente) throws IOException {

        for (String user : lista_usuarios.keySet()) {
            if (user.equalsIgnoreCase(usuario)) {
                saida.println("login:false");
            }
        }
        saida.println("login:true");
        new Conexao(cliente);
        atualizarListaUsuarios(usuario, cliente, false);
    }

    public static void atualizarListaUsuarios(String usuario, Socket endereco, boolean remover) throws IOException {
        if (remover) {
            lista_usuarios.remove(usuario);
            System.out.println(lista_usuarios);
        } else {
            lista_usuarios.put(usuario, endereco);
        }
        String usuarios = "lista_usuarios:";

        for (String user : lista_usuarios.keySet()) {
            usuarios += user + ";";
        }

        sendToAll(usuarios.substring(0, usuarios.length() - 1), true);
    }

    public void recebeMensagem(Socket cliente, String mensa) throws IOException {
        lista_usuarios.keySet().stream().filter((remet) -> (lista_usuarios.get(remet) == cliente)).forEachOrdered((remet) -> {
            remetente = remet; // Remetente é a variável global lá em cima!
        });

        if (mensa.equalsIgnoreCase("sair")) {
            sendToOne(cliente, cliente, "Desconectado");
            atualizarListaUsuarios(remetente, cliente, true);
            cliente.close();
        } else {
            String[] protocolo = mensa.split(":");
            if (protocolo[0].equalsIgnoreCase("mensagem")) {
                if (protocolo[1].contains(";")) {
                    String[] destinos = protocolo[1].split(";");
                    sendToMany(destinos, protocolo[1], protocolo[2]);
                } else if (protocolo[1].equals("*")) {
                    sendToAll(protocolo[2], false);
                } else {
                    if (lista_usuarios.get(protocolo[1]) != null) {
                        sendToOne(cliente, lista_usuarios.get(protocolo[1]), protocolo[2]);
                    } else {
                        saida = new PrintStream(cliente.getOutputStream());
                        saida.println("Destinatário não está conectado!");
                    }
                }
            }
        }
    }

    public static void sendToOne(Socket remetent, Socket destino, String msg) throws IOException {
        saida = new PrintStream(destino.getOutputStream());

        lista_usuarios.keySet().stream().filter((dest) -> (lista_usuarios.get(dest) == destino)).forEachOrdered((dest) -> {

            destinatario = dest;

            if (msg.equalsIgnoreCase("desconectado")) {
                saida.println(msg);
                return;
            }

            saida.println("transmitir:" + remetente + ":" + destinatario + ":" + msg);
        });

    }

    public static void sendToAll(String msg, Boolean servidor) throws IOException {
        for (Socket user : lista_usuarios.values()) {
            saida = new PrintStream(user.getOutputStream());
            if (servidor) {
                saida.println(msg);
            } else {
                saida.println("transmitir:" + remetente + ":*:" + msg);
            }

        }
    }

    public static void sendToMany(String destinos[], String destinosMsg, String msg) {
        for (String destino : destinos) {
            Socket desti = lista_usuarios.get(destino);
            if (desti != null) {
                lista_usuarios.keySet().stream().filter((dest) -> (lista_usuarios.get(dest) == desti)).forEachOrdered((dest) -> {

                    destinatario = destinosMsg;

                    try {
                        saida = new PrintStream(desti.getOutputStream());
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    saida.println("transmitir:" + remetente + ":" + destinatario + ":" + msg);
                });
            }
        }
    }

}
