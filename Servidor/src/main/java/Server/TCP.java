package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TCP {
    boolean listening= true;
    ArrayList<Cliente> clientesEnLinea;
    ArrayList<TCPServerHilo> hilosClientes = new ArrayList<>();


    public TCP(ArrayList<Cliente> clientes) {
        this.clientesEnLinea = clientes;
	}


	public void ejecutar() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Esperando cliente tcp");
            while (listening) {
    
                TCPServerHilo hilo = new TCPServerHilo(serverSocket.accept(), this);
                hilosClientes.add(hilo);
                hilo.start();
            }
    
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto: 4444.");
            System.exit(1);
        }
        System.out.println("Puerto abierto: 4444.");

    }

}