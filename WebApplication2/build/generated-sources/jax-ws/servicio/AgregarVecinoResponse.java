
package servicio;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AgregarVecinoResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "agregarVecinoResult"
})
@XmlRootElement(name = "AgregarVecinoResponse")
public class AgregarVecinoResponse {

    @XmlElementRef(name = "AgregarVecinoResult", namespace = "http://Servicio", type = JAXBElement.class, required = false)
    protected JAXBElement<String> agregarVecinoResult;

    /**
     * Obtiene el valor de la propiedad agregarVecinoResult.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAgregarVecinoResult() {
        return agregarVecinoResult;
    }

    /**
     * Define el valor de la propiedad agregarVecinoResult.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAgregarVecinoResult(JAXBElement<String> value) {
        this.agregarVecinoResult = value;
    }

}
