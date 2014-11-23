/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity.view;

import atlas.entity.Category;

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
}
