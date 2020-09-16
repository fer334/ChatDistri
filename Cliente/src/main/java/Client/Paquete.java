package Client;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * Clase que sirve para enviar paquetes entre el servidor y el cliente
 * tipo_operacion 1 pedir lista de usuarios conectados
 * tipo_operacion 2 pedir realizar llamada a un usuario especifico
 * 
 * 
 */
public class Paquete {
    private Integer estado,tipo_operacion;
    private String mensaje;
    ArrayList<String> otro;

    public Paquete(Integer estado, String mensaje, Integer tipo_operacion) {
        this.mensaje=mensaje;
        this.estado =estado;
        this.tipo_operacion=tipo_operacion;
    }

    public Paquete(Integer estado, ArrayList<String> otro, Integer tipo_operacion) {
        this.otro=otro;
        this.estado=estado;
        this.tipo_operacion=tipo_operacion;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getTipo_operacion() {
        return this.tipo_operacion;
    }

    public void setTipo_operacion(Integer tipo_operacion) {
        this.tipo_operacion = tipo_operacion;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ArrayList<String> getOtro() {
        return this.otro;
    }

    public void setOtro(ArrayList<String> otro) {
        this.otro = otro;
    }

    public String JSONToString() {
        Paquete p = this;
        JSONObject obj = new JSONObject();

        obj.put("estado", p.getEstado());
        obj.put("tipo_operacion", p.getTipo_operacion());
        obj.put("mensaje", p.getMensaje());

        // Si hay otro datos agrego, recordar que otro es un array
        if(otro!=null){
            JSONArray list = new JSONArray();
            for (String temp : this.otro) {
                list.add(temp);
            }
            obj.put("otro", list);
        }
        return obj.toJSONString();
    }

    public static Paquete JSONstrToObj(String str) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(str.trim());
            JSONObject jsonObject = (JSONObject) obj;
            
            Integer estado = ((Long) jsonObject.get("estado")).intValue();
            String mensaje = (String) jsonObject.get("mensaje");
            Integer tipo_operacion = ((Long) jsonObject.get("tipo_operacion")).intValue();
            
            Paquete r= new Paquete(estado, mensaje, tipo_operacion);

            // Si tiene el algo en el campo otro...
            if(jsonObject.get("otro")!=null){
                ArrayList<String> temp = new ArrayList<String>();
                JSONArray msg = (JSONArray) jsonObject.get("otro");
                Iterator<String> iterator = msg.iterator();
                while (iterator.hasNext()) {
                    temp.add(iterator.next());
                }
                r.setOtro(temp);
            }
    
            return r;

        } catch (ParseException e) {
            System.err.println("Error al parsear el paquete, detalles:"+e);
            return null;
        }
    }

}