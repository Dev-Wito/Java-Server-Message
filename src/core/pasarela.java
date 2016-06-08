/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import config.Globales;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.simple.JSONObject;

/**
 *
 * @author wito
 */
public class pasarela extends Thread {

    public static String call(JSONObject send) {
        DataInputStream Response; //Desde el Servidor
        PrintWriter callback; // Haciea el servidor
        Socket Soquete = null;
        String MSJ = json.encode(send);
        try {
            Soquete = new Socket(Globales.TERMINAL_SERVER_API, Globales.TERMINAL_SERVER_PORT);
            Response = new DataInputStream(Soquete.getInputStream());
            callback = new PrintWriter(Soquete.getOutputStream(), true);
            callback.println(MSJ);
            MSJ = Response.readUTF();
            Soquete.close();
        } catch (IOException ex) {
            System.err.println("Servicio no disponibles");
        }
        return MSJ;
    }

    public static String backup(JSONObject send) {
        DataInputStream Response; //Desde el Servidor
        PrintWriter callback; // Haciea el servidor
        Socket Soquete = null;
        String MSJ = json.encode(send);
        try {
            Soquete = new Socket(Globales.TERMINAL_SERVER_API, 9960);
            Response = new DataInputStream(Soquete.getInputStream());
            callback = new PrintWriter(Soquete.getOutputStream(), true);
            callback.println(MSJ);
            MSJ = Response.readUTF();
            Soquete.close();
        } catch (IOException ex) {
            System.err.println("Servicio no disponibles");
        }
        return MSJ;
    }
}
