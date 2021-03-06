/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents localized content of a page.
 * This entity class is mapped to "PAGECONTENT" database table.
 *
 * @author Michal Smrha
 * @see atlas.entity.Page
 * @see atlas.entity.PageComponent
 */
@Entity
@Table(name = "PAGECONTENT")
@XmlRootElement
public class PageContent implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PageContentPK pageContentPK;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @Column(name = "PUBLISHED")
    private Boolean published;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "pageContent")
    private List<ImageComponent> imageComponentList;
    @JoinColumn(name = "LANGUAGE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Language language1;
    @JoinColumn(name = "PAGE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Page page1;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "pageContent")
    private List<HeadlineComponent> headlineComponentList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "pageContent")
    private List<TextComponent> textComponentList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "pageContent")
    private List<ModelComponent> modelComponentList;

    public PageContent() {
    }

    public PageContent(PageContentPK pageContentPK) {
        this.pageContentPK = pageContentPK;
    }

    public PageContent(int page, int language) {
        this.pageContentPK = new PageContentPK(page, language);
    }
    
    public PageContentPK getPageContentPK() {
        return pageContentPK;
    }

    public void setPageContentPK(PageContentPK pageContentPK) {
        this.pageContentPK = pageContentPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    @XmlTransient
    public List<ImageComponent> getImageComponentList() {
        return imageComponentList;
    }

    public void setImageComponentList(List<ImageComponent> imageComponentList) {
        this.imageComponentList = imageComponentList;
    }

    public Language getLanguage1() {
        return language1;
    }

    public void setLanguage1(Language language1) {
        this.language1 = language1;
    }

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page page1) {
        this.page1 = page1;
        if (!page1.getPageContentList().contains(this)) {
            page1.getPageContentList().add(this);
        }
    }

    @XmlTransient
    public List<HeadlineComponent> getHeadlineComponentList() {
        return headlineComponentList;
    }

    public void setHeadlineComponentList(List<HeadlineComponent> headlineComponentList) {
        this.headlineComponentList = headlineComponentList;
    }

    @XmlTransient
    public List<TextComponent> getTextComponentList() {
        return textComponentList;
    }

    public void setTextComponentList(List<TextComponent> textComponentList) {
        this.textComponentList = textComponentList;
    }

    @XmlTransient
    public List<ModelComponent> getModelComponentList() {
        return modelComponentList;
    }

    public void setModelComponentList(List<ModelComponent> modelComponentList) {
        this.modelComponentList = modelComponentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pageContentPK != null ? pageContentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PageContent)) {
            return false;
        }
        PageContent other = (PageContent) object;
        if ((this.pageContentPK == null && other.pageContentPK != null) || (this.pageContentPK != null && !this.pageContentPK.equals(other.pageContentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.PageContent[ pageContentPK=" + pageContentPK + " ]";
    }
    
}
