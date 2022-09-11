package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.util.Date;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        try {
          // This code processes HTTP requests and generates
            InputStream in = new BufferedInputStream(socket.getInputStream());
            //BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            Date fecha= new Date();
            byte[] recibo=new byte[1024];
            int read = in.read(recibo);

            byte[] respuesta=FuncionesHTTP.procesar(new String(recibo),fecha);
          // HTTP responses
            out.write(respuesta);
            out.flush();
          // Uncomment next catch clause after implementing the logic
          //
         } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
