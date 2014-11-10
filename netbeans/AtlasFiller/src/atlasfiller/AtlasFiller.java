package atlasfiller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Michal
 */
public class AtlasFiller {

    /**
     * @param args the command line arguments
     */
    
    private static int enLangId = -1;
    private static int csLangId = -1;
    
    public static void main(String[] args) {
        
        Category root = new Category("xml/group/", "0", ".xml", "xml/label/");
        root.constructSubcategories();
                
        Connection conn;
        
        try {
            conn = DBConnector.getConnection();
            
            updateLangIds(conn);
            
            String lbltxt;
//            //String breakline = "<br />";
//            for (Label l : root.getChildren().get(0).getChildren().get(0).getPages().get(0)
//                    .getLabels()) {
//                lbltxt = l.getTextEn();
//                lbltxt = StringEscapeUtils.escapeEcmaScript(lbltxt);
//                //lbltxt = URLEncoder.encode(lbltxt, "UTF-8");
//                //lbltxt = lbltxt.replace("\r\n", breakline).replace("\n", breakline).replace("\r", breakline);
//                System.out.println("Label: " + lbltxt);
//            }
            
            
            for (Category cat : root.getChildren() ) {
                insertCategory(conn, cat, -1);
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }

    // insert category into DB as well as its subcategories and pages
    // parentId -1 => null
    private static void insertCategory(
            Connection conn, Category category, int parentId)
            throws SQLException {
        
        // common category info
        String query = "INSERT INTO category "
                    + "(parent, latin) VALUES (?, ?)";
            
        PreparedStatement ps = conn.prepareStatement(query, new String[] {"gen_id"});
        
        if (parentId == -1) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, parentId);
        }
        ps.setString(2, category.getLatin());
        
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int catId = rs.getInt(1);
        
        // localized info
        query = "INSERT INTO categoryInfo "
                    + "(category, language, name) VALUES (?, ?, ?)";
        ps = conn.prepareStatement(query);
        ps.setInt(1, catId);
        // English
        ps.setInt(2, enLangId);
        ps.setString(3, category.getNameEn());        
        ps.executeUpdate();
        // Czech
        ps.setInt(2, csLangId);
        ps.setString(3, category.getNameCz());        
        ps.executeUpdate();
        
        // insert subcategories
        for (Category cat : category.getChildren()) {
            insertCategory(conn, cat, catId);
        }
        
        // insert pages
        for (Page page : category.getPages()) {
            insertPage(conn, page, catId);
        }
        
    }
    
    // insert page into DB as well as its subcategories and pages
    // parentId -1 => null
    private static void insertPage(
            Connection conn, Page page, int parentId)
            throws SQLException {
        
        // common page info
        String query = "INSERT INTO page "
                    + "(parent, latin) VALUES (?, ?)";
            
        PreparedStatement ps = conn.prepareStatement(query, new String[] {"gen_id"});
        
        if (parentId == -1) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, parentId);
        }
        ps.setString(2, page.getLatin());
        
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int pageId = rs.getInt(1);
        
        // localized info
        query = "INSERT INTO pageContent "
                    + "(page, language, name, published) VALUES (?, ?, ?, ?)";
        ps = conn.prepareStatement(query);
        ps.setInt(1, pageId);
        ps.setBoolean(4, true);
        // English
        ps.setInt(2, enLangId);
        ps.setString(3, page.getNameEn());        
        ps.executeUpdate();
        // Czech
        ps.setInt(2, csLangId);
        ps.setString(3, page.getNameCz());        
        ps.executeUpdate();
        
        // model
        query = "INSERT INTO model "
                    + "(filename, name) VALUES (?, ?)";
            
        ps = conn.prepareStatement(query, new String[] {"gen_id"});
        
        ps.setString(1, page.getModelPath());
        ps.setString(2, page.getLatin());
        
        ps.executeUpdate();
        
        rs = ps.getGeneratedKeys();
        rs.next();
        int modelId = rs.getInt(1);
        
        // labels
        for (Label label : page.getLabels()) {
            insertLabel(conn, label, modelId);
        }
        
        // model page components
        query = "INSERT INTO modelComponent "
                    + "(page, language, model, description, comp_order) "
                    + "VALUES (?, ?, ?, ?, ?)";
        ps = conn.prepareStatement(query);
        ps.setInt(1, pageId);
        ps.setInt(3, modelId);
        ps.setInt(5, 1);
        // English
        ps.setInt(2, enLangId);
        ps.setString(4, page.getNameEn() + " - 3D model");        
        ps.executeUpdate();
        // Czech
        ps.setInt(2, csLangId);
        ps.setString(4, page.getNameCz() + " - 3D model");        
        ps.executeUpdate();
        
    }
    
    // insert label into DB
    // modelId -1 => null
    private static void insertLabel(
            Connection conn, Label label, int modelId)
            throws SQLException {
        
        // need to rescale, old version was normalized to 300 units, new to 100
        label.rescale(100.0/300.0);
        // lines seem unnecessarily long in new version
        // make half by default, finetune manually
        label.changeLength(0.5);
        
        // common label info
        String query = "INSERT INTO label "
                    + "(model, mark_x, mark_y, mark_z,"
                    + "label_x, label_y, label_z) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
        PreparedStatement ps = conn.prepareStatement(query, new String[] {"gen_id"});
        
        if (modelId == -1) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, modelId);
        }
        ps.setDouble(2, label.getMarkX());
        ps.setDouble(3, label.getMarkY());
        ps.setDouble(4, label.getMarkZ());
        ps.setDouble(5, label.getLabelX());
        ps.setDouble(6, label.getLabelY());
        ps.setDouble(7, label.getLabelZ());
        
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int labelId = rs.getInt(1);
        
        // localized info
        query = "INSERT INTO labelContent "
                    + "(label, language, title, text) VALUES (?, ?, ?, ?)";
        ps = conn.prepareStatement(query);
        ps.setInt(1, labelId);
        // English
        ps.setInt(2, enLangId);
        ps.setString(3, StringEscapeUtils.escapeEcmaScript(label.getTitleEn()));
        ps.setString(4, StringEscapeUtils.escapeEcmaScript(label.getTextEn()));
        ps.executeUpdate();
        // Czech
        ps.setInt(2, csLangId);
        ps.setString(3, StringEscapeUtils.escapeEcmaScript(label.getTitleCz()));
        ps.setString(4, StringEscapeUtils.escapeEcmaScript(label.getTextCz()));       
        ps.executeUpdate();
        
    }
    
    private static void updateLangIds(Connection conn) throws SQLException {
        String query = "SELECT id FROM language "
                    + "WHERE short = ?";
            
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, "en");
        ResultSet rs = ps.executeQuery();
        rs.next();
        enLangId = rs.getInt("id");
        
        ps.setString(1, "cs");
        rs = ps.executeQuery();
        rs.next();
        csLangId = rs.getInt("id");
        
        System.out.println("Language IDs: Eng=" + enLangId + " Cz=" + csLangId);
    }
}
