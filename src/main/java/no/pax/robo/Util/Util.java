package no.pax.robo.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created: rak
 * Date: 29.09.12
 */
public class Util {
    public static final int DEFAULT_IDLE_TIME = 1000 * 60 * 60 * 10;
    public static final String PROTOCOL_NAME = "robo";
    public static final String SECRET = "XXX";

    public static final String SOCKET_SERVER = "localhost";
    public static final int SOCKET_PORT = 60010;
    public static final Boolean DEBUG_MODE = true;


    public static JSONObject convertToJSon(String data) {
        try {
            return new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getSocketFormat(String motorA, String motorB) {
        return "A " + getMotorInfo(motorA) + " B " + getMotorInfo(motorB) + " " + SECRET;
    }

    private static String getMotorInfo(String info) {
        String direction = info.startsWith("-") ? "B" : "F";
        String currentSpeed = info.replaceAll("-", "");
        return direction + " " + currentSpeed;
    }
}
