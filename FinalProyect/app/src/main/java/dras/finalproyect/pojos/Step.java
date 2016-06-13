
package dras.finalproyect.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable, Comparable {

    private Integer idmaking;
    private String step;
    private String picture;

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
    public Step(Integer idmaking, String step, String picture) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.idmaking);
        dest.writeString(this.step);
        dest.writeString(this.picture);
    }

    protected Step(Parcel in) {
        this.idmaking = (Integer) in.readValue(Integer.class.getClassLoader());
        this.step = in.readString();
        this.picture = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int compareTo(Object another) {
        if(this.idmaking > ((Step) another).getIdmaking())
            return 1;
        if(this.idmaking < ((Step) another).getIdmaking())
            return -1;
        return 0;
    }
}
