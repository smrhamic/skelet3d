/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.entity;

/**
 *
 * @author Michal
 */
public abstract class PageComponent implements Comparable<PageComponent> {
    
    public abstract Integer getCompOrder();
    public abstract void setCompOrder(Integer componentOrder);
    public abstract String getComponentType();
    
    @Override
    public int compareTo(PageComponent c) {
        return this.getCompOrder() - c.getCompOrder();
    }
}
