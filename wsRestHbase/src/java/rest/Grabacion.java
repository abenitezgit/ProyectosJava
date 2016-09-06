/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "grabacion")
public class Grabacion implements Serializable {

   private static final long serialVersionUID = 1L;
   
   private String  conniID;
   private String interactionID;
   private String name;
   private String dnis;
   private String ani;
   private String urlPath;
   private String fecha;
   

   public Grabacion(){}
   
   public Grabacion(String connID, String interactionID, String name, String dnis, String ani, String urlPath, String fecha){
      this.conniID = connID;
      this.interactionID = interactionID;
      this.name = name;
      this.dnis = dnis;
      this.ani = ani;
      this.urlPath = urlPath;
      this.fecha = fecha;
   }

    public String getConniID() {
        return conniID;
    }

    @XmlElement
    public void setConniID(String conniID) {
        this.conniID = conniID;
    }

    public String getInteractionID() {
        return interactionID;
    }

    @XmlElement
    public void setInteractionID(String interactionID) {
        this.interactionID = interactionID;
    }

    public String getDnis() {
        return dnis;
    }

    @XmlElement
    public void setDnis(String dnis) {
        this.dnis = dnis;
    }

    public String getAni() {
        return ani;
    }

    @XmlElement
    public void setAni(String ani) {
        this.ani = ani;
    }

    public String getUrlPath() {
        return urlPath;
    }

    @XmlElement
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getFecha() {
        return fecha;
    }

    @XmlElement
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

   public String getName() {
      return name;
   }
   @XmlElement
   public void setName(String name) {
      this.name = name;
   }
}