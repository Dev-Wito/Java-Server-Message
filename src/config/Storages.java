/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import core.json;
import core.pasarela;
import java.sql.Connection;
import java.sql.DriverManager;
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
public final class Storages {

    private String Servidor_IP = "";
    private String Usuario = "";
    private String Contra = "";
    private String Base = "";

    public Storages(int num_serv) {
        this.ini(num_serv);
    }

    public String getServer() {
        return Servidor_IP;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getContra() {
        return Contra;
    }

    public String getBase() {
        return Base;
    }

    public void setServer(String SVR) {
        this.Servidor_IP = SVR;
    }

    public void setUsuario(String USR) {
        this.Usuario = USR;
    }

    public void setContra(String CT) {
        this.Contra = CT;
    }

    public void setBase(String BD) {
        this.Base = BD;
    }

    protected void ini(int num_serv) {
        Connection CX = null;
        int cont = 0;
        ResultSet resultado = null;
        Statement consulta;

        try {
            Class.forName("org.sqlite.JDBC");
            CX = DriverManager.getConnection("jdbc:sqlite:Storages.db");
        } catch (Exception e) {
            System.err.println("No se pude conectar al repositorio de servidores de BD\n");
        }

        try {
            consulta = CX.createStatement();
            String SQL = "SELECT * FROM servidores LIMIT " + num_serv + ",1";
            resultado = consulta.executeQuery(SQL);
            while (resultado.next()) {
                this.setServer(resultado.getString("server_ip"));
                this.setUsuario(resultado.getString("usuario"));
                this.setContra(resultado.getString("clave"));
                this.setBase(resultado.getString("database"));
                break;
            }
            CX.close();
        } catch (SQLException ex) {
            System.err.println("No se pudo ejecturar la consulta a la base maestra");
        }
    }

    public static boolean infoTerminal() {
        Connection CX = null;
        int cont = 0;
        ResultSet resultado = null;
        Statement consulta;

        try {
            Class.forName("org.sqlite.JDBC");
            CX = DriverManager.getConnection("jdbc:sqlite:Storages.db");
        } catch (Exception e) {
            System.err.println("No se pude conectar al repositorio de informacion de terminal\n");
            return false;
        }

        try {
            consulta = CX.createStatement();
            String SQL = "SELECT * FROM infoterminal LIMIT 1";
            resultado = consulta.executeQuery(SQL);
            while (resultado.next()) {
                Globales.TERMINAL_ID = resultado.getInt("sucursal_id");
                Globales.TERMINAL_SERVER_API = resultado.getString("servidor");
                Globales.TERMINAL_SERVER_PORT = resultado.getInt("puerto");
                break;
            }
            CX.close();
            JSONObject vector = new JSONObject();
            vector.put("API", "getSucursal");
            vector.put("ID", Globales.TERMINAL_ID);
            String rta = pasarela.call(vector);
            vector.clear();
            vector = json.decode(rta);
            Globales.SUCURSAL_NOMBRE = vector.get("nom_suc").toString();
            Globales.SUCURSAL_CIUDAD = vector.get("ciu_suc").toString();
            Globales.SUCURSAL_DIR = vector.get("dir_suc").toString();
        } catch (SQLException ex) {
            System.err.println("No se pudo cargar datos de la sucursal");
            return false;
        }
        return true;
    }

    public static boolean setInfoTerminal(String[] args) {
        Connection CX = null;
        try {
            Class.forName("org.sqlite.JDBC");
            CX = DriverManager.getConnection("jdbc:sqlite:Storages.db");
        } catch (Exception e) {
            System.err.println("No se pude conectar al repositorio de informacion de terminal\n");
            return false;
        }
        Statement Query;
        try {
            Query = CX.createStatement();
            Query.executeUpdate("UPDATE infoterminal SET sucursal_id='" + args[0] + "', servidor='" + args[1] + "', puerto='" + args[2] + "'");
        } catch (SQLException ex) {
            System.err.println("No se actualizo la informacion del terminal");
            return false;
        }
        try {
            CX.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
}
