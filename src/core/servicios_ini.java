/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author wito
 */
public class servicios_ini extends Thread {

    public void run() {
        BufferedReader Cabezon;
        DataOutputStream alCabezon;
        ServerSocket Cerebellum = null;
        Socket Soquete = null;
        String MSJ;
        try {
            Cerebellum = new ServerSocket(6969);
            System.out.println("Servicios Iniciados");
            while (true) {
                Soquete = Cerebellum.accept();
                System.out.println("Conexión iniciada");
                Cabezon = new BufferedReader(new InputStreamReader(Soquete.getInputStream()));
                alCabezon = new DataOutputStream(Soquete.getOutputStream());
                MSJ = Cabezon.readLine();
                MSJ = this.service(MSJ);
                alCabezon.writeUTF(MSJ);
                alCabezon.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected String service(String solicitud) {
        JSONObject obj = null;
        obj = json.decode(solicitud);
        String API = obj.get("API").toString();
        String Response = null;
        switch (API) {
            case "combologin":
                break;
            default:
                obj.clear();
                obj.put("error", true);
                obj.put("msg", "No se reconoce el servicio");
                return json.encode(obj);
        }
        return Response;
    }
}
