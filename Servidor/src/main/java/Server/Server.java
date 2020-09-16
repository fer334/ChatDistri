package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    boolean listening= true;
    ArrayList<Cliente> clientesEnLinea = new ArrayList<>();
    ArrayList<TCPServerHilo> hilosClientes = new ArrayList<>();
    private int puertoServidor;


    public Server(int puertoServidor) {
        this.puertoServidor =puertoServidor;
	}


	public void ejecutar() {


        UDP udp = new UDP(puertoServidor, this);
        udp.start();

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(puertoServidor);
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