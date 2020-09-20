package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Image.*;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URL.*;
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

        LaminaMarcoCliente milamina = new LaminaMarcoCliente(tcp, udp);

        add(milamina);
        setDefaultCloseOperation(3);
        setBounds(700, 300, 300, 500);

        setVisible(true);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                milamina.tcp.terminar(milamina.getNick());
                e.getWindow().dispose();
            }
        });

    }

}


class LaminaMarcoCliente extends JPanel implements Runnable {// interfaz

    private JTextField campo1;// campo donde se escribe el texto a enviar
    private JLabel nick; // campo donde se visualizará el nick name del usuario
    private JComboBox<String> ip; // campo donde se visualizará la direccion ip del cliente con el que se está
                                  // llevando a cabo la conversacion
    private JTextArea campochat;// area de chat
    private JButton miboton; // boton para enviar mensajes
    private String nickuser;
    JButton llamarButton;
    JButton terminar;
    TCP tcp;
    UDP udp;

    public LaminaMarcoCliente(TCP tcp, UDP udp) {
        this.tcp = tcp;
        this.udp = udp;
        String nick_usuario = JOptionPane.showInputDialog("Ingrese su Ni:");
        nickuser = nick_usuario;

        setLayout(null);

        String entorno = "<html><body>Nick: "+nick_usuario+"<br><br>En Linea:</body></html>";
        JLabel n_nick = new JLabel(entorno);

        tcp.conectarse(nick_usuario);

        ip = new JComboBox<String>();// configuramos el cuadro de texto para que aparezca a la izquierda
        JButton refreshOnlines;
        try{
            refreshOnlines = new JButton(new ImageIcon(((new ImageIcon(
                new URL("https://icons.iconarchive.com/icons/hopstarter/soft-scraps/24/Button-Refresh-icon.png"))
                .getImage()
                .getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH)))));
            //refreshOnlines.setBorder(BorderFactory.createEmptyBorder());
            refreshOnlines.setContentAreaFilled(false);;

        }catch(MalformedURLException e){
            refreshOnlines = new JButton("Refresh");

        }
        llamarButton = new JButton("LLamar");


        campochat = new JTextArea(12, 20);// lugar de colocacion del area de texto, las coordenadas son 12 y 20
        campochat.setLineWrap(true);
        campochat.setWrapStyleWord(true);
        campo1 = new JTextField(20); // area donde se escribirá el mensaje a enviar

        miboton = new JButton("Enviar"); // boton para enviar el texto escrito

        terminar = new JButton("Terminar llamada"); // boton para enviar el texto escrito

        terminar.setVisible(false);

        miboton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                campochat.append(nick_usuario+": "+campo1.getText() + "\n");
                tcp.enviar(campo1.getText(), nick_usuario);
                campo1.setText("");
            }
        });
        terminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	terminar.setVisible(false);
            	llamarButton.setVisible(true);
                System.out.println("enviando mensaje de terminar");
                tcp.terminar(nick_usuario);
            }
        });
        llamarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	llamarButton.setVisible(false);
            	terminar.setVisible(true);
                tcp.realizarLlamada(ip.getSelectedItem().toString(), nickuser);
            }
        });

       refreshOnlines.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ip.removeAllItems();
				ArrayList<String> clientesOnline = udp.getClientesOnline();
				for(int i=0; i<clientesOnline.size(); i++) {
					System.out.println(clientesOnline.get(i));
                    if (!clientesOnline.get(i).equals(nick_usuario)) {
                        ip.addItem(clientesOnline.get(i));
                    }

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

        n_nick.setBounds(10,10,100,60);
        refreshOnlines.setBounds(160,40,40,30);
        llamarButton.setBounds(160,75,80,25);
        campochat.setBounds(10,105,265,240);
        campo1.setBounds(10,350,185,20);
        miboton.setBounds(200, 349, 80, 22);
        ip.setBounds(65, 45, 80, 25);
        terminar.setBounds(20, 380 , 250, 25);
        add(n_nick);
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

    public String getNick() {
    	return this.nickuser;
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        while(true){//el cliente se pone a la escucha
            String mensaje = this.tcp.escuchar();
            if(mensaje.equals("codellamada")) {
            	llamarButton.setVisible(false);
            	terminar.setVisible(true);
            }else if(mensaje.equals("codeterminar")){
            	terminar.setVisible(false);
            	llamarButton.setVisible(true);
                System.out.println("enviando mensaje de terminar");
                tcp.terminar(nickuser);
        	}else {
            	campochat.append(mensaje + "\n");
            }
        	
        }

    }

}
