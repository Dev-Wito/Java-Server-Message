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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
                System.out.println("Procesando solicitud PID: " + Math.round(Math.random() * 10000));
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
            case "getCuentas":
                Response = getCuentas();
                break;
            case "getTiposCuentas":
                Response = getTiposCuentas();
                break;
            case "getClientes":
                Response = getClientes();
                break;
            case "setMovimiento":
                Response = setMovimiento(obj);
                break;
            case "setCliente":
                Response = setCliente(obj);
                break;
            case "setCuenta":
                Response = setCuenta(obj);
                break;
            case "getAllUsuarios":
                Response = getAllUsuarios(obj.get("Search").toString());
                break;
            case "setUsuario":
                Response = setUsuario(obj);
                break;
            case "dropUser":
                Response = dropUser(obj.get("id").toString());
                break;
            case "setBitacoraSesion":
                Response = setBitacoraSesion(obj);
                break;
            case "setBitacoraFinSesion":
                Response = setBitacoraFinSesion(obj);
                break;
            case "getBitSesiones":
                Response = getBitSesiones();
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
        ResultSet Response = this.getQuery("SELECT cuentas.id,CONCAT(tipo_cuentas.nombre,\" N°:\", cuentas.numero_cuenta) AS label FROM cuentas INNER JOIN tipo_cuentas  ON (cuentas.tipo_cuenta_id = tipo_cuentas.id) WHERE cuentas.persona_id=" + $ID);
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

    protected String setMovimiento(JSONObject obj) {
        String rta = "";
        ResultSet Response;
        String SQL_Insert = "CALL SP_REGISTRAR_MOVIMIENTO(?,?,?,?)";
        try {
            CallableStatement Sentencia = ConectDB.prepareCall(SQL_Insert);
            Sentencia.setInt(1, Integer.parseInt(obj.get("cuenta_id").toString()));
            Sentencia.setInt(2, Integer.parseInt(obj.get("tipo_movimiento_id").toString()));
            Sentencia.setInt(3, Integer.parseInt(obj.get("sucursal_id").toString()));
            Sentencia.setInt(4, Integer.parseInt(obj.get("valor_movimiento").toString()));
            Sentencia.execute();
            Response = Sentencia.getResultSet();
            while (Response.next()) {
                obj.clear();
                obj.put("result", Response.getInt("result"));
                obj.put("msg", Response.getString("MSG"));
                obj.put("saldo_anterior", Response.getInt("saldo_anterior"));
                obj.put("saldo_actual", Response.getInt("saldo"));
                obj.put("costo_movimiento", Response.getInt("costo_movimiento"));
                break;
            }
            rta = json.encode(obj);

        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }

    protected String getCuentas() {
        String $rta = "";
        JSONObject vector = new JSONObject();
        ResultSet Response = this.getQuery("SELECT cuentas.numero_cuenta, tipo_cuentas.nombre AS nombre_cuenta, DATE_FORMAT(cuentas.fecha_apertura,\"%d-%m-%Y\") AS fecha_apertura, personas.nombres AS nombre_persona, cuentas.saldo FROM cuentas INNER JOIN tipo_cuentas ON (tipo_cuentas.id=cuentas.tipo_cuenta_id) INNER JOIN personas ON (personas.id=cuentas.persona_id)");
        try {
            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String cuentas[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                vector.clear();
                vector.put("numero_cuenta", Response.getString("numero_cuenta"));
                vector.put("nombre_cuenta", Response.getString("nombre_cuenta"));
                vector.put("fecha_apertura", Response.getString("fecha_apertura"));
                vector.put("nombre_persona", Response.getString("nombre_persona"));
                vector.put("saldo", Response.getString("saldo"));
                cuentas[v] = json.encode(vector);
            }
            $rta = "{\"cuentas\":[" + String.join(",", cuentas) + "]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return $rta;
    }

    protected String setCliente(JSONObject obj) {
        String rta = "fail";
        PreparedStatement SentenciaPersonas = null, SentenciaUsuarios = null;
        String sql1 = "INSERT INTO personas (num_ident, nombres, correo, celular) VALUES (?,?,?,?)";
        String sql2 = "INSERT INTO usuarios (persona_id, usuario, llave, rol) VALUES (?,?,PASSWORD(?),'Cliente')";
        try {
            SentenciaPersonas = ConectDB.prepareStatement(sql1, PreparedStatement.RETURN_GENERATED_KEYS);
            SentenciaPersonas.setString(1, obj.get("num_ident").toString());
            SentenciaPersonas.setString(2, obj.get("nombres").toString());
            SentenciaPersonas.setString(3, obj.get("correo").toString());
            SentenciaPersonas.setString(4, obj.get("celular").toString());
            SentenciaPersonas.executeUpdate();
            int idPersona = 0;
            ResultSet keys = SentenciaPersonas.getGeneratedKeys();
            keys.first();
            idPersona = keys.getInt(1);
            keys.close();

            SentenciaUsuarios = ConectDB.prepareStatement(sql2);
            SentenciaUsuarios.setInt(1, idPersona);
            SentenciaUsuarios.setString(2, obj.get("usuario").toString());
            SentenciaUsuarios.setString(3, obj.get("usuario").toString());
            SentenciaUsuarios.executeUpdate();

            rta = "success";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }

    protected String getTiposCuentas() {
        String $rta = "";
        JSONObject vector = new JSONObject();
        ResultSet Response = this.getQuery("SELECT id, nombre AS label FROM tipo_cuentas");
        try {
            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String tipoCuentas[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                vector.clear();
                vector.put("id", Response.getInt("id"));
                vector.put("label", Response.getString("label"));
                tipoCuentas[v] = json.encode(vector);
            }
            $rta = "{\"tiposCuentas\":[" + String.join(",", tipoCuentas) + "]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return $rta;
    }

    protected String getClientes() {
        String $rta = "";
        JSONObject vector = new JSONObject();
        ResultSet Response = this.getQuery("SELECT personas.id, CONCAT_WS(' - ',personas.num_ident, personas.nombres) AS label FROM personas INNER JOIN usuarios ON (usuarios.persona_id=personas.id) WHERE usuarios.rol='Cliente'");
        try {
            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String clientes[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                vector.clear();
                vector.put("id", Response.getInt("id"));
                vector.put("label", Response.getString("label"));
                clientes[v] = json.encode(vector);
            }
            $rta = "{\"clientes\":[" + String.join(",", clientes) + "]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return $rta;
    }

    protected String setCuenta(JSONObject obj) {
        String rta = "fail";
        PreparedStatement SentenciaCuentas = null;
        String sql = "INSERT INTO cuentas (persona_id, tipo_cuenta_id, sucursal_id, saldo) VALUES (?,?,?,?)";
        try {
            SentenciaCuentas = ConectDB.prepareStatement(sql);
            SentenciaCuentas.setInt(1, Integer.parseInt(obj.get("cliente_id").toString()));
            SentenciaCuentas.setInt(2, Integer.parseInt(obj.get("tipo_cuenta_id").toString()));
            SentenciaCuentas.setInt(3, Integer.parseInt(obj.get("sucursal_id").toString()));
            SentenciaCuentas.setInt(4, Integer.parseInt(obj.get("saldo_ini").toString()));
            SentenciaCuentas.executeUpdate();

            rta = "success";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }

    protected String getAllUsuarios(String Search) {
        String rta = "";
        JSONObject vector = new JSONObject();
        String SQL = "SELECT usuarios.id , personas.num_ident , personas.nombres , personas.correo, personas.celular, usuarios.usuario , usuarios.rol FROM usuarios INNER JOIN personas  ON (usuarios.persona_id = personas.id)";
        if (!Search.equals("")) {
            SQL = SQL + " WHERE num_ident LIKE '%" + Search + "%' OR usuario LIKE '%" + Search + "%' OR nombres LIKE '%" + Search + "%' OR rol LIKE '%" + Search + "%'";
        }
        ResultSet Response = this.getQuery(SQL);
        try {
            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String usuarios[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                vector.clear();
                vector.put("id", Response.getInt("id"));
                vector.put("nuid", Response.getString("num_ident"));
                vector.put("nom", Response.getString("nombres"));
                vector.put("cor", Response.getString("correo"));
                vector.put("cel", Response.getString("celular"));
                vector.put("usu", Response.getString("usuario"));
                vector.put("rol", Response.getString("rol"));

                usuarios[v] = json.encode(vector);
            }
            rta = "{\"usuarios\":[" + String.join(",", usuarios) + "]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }

    protected String setUsuario(JSONObject obj) {
        String rta = "";
        ResultSet Response;
        String SQL_Insert = "CALL SP_REGISTRAR_USUARIO(?,?,?,?,?,?,?,?)";
        try {
            CallableStatement Sentencia = ConectDB.prepareCall(SQL_Insert);
            Sentencia.setInt(1, Integer.parseInt(obj.get("id").toString()));
            Sentencia.setString(2, obj.get("usu").toString());
            Sentencia.setString(3, obj.get("key").toString());
            Sentencia.setString(4, obj.get("rol").toString());
            Sentencia.setString(5, obj.get("nuid").toString());
            Sentencia.setString(6, obj.get("nom").toString());
            Sentencia.setString(7, obj.get("cor").toString());
            Sentencia.setString(8, obj.get("cel").toString());
            Sentencia.execute();
            Response = Sentencia.getResultSet();
            while (Response.next()) {
                obj.clear();
                obj.put("msg", Response.getString("msg"));
                break;
            }
            rta = json.encode(obj);

        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }

    protected String dropUser(String id) {
        String rta = "{\"msg\":\"Fallo la eliminacion\"}";
        PreparedStatement SentenciaCuentas = null;
        String sql = "DELETE FROM personas WHERE id=(SELECT persona_id FROM usuarios WHERE usuarios.id=? LIMIT 1)";
        try {
            SentenciaCuentas = ConectDB.prepareStatement(sql);
            SentenciaCuentas.setInt(1, Integer.parseInt(id));
            SentenciaCuentas.executeUpdate();
            rta = "{\"msg\":\"Se elimino correctamente\"}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }

    protected String setBitacoraSesion(JSONObject obj){
    String rta="";
    PreparedStatement SentenciaBitacora = null;
        String sql = "INSERT INTO bitacora_sesion (usuario_id, sucursal_id, inicio) VALUES (?, ?, CURRENT_TIMESTAMP())";
        try {
            SentenciaBitacora = ConectDB.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            SentenciaBitacora.setInt(1, Integer.parseInt(obj.get("usuario_id").toString()));
            SentenciaBitacora.setInt(2, Integer.parseInt(obj.get("sucursal_id").toString()));
            SentenciaBitacora.executeUpdate();
            
            int idSesion;
            ResultSet keys = SentenciaBitacora.getGeneratedKeys();
            keys.first();
            idSesion = keys.getInt(1);
            keys.close();

            rta += idSesion;
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }
    
    protected String setBitacoraFinSesion(JSONObject obj){
    String rta="fail";
    PreparedStatement SentenciaBitacora = null;
        String sql = "UPDATE bitacora_sesion SET fin = CURRENT_TIMESTAMP() WHERE id = ?";
        try {
            SentenciaBitacora = ConectDB.prepareStatement(sql);
            SentenciaBitacora.setInt(1, Integer.parseInt(obj.get("idSesion").toString()));
            SentenciaBitacora.executeUpdate();
            
            rta = "success";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }
    
    protected String getBitSesiones(){
        String rta="";
        String sql="SELECT usuarios.usuario, usuarios.rol, personas.nombres AS nombre, sucursales.nombre AS sucursal, sucursales.ciudad, DATE_FORMAT(bitacora_sesion.inicio, '%d-%m-%Y') AS fechaInicio, DATE_FORMAT(bitacora_sesion.inicio, '%h:%i:%s %p') AS horaInicio, DATE_FORMAT(bitacora_sesion.fin, '%d-%m-%Y') AS fechaFin, DATE_FORMAT(bitacora_sesion.fin, '%h:%i:%s %p') AS horaFin "
                + "FROM bitacora_sesion "
                + "INNER JOIN usuarios ON (usuarios.id=bitacora_sesion.usuario_id) "
                + "INNER JOIN personas ON (personas.id=usuarios.persona_id) "
                + "INNER JOIN sucursales ON (sucursales.id=bitacora_sesion.sucursal_id) "
                + "ORDER BY bitacora_sesion.id DESC";
        JSONObject vector = new JSONObject();
        ResultSet Response = this.getQuery(sql);
        try {
            Response.last();
            int cantFilas = Response.getRow();
            Response.beforeFirst();
            String sesiones[] = new String[cantFilas];
            for (int v = 0; Response.next(); v++) {
                vector.clear();
                vector.put("usuario", Response.getString("usuario"));
                vector.put("rol", Response.getString("rol"));
                vector.put("nombre", Response.getString("nombre"));
                vector.put("sucursal", Response.getString("sucursal"));
                vector.put("ciudad", Response.getString("ciudad"));
                vector.put("fechaInicio", Response.getString("fechaInicio"));
                vector.put("horaInicio", Response.getString("horaInicio"));
                vector.put("fechaFin", Response.getString("fechaFin"));
                vector.put("horaFin", Response.getString("horaFin"));
                sesiones[v] = json.encode(vector);
            }
            rta = "{\"sesiones\":[" + String.join(",", sesiones) + "]}";
        } catch (SQLException ex) {
            Logger.getLogger(servicios_ini.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rta;
    }
}
