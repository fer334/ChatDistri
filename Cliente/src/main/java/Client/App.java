package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.*;

import Client.UDP;
import org.json.simple.parser.ParseException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws ParseException {
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

        // int puertoServidor = 9876;
        // Paquete respuesta;

        // UDP udp = new UDP(direccionServidor,puertoServidor);
        // respuesta=udp.enviarPaquete(new Paquete(0,"",1));
        // System.out.println(respuesta.JSONToString());

        // // TODO menu para elegir con quien conectarse

        // //Realizar llamada
        // BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        // try {
        // System.out.println("llamar a fer?");
        // if (stdIn.readLine().equals("s")) {
        // System.out.println("llamando a fer");
        // tcp.realizarLlamada("Fer");

        // }else{
        // System.out.println("Recibo llamada");
        // tcp.realizarLlamada("s");
        // }
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        MarcoCliente mimarco = new MarcoCliente(tcp);

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoCliente extends JFrame {

    TCP tcp;
    public MarcoCliente(TCP tcp) {
        this.tcp=tcp;
        setBounds(600, 300, 280, 350);

        LaminaMarcoCliente milamina = new LaminaMarcoCliente(tcp);

        add(milamina);

        setVisible(true);

        // addWindowListener( new EnvioOnline());// se dispara el evento de ventana cuando un cliente se
                                                              // conecta a la
                                             // aplicacion y envia unos datos al servidor automaticamente

    }

}

// //evento de ventana para enviar al servidor el estado activo de un cliente
// class EnvioOnline extends WindowAdapter {
	
// 	public void windowOpened(WindowEvent e) {
		
// 		try {
			
// 			Socket misocket = new Socket("192.168.100.77", 9999);
			
// 			PaqueteEnvio datos = new PaqueteEnvio();
			
// 			datos.setMensaje("online");
			
// 			ObjectOutputStream paquete_datos_inicial = new ObjectOutputStream(misocket.getOutputStream());
			
// 			paquete_datos_inicial.writeObject(datos);
			
// 			misocket.close();
						
// 		}catch (Exception ex) {
// 			System.out.println(ex.getMessage());
// 		}
		
// 	}
	
// }

class LaminaMarcoCliente extends JPanel implements Runnable{//interfaz

    private JTextField campo1;//campo donde se escribe el texto a enviar
    private JLabel nick; //campo donde se visualizará el nick name del usuario
    private JComboBox ip; //campo donde se visualizará la direccion ip del cliente con el que se está llevando a cabo la conversacion
    private JTextArea campochat;//area de chat
    private JButton miboton; //boton para enviar mensajes
    TCP tcp;

    public LaminaMarcoCliente(TCP tcp ) {
    	this.tcp= tcp;
    	String nick_usuario = JOptionPane.showInputDialog("Ingrese su Ni:");
    	
    	JLabel n_nick = new JLabel("Nick:");
    	add(n_nick);

        nick = new JLabel();
        nick.setText(nick_usuario);
        add(nick);

        JLabel texto = new JLabel("Online:");
        add(texto);

        ip = new JComboBox();//configuramos el cuadro de texto para que aparezca a la izquierda 
        ip.addItem("Usuario1");
        ip.addItem("Usuario2");
        ip.addItem("Usuario3");
        //ip.addItem("192.168.100.46");//ip de la vm Xubuntu
        //ip.addItem("");//ip de la vm Ubuntu Mate
        
        add(ip); //agregamos el cuadro de texto a la lamina(interfaz)

        campochat = new JTextArea(12, 20);//lugar de colocacion del area de texto, las coordenadas son 12 y 20
        add(campochat);//se agrega el campo de texto a la lamina(interfaz)

        campo1 = new JTextField(20); //area donde se escribirá el mensaje a enviar
        add(campo1); //se agrega el campo de texto a la lamina(interfaz)

        miboton = new JButton("Enviar"); //boton para enviar el texto escrito
        JButton ferButton = new JButton("fer"); // boton para enviar el texto escrito
        JButton a = new JButton("a"); // boton para enviar el texto escrito

        EnviaTexto mievento = new EnviaTexto(this.tcp);

        miboton.addActionListener(mievento);
        ferButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                tcp.realizarLlamada("Fer");
            }
        });
        a.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                tcp.realizarLlamada("a");
            }
        });
        add(miboton);//se grega el boton a la lamina(interfaz)
        add(ferButton);
        add(a);
        
        Thread mihilo = new Thread(this);
        
        mihilo.start();

    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.            
            
            while(true){//el cliente se pone a la escucha
            
                
                campochat.append(this.tcp.escuchar() + "\n");
            
            }
                
    }

    private class EnviaTexto implements ActionListener {

        private TCP tcp;

        public EnviaTexto(TCP tcp) {
            this.tcp = tcp;
            
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            // try {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                //System.out.println(campo1.getText());
                
                campochat.append("\n" + campo1.getText());

                this.tcp.enviar(campo1.getText());
                
            //     Socket misocket = new Socket("192.168.100.77", 9090);

            //     PaqueteEnvio datos = new PaqueteEnvio();

            //     datos.setNick(nick.getText());//almacenamos el texto escrito en el cuadro de texto para el nick name
            //     datos.setIP(ip.getSelectedItem().toString());//almacenamos el texto escrito en el cuadro de texto para la ip
            //     datos.setMensaje(campo1.getText());

            //     ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());//se crea un objeto para cargarlo posteriormente de los datos a ser enviados

            //     paquete_datos.writeObject(datos); //se cargan los datos del objeto PaqueteEnvio

            //     misocket.close(); //se cierra el socket

            //     //Debemos enviar el objeto datos al servidor ya que él actuará de intermediario entre un cliente y otro
            //     //DataOutputStream flujo_salida = new DataOutputStream(misocket.getOutputStream());
            //     //flujo_salida.writeUTF(campo1.getText());
            //     //flujo_salida.close();
            // } catch (IOException ex) {
            //     System.out.println(ex.getMessage());
                
            // }

        }

    }

}