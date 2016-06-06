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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author wito
 */
public class servicios_ini extends Thread {

    public static Connection ConectDB;

    public servicios_ini(Connection Conexion) {
        servicios_ini.ConectDB = Conexion;
    }

    protected ResultSet getQuery(String SQL) {
        ResultSet Response = null;
        Statement Query;
        if (ConectDB != null) {
            try {
                Query = ConectDB.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                Response = Query.executeQuery(SQL);
            } catch (SQLException ex) {
                System.err.println("No Se:" + ex);
                return null;
            }
        }
        return Response;
    }

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
                System.out.println("Procesando solicitud...");
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
        String Response = "{}";
        switch (API) {
            case "combologin":
                Response = listarUsuarios();
                break;
            case "autenticar":
                Response = autenticar(obj.get("parameters").toString());
                break;
            case "getSucursal":
                Response = getSucursal(obj.get("ID").toString());
                break;
            case "getCuentasUsuario":
                Response = getCuentasUsuario(obj.get("persona_id").toString());
                break;
            case "getTipoMovimiento":
                Response = getTipoMovimiento();
                break;
            default:
                obj.clear();
                obj.put("error", true);
                obj.put("msg", "No se reconoce el servicio");
                return json.encode(obj);
        }
        return Response;
    }

    protected String listarUsuarios() {
        ResultSet Response = null;
        String rta = "";
        Response = getQuery("SELECT usuario FROM usuarios");
        try {

            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String usuarios[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                usuarios[v] = Response.getString("usuario");
            }
            rta = "{\"usuarios\":[\"" + String.join("\",\"", usuarios) + "\"]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rta;
    }

    protected String autenticar(String Parametros) {
        String rta = "";
        JSONObject Param = json.decode(Parametros);
        ResultSet Response = this.getQuery("SELECT COUNT(usuarios.id) AS Exist, usuarios.id , usuarios.persona_id, personas.num_ident, personas.nombres, personas.correo, personas.celular, usuarios.usuario, usuarios.llave, usuarios.rol FROM usuarios INNER JOIN personas  ON (usuarios.persona_id = personas.id) WHERE usuario='" + Param.get("usu").toString() + "' AND llave=PASSWORD('" + Param.get("pass") + "')");
        try {
            while (Response.next()) {
                Param.clear();
                Param.put("Auth", Response.getInt("Exist"));
                Param.put("Usu_id", Response.getInt("id"));
                Param.put("Per_id", Response.getInt("persona_id"));
                Param.put("Per_nid", Response.getString("num_ident"));
                Param.put("Per_nom", Response.getString("nombres"));
                Param.put("Per_correo", Response.getString("correo"));
                Param.put("Per_cel", Response.getString("celular"));
                Param.put("Usu_login", Response.getString("usuario"));
                Param.put("Usu_rol", Response.getString("rol"));
                break;
            }
            rta = json.encode(Param);
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }

    protected String getSucursal(String ID) {
        String rta = "";
        JSONObject vector = new JSONObject();
        ResultSet Response = this.getQuery("SELECT COUNT(id) AS Existe, nombre,ciudad,direccion FROM sucursales WHERE id='" + ID + "'");
        try {
            while (Response.next()) {
                vector.put("valida", Response.getInt("Existe"));
                vector.put("nom_suc", Response.getString("nombre"));
                vector.put("ciu_suc", Response.getString("ciudad"));
                vector.put("dir_suc", Response.getString("direccion"));
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json.encode(vector);
    }

    protected String getCuentasUsuario(String $ID) {
        String $rta = "";
        JSONObject vector = new JSONObject();
        ResultSet Response = this.getQuery("SELECT cuentas.id,CONCAT(\"Cuenta: \",tipo_cuentas.nombre,\" N°:\", cuentas.numero_cuenta) AS label FROM cuentas INNER JOIN tipo_cuentas  ON (cuentas.tipo_cuenta_id = tipo_cuentas.id) WHERE cuentas.persona_id=" + $ID);
        try {
            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String cuentas[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                vector.clear();
                vector.put("id", Response.getInt("id"));
                vector.put("label", Response.getString("label"));
                cuentas[v] = json.encode(vector);
            }
            $rta = "{\"cuentas\":[" + String.join(",", cuentas) + "]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return $rta;
    }

    protected String getTipoMovimiento() {
        String $rta = "";
        JSONObject vector = new JSONObject();
        ResultSet Response = this.getQuery("SELECT id,nombre FROM tipo_movimientos");
        try {
            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String cuentas[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                vector.clear();
                vector.put("id", Response.getInt("id"));
                vector.put("label", Response.getString("nombre"));
                cuentas[v] = json.encode(vector);
            }
            $rta = "{\"movimientos\":[" + String.join(",", cuentas) + "]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return $rta;
    }
}
