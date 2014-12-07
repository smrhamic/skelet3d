/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a section headline in page content.
 * This entity class is mapped to "HEADLINECOMPONENT" database table.
 * In addition to mapped properties, common PageComponent properties
 * are present.
 *
 * @author Michal Smrha
 * @see atlas.entity.PageContent
 * @see atlas.entity.PageComponent
 */
@Entity
@Table(name = "HEADLINECOMPONENT")
@XmlRootElement
public class HeadlineComponent extends PageComponent implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "TEXT")
    private String text;
    @Column(name = "COMP_ORDER")
    private Integer compOrder;
    @JoinColumns({
        @JoinColumn(name = "PAGE", referencedColumnName = "PAGE"),
        @JoinColumn(name = "LANGUAGE", referencedColumnName = "LANGUAGE")})
    @ManyToOne(optional = false)
    private PageContent pageContent;

    public HeadlineComponent() {
    }

    public HeadlineComponent(Integer id) {
        this.id = id;
    }

    @Override
    public String getComponentType() {
        return "headline";
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Integer getCompOrder() {
        return compOrder;
    }

    @Override
    public void setCompOrder(Integer compOrder) {
        this.compOrder = compOrder;
    }

    public PageContent getPageContent() {
        return pageContent;
    }

    public void setPageContent(PageContent pageContent) {
        this.pageContent = pageContent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HeadlineComponent)) {
            return false;
        }
        HeadlineComponent other = (HeadlineComponent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.HeadlineComponent[ id=" + id + " ]";
    }
    
}
