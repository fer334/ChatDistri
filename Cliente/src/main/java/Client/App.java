package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;

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
        setBounds(300, 100, 300, 500);

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

 
        JLabel Labelnick = new JLabel("Nick: ");
        JLabel nick = new JLabel(nick_usuario);
        JLabel online= new JLabel("En linea: ");   
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

        Labelnick.setBounds(10,10,40,20);
        nick.setBounds(45, 10, 100, 20);
        online.setBounds(10, 50, 80, 20);
        refreshOnlines.setBounds(245,45,30,30);
        llamarButton.setBounds(75,85,200,30);
        campochat.setBounds(10,120,265,250);
        campo1.setBounds(10,380,185,25);
        miboton.setBounds(200, 380, 75, 25);
        ip.setBounds(75, 45, 165, 30);
        terminar.setBounds(20, 410 , 250, 30);
        add(Labelnick);
        add(nick);
        add(online);
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
        	}else if(mensaje.equals("codeocupado")) {
        		campochat.append("USUARIO "+ip.getSelectedItem().toString() +" OCUPADO");
        		llamarButton.setVisible(true);
            	terminar.setVisible(false);
        	}else {
            	campochat.append(mensaje + "\n");
            }
        	
        }

    }

}
