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

    TCP servidor;

    public TCPServerHilo(Socket socket, TCP servidor) {
        super("TCPServerHilo");
        this.socket = socket;
        this.servidor = servidor;
    }

    public void run() {

        try {
            SocketAddress addr = socket.getRemoteSocketAddress();
            int port = socket.getPort();
            System.out.println("La dir del cliente es: " + addr.toString());
            System.out.println("Su puerto es: " + port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("Bienvenido!");
            out.println("Ingrese un nombre de usuario");
            String inputLine = in.readLine();
            Cliente e = new Cliente(addr, port, inputLine);
            this.cliente = e;
            servidor.clientesEnLinea.add(e);
            out.println("Cliente agregado");
            System.out.println("Cliente agregado");

            while ((inputLine = in.readLine()) != null) {
                System.out.println("esperando tcp");
                Paquete paquete = Paquete.JSONstrToObj(inputLine);

                // Vemos que codigo de operacion tiene
                if (paquete.getTipo_operacion() == 2) {
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
        TCPServerHilo hiloCliente2 = null;
        int posHiloCliente2 = 0;
        for (int i = 0; i < servidor.hilosClientes.size(); i++) {
            if (servidor.hilosClientes.get(i).cliente.getUsername().equals(usuario)) {
                try {
                    hiloCliente2 = (TCPServerHilo) servidor.hilosClientes.get(posHiloCliente2).clone();
                } catch (CloneNotSupportedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                posHiloCliente2 = i;
                break;
            }
        }

        try {
            // System.out.println("puerto this"+this.socket.getPort());
            // System.out.println("puerto hiloCliente2 "+ servidor.hilosClientes.get(posHiloCliente2));
            // System.out.println("puerto hiloCliente2 "+ hiloCliente2);
            System.out.println("Antes del while");
            while (true) {
                String entrada = servidor.hilosClientes.get(posHiloCliente2).in.readLine();
                this.out.println(entrada);
                servidor.hilosClientes.get(posHiloCliente2).out.println(entrada);
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
