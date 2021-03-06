package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

import Client.Paquete;

public class UDP extends Thread{
    private int puertoServidor;
    private ArrayList<Paquete> paquetes = new ArrayList<Paquete>();
    private Server server;
    DatagramSocket serverSocket;

    public UDP(int puertoServidor, Server s){
        this.server=s;
        this.puertoServidor = puertoServidor;
    }

    public void run(){
        try {
            // 1) Creamos el socket Servidor de Datagramas (UDP)
        	serverSocket = new DatagramSocket(puertoServidor);
            System.out.println("Servidor Sistemas Distribuidos - UDP ");

            // 2) buffer de datos a enviar y recibir
            byte[] receiveData = new byte[1024];

            // 3) Servidor siempre esperando
            while (true) {

                receiveData = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                System.out.println("Esperando a algun cliente... ");

                // 4) Receive LLAMADA BLOQUEANTE
                serverSocket.receive(receivePacket);

                System.out.println("________________________________________________");
                System.out.println("Aceptamos un paquete");

                // Datos recibidos e Identificamos quien nos envio
                String datoRecibido = new String(receivePacket.getData());
                datoRecibido = datoRecibido.trim();
                System.out.println("DatoRecibido: " + datoRecibido);

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                // Convertimos el string recibido a un obj Paquete
                // y luego anhadimos al array
                Paquete p = Paquete.JSONstrToObj(datoRecibido);
                paquetes.add(p);

                // Creamos el objeto cliente con los datos del paquete
                Cliente cliente = new Cliente(IPAddress, port, p.getMensaje());

                System.out.println("De : " + IPAddress + ":" + port);

                System.out.println("Recibo: " + datoRecibido);
                System.out.println("Operacion: " + p.getTipo_operacion());

                if (p.getTipo_operacion() == 1) {
                    ArrayList<String> usuarios = new ArrayList<>();
                    this.server.writer = new FileWriter("log.txt", true);
                    this.server.writer.write(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()).toString()+": "+"El cliente con direccion: "+cliente.getdireccion()+":"+cliente.getPort()+" solicito ver los usuarios online\n");
                    this.server.writer.close();
                    for (Cliente c : server.clientesEnLinea) {
                        usuarios.add(c.getUsername());
                    }
                    Paquete respuesta = new Paquete(0, usuarios, 0);
                    enviarPaquete(serverSocket, cliente, respuesta);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void enviarPaquete(DatagramSocket serverSocket, Cliente cliente, Paquete respuesta) {
        byte[] sendData = new byte[1024];
        sendData = respuesta.JSONToString().getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, cliente.getIPAddress(),
                cliente.getPort());
        try {
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
