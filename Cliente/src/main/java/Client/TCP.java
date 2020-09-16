package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCP {
    Socket unSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    public TCP(){
        try {
            unSocket = new Socket("127.0.0.1", 4444);
            out = new PrintWriter(unSocket.getOutputStream(), true);
    
            // viene del servidor
            in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // enviamos nosotros
    }
    
    void conectarse() {


        try {
            

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(in.readLine());
            System.out.println(in.readLine());
            // escribimos al servidor nuestro nombre de usuario
            out.println(stdIn.readLine());
            System.out.println(in.readLine());
            

            // while ((fromServer = in.readLine()) != null) {
            // System.out.println("Servidor: " + fromServer);
            // if (fromServer.equals("Bye")) {
            // break;
            // }

            // fromUser = stdIn.readLine();
            // if (fromUser != null) {
            // System.out.println("Cliente: " + fromUser);

            // // escribimos al servidor
            // out.println(fromUser);
            // }
            // }

            // out.close();
            // in.close();
            // stdIn.close();
            // unSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        }

    }

	public void realizarLlamada(String usuario) {

        String fromServer;
        String fromUser;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        Paquete paquete = new Paquete(0, usuario, 2);
        out.println(paquete.JSONToString());
    }
    

	public void recibirLlamada() {
        String fromServer;
        String fromUser;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        try {
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Servidor: " + fromServer);
                if (fromServer.equals("Bye")) {
                    break;
                }

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Cliente: " + fromUser);

                    // escribimos al servidor
                    out.println(fromUser);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

	public String escuchar() {
        try {
            return in.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
	}

	public void enviar(String text) {
        out.println(text);
	}
}