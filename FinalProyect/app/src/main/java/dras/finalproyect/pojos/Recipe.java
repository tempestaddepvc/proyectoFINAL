
package dras.finalproyect.pojos;

import java.util.ArrayList;
import java.util.List;
public class Recipe {

    private Integer idrecipe;
    private String name;
    private String details;
    private String picture;
    private Integer difficulty;
    private Integer time;
    private Integer diners;
    private String creator;
    private List<Quantity> quantities = null;
    private List<Step> steps = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Recipe() {
    }

//    /**
//     *
//     * @param picture
//     * @param time
//     * @param idrecipe
//     * @param details
//     * @param name
//     * @param diners
//     * @param difficulty
//     * @param creator
//     */
//    public Recipe(Integer idrecipe, String name, String details, String picture, Integer difficulty, Integer time, Integer diners, String creator) {
//        this.idrecipe = idrecipe;
//        this.name = name;
//        this.details = details;
//        this.picture = picture;
//        this.difficulty = difficulty;
//        this.time = time;
//        this.diners = diners;
//        this.creator = creator;
//    }


    /**
     * 
     * @param picture
     * @param time
     * @param idrecipe
     * @param details
     * @param quantities
     * @param name
     * @param diners
     * @param difficulty
     * @param steps
     * @param creator
     */
    public Recipe(Integer idrecipe, String name, String details, String picture, Integer difficulty, Integer time, Integer diners, String creator, List<Quantity> quantities, List<Step> steps) {
        this.idrecipe = idrecipe;
        this.name = name;
        this.details = details;
        this.picture = picture;
        this.difficulty = difficulty;
        this.time = time;
        this.diners = diners;
        this.creator = creator;
        this.quantities = new ArrayList<Quantity>();
        this.quantities = quantities;
        this.steps = new ArrayList<Step>();
        this.steps = steps;
    }

    /**
     * 
     * @return
     *     The idrecipe
     */
    public Integer getIdrecipe() {
        return idrecipe;
    }

    /**
     * 
     * @param idrecipe
     *     The idrecipe
     */
    public void setIdrecipe(Integer idrecipe) {
        this.idrecipe = idrecipe;
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
     *     The details
     */
    public String getDetails() {
        return details;
    }

    /**
     * 
     * @param details
     *     The details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * 
     * @return
     *     The picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 
     * @param picture
     *     The picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * 
     * @return
     *     The difficulty
     */
    public Integer getDifficulty() {
        return difficulty;
    }

    /**
     * 
     * @param difficulty
     *     The difficulty
     */
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * 
     * @return
     *     The time
     */
    public Integer getTime() {
        return time;
    }

    /**
     * 
     * @param time
     *     The time
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * 
     * @return
     *     The diners
     */
    public Integer getDiners() {
        return diners;
    }

    /**
     * 
     * @param diners
     *     The diners
     */
    public void setDiners(Integer diners) {
        this.diners = diners;
    }

    /**
     * 
     * @return
     *     The creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 
     * @param creator
     *     The creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 
     * @return
     *     The quantities
     */
    public List<Quantity> getQuantities() {
        return quantities;
    }

    /**
     * 
     * @param quantities
     *     The quantities
     */
    public void setQuantities(List<Quantity> quantities) {
        this.quantities = quantities;
    }

    /**
     * 
     * @return
     *     The steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * 
     * @param steps
     *     The steps
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

}
