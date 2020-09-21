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
        Paquete p = new Paquete(0, username, 0, null);
        out.println(p.JSONToString());
    }

	public void realizarLlamada(String destino, String origen) {
        Paquete paquete = new Paquete(0, destino, 2, origen);
        out.println(paquete.JSONToString());
        out.flush();
    }
	
	public void terminar(String nick_usuario) {
        Paquete paquete = new Paquete(0, nick_usuario, 5, null);
        out.println(paquete.JSONToString());
        out.flush();
	}


	public void recibirLlamada(String llamante) {
        this.realizarLlamada(llamante, null);
	}

	public String escuchar() {
        try {
            String temp = in.readLine();
            System.out.println(temp);
            Paquete p = Paquete.JSONstrToObj(temp);
            if (p.getTipo_operacion()==4) {
                return p.getSender()+": "+p.getMensaje();
            }else if(p.getTipo_operacion()==6) {
            	this.recibirLlamada(p.getSender());
            	return "codellamada";
            }else if(p.getTipo_operacion()==5) {
            	return "codeterminar";
            }else if(p.getTipo_operacion()==7) {
            	return "codeocupado";
            }
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
	}

	public void enviar(String text, String sender) {
        Paquete paquete = new Paquete(0,text,3, sender);
        out.println(paquete.JSONToString());
	}
}
