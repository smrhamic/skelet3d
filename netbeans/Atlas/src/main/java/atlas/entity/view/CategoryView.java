/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity.view;

import atlas.entity.Category;

/**
 * Represents a localized view of a category.
 * Basically a set of attributes of CategoryInfo and its related Category.
 * Namely, attributes useful for generating category lists and links
 * (ID, localized name, latin name, number of pages).
 * 
 * @author Michal Smrha
 * @see atlas.entity.Category
 * @see atlas.entity.CategoryInfo
 */
public class CategoryView {
        
    private int id;
    private String name;
    private String latin;
    private int numPages;

    /**
     * Constructs a blank CategoryView.
     */
    public CategoryView() {}

    /**
     * Constructs a populated CategoryView based on given values.
     *
     * @param c Category to construct a view for.
     * @param name Localized name of the category.
     * @param numPages Number of pages in the category.
     */
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
    
}
