package es.udc.redes.tutorial.tcp.server;
//Diego Suarez Ramos : diego.suarez.ramos@udc.es
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String argv[]) throws IOException {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
      System.exit(-1);
    }
    ServerSocket ssocket = null;
    Socket socket = null;
    try {
      // Create a server socket
      ssocket = new ServerSocket(Integer.parseInt(argv[0]));
      // Set a timeout of 300 secs
      ssocket.setSoTimeout(300000);
      while (true) {
        // Wait for connections
        socket=ssocket.accept();
        // Create a ServerThread object, with the new connection as parameter
        ServerThread s2 = new ServerThread(socket);
        // Initiate thread using the start() method
        s2.start();
      }
    // Uncomment next catch clause after implementing the logic
      } catch (SocketTimeoutException e) {
        System.err.println("Nothing received in 300 secs");
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
      } finally{
	    if(ssocket!=null){
          ssocket.close();
        }
    }
  }
}
