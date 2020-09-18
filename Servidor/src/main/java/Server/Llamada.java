package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import Client.Paquete;

public class Llamada extends Thread {
    BufferedReader in;
    PrintWriter out;
    public Llamada(BufferedReader in, PrintWriter out) {
        this.in=in;
        this.out=out;
        
    }
    public void run() {
        try {
            while (true) {
                System.out.println("Al entrar al while");
                String entrada;
                    entrada = in.readLine();
            
                Paquete pentrada = Paquete.JSONstrToObj(entrada);
                if (pentrada.getTipo_operacion() == 3) {
                    Paquete p = new Paquete(0, pentrada.getMensaje(), 4);
                    out.println(p.JSONToString());
                }
                System.out.println("Al salir del while");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}