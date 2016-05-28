
package dras.finalproyect.pojos;

public class Quantity {

    private Integer idingredient;
    private String name;
    private Integer cant;
    private String measure;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Quantity() {
    }

    /**
     * 
     * @param measure
     * @param cant
     * @param idingredient
     * @param name
     */
    public Quantity(Integer idingredient, String name, Integer cant, String measure) {
        this.idingredient = idingredient;
        this.name = name;
        this.cant = cant;
        this.measure = measure;
    }

    /**
     * 
     * @return
     *     The idingredient
     */
    public Integer getIdingredient() {
        return idingredient;
    }

    /**
     * 
     * @param idingredient
     *     The idingredient
     */
    public void setIdingredient(Integer idingredient) {
        this.idingredient = idingredient;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The cant
     */
    public Integer getCant() {
        return cant;
    }

    /**
     * 
     * @param cant
     *     The cant
     */
    public void setCant(Integer cant) {
        this.cant = cant;
    }

    /**
     * 
     * @return
     *     The measure
     */
    public String getMeasure() {
        return measure;
    }

    /**
     * 
     * @param measure
     *     The measure
     */
    public void setMeasure(String measure) {
        this.measure = measure;
    }

}
