package Server;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Cliente {

    private String direccion;
    private int port;
    private String username;

    public Cliente() {
    }

    public Cliente(String direccion, int port, String username) {
        this.direccion = direccion;
        this.port = port;
        this.username = username;
    }
    
	public Cliente(SocketAddress addr, int port, String username) {
        
        this.direccion = addr.toString().split(":")[0].split("/")[1];
        this.port = port;
        this.username = username;
	}

	public Cliente(InetAddress direccion, int port, String username) {
        this.direccion = direccion.getHostAddress();
        System.out.println("La dic es"+this.direccion);
        this.port = port;
        this.username = username;
    }

	public String getdireccion() {
        return this.direccion;
    }

    public void setdireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Cliente direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public Cliente port(int port) {
        this.port = port;
        return this;
    }

    public Cliente username(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " direccion='" + getdireccion() + "'" +
            ", port='" + getPort() + "'" +
            ", username='" + getUsername() + "'" +
            "}";
    }


    @Override
    public boolean equals(Object o) {
        Cliente cliente = (Cliente) o;
        if (cliente.username == this.username)
            return true;
        return false;
    }

	public InetAddress getIPAddress() {
        InetAddress r= null;
        try {
            r=  InetAddress.getByName(direccion);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
	}

    
}