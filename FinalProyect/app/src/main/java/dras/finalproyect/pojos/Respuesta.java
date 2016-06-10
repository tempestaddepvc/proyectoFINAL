
package dras.finalproyect.pojos;


public class Respuesta {

    private Boolean error;
    private Object message;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Respuesta() {
    }

    /**
     * 
     * @param message
     * @param error
     */
    public Respuesta(Boolean error, Object message) {
        this.error = error;
        this.message = message;
    }

    /**
     * 
     * @return
     *     The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     * 
     * @param error
     *     The error
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     * 
     * @return
     *     The message
     */
    public Object getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
