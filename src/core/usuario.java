/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author wito
 */
public class usuario {

    private int id;
    private String Documento;
    private String Nombres;
    private String Correo;
    private String Celular;
    private String Login;
    private String Rol;

    public usuario(int id, String Documento, String Nombres, String Correo, String Celular, String Login, String Rol) {
        this.id = id;
        this.Documento = Documento;
        this.Nombres = Nombres;
        this.Correo = Correo;
        this.Celular = Celular;
        this.Login = Login;
        this.Rol = Rol;
    }

    public int getId() {
        return this.id;
    }

    public String getDoc() {
        return this.Documento;
    }

    public String getNom() {
        return this.Nombres;
    }

    public String getCorr() {
        return this.Correo;
    }

    public String getCel() {
        return this.Celular;
    }

    public String getLog() {
        return this.Login;
    }

    public String getRol() {
        return this.Rol;
    }

}
