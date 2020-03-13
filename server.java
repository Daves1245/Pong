import java.net.*;
import java.io.*;

public class server {

  /* Server info */
  private static final int PORT = 33402;

  /* Helpful global variables */
  private static final int BUFF_SIZE = 256;
  private byte[] buff = new byte[BUFF_SIZE]; 
  private ArrayList<ClientPair> conns;

  /* Connection establishing resources */
  private DatagramSocket server = null;

  public server(int port) {
    try {
      server = new DatagramSocket(PORT);
    } catch (SocketException se) {
      se.printStackTrace();
    }
  }

  public server() {
    this(PORT);
  }

  public void run() {
    /* Just run a simple echo server for now */
    while (true) {
      DatagramPacket pack = new DatagramPacket(buff, buff.length);
      try {
      server.receive(pack);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }

      /* Grab address and port so we can send a response back */
      InetAddress caddr = pack.getAddress();
      int cport = pack.getPort();

      /* Set up the response to the client */
      pack = new DatagramPacket(buff, buff.length, caddr, cport);
      String recvd = new String(pack.getData(), 0, pack.getLength());

      /* Check for exit */
      if (recvd.equals("exit")) {
        break;
      }

      /* Send the response */
      try {
        server.send(pack);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }

    /* Cleanup */
    server.close();
  }

  public static void main(String[] args) {
    server s = new server();
    s.run();
  }
}

class ClientPair {
  public InetAddr first;
  public InetAddr second;
}
