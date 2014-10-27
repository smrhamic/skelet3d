package atlas.entity;

import atlas.entity.Label;
import atlas.entity.LabelContentPK;
import atlas.entity.Language;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-10-27T19:30:47")
@StaticMetamodel(LabelContent.class)
public class LabelContent_ { 

    public static volatile SingularAttribute<LabelContent, Language> language1;
    public static volatile SingularAttribute<LabelContent, String> text;
    public static volatile SingularAttribute<LabelContent, LabelContentPK> labelContentPK;
    public static volatile SingularAttribute<LabelContent, String> title;
    public static volatile SingularAttribute<LabelContent, Label> label1;

}