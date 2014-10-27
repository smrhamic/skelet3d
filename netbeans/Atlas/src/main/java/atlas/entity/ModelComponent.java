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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Michal
 */
@Entity
@Table(name = "MODELCOMPONENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModelComponent.findAll", query = "SELECT m FROM ModelComponent m")})
public class ModelComponent extends PageComponent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "COMP_ORDER")
    private Integer compOrder;
    @JoinColumn(name = "MODEL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Model model;
    @JoinColumns({
        @JoinColumn(name = "PAGE", referencedColumnName = "PAGE"),
        @JoinColumn(name = "LANGUAGE", referencedColumnName = "LANGUAGE")})
    @ManyToOne(optional = false)
    private PageContent pageContent;

    public ModelComponent() {
    }

    public ModelComponent(Integer id) {
        this.id = id;
    }
    
    @Override
    public String getComponentType() {
        return "model";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getCompOrder() {
        return compOrder;
    }

    public void setCompOrder(Integer compOrder) {
        this.compOrder = compOrder;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
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
        if (!(object instanceof ModelComponent)) {
            return false;
        }
        ModelComponent other = (ModelComponent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.ModelComponent[ id=" + id + " ]";
    }
    
}
