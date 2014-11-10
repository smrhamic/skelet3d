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
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Michal
 */
@Entity
@Table(name = "CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")})
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "LATIN")
    private String latin;
    @OneToMany(mappedBy = "category")
    private List<Page> pageList;
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> categoryList;
    @JoinColumn(name = "PARENT", referencedColumnName = "ID")
    @ManyToOne
    private Category parentCategory;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category1")
    private List<CategoryInfo> categoryInfoList;
    
    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }
    
    public static Category getCategoryById(EntityManager em, int id) {
        // find category by id
        // if nothing matches, null is returned
        try {
            TypedQuery<Category> query = em.createQuery(
                    "SELECT c FROM Category c "
                            + "WHERE c.id = :id",
                    Category.class);
            return query.setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public static List<Category> getRootCategories(EntityManager em) {
        // find categories with no parents
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c "
                        + "WHERE c.parentCategory IS NULL",
                Category.class);
        return query.getResultList();
    }
    
    public int getNumberOfPages(Language lang, boolean publishedOnly) {
        int numPages = 0;
        for (Page page : pageList) {
            for (PageContent pc : page.getPageContentList()) {
                if (pc.getLanguage1().equals(lang) && (!publishedOnly || pc.getPublished())) {
                    numPages++;
                }
            }
        }
        for (Category cat : categoryList) {
            numPages += cat.getNumberOfPages(lang, publishedOnly);
        }
        return numPages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
    }

    @XmlTransient
    public List<Page> getPageList() {
        return pageList;
    }

    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }

    @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @XmlTransient
    public List<CategoryInfo> getCategoryInfoList() {
        return categoryInfoList;
    }

    public void setCategoryInfoList(List<CategoryInfo> categoryInfoList) {
        this.categoryInfoList = categoryInfoList;
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
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atlas.entity.Category[ id=" + id + " ]";
    }
    
}
