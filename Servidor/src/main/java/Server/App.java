package Server;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App {

    private static ArrayList<Cliente> clientes = new ArrayList<Cliente>();

    public static void main(String[] args) {
        // Variables
        
        int puertoServidor = 9876;
        UDP udp = new UDP(puertoServidor, clientes);
        udp.start();
        TCP tms = new TCP(clientes);
        tms.ejecutar();

    }

}
