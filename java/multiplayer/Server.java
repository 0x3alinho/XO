package multiplayer;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running. Waiting for 2 players...");
            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                System.out.println("Player connected: " + socket);
            }

            // إرسال أدوار اللاعبين
            DataOutputStream out1 = new DataOutputStream(clients.get(0).getOutputStream());
            out1.writeUTF("X");

            DataOutputStream out2 = new DataOutputStream(clients.get(1).getOutputStream());
            out2.writeUTF("O");

            // إطلاق الخيوط للتبادل بين اللاعبين
            new Thread(() -> relayMessages(clients.get(0), clients.get(1))).start();
            new Thread(() -> relayMessages(clients.get(1), clients.get(0))).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void relayMessages(Socket from, Socket to) {
        try {
            DataInputStream in = new DataInputStream(from.getInputStream());
            DataOutputStream out = new DataOutputStream(to.getOutputStream());

            while (true) {
                String msg = in.readUTF();
                out.writeUTF(msg); // إرسال الرسالة للطرف الثاني
            }
        } catch (IOException e) {
            System.out.println("Player disconnected.");
        }
    }
}
