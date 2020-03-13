import java.net.*;
import java.io.*;

import java.util.Scanner;

public class client {
  private DatagramSocket socket;
  private InetAddress addr;
  private Scanner in;
  private byte[] buff;
  private static final int PORT = 33402;

  public client(String ip) {
    try {
      socket = new DatagramSocket();
      addr = InetAddress.getByName(ip);
      System.out.println(addr);
      in = new Scanner(System.in);
    } catch (SocketException se) {
      se.printStackTrace();
    } catch (UnknownHostException uh) {
      uh.printStackTrace();
    }
  }

  public void run() {
    while (true) {
      try {
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
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  public void close() {
    socket.close();
    System.exit(0);
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("USAGE: client [HOSTNAME]");
      System.exit(1);
    }

    client c = new client(args[0]);
    c.run();
  }
}
