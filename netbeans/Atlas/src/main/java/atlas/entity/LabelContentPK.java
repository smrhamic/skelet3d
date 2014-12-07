/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Primary key for LabelContent.
 *
 * @author Michal Smrha
 * @see atlas.entity.LabelContent
 */
@Embeddable
public class LabelContentPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "LABEL")
    private int label;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LANGUAGE")
    private int language;

    public LabelContentPK() {
    }

    public LabelContentPK(int label, int language) {
        this.label = label;
        this.language = language;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) label;
        hash += (int) language;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LabelContentPK)) {
            return false;
        }
        LabelContentPK other = (LabelContentPK) object;
        if (this.label != other.label) {
            return false;
        }
        if (this.language != other.language) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.LabelContentPK[ label=" + label + ", language=" + language + " ]";
    }
    
}
