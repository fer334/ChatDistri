package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class UDP {

    String direccionServidor;
    int puertoServidor;
    DatagramSocket clientSocket;

    public UDP(String direccionServidor, int puertoServidor) {
        this.direccionServidor = direccionServidor;
        this.puertoServidor = puertoServidor;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Metodo que envia un paquete y recibe una respuesta del servidor
    public Paquete enviarPaquete(Paquete peticion) {
        Paquete respuesta=null;
        try {
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
            //clientSocket.close();
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return respuesta;
    }
    
    public ArrayList<String> getClientesOnline() {
    	Paquete p = new Paquete(0, " ", 1, null);
    	Paquete r = this.enviarPaquete(p);
    	return r.getOtro();
    	
    }
    

	public void terminar(String nick_usuario) {
        Paquete paquete = new Paquete(0, nick_usuario, 5, null);
        enviarPaquete(paquete);
	}
}
