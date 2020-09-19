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
import java.util.ArrayList;

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
        int puertoServidor = 9876;
        TCP tcp = new TCP(direccionServidor, puertoServidor);

        // Paquete respuesta;

        UDP udp = new UDP(direccionServidor, puertoServidor);
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

    }

}


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

        JLabel texto = new JLabel("En Linea:");

        tcp.conectarse(nick_usuario);

        ip = new JComboBox<String>();// configuramos el cuadro de texto para que aparezca a la izquierda
        JButton refreshOnlines = new JButton("Reload");
        JButton llamarButton = new JButton("LLamar");


        campochat = new JTextArea(12, 20);// lugar de colocacion del area de texto, las coordenadas son 12 y 20

        campo1 = new JTextField(20); // area donde se escribirá el mensaje a enviar

        miboton = new JButton("Enviar"); // boton para enviar el texto escrito
        JButton terminar = new JButton("Terminar llamada"); // boton para enviar el texto escrito

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
                tcp.terminar(nick_usuario);
            }
        });
        llamarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcp.realizarLlamada("a");
            }
        });
        
        refreshOnlines.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ip.removeAllItems();
				ArrayList<String> clientesOnline = udp.getClientesOnline();
				for(int i=0; i<clientesOnline.size(); i++) {
					System.out.println(clientesOnline.get(i));
					ip.addItem(clientesOnline.get(i));
				}
				
			}
        });

        /*ip.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    tcp.realizarLlamada(e.getItem().toString());
                }

            }
        });*/

        add(n_nick);
        add(nick);
        add(texto);
        add(ip); // agregamos el cuadro de texto a la lamina(interfaz)
        add(refreshOnlines);
        add(llamarButton);
        add(campochat);// se agrega el campo de texto a la lamina(interfaz)
        add(campo1); // se agrega el campo de texto a la lamina(interfaz)
        add(miboton);// se grega el boton a la lamina(interfaz)
        add(terminar);

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
