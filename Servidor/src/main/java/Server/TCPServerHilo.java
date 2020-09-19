package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;

import Client.Paquete;


public class TCPServerHilo extends Thread {

    public Socket socket = null;
    PrintWriter out;
    BufferedReader in;
    Cliente cliente;

    Server servidor;
    public boolean enLlamada;

    public TCPServerHilo(Socket socket, Server servidor) {
        super("TCPServerHilo");
        this.socket = socket;
        this.servidor = servidor;
    }

    public void run() {
        try {
            SocketAddress addr = socket.getRemoteSocketAddress();
            int port = socket.getPort();
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("esperando tcp");
                Paquete paquete = Paquete.JSONstrToObj(inputLine);

                // Vemos que codigo de operacion tiene
                if (paquete.getTipo_operacion()==0) {
                	Cliente e = new Cliente(addr, port, paquete.getMensaje());
                    this.cliente = e;
                    servidor.clientesEnLinea.add(e);
                }else if (paquete.getTipo_operacion() == 2) {
                    System.out.println("paquete recibido llamando a 2");
                    llamarA(paquete.getMensaje());
                }else if(paquete.getTipo_operacion()==5) {
                	System.out.println("Terminando llamada");
                    this.enLlamada = false;
                }
            }

            out.close();
            in.close();
            for(int i=0; i< servidor.clientesEnLinea.size();i++) {
            	System.out.println(servidor.clientesEnLinea.get(i).getPort() == socket.getPort());
            	if(servidor.clientesEnLinea.get(i).getPort() == socket.getPort()) {
            		System.out.println("Cliente "+servidor.clientesEnLinea.get(i).getUsername()+" fuera");
            		servidor.clientesEnLinea.remove(i);
            		break;
            	}
            }
            
            socket.close();
            
            
           
            
            System.out.println("Finalizando Hilo");
           
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void llamarA(String usuario) {
        int posHiloCliente2 = 0;
        for (int i = 0; i < servidor.hilosClientes.size(); i++) {
            if (servidor.hilosClientes.get(i).cliente.getUsername().equals(usuario)) {
                posHiloCliente2 = i;
                break;
            }
        }

        try {
            System.out.println("Antes del while");
            enLlamada=true;
            while (enLlamada) {
                String entrada = this.in.readLine();
                Paquete pentrada = Paquete.JSONstrToObj(entrada);
                if (pentrada.getTipo_operacion()==3) {
                    Paquete p = new Paquete(0, pentrada.getMensaje(), 4);
                    servidor.hilosClientes.get(posHiloCliente2).out.println(p.JSONToString());
                    servidor.hilosClientes.get(posHiloCliente2).out.flush();
                }
            }
            System.out.println("While de llamada finalizado");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
