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
                }
            }

            // String inputLine, outputLine;

            // while ((inputLine = in.readLine()) != null) {
            // System.out.println("Mensaje recibido: " + inputLine);

            // //out.println(inputLine);

            // //to-do: utilizar json
            // if (inputLine.equals("Bye")) {
            // outputLine = "Usted apago el hilo";
            // break;

            // }else if (inputLine.equals("Terminar todo")) {
            // servidor.listening = false;
            // outputLine = "Usted apago todo";
            // break;

            // }else if (inputLine.split(":").length > 1) {
            // String usuario = inputLine.split(":")[1];
            // servidor.clientesEnLinea.add(usuario);
            // outputLine = "Usuario/a "+usuario+"agregado";

            // }else {
            // outputLine = "Lista de usuarios: " ;

            // Iterator<String> iter = servidor.usuarios.iterator();

            // while (iter.hasNext()) {
            // outputLine = outputLine + " - " + iter.next();
            // }
            // }

            // out.println(outputLine);
            // }
            out.close();
            in.close();
            socket.close();
            System.out.println("Finalizando Hilo");

        } catch (IOException e) {
            e.printStackTrace();
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
            // System.out.println("puerto this"+this.socket.getPort());
            // System.out.println("puerto hiloCliente2 "+ servidor.hilosClientes.get(posHiloCliente2));
            // System.out.println("puerto hiloCliente2 "+ hiloCliente2);
            System.out.println("Antes del while");
            enLlamada=true;
            while (enLlamada) {
                System.out.println("Al entrar al while");
                String entrada = this.in.readLine();
                Paquete pentrada = Paquete.JSONstrToObj(entrada);
                if (pentrada.getTipo_operacion()==3) {
                    Paquete p = new Paquete(0, pentrada.getMensaje(), 4);
                    servidor.hilosClientes.get(posHiloCliente2).out.println(p.JSONToString());
                }
                System.out.println("Al salir del while");
            }
            // System.out.println("despues del enviar la salida al fer");
            // System.out.println("despues del enviar la salida al otro");

            // PrintWriter out2 = new PrintWriter(socketDelCliente2.getOutputStream(), true);
            // BufferedReader in2 = new BufferedReader(new InputStreamReader(socketDelCliente2.getInputStream()));
            // PrintWriter out1 = new PrintWriter(socket.getOutputStream(), true);
            // BufferedReader in1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // String inputLine1, inputLine2=null;
            // while ((inputLine1 = in.readLine()) != null || (inputLine2 = in2.readLine()) != null ) {
            //     System.out.println(inputLine1);
            //     out1.println(inputLine1);
            //     out2.println(inputLine2);
            // }
            // System.out.println("Dejo de enviar datos");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
