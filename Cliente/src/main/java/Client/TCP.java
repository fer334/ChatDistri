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

    public TCP(String dir, int port){
        try {
            unSocket = new Socket(dir, port);
            out = new PrintWriter(unSocket.getOutputStream(), true);

            // viene del servidor
            in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // enviamos nosotros
    }

    void conectarse(String username) {

        // escribimos al servidor nuestro nombre de usuario
        Paquete p = new Paquete(0, username, 0);
        out.println(p.JSONToString());
    }

	public void realizarLlamada(String usuario) {
        Paquete paquete = new Paquete(0, usuario, 2);
        out.println(paquete.JSONToString());
        out.flush();
    }
	
	public void terminar(String nick_usuario) {
        Paquete paquete = new Paquete(0, nick_usuario, 5);
        out.println(paquete.JSONToString());
        out.flush();
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
            Paquete p = Paquete.JSONstrToObj(in.readLine());
            if (p.getTipo_operacion()==4) {
                return p.getMensaje();
            }
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
	}

	public void enviar(String text) {
        Paquete paquete = new Paquete(0,text,3);
        out.println(paquete.JSONToString());
	}
}
