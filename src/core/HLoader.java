/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author wito
 */
public class HLoader extends Thread {

    DefaultTableModel MTabla;
    String Search;

    public HLoader(DefaultTableModel MTabla, String Search) {
        this.MTabla = MTabla;
        this.Search = Search;
    }

    public void run() {
        JSONObject vector = new JSONObject();
        vector.put("API", "getAllUsuarios");
        vector.put("Search", this.Search);
        String rta = pasarela.call(vector);
        vector.clear();
        vector = json.decode(rta);
        JSONArray Array_Usuarios = (JSONArray) vector.get("usuarios");
        MTabla.removeRow(0);
        for (int v = 0; v < Array_Usuarios.size(); v++) {
            JSONObject Datos = json.decode(Array_Usuarios.get(v).toString());
            usuario AllData = new usuario(Integer.parseInt(Datos.get("id").toString()), Datos.get("nuid").toString(), Datos.get("nom").toString(), Datos.get("cor").toString(), Datos.get("cel").toString(), Datos.get("usu").toString(), Datos.get("rol").toString());
            Object[] Row = {AllData, Datos.get("usu").toString(), Datos.get("nom").toString(), Datos.get("rol").toString()};
            MTabla.addRow(Row);
        }
    }
}
