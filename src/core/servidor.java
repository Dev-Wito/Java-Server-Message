/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import config.DataBase;
import config.Storages;
import interfaces.conf_terminal;
import interfaces.login;
import java.sql.Connection;

/**
 *
 * @author wito
 */
public class servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Storages.infoTerminal();
        if (args.length > 0 && args[0].equals("servidor")) {
            DataBase N = new DataBase();
            Connection C = N.getConexion();
            new servicios_ini(C).start();
            new servicios_ini(C).backups();
            
        } else if (args.length > 0 && args[0].equals("config")) {
            conf_terminal.main(null);
        } else {
            login.main(null);
        }
    }

}
