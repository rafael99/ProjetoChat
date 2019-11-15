package projetochat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket cliente = new Socket("127.0.0.1", 2424);

        System.out.println("Realize login...");

        new Thread() {
            @Override
            public void run() {
                try {
                    Scanner entrada = new Scanner(cliente.getInputStream());
                    while (true) {
                        while(entrada.hasNextLine()) {
                            String mensagem = entrada.nextLine();
                            if (mensagem == null || mensagem.isEmpty()) {
                                continue;
                            }
                            
                            System.out.println(mensagem);
                        }                        
                    }

                } catch (IOException e) {
                    System.out.println("impossivel ler a mensagem do servidor");
                    e.printStackTrace();
                }
            }
        }.start();

        Scanner teclado = new Scanner(System.in);
        PrintStream saida = new PrintStream(cliente.getOutputStream());

        while (teclado.hasNextLine()) {            
            saida.println(teclado.nextLine());
        }

        saida.close();
        teclado.close();
    }
}
