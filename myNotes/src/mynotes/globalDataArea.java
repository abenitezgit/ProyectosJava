/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynotes;

/**
 *
 * @author andresbenitez
 */
public class globalDataArea {
    private String catID;
    private String cmdMenu;
    private int maxNoteID;

    public int getMaxNoteID() {
        return maxNoteID;
    }

    public void setMaxNoteID(int maxNoteID) {
        this.maxNoteID = maxNoteID;
    }

    public String getCmdMenu() {
        return cmdMenu;
    }

    public void setCmdMenu(String cmdMenu) {
        this.cmdMenu = cmdMenu;
    }
    
    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }
    
}
