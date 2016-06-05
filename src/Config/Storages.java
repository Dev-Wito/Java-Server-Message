/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}
