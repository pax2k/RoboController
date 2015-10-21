package no.pax.robo.server;

import no.pax.robo.Util.Util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author Raymond Koteng.
 */
public class RoboSocketClient extends Thread {

    private BufferedWriter out;

    public RoboSocketClient() {
        initSocket();
    }

    public synchronized void sendMessage(String message) {
        if (out == null) {
            System.out.println("No socket, abort");
            return;
        }

        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void initSocket() {
        try {
            Socket s = new Socket(Util.SOCKET_SERVER, Util.SOCKET_PORT);
            s.setTcpNoDelay(true);
            s.setKeepAlive(true);

            out = new BufferedWriter(
                    new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
