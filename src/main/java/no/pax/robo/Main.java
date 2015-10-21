package no.pax.robo;

import no.pax.robo.Util.Util;
import no.pax.robo.server.RoboServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private RoboServer server;

    public static void main(String... arg) throws Exception {
        new Main();
    }

    public Main() {
        try {
            startDebugServer();
            startEmbeddedServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startEmbeddedServer() throws Exception {
        Runnable serverRunnable = () -> {
            try {
                server = new RoboServer();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Scanner scanner = new Scanner(System.in);
            final String next = scanner.next();

            if (next.equals("x")) {
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();
    }

    /**
     * Normally we will use the actual robot. This is for debuging/testing.
     */
    public static void startDebugServer() {
        if (!Util.DEBUG_MODE) {
            System.out.println("Not in debug mode");
            return;
        }

        new Thread() {

            @Override
            public void run() {
                ServerSocket serverSocket;
                try {
                    System.out.println("Debug server up and running");
                    serverSocket = new ServerSocket(Util.SOCKET_PORT);
                    Socket s = serverSocket.accept();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));

                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("Message from client: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
