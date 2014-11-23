/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity.view;

import atlas.entity.Language;
import atlas.entity.Model;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Represents a set of attributes of LabelContent and its related Label.
 * Namely, attributes needed for displaying labels such as localized
 * title, text and label position.
 * 
 * Also used to persist updates made in the view.
 * 
 * @author Michal
 */
public class LabelView {
        
    private int id;
    private String title;
    private String text;
    private String action;
    private double markX, markY, markZ;
    private double labelX, labelY, labelZ;

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

//    public static List<LabelView> getLabelViews(
//        EntityManager em, Model model, Language lang) {
//        // find labels for model in language
//
//        TypedQuery<LabelView> query = em.createQuery(
//                "SELECT NEW atlas.entity.view.LabelView(l.id, lc.title, lc.text, "
//                        + "l.markX, l.markY, l.markZ, "
//                        + "l.labelX, l.labelY, l.labelZ) "
//                        + "FROM Label l JOIN l.labelContentList lc "
//                        + "WHERE l.model = :model AND lc.language1 = :lang",
//                LabelView.class);
//        List<LabelView> lvs = query.setParameter("model", model).setParameter("lang", lang)
//                .getResultList();
//
//        return lvs;
//    }

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
