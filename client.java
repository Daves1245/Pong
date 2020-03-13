import java.net.*;
import java.io.*;

import java.util.Scanner;

public class client {
  private DatagramSocket socket;
  private InetAddress addr;
  private Scanner in;
  private byte[] buff;
  private static final int PORT = 33402;

  public client() {
    try {
    socket = new DatagramSocket();
    addr = InetAddress.getByName("localhost");
    in = new Scanner(System.in);
    } catch (SocketException se) {
      se.printStackTrace();
    }
  }

  public void run() {
    while (true) {
      String line = in.nextLine();
      buff = line.getBytes();
      DatagramPacket pack = new DatagramPacket(buff, buff.length, addr, PORT);
      socket.send(pack);
      pack = new DatagramPacket(buff, buff.length);
      socket.receive(pack);
      String recvd = new String(pack.getData(), 0, pack.getLength());
      if (recvd.equals("exit")) {
        close();
      }
      System.out.println(recvd);
    }
  }

  public void close() {
    sock.close();
    System.exit(0);
  }

  public static void main(String[] args) {

  }
}
