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
                    System.out.println("paquete recibido llamando a "+paquete.getMensaje());
                    llamarA(paquete.getMensaje(), paquete.getSender());
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

    private void llamarA(String destino, String origen) {
        int posHiloCliente2 = 0;
        for (int i = 0; i < servidor.hilosClientes.size(); i++) {
            if (servidor.hilosClientes.get(i).cliente.getUsername().equals(destino)) {
                posHiloCliente2 = i;
                break;
            }
        }

        try {
            System.out.println("Antes del while");
            enLlamada=true;
            Paquete p;
            if(origen != null) {
            	p = new Paquete(0, "", 6, origen);
                servidor.hilosClientes.get(posHiloCliente2).out.println(p.JSONToString());
                servidor.hilosClientes.get(posHiloCliente2).out.flush();
            }
            while (enLlamada) {
                System.out.println("Al entrar al while");
                String entrada = this.in.readLine();
                Paquete pentrada = Paquete.JSONstrToObj(entrada);
                if (enLlamada==true && pentrada.getTipo_operacion()==3) {
                    p = new Paquete(0, pentrada.getMensaje(), 4, pentrada.getSender());
                    servidor.hilosClientes.get(posHiloCliente2).out.println(p.JSONToString());
                    servidor.hilosClientes.get(posHiloCliente2).out.flush();
                }else if(pentrada.getTipo_operacion()==5) {
                    System.out.println("Terminando llamada");
                    this.enLlamada = false;
                    p = new Paquete(0, pentrada.getMensaje(), 5, null);
                    servidor.hilosClientes.get(posHiloCliente2).out.println(p.JSONToString());
                    servidor.hilosClientes.get(posHiloCliente2).out.flush();
                    servidor.hilosClientes.get(posHiloCliente2).enLlamada = false;
                }
                System.out.println("Al salir del while");
            }
            System.out.println("While de llamada finalizado");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
