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
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Michal
 */
@Entity
@Table(name = "LANGUAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Language.findAll", query = "SELECT l FROM Language l")})
public class Language implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 10)
    @Column(name = "SHORT")
    private String short1;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @Size(max = 255)
    @Column(name = "FLAG_FILE")
    private String flagFile;
    @Column(name = "DEFAULT_LANG")
    private Boolean defaultLang;

    public Language() {
    }

    public Language(Integer id) {
        this.id = id;
    }
    
    public static Language getDefaultLanguage(EntityManager em) {
        // get default flagged language
        // if none, null
        try {
            TypedQuery<Language> query = em.createQuery(
                    "SELECT l FROM Language l "
                            + "WHERE l.defaultLang = TRUE",
                    Language.class);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShort1() {
        return short1;
    }

    public void setShort1(String short1) {
        this.short1 = short1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlagFile() {
        return flagFile;
    }

    public void setFlagFile(String flagFile) {
        this.flagFile = flagFile;
    }

    public Boolean getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(Boolean defaultLang) {
        this.defaultLang = defaultLang;
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
        if (!(object instanceof Language)) {
            return false;
        }
        Language other = (Language) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.Language[ id=" + id + " ]";
    }
    
}
