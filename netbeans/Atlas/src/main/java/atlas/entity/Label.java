/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Michal
 */
@Entity
@Table(name = "LABEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Label.findAll", query = "SELECT l FROM Label l")})
public class Label implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MARK_X")
    private Double markX;
    @Column(name = "MARK_Y")
    private Double markY;
    @Column(name = "MARK_Z")
    private Double markZ;
    @Column(name = "LABEL_X")
    private Double labelX;
    @Column(name = "LABEL_Y")
    private Double labelY;
    @Column(name = "LABEL_Z")
    private Double labelZ;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "label1")
    private List<LabelContent> labelContentList;
    @JoinColumn(name = "MODEL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Model model;

    public Label() {
    }

    public Label(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMarkX() {
        return markX;
    }

    public void setMarkX(Double markX) {
        this.markX = markX;
    }

    public Double getMarkY() {
        return markY;
    }

    public void setMarkY(Double markY) {
        this.markY = markY;
    }

    public Double getMarkZ() {
        return markZ;
    }

    public void setMarkZ(Double markZ) {
        this.markZ = markZ;
    }

    public Double getLabelX() {
        return labelX;
    }

    public void setLabelX(Double labelX) {
        this.labelX = labelX;
    }

    public Double getLabelY() {
        return labelY;
    }

    public void setLabelY(Double labelY) {
        this.labelY = labelY;
    }

    public Double getLabelZ() {
        return labelZ;
    }

    public void setLabelZ(Double labelZ) {
        this.labelZ = labelZ;
    }

    @XmlTransient
    public List<LabelContent> getLabelContentList() {
        return labelContentList;
    }

    public void setLabelContentList(List<LabelContent> labelContentList) {
        this.labelContentList = labelContentList;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
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
        if (!(object instanceof Label)) {
            return false;
        }
        Label other = (Label) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.Label[ id=" + id + " ]";
    }
    
}
