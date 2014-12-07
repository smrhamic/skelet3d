/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity;

/**
 * Provides common interface for all page component entities.
 * Requires the component to have accessible "type" and position in page (order).
 * Implementations include TextComponent, HeadlineComponent, ImageComponent or
 * ModelComponent.
 *
 * @author Michal Smrha
 */
public abstract class PageComponent implements Comparable<PageComponent> {
    
    /**
     * Gets compOrder, which is used for sorting components within a page.
     * Typically compOrder is an index of this component
     * (1 = top-most component; 2 = second from top etc.)
     * However, it does not have to be an index and components are simlpy sorted
     * by this value.
     *
     * @return Position of component in page
     * @see #getCompOrder()
     */
    public abstract Integer getCompOrder();
    
    /**
     * Sets compOrder, which is used for sorting components within a page.
     * Typically compOrder is an index of this component
     * (1 = top-most component; 2 = second from top etc.)
     * However, it does not have to be an index and components are simlpy sorted
     * by this value.
     *
     * @param componentOrder Position of component in page.
     * @see #getCompOrder() 
     */
    public abstract void setCompOrder(Integer componentOrder);
    
    /**
     * Gets the type of this component.
     * Currently implemented types include "text", "headline", "image" or "model".
     *
     * @return Component type.
     */
    public abstract String getComponentType();
    
    @Override
    public int compareTo(PageComponent c) {
        return this.getCompOrder() - c.getCompOrder();
    }
}
