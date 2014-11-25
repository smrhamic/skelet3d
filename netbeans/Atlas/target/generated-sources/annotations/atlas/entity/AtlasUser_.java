package atlas.entity;

import atlas.entity.UserRole;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-25T14:56:03")
@StaticMetamodel(AtlasUser.class)
public class AtlasUser_ { 

    public static volatile SingularAttribute<AtlasUser, String> salt;
    public static volatile SingularAttribute<AtlasUser, String> pass;
    public static volatile SingularAttribute<AtlasUser, String> name;
    public static volatile SingularAttribute<AtlasUser, String> login;
    public static volatile SingularAttribute<AtlasUser, UserRole> userRole;

}