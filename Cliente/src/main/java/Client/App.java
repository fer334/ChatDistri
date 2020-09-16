package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

        // // Envio paquete de primera conexion
        // respuesta = udp.enviarPaquete(new Paquete(0,"Fer2",0));
        // System.out.println(respuesta.JSONToString());

        // // Si al conectarse no hubo problemas, enviamos otro paquete
        // if(respuesta.getEstado()==0){
        // Pido la lista de todos los usuarios conectados
        // }

        String direccionServidor = "127.0.0.1";
        int puertoServidor = 9876;
        TCP tcp = new TCP(direccionServidor, puertoServidor);

        // Paquete respuesta;

        UDP udp = new UDP(direccionServidor, puertoServidor);
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
        MarcoCliente mimarco = new MarcoCliente(tcp, udp);

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoCliente extends JFrame {

    TCP tcp;
    UDP udp;

    public MarcoCliente(TCP tcp, UDP udp) {
        this.tcp = tcp;
        this.udp = udp;
        setBounds(600, 300, 280, 350);

        LaminaMarcoCliente milamina = new LaminaMarcoCliente(tcp, udp);

        add(milamina);

        setVisible(true);

        // addWindowListener( new EnvioOnline());// se dispara el evento de ventana
        // cuando un cliente se
        // conecta a la
        // aplicacion y envia unos datos al servidor automaticamente

    }

}

// //evento de ventana para enviar al servidor el estado activo de un cliente
// class EnvioOnline extends WindowAdapter {

// public void windowOpened(WindowEvent e) {

// try {

// Socket misocket = new Socket("192.168.100.77", 9999);

// PaqueteEnvio datos = new PaqueteEnvio();

// datos.setMensaje("online");

// ObjectOutputStream paquete_datos_inicial = new
// ObjectOutputStream(misocket.getOutputStream());

// paquete_datos_inicial.writeObject(datos);

// misocket.close();

// }catch (Exception ex) {
// System.out.println(ex.getMessage());
// }

// }

// }

class LaminaMarcoCliente extends JPanel implements Runnable {// interfaz

    private JTextField campo1;// campo donde se escribe el texto a enviar
    private JLabel nick; // campo donde se visualizará el nick name del usuario
    private JComboBox<String> ip; // campo donde se visualizará la direccion ip del cliente con el que se está
                                  // llevando a cabo la conversacion
    private JTextArea campochat;// area de chat
    private JButton miboton; // boton para enviar mensajes
    TCP tcp;
    UDP udp;

    public LaminaMarcoCliente(TCP tcp, UDP udp) {
        this.tcp = tcp;
        this.udp = udp;
        String nick_usuario = JOptionPane.showInputDialog("Ingrese su Ni:");

        JLabel n_nick = new JLabel("Nick:");

        nick = new JLabel();
        nick.setText(nick_usuario);

        JLabel texto = new JLabel("Online:");

        tcp.conectarse(nick_usuario);

        ip = new JComboBox<String>();// configuramos el cuadro de texto para que aparezca a la izquierda
        ip.addItem("u");
        ip.addItem("ali");
        ip.addItem("fer");
        // ip.addItem("192.168.100.46");//ip de la vm Xubuntu
        // ip.addItem("");//ip de la vm Ubuntu Mate

        campochat = new JTextArea(12, 20);// lugar de colocacion del area de texto, las coordenadas son 12 y 20

        campo1 = new JTextField(20); // area donde se escribirá el mensaje a enviar

        miboton = new JButton("Enviar"); // boton para enviar el texto escrito
        JButton terminar = new JButton("Terminar llamada"); // boton para enviar el texto escrito
        JButton a = new JButton("a"); // boton para enviar el texto escrito

        miboton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                campochat.append(campo1.getText() + "\n");
                tcp.enviar(campo1.getText());
            }
        });
        terminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("enviando mensaje de terminar");
                udp.terminar(nick_usuario);
            }
        });
        a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcp.realizarLlamada("a");
            }
        });

        ip.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    tcp.realizarLlamada(e.getItem().toString());
                }

            }
        });

        add(n_nick);
        add(nick);
        add(texto);
        add(ip); // agregamos el cuadro de texto a la lamina(interfaz)
        add(campochat);// se agrega el campo de texto a la lamina(interfaz)
        add(campo1); // se agrega el campo de texto a la lamina(interfaz)
        add(miboton);// se grega el boton a la lamina(interfaz)
        add(terminar);
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

}