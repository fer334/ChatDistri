package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import Client.UDP;
import org.json.simple.parser.ParseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException
    {
        String direccionServidor = "127.0.0.1";

        
        // // Envio paquete de primera conexion
        // respuesta = udp.enviarPaquete(new Paquete(0,"Fer2",0));
        // System.out.println(respuesta.JSONToString());
        
        // // Si al conectarse no hubo problemas, enviamos otro paquete
        // if(respuesta.getEstado()==0){
            // Pido la lista de todos los usuarios conectados
            // }
            
            
        TCP tcp = new TCP();
        tcp.conectarse();
            
        int puertoServidor = 9876;
        Paquete respuesta;
        
        UDP udp = new UDP(direccionServidor,puertoServidor);
        respuesta=udp.enviarPaquete(new Paquete(0,"",1));
        System.out.println(respuesta.JSONToString());
        
        // TODO menu para elegir con quien conectarse

        //Realizar llamada
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("llamar a fer?");
            if (stdIn.readLine().equals("s")) {
                System.out.println("llamando a fer");
                tcp.realizarLlamada("Fer");

            }else{
                System.out.println("Recibo llamada");
                tcp.realizarLlamada("s");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
