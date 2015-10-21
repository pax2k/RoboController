package no.pax.robo.server;


import no.pax.robo.Util.Util;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RoboServlet extends HttpServlet {
    private WebSocketFactory _wsFactory;
    RoboSocketClient client = new RoboSocketClient();

    /**
     * Initialise the servlet by creating the WebSocketFactory.
     */
    @Override
    public void init() throws ServletException {
        // Create and configure WS factory
        _wsFactory = new WebSocketFactory(new WebSocketFactory.Acceptor() {
            public boolean checkOrigin(HttpServletRequest request, String origin) {
                // Allow all origins
                return true;
            }

            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
                // Return new WebSocket for connections
                if (Util.PROTOCOL_NAME.equals(protocol)) {
                    return new DrinkITWebSocket();

                }
                return null;
            }
        });

        int maxTextMessageSize = 32 * 1024; // default are 16 * 1024, need more space in order to send images.
        _wsFactory.setMaxTextMessageSize(maxTextMessageSize);
        _wsFactory.setMaxIdleTime(Util.DEFAULT_IDLE_TIME);
    }

    /**
     * Handle the handshake GET request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // If the WebSocket factory accepts the connection, then return
        if (_wsFactory.acceptWebSocket(request, response)) {
            return;
        }
        // Otherwise send an HTTP error.
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Websocket only");
    }

    /**
     * <p>This class implements the {@link OnTextMessage} interface so that
     * it can handle the call backs when websocket messages are received on
     * a connection.
     * </p>
     */
    private class DrinkITWebSocket implements WebSocket.OnTextMessage {
        volatile Connection _connection;

        /**
         * Callback for when a WebSocket connection is opened.
         * <p>Remember the passed {@link Connection} object for later sending and
         * add this WebSocket to the members set.
         */
        public void onOpen(Connection connection) {
            _connection = connection;
        }

        /**
         * Callback for when a WebSocket connection is closed.
         * <p>Remove this WebSocket from the members set.
         */
        public void onClose(int closeCode, String message) {
            System.out.println("Connection close, " + closeCode + ". Message: " + message);
        }

        /**
         * Callback for when a WebSocket message is received.
         * <p>Send the message to all connections in the members set.
         */
        public void onMessage(String data) {
            final JSONObject jsonObject = Util.convertToJSon(data);

            try {
                final String motorA = String.valueOf(jsonObject.get("motorA"));
                final String motorB = String.valueOf(jsonObject.get("motorB"));
                final String socketFormat = Util.getSocketFormat(motorA, motorB);

                System.out.println(socketFormat);
                client.sendMessage(socketFormat);
            } catch (JSONException e) {
                System.out.println("Error during parsing: " + e.getMessage());
            }
        }
    }
}
