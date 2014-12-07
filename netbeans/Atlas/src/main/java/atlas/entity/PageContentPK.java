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
 * Primary key for PageContent.
 *
 * @author Michal Smrha
 * @see atlas.entity.PageContent
 */
@Embeddable
public class PageContentPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PAGE")
    private int page;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LANGUAGE")
    private int language;

    public PageContentPK() {
    }

    public PageContentPK(int page, int language) {
        this.page = page;
        this.language = language;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
        hash += (int) page;
        hash += (int) language;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PageContentPK)) {
            return false;
        }
        PageContentPK other = (PageContentPK) object;
        if (this.page != other.page) {
            return false;
        }
        if (this.language != other.language) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.PageContentPK[ page=" + page + ", language=" + language + " ]";
    }
    
}
