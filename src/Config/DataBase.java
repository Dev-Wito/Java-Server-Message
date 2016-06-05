/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class DataBase {

    public Storages Array_Storages[] = new Storages[2];

    public DataBase() {
        for (int i = 0; i < 2; i++) {
            Array_Storages[i] = new Storages(i);
        }
    }

    public Connection getConexion() {
        Connection MY_CX = null;
        int Intentos = 0;
        for (int i = 0; i < Array_Storages.length; i++) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String servidor = "jdbc:mysql://" + Array_Storages[i].getServer() + ":3306/" + Array_Storages[i].getBase();
                System.out.println("Queriendo Conectar " + servidor + "/");
                Intentos++;
                MY_CX = DriverManager.getConnection(Array_Storages[i].getServer(), Array_Storages[i].getUsuario(), Array_Storages[i].getContra());
                break;
            } catch (ClassNotFoundException ex) {
                System.out.println("No Se Realizo La Conexión Clase No Encontrada");
            } catch (SQLException ex) {
                System.out.println("No Se Realizo La Conexión, Errores en valores de conexion");
            }
        }

        if (Array_Storages.length >= Intentos) {
            MY_CX = null;
            System.err.println("No hay bases de datos disponibles");
        }

        return MY_CX;

    }
}
