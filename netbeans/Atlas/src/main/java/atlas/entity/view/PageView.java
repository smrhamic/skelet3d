/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity.view;

import atlas.entity.Language;
import atlas.entity.Page;
import atlas.entity.PageContent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Represents a set of attributes of PageContent and its related Page.
 * Namely, attributes needed for generating lists and links such as localized
 * name and category ID.
 * 
 * @author Michal
 */
public class PageView {
        
        private int id;
        private String name;
        private String latin;
        private Boolean published;
        
        public PageView() {}
        
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
        
        public static PageView getPageView(
            EntityManager em, Page page, Language lang) {
            // find info for page in language
            String nameTemp;
            Boolean publishedTemp;
            PageContent pcTemp;
            try {
                TypedQuery<PageContent> query = em.createQuery(
                        "SELECT c FROM PageContent c "
                                + "WHERE c.page1 = :page AND c.language1 = :lang "
                                + "AND c.published = TRUE",
                        PageContent.class);
                pcTemp = query.setParameter("page", page).setParameter("lang", lang)
                        .getSingleResult();
                nameTemp = pcTemp.getName();
                publishedTemp = pcTemp.getPublished();
            } catch (NoResultException e) {
                nameTemp = "missing language variant";
                publishedTemp = false;
            }
            return new PageView(page, nameTemp, publishedTemp);
        }
    }
