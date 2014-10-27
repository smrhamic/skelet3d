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
 *
 * @author Michal
 */
@Embeddable
public class CategoryInfoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "CATEGORY")
    private int category;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LANGUAGE")
    private int language;

    public CategoryInfoPK() {
    }

    public CategoryInfoPK(int category, int language) {
        this.category = category;
        this.language = language;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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
        hash += (int) category;
        hash += (int) language;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoryInfoPK)) {
            return false;
        }
        CategoryInfoPK other = (CategoryInfoPK) object;
        if (this.category != other.category) {
            return false;
        }
        if (this.language != other.language) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.CategoryInfoPK[ category=" + category + ", language=" + language + " ]";
    }
    
}
