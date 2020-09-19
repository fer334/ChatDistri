package Server;

/**
 * Hello world!
 *
 */
public class App {


    public static void main(String[] args) {
        // Variables

        int puertoServidor = 9876;
        Server server = new Server(puertoServidor);
        server.ejecutar();

    }

}
