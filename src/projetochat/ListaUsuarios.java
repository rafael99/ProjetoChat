package projetochat;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ListaUsuarios {
    public final Map<String, Socket> lista_usuarios = new HashMap<String, Socket>();

    public ListaUsuarios() {
    }
    
    public void adicionarUsuario(String nome, Socket usuario){
        System.out.println("antes:"+lista_usuarios.keySet());
        lista_usuarios.put(nome, usuario);
        System.out.println("dps:"+lista_usuarios.keySet());
//        System.out.println(lista_usuarios.keySet());
//        System.out.println(lista_usuarios);
    }
    
    public String exibirUsuarios(){
        String usuarios = "lista_usuarios:";       

        for (String user : lista_usuarios.keySet()) {
            usuarios += user + ";";
        }
        String lista = usuarios.substring(0, usuarios.length() - 1);
        return lista;
    }    
    
}
