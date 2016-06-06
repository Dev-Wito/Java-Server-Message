/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author wito
 */
public class json {

    public json() {
    }

    public static String encode(JSONObject obj) {
        String jsonText = "{}";
        try {
            StringWriter out = new StringWriter();
            obj.writeJSONString(out);
            jsonText = out.toString();
        } catch (IOException ex) {
            Logger.getLogger(json.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonText;
    }

    public static JSONObject decode(String s) {
        JSONParser parser = new JSONParser();
        JSONObject Vector = null;
        try {
            Object obj = parser.parse(s);
            Vector = (JSONObject) parser.parse(s);
            //JSONArray PK = (JSONArray) Vector.get("values");
            
        } catch (ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
        return Vector;
    }
}
