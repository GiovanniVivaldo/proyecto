package cliente;
import java.awt.Desktop;
import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Cliente {
    

    BufferedReader delTeclado;
    DataOutputStream alservidor;
    FileInputStream entrada;

    
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    FileOutputStream destino = null;
    BufferedOutputStream out = null;
    BufferedInputStream in = null;
    boolean respuesta = false;
    int tam = 0;
    String nombreArchivo = null;

    
    public void iniciar() {
        try {
            Socket yo = new Socket("192.168.1.78", 5433);

            delTeclado = new BufferedReader(new InputStreamReader(System.in));
            alservidor = new DataOutputStream(yo.getOutputStream());
            DataInputStream delServidor = new DataInputStream(yo.getInputStream());

            escribir("Teclee el nombre del archivo:");
            String el = delTeclado.readLine();
            alservidor.writeUTF(el);
            respuesta = delServidor.readBoolean();

            if (respuesta == true) {
                
                escribir("[Servidor]" + delServidor.readUTF());
                escribir("[Servidor]:" + delServidor.readUTF());
                tam = delServidor.readInt();
                nombreArchivo = delServidor.readUTF();

                destino = new FileOutputStream("C:\\Users\\Giovanni Vivaldo\\Documents\\proyecto\\Cliente\\destino\\" + nombreArchivo);
                out = new BufferedOutputStream(destino);
                in = new BufferedInputStream(yo.getInputStream());
                

                byte[] buffer = new byte[tam];

                
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) in.read();
                }
                
                out.write(buffer);

                escribir("Se recivio el archivo");

                out.flush();
                abrir_archivo();
                in.close();
                out.close();
                yo.close();
            } else {
                escribir("[Servidor]" + delServidor.readUTF());

                iniciar1();
            }
        } catch (Exception e) {
            System.out.println("error " + e.getMessage() + " cliente");
            iniciar1();
        }

    }

    public void iniciar1() {
        try {
            Socket yo = new Socket("192.168.1.78", 5433);

            delTeclado = new BufferedReader(new InputStreamReader(System.in));
            alservidor = new DataOutputStream(yo.getOutputStream());
            DataInputStream delServidor = new DataInputStream(yo.getInputStream());

            escribir("Teclee el nombre del archivo:");
            String el = delTeclado.readLine();
            alservidor.writeUTF(el);
            respuesta = delServidor.readBoolean();

            if (respuesta == true) {
                escribir("[Servidor]" + delServidor.readUTF());
                escribir("[Servidor]:" + delServidor.readUTF());
                tam = delServidor.readInt();
                nombreArchivo = delServidor.readUTF();

                destino = new FileOutputStream("C:\\Users\\Giovanni Vivaldo\\Documents\\proyecto\\Cliente\\destino\\" + nombreArchivo);
                out = new BufferedOutputStream(destino);
                in = new BufferedInputStream(yo.getInputStream());

                byte[] buffer = new byte[tam];
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) in.read();
                }
                out.write(buffer);

                escribir("Se recivio el archivo");

                out.flush();
                abrir_archivo();
                in.close();
                out.close();
                yo.close();
            } else {
                escribir("[Servidor]" + delServidor.readUTF());

                iniciar();
            }
        } catch (Exception e) {
            System.out.println("error " + e.getMessage() + " cliente");
            iniciar();
        }

    }
    
    public static void main(String args[]) {
        Cliente ea = new Cliente();
        ea.iniciar();
    }

    public static void escribir(String txt) {
        System.out.println(txt);
    }

    public void abrir_archivo() {

        Socket s = null;
        ServerSocket ss = null;
        Servidor1.Tarea ob;
        int id = 0;

        try {

            ss = new ServerSocket(5431);
            while(true){
            
                Servidor1.Tarea.escribir("Socket escuchando en puerto 5431");
                s = ss.accept();
                id++;
                Servidor1.Tarea.escribir("\nSe conecto el cliente No." + id + " desde la IP: " + s.getInetAddress());
                Servidor1.Tarea.escribir("**************************************************");
                ob = new Servidor1.Tarea(s, id);
                ob.start();                

            }

        } catch (IOException e) {
            Servidor1.Tarea.escribir(e.getMessage() + " [servidor]");
            System.exit(3);
        } finally {
        }
        
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
}