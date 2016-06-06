/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

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
                MY_CX = DriverManager.getConnection(servidor, Array_Storages[i].getUsuario(), Array_Storages[i].getContra());
                if (MY_CX != null) {
                    Intentos=0;
                    break;
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("No Se Realizo La Conexión Clase No Encontrada");
            } catch (SQLException ex) {
                System.out.println("No Se Realizo La Conexión, Errores en valores de conexion o disponibilidad del servicio");
            }
        }

        if (Intentos >= Array_Storages.length) {
            MY_CX = null;
            System.err.println("No hay bases de datos disponibles");
            JOptionPane.showMessageDialog(null, "No Hay bases de datos disponibles", "Error de comunicacion", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return MY_CX;

    }
}
