
package dras.finalproyect.pojos;

public class Step {

    private Integer idmaking;
    private String step;
    private Object picture;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Step() {
    }

    /**
     * 
     * @param picture
     * @param idmaking
     * @param step
     */
    public Step(Integer idmaking, String step, Object picture) {
        this.idmaking = idmaking;
        this.step = step;
        this.picture = picture;
    }

    /**
     * 
     * @return
     *     The idmaking
     */
    public Integer getIdmaking() {
        return idmaking;
    }

    /**
     * 
     * @param idmaking
     *     The idmaking
     */
    public void setIdmaking(Integer idmaking) {
        this.idmaking = idmaking;
    }

    /**
     * 
     * @return
     *     The step
     */
    public String getStep() {
        return step;
    }

    /**
     * 
     * @param step
     *     The step
     */
    public void setStep(String step) {
        this.step = step;
    }

    /**
     * 
     * @return
     *     The picture
     */
    public Object getPicture() {
        return picture;
    }

    /**
     * 
     * @param picture
     *     The picture
     */
    public void setPicture(Object picture) {
        this.picture = picture;
    }

}
