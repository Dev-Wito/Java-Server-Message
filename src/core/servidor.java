/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import config.DataBase;
import interfaces.Login;

/**
 *
 * @author wito
 */
public class servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("cliente")) {
            Login.main(null);
        } else {
            DataBase N = new DataBase();
            new servicios_ini(N.getConexion()).start();
        }
    }

}
