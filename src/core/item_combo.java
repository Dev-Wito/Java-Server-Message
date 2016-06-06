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
public class item_combo {

    private int id;
    private String label;

    public item_combo(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.label;
    }

    public String getLabel() {
        return this.label;
    }
}
