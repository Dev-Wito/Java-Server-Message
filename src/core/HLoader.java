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

    public void tabla2() {
        JSONObject vector = new JSONObject();
        vector.put("API", "getListTPMovimientos");
        String rta = pasarela.call(vector);
        vector.clear();
        vector = json.decode(rta);
        JSONArray Array_Usuarios = (JSONArray) vector.get("tipos");
        MTabla.removeRow(0);
        for (int v = 0; v < Array_Usuarios.size(); v++) {
            JSONObject Datos = json.decode(Array_Usuarios.get(v).toString());
            Object[] Row = {Integer.parseInt(Datos.get("id").toString()), Datos.get("nom").toString(), Datos.get("local").toString(), Datos.get("remoto").toString()};
            MTabla.addRow(Row);
        }
    }

    public void tabla3() {
        JSONObject vector = new JSONObject();
        vector.put("API", "getListSucursales");
        String rta = pasarela.call(vector);
        vector.clear();
        vector = json.decode(rta);
        JSONArray Array_Usuarios = (JSONArray) vector.get("sucursales");
        MTabla.removeRow(0);
        for (int v = 0; v < Array_Usuarios.size(); v++) {
            JSONObject Datos = json.decode(Array_Usuarios.get(v).toString());
            Object[] Row = {Integer.parseInt(Datos.get("id").toString()), Datos.get("nom").toString(), Datos.get("ciud").toString(), Datos.get("dir").toString()};
            MTabla.addRow(Row);
        }
    }

    public void tabla4() {
        JSONObject vector = new JSONObject();
        vector.put("API", "getListTPCuenta");
        String rta = pasarela.call(vector);
        vector.clear();
        vector = json.decode(rta);
        JSONArray Array_Usuarios = (JSONArray) vector.get("cuentas");
        MTabla.removeRow(0);
        for (int v = 0; v < Array_Usuarios.size(); v++) {
            JSONObject Datos = json.decode(Array_Usuarios.get(v).toString());
            Object[] Row = {Integer.parseInt(Datos.get("id").toString()), Datos.get("nom").toString()};
            MTabla.addRow(Row);
        }
    }

    public void tablaBit() {
        JSONObject vector = new JSONObject();
        vector.put("API", "getBitUsu");
        String rta = pasarela.call(vector);
        vector.clear();
        vector = json.decode(rta);
        JSONArray Array_Usuarios = (JSONArray) vector.get("bitacora");
        for (int v = 0; v < Array_Usuarios.size(); v++) {
            JSONObject Datos = json.decode(Array_Usuarios.get(v).toString());
            Object[] Row = {Datos.get("edi").toString(), Datos.get("acc").toString(),Datos.get("usu").toString(),Datos.get("fec").toString()};
            MTabla.addRow(Row);
        }
    }
}
