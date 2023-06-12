package cliente;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket s = null;
        ServerSocket ss = null;
        Tarea ob;
        int id = 0;

        try {

            ss = new ServerSocket(5433);
            while(true){
            
                Tarea.escribir("Socket escuchazndo en puerto 5433");
                s = ss.accept();
                id++;
                Tarea.escribir("\nSe conecto el cliente No." + id + " desde la IP: " + s.getInetAddress());
                Tarea.escribir("**************************************************");
                ob = new Tarea(s, id);
                ob.start();                

            }

        } catch (IOException e) {
            Tarea.escribir(e.getMessage() + " [servidor]");
            System.exit(3);
        } finally {
        }

    }

    static class Tarea extends Thread {

        int id;
        Socket s = null;
        
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        DataInputStream entradaCliente = null;
        DataOutputStream salidaCliente = null;

        File archivo = null;
        long tiempoinicio = 0;
        long tiempofinal= 0;
        long tiempo_total=0;
        long initialTime;
        Tarea(Socket socket, int id) {
            this.s = socket;
            this.id = id;
        }

        boolean checar(String nombre) {
            archivo = new File("C:\\Users\\Giovanni Vivaldo\\Documents\\proyecto\\Cliente\\destino\\" + nombre);
            if (archivo.exists()) {
                return true;
            } else {
                return false;
            }
        }

        public void run() {
            
            
            try {
                entradaCliente = new DataInputStream(s.getInputStream());
                salidaCliente = new DataOutputStream(s.getOutputStream());
                String nombreArchivo = entradaCliente.readUTF();
                
                if (checar(nombreArchivo) == true) {
                    tiempoinicio=(System.currentTimeMillis() - this.initialTime);
                    System.out.println("El servidor comienza envio a cliente :" + id + " EN EL TIEMPO: "
                    + (System.currentTimeMillis() - this.initialTime)
                    + " milisegundos");
                    salidaCliente.writeBoolean(true);
                    salidaCliente.writeUTF("SI existe el archivo:" + nombreArchivo + " en el servidor");
                    salidaCliente.writeUTF("Tamaño del archivo:" + (archivo.length() / 1024) + " KB | Nombre:" + archivo.getName());
                    salidaCliente.writeInt((int) archivo.length());
                    salidaCliente.writeUTF(nombreArchivo);
                    escribir("Enviando archivo:" + nombreArchivo + " a " + s.getInetAddress());
                    FileInputStream entrada = new FileInputStream(archivo);
                    BufferedInputStream leerArch = new BufferedInputStream(entrada);
                  
                    BufferedOutputStream salida = new BufferedOutputStream(s.getOutputStream()); 
                    byte[] arreglo = new byte[(int) archivo.length()];
                    leerArch.read(arreglo);

                    for (int i = 0; i < arreglo.length; i++) {
                        salida.write(arreglo[i]);
                    }
                    tiempofinal=(System.currentTimeMillis() - this.initialTime);
                    tiempo_total=tiempofinal-tiempoinicio;
                    escribir("Archivo Enviado a cliente:" + id);

                    System.out.println("El servidor termino con cliente" + id + "  EN UN TIEMPO DE: "
                            + tiempo_total + " milisegundos");
                    System.out.println("Tiempo del cliente "+id +": ("+(System.currentTimeMillis() - this.initialTime)+") milisegundos");

                    salida.flush();
                    salida.flush();
                    salida.close();
                    entrada.close();
                }

                if (checar(nombreArchivo) == false) {
                    salidaCliente.writeBoolean(false);
                    salidaCliente.writeUTF("NO existe el archivo:" + nombreArchivo + " en el servidor");
                    escribir("se envio respuesta al cliente");
                }
            } catch (Exception ex) {
                escribir(ex.getMessage() + " id:" + id);
            } finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                    if (s != null) {
                        s.close();
                    }
                    System.out.println("Termino proceso para cliente: " + id);

                } catch (Exception e) {
                    System.out.println(e.getMessage() + " [servidor]");
                }
            }
        }

        public static void escribir(String txt) {
            System.out.println(txt);
        }
    }

}
