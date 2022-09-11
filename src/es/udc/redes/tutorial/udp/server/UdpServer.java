package es.udc.redes.tutorial.udp.server;
//Diego Suarez Ramos : diego.suarez.ramos@udc.es
import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        DatagramSocket socket=null;
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }

        try {
            // Create a server socket
            socket = new DatagramSocket(Integer.parseInt(argv[0]));
            // Set maximum timeout to 300 secs
            socket.setSoTimeout(300000);
            byte[] buf = new byte[1024];
            while (true) {
                // Prepare datagram for reception
                DatagramPacket packet = new DatagramPacket(buf,buf.length);
                // Receive the message
                socket.receive(packet);
                System.out.println("CLIENT: Received "
                        + new String(packet.getData(), 0, packet.getLength())
                        + " from " + packet.getAddress().toString() + ":"
                        + packet.getPort());
                // Prepare datagram to send response
                DatagramPacket spacket= new DatagramPacket(packet.getData(), packet.getLength(),
                        packet.getAddress(), packet.getPort());
                // Send response
                socket.send(spacket);
                System.out.println("SERVER: Sending "
                        + new String(spacket.getData(),0, spacket.getLength()) + " to "
                        + spacket.getAddress().toString() + ":"
                        + spacket.getPort());
            }
          
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // Close the socket
            if(socket != null) {
                socket.close();
            }
        }
    }
}
