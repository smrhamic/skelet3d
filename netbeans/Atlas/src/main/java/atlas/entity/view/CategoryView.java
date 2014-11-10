/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity.view;

import atlas.entity.Category;
import atlas.entity.CategoryInfo;
import atlas.entity.Language;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Represents a set of attributes of CategoryInfo and its related Category.
 * Namely, attributes needed for generating lists and links such as localized
 * name and category ID.
 * 
 * @author Michal
 */
public class CategoryView {
        
    private int id;
    private String name;
    private String latin;
    private int numPages;

    public CategoryView() {}

    public CategoryView(Category c, String name, int numPages) {
        this.id = c.getId();
        this.latin = c.getLatin();
        this.name = name;
        this.numPages = numPages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }
    
    

    public static CategoryView getDefaultCategoryView() {
        CategoryView cv = new CategoryView();
        cv.id = 0;
        cv.latin = "no name";
        cv.name = "no name";
        return cv;
    }

    public static CategoryView getCategoryView(
        EntityManager em, Category cat, Language lang) {
        // find info for category in language: localized name
        String nameTemp;
        try {
            TypedQuery<CategoryInfo> query = em.createQuery(
                    "SELECT i FROM CategoryInfo i "
                            + "WHERE i.category1 = :cat AND i.language1 = :lang",
                    CategoryInfo.class);
            nameTemp = query.setParameter("cat", cat).setParameter("lang", lang)
                    .getSingleResult().getName();
        } catch (NoResultException e) {
            nameTemp = "missing language variant";
        }
        // count visible models for this language variant and create         
        return new CategoryView(cat, nameTemp, cat.getNumberOfPages(lang, true));
    }
}
