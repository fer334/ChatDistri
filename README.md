# ChatDistri

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

# Team Error 500

## Integrantes

  - Gerardo Cabrera [![alt text](http://i.imgur.com/9I6NRUm.png)](https://github.com/ger99)
  - Giuliano Gonzalez [![alt text](http://i.imgur.com/9I6NRUm.png)](https://github.com/Giuli1297)
  - Walter Gauto [![alt text](http://i.imgur.com/9I6NRUm.png)](https://github.com/waltergauto)
  - Elias Caceres [![alt text](http://i.imgur.com/9I6NRUm.png)](https://github.com/elias-py)
  - Luis Rios [![alt text](http://i.imgur.com/9I6NRUm.png)](https://github.com/fer334)

## Requisitos de instalación
  - jdk
  - maven
    - json-simple
  - git
  - Eclipse IDE
## Instalacion
```sh
$ git clone https://github.com/fer334/ChatDistri
```
para ingresar al proyecto servidor
```sh
$ cd Servidor
```
para ingresar al proyecto cliente
```sh
$ cd Cliente
```

## Compilacion y ejecucion
- Instalar y ejecutar Eclipse IDE.
- Abrir menú File y submenú Import.
- Elegir Existing Maven Project y seleccionar Next.
- Seleccionar la carpeta del proyecto ChatDistri.
- Marcar los proyecto de Cliente y Servidor y seleccionar Finish. 
- Realizar click derecho sobre el proyecto, luego submenú Maven y Update Project. 

## Documentacion de la api ofrecida por el servidor
El servidor recibe una instancia de la clase Paquete que contiene:
  - estado: si la transaccion fue existosa o no 
  - tipo_operacion:
    - tipo_operacion 0 pedir conectarse ingresando el nombre de usuario que va utilizar
    - tipo_operacion 1 pedir lista de usuarios conectados
    - tipo_operacion 2 pedir conectar llamada con usuario especifico
    - tipo_operacion 3 enviar mensaje dentro de llamada
    - tipo_operacion 4 recibir mensaje dentro de llamada
    - tipo_operacion 5 terminar llamada
  - mensaje: mensaje que se desea transmitir que es un String
  - otro: mensaje que se desea transmitir pero con una estructura diferente a String
  - sender: Cadena que indica el username del que envio
  
Servidor
  El servidor recibe datos en formato JSON que luego se convierten en un objeto Paquete. Por ejemplo:
  {"estado":0,"sender":"user1","mensaje":"Hola","tipo_operacion":4}
  Este JSON se convierte a un objeto Paquete donde:
  Paquete paq = (estado  = 0, mensaje = "Hola", tipo_operacion = 4, sender = user1)
Además, el servidor responde a los clientes de acuerdo a su tipo de operación(detallado más arriba) y lo hace instanciando la clase Paquete. Por ejemplo:
Paquete consultaOnline= (estado  = 0, mensaje = "", tipo_operacion = 1, sender = null)
En este caso, el servidor enviará una respuesta tipo: Paquete respuesta = new Paquete(0, usuarios, 0); donde usuarios es un ArrayList de usuarios. Vale la pena resaltar de también este paquete se convierte a JSON.
Metodos
- TCP.conectarse(username): recibe un String username y solicita al servidor ingresar a la lista de clientes online. En servidor crea dos hilos(uno para TCP y otro para UDP) para su comunicación con el cliente.
- TCP.realizarLlamada(destino, origen): realiza la accion de "llamar". Llama al usuario destino enviado como parámetro. El servidor conecta la entrada y salida del origen y destino para que puedan establecer la llamada.
- TCP.terminar(usuario): realiza la funcionalidad de terminar una llamada. El servidor desconecta la entrada y salida del origen y vuelve a los dos clientes a un estado disponible para que puedan recibir llamadas nuevamente.
- UDP.verOnline(): retorna un array con la lista de clientes conectados.


## License
----

MIT


**Free Software, Hell Yeah!**
