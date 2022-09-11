package es.udc.redes.tutorial.tcp.server;
//Diego Suarez Ramos : diego.suarez.ramos@udc.es
import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket ssocket = null;
        Socket socket = null;
        try {
            // Create a server socket
            ssocket = new ServerSocket(Integer.parseInt(argv[0]));
            // Set a timeout of 300 secs
            ssocket.setSoTimeout(300000);
            String received;
            while (true) {
                // Wait for connections
                socket= ssocket.accept();
                // Set the input channel
                BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                
                // Set the output channel
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                // Receive the client message
                received=in.readLine();
                System.out.println("SERVER: Received " + received
                        + " from " + socket.getInetAddress().toString()
                        + ":" + socket.getPort());
                // Send response to the client
                System.out.println("CLIENT: Sending " + received +
                        " to " + socket.getInetAddress().toString() +
                        ":" + socket.getPort());
                out.println(received);
                // Close the streams
                in.close();
                out.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
           System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
             if(socket!=null){
                 socket.close();
             }
            if(ssocket!=null){
                ssocket.close();
            }
        }
    }
}
