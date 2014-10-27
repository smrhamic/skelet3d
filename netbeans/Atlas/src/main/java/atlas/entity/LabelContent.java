/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Michal
 */
@Entity
@Table(name = "LABELCONTENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LabelContent.findAll", query = "SELECT l FROM LabelContent l")})
public class LabelContent implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LabelContentPK labelContentPK;
    @Size(max = 255)
    @Column(name = "TITLE")
    private String title;
    @Lob
    @Column(name = "TEXT")
    private String text;
    @JoinColumn(name = "LABEL", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Label label1;
    @JoinColumn(name = "LANGUAGE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Language language1;

    public LabelContent() {
    }

    public LabelContent(LabelContentPK labelContentPK) {
        this.labelContentPK = labelContentPK;
    }

    public LabelContent(int label, int language) {
        this.labelContentPK = new LabelContentPK(label, language);
    }

    public LabelContentPK getLabelContentPK() {
        return labelContentPK;
    }

    public void setLabelContentPK(LabelContentPK labelContentPK) {
        this.labelContentPK = labelContentPK;
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

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label label1) {
        this.label1 = label1;
    }

    public Language getLanguage1() {
        return language1;
    }

    public void setLanguage1(Language language1) {
        this.language1 = language1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (labelContentPK != null ? labelContentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LabelContent)) {
            return false;
        }
        LabelContent other = (LabelContent) object;
        if ((this.labelContentPK == null && other.labelContentPK != null) || (this.labelContentPK != null && !this.labelContentPK.equals(other.labelContentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.LabelContent[ labelContentPK=" + labelContentPK + " ]";
    }
    
}
