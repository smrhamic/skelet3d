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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents localized information about a category.
 * This entity class is mapped to "CATEGORYINFO" database table.
 *
 * @author Michal Smrha
 * @see atlas.entity.Category
 * @see atlas.entity.view.CategoryView
 */
@Entity
@Table(name = "CATEGORYINFO")
@XmlRootElement
public class CategoryInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CategoryInfoPK categoryInfoPK;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "CATEGORY", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Category category1;
    @JoinColumn(name = "LANGUAGE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Language language1;

    public CategoryInfo() {
    }

    public CategoryInfo(CategoryInfoPK categoryInfoPK) {
        this.categoryInfoPK = categoryInfoPK;
    }

    public CategoryInfo(int category, int language) {
        this.categoryInfoPK = new CategoryInfoPK(category, language);
    }

    public CategoryInfoPK getCategoryInfoPK() {
        return categoryInfoPK;
    }

    public void setCategoryInfoPK(CategoryInfoPK categoryInfoPK) {
        this.categoryInfoPK = categoryInfoPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory1() {
        return category1;
    }

    public void setCategory1(Category category1) {
        this.category1 = category1;
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
        hash += (categoryInfoPK != null ? categoryInfoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoryInfo)) {
            return false;
        }
        CategoryInfo other = (CategoryInfo) object;
        if ((this.categoryInfoPK == null && other.categoryInfoPK != null) || (this.categoryInfoPK != null && !this.categoryInfoPK.equals(other.categoryInfoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.CategoryInfo[ categoryInfoPK=" + categoryInfoPK + " ]";
    }
    
}