package hibernate.namestrategy;

import org.hibernate.boot.model.naming.*;
import org.hibernate.internal.util.StringHelper;

/**
 * Created by admin on 2017/7/22.
 */
public class UmisImplicitNamingStrategy extends ImplicitNamingStrategyLegacyHbmImpl {

    @Override
    protected String transformEntityName(EntityNaming entityNaming) {
        System.out.println("----------------------" + entityNaming.getEntityName() + "-------------------");
        return StringHelper.unqualify( entityNaming.getEntityName()).toLowerCase();
    }

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        System.out.println("----------------------" + source.toString() + "-------------------");
        return source.isCollectionElement()
                ? toIdentifier( "elt", source.getBuildingContext() )
                : super.determineBasicColumnName( source );
    }
}
