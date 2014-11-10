/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class Page {
    
    //private Category parent;
    private String latin;
    private String nameEn;
    private String nameCz;
    private String modelPath;
    private List<Label> labels;
    
    public Page(String labelFolder, String labelId, String extension) {

        labels = new ArrayList<>();
        Document xml = XMLReader.loadXML(labelFolder + labelId + extension);
        if (xml == null) return;
        
        // root tag should be only child, get its children
        NodeList subNodes = xml.getFirstChild().getChildNodes();
        
        Node node;
        NamedNodeMap attribs;
        Label label;
        
        for (int i = 0; i < subNodes.getLength(); i++) {
            node = subNodes.item(i);
            // need to check node type because of blank text nodes
            if (node.getNodeName().equals("label")) {
                // get label attributes
                attribs = node.getAttributes();
                // fill label object from attribs
                label = new Label();
                label.setTitleEn(attribs.getNamedItem("label").getNodeValue());
                label.setTitleCz(attribs.getNamedItem("label_CZ").getNodeValue());
                label.setTextEn(attribs.getNamedItem("text").getNodeValue());
                label.setTextCz(attribs.getNamedItem("text_CZ").getNodeValue());
                label.setLabelX(Double.parseDouble(attribs.getNamedItem("x2").getNodeValue()));
                label.setLabelY(Double.parseDouble(attribs.getNamedItem("y2").getNodeValue()));
                label.setLabelZ(Double.parseDouble(attribs.getNamedItem("z2").getNodeValue()));
                label.setMarkX(Double.parseDouble(attribs.getNamedItem("x").getNodeValue()));
                label.setMarkY(Double.parseDouble(attribs.getNamedItem("y").getNodeValue()));
                label.setMarkZ(Double.parseDouble(attribs.getNamedItem("z").getNodeValue()));
                
                labels.add(label);
            }
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

    public String getModelPath() {
        if (modelPath.startsWith("model/")) {
            return modelPath.substring(6);
        }
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

}
