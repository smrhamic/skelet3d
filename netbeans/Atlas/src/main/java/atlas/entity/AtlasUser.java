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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Michal
 */
@Entity
@Table(name = "ATLASUSER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtlasUser.findAll", query = "SELECT a FROM AtlasUser a")})
public class AtlasUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "LOGIN")
    private String login;
    @Size(max = 255)
    @Column(name = "PASS")
    private String pass;
    @Size(max = 255)
    @Column(name = "SALT")
    private String salt;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "ROLE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UserRole userRole;

    public AtlasUser() {
    }   
    
    public AtlasUser(String login) {
        this.login = login;
    }

//    public static AtlasUser login(EntityManager em, String login, String pass) {
//        // find user with this login, password
//        // if nothing matches, null is returned
//        try {
//            TypedQuery<AtlasUser> query = em.createQuery(
//                    "SELECT u FROM AtlasUser u "
//                            + "WHERE u.login = :login AND u.pass = :pass",
//                    AtlasUser.class);
//            return query.setParameter("login", login).setParameter("pass", pass)
//                    .getSingleResult();
//        } catch (NoResultException e) {
//            return null;
//        }
//    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtlasUser)) {
            return false;
        }
        AtlasUser other = (AtlasUser) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.AtlasUser[ login=" + login + " ]";
    }
    
}
