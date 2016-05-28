
package dras.finalproyect.pojos;

public class Ingredient {

    private Integer idingredient;
    private String name;

    /**
     *
     * @param idingredient
     * @param name
     */
    public Ingredient(Integer idingredient, String name) {
        this.idingredient = idingredient;
        this.name = name;
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



}
