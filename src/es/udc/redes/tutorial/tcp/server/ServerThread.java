package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;
//Diego Suarez Ramos : diego.suarez.ramos@udc.es
/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

  private Socket socket;

  public ServerThread(Socket s) {
    super();
    socket = s;
  }

  public void run() {
    String received;
    try {
      // Set the input channel
      BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
      // Set the output channel
      PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
      // Receive the message from the client
      received=in.readLine();
      System.out.println("SERVER: Received " + received
              + " from " + socket.getInetAddress().toString()
              + ":" + socket.getPort());
      // Sent the echo message to the client
      out.println(received);
      System.out.println("SERVER: Sending "
              + received
              + socket.getInetAddress().toString() + ":"
              + socket.getPort());
      // Close the streams
      in.close();
      out.close();

    // Uncomment next catch clause after implementing the logic
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