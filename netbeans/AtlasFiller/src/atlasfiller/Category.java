package atlasfiller;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michal
 */
public class Category {
    
    //private Category parent;
    private String latin;
    private String nameEn;
    private String nameCz;
    private List<Category> children;
    private List<Page> pages;
    private List<String> childIds;
    
    private final String folder;
    private final String extension;
    private final String labelFolder;
    

    public Category(String folder, String fileId, String extension, String labelFolder) {
        // parent passed in param
        //this.parent = parent;
        this.folder = folder;
        this.extension = extension;
        this.labelFolder = labelFolder;
        
        Document xml = XMLReader.loadXML(folder + fileId + extension);
        if (xml == null) return;
        // root node (only child)
        Node group = xml.getFirstChild();
        // read attributes
        NamedNodeMap attribs = group.getAttributes();
        this.latin = attribs.getNamedItem("latin").getNodeValue();
        this.nameEn = attribs.getNamedItem("name").getNodeValue();
        this.nameCz = attribs.getNamedItem("name_CZ").getNodeValue();
        // get subgroups and bones
        NodeList subNodes = group.getChildNodes();
        Node node;
        // "children" and "bones" should be present
        for (int i = 0; i < subNodes.getLength(); i++) {
            node = subNodes.item(i);
            // work with "children" (subgroups)
            if (node.getNodeName().equals("children")) {
                NodeList ch = node.getChildNodes();
                // register child IDs in childIds list
                childIds = new ArrayList<>();
                for (int j = 0; j < ch.getLength(); j++) {
                    // need to check node type because of blank text nodes
                    if (ch.item(j).getNodeName().equals("child")) {
                        // take child node, then its "id" attribute value
                        childIds.add(ch.item(j).getAttributes().getNamedItem("id").getNodeValue());
                    }
                }
            }
            
            // work with "bones" (pages)
            else if (node.getNodeName().equals("bones")) {
                NodeList ch = node.getChildNodes();
                // register bones
                pages = new ArrayList<>();
                for (int j = 0; j < ch.getLength(); j++) {
                    // need to check node type because of blank text nodes
                    if (ch.item(j).getNodeName().equals("bone")) {
                        NamedNodeMap attrTemp = ch.item(j).getAttributes();
                        Page bone = new Page(labelFolder,
                                attrTemp.getNamedItem("id").getNodeValue(), extension);
                        bone.setLatin(attrTemp.getNamedItem("latin").getNodeValue());
                        bone.setNameEn(attrTemp.getNamedItem("name").getNodeValue());
                        bone.setNameCz(attrTemp.getNamedItem("name_CZ").getNodeValue());
                        bone.setModelPath(attrTemp.getNamedItem("model").getNodeValue());
                        
                        pages.add(bone);
                    }
                }
            }
        }
        
    }
    
    public void constructSubcategories() {
        children = new ArrayList<>();
        Category child;
        for (String id : childIds) {
            child = new Category(folder, id, extension, labelFolder);
            children.add(child);
            child.constructSubcategories();
        }
    }
    
//    public Category getParent() {
//        return parent;
//    }
//
//    public void setParent(Category parent) {
//        this.parent = parent;
//    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameCz() {
        return nameCz;
    }

    public void setNameCz(String nameCz) {
        this.nameCz = nameCz;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public List<String> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<String> childIds) {
        this.childIds = childIds;
    }


}
