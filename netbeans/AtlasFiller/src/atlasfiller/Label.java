/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlasfiller;

/**
 *
 * @author Michal
 */
public class Label {
    
    private String titleEn;
    private String titleCz;
    private String textEn;
    private String textCz;
    private double markX;
    private double markY;
    private double markZ;
    private double labelX;
    private double labelY;
    private double labelZ;


    // rescale all coordinates
    public void rescale(double scale) {
        markX *= scale;
        markY *= scale;
        markZ *= scale;
        labelX *= scale;
        labelY *= scale;
        labelZ *= scale;
    }
    
    // change distance of mark and label (line length)
    public void changeLength(double scale) {
        labelX = markX + scale*(labelX - markX);
        labelY = markY + scale*(labelY - markY);
        labelZ = markZ + scale*(labelZ - markZ);
    }
    
    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleCz() {
        return titleCz;
    }

    public void setTitleCz(String titleCz) {
        this.titleCz = titleCz;
    }

    public String getTextEn() {
        return textEn;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public String getTextCz() {
        return textCz;
    }

    public void setTextCz(String textCz) {
        this.textCz = textCz;
    }

    public double getMarkX() {
        return markX;
    }

    public void setMarkX(double markX) {
        this.markX = markX;
    }

    public double getMarkY() {
        return markY;
    }

    public void setMarkY(double markY) {
        this.markY = markY;
    }

    public double getMarkZ() {
        return markZ;
    }

    public void setMarkZ(double markZ) {
        this.markZ = markZ;
    }

    public double getLabelX() {
        return labelX;
    }

    public void setLabelX(double labelX) {
        this.labelX = labelX;
    }

    public double getLabelY() {
        return labelY;
    }

    public void setLabelY(double labelY) {
        this.labelY = labelY;
    }

    public double getLabelZ() {
        return labelZ;
    }

    public void setLabelZ(double labelZ) {
        this.labelZ = labelZ;
    }

}
