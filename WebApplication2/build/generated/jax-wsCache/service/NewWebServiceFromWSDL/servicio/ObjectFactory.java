
package servicio;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the servicio package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ListaVecinosResponseListaVecinosResult_QNAME = new QName("http://Servicio", "ListaVecinosResult");
    private final static QName _AgregarVecinoResponseAgregarVecinoResult_QNAME = new QName("http://Servicio", "AgregarVecinoResult");
    private final static QName _AgregarVecinoNombre_QNAME = new QName("http://Servicio", "nombre");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: servicio
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AgregarVecinoResponse }
     * 
     */
    public AgregarVecinoResponse createAgregarVecinoResponse() {
        return new AgregarVecinoResponse();
    }

    /**
     * Create an instance of {@link ListaVecinosResponse }
     * 
     */
    public ListaVecinosResponse createListaVecinosResponse() {
        return new ListaVecinosResponse();
    }

    /**
     * Create an instance of {@link ListaVecinos }
     * 
     */
    public ListaVecinos createListaVecinos() {
        return new ListaVecinos();
    }

    /**
     * Create an instance of {@link AgregarVecino }
     * 
     */
    public AgregarVecino createAgregarVecino() {
        return new AgregarVecino();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Servicio", name = "ListaVecinosResult", scope = ListaVecinosResponse.class)
    public JAXBElement<ArrayOfstring> createListaVecinosResponseListaVecinosResult(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ListaVecinosResponseListaVecinosResult_QNAME, ArrayOfstring.class, ListaVecinosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Servicio", name = "AgregarVecinoResult", scope = AgregarVecinoResponse.class)
    public JAXBElement<String> createAgregarVecinoResponseAgregarVecinoResult(String value) {
        return new JAXBElement<String>(_AgregarVecinoResponseAgregarVecinoResult_QNAME, String.class, AgregarVecinoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Servicio", name = "nombre", scope = AgregarVecino.class)
    public JAXBElement<String> createAgregarVecinoNombre(String value) {
        return new JAXBElement<String>(_AgregarVecinoNombre_QNAME, String.class, AgregarVecino.class, value);
    }

}
