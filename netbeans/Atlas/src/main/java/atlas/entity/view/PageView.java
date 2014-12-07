/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity.view;

import atlas.entity.Page;

/**
 * Represents a localized view of a page.
 * Basically a set of attributes of PageContent and its related Page.
 * Namely, attributes useful for generating page lists and links
 * (ID, localized name, latin name, if page is published).
 * Actual page content other than name is not included!
 * 
 * @author Michal Smrha
 * @see atlas.entity.Page
 * @see atlas.entity.PageContent
 */
public class PageView {
        
    private int id;
    private String name;
    private String latin;
    private Boolean published;

    /**
     * Constructs a blank PageView.
     */
    public PageView() {}

    /**
     * Constructs a populated PageView based on given values.
     *
     * @param p Page to construct a view for.
     * @param name Localized name of the page.
     * @param published True if page is published, false otherwise.
     */
    public PageView(Page p, String name, boolean published) {
        this.id = p.getId();
        this.latin = p.getLatin();
        this.name = name;
        this.published = published;
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

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

}
