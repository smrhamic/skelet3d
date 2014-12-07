/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity.view;

/**
 * Represents a localized view of label.
 * Basically a set of attributes of LabelContent and its related Label.
 * 
 * @author Michal Smrha
 * @see atlas.entity.Label
 * @see atlas.entity.LabelContent
 */
public class LabelView {
        
    private int id;
    private String title;
    private String text;
    private String action;
    private double markX, markY, markZ;
    private double labelX, labelY, labelZ;

    /**
     * Constructs a populated LabelView.
     *
     * @param id Label ID
     * @param title Localized title
     * @param text Localized text (description, details)
     * @param markX X coordinate of the mark (point on 3D model)
     * @param markY Y coordinate of the mark (point on 3D model)
     * @param markZ Z coordinate of the mark (point on 3D model)
     * @param labelX X coordinate of the label (where label floats)
     * @param labelY Y coordinate of the label (where label floats)
     * @param labelZ Z coordinate of the label (where label floats)
     */
    public LabelView(int id, String title, String text,
            double markX, double markY, double markZ,
            double labelX, double labelY, double labelZ) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.markX = markX;
        this.markY = markY;
        this.markZ = markZ;
        this.labelX = labelX;
        this.labelY = labelY;
        this.labelZ = labelZ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
