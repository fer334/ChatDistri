package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.simple.parser.ParseException;

public class UDP {

    String direccionServidor ;
    int puertoServidor ;

    public UDP(String direccionServidor, int puertoServidor) {
        this.direccionServidor = direccionServidor;
        this.puertoServidor = puertoServidor;
    }

    // Metodo que envia un paquete y recibe una respuesta del servidor
    public Paquete enviarPaquete(Paquete peticion) throws ParseException {
        Paquete respuesta=null;
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName(direccionServidor);
            System.out.println("Intentando conectar a = " + IPAddress + ":" + puertoServidor + " via UDP...");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            // Creamos un paquete para conectarse al servidor
            
            sendData = peticion.JSONToString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puertoServidor);

            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            System.out.println("Esperamos si viene la respuesta.");

            // Vamos a hacer una llamada BLOQUEANTE entonces establecemos un timeout maximo
            // de espera
            clientSocket.setSoTimeout(10000);

            try {
                // ESPERAMOS LA RESPUESTA, BLOQUENTE
                clientSocket.receive(receivePacket);

                String r = new String(receivePacket.getData());
                respuesta = Paquete.JSONstrToObj(r.trim());

                InetAddress returnIPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                System.out.println("Respuesta desde =  " + returnIPAddress + ":" + port);
                // System.out.println("Asignaturas: ");

            } catch (SocketTimeoutException ste) {

                System.err.println("TimeOut: El paquete udp se asume perdido.");
                
            }
            clientSocket.close();
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return respuesta;
    }
}