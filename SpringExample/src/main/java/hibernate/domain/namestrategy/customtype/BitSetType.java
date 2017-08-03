package hibernate.domain.namestrategy.customtype;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.BitSet;

/**
 * 通过如下方式注册类型转换
 * configuration.registerTypeContributor( (typeContributions, serviceRegistry) -> {
 *    typeContributions.contributeType( BitSetType.INSTANCE );
  *   } );
 *   或者
 *   ServiceRegistry standardRegistry =
 *    new StandardServiceRegistryBuilder().build();
 *
 *   MetadataSources sources = new MetadataSources( standardRegistry );
 *
 *    MetadataBuilder metadataBuilder = sources.getMetadataBuilder();
 *
 *    metadataBuilder.applyBasicType( BitSetType.INSTANCE );
 */
public class BitSetType extends AbstractSingleColumnStandardBasicType<BitSet> implements DiscriminatorType<BitSet> {
    public static final BitSetType INSTANCE = new BitSetType();
    public BitSetType() {
        super(VarcharTypeDescriptor.INSTANCE, BitSetTypeDescriptor.INSTANCE);
    }
    @Override
    public BitSet stringToObject(String xml) throws Exception {
        return fromString(xml);
    }

    @Override
    public String objectToSQLString(BitSet value, Dialect dialect) throws Exception {
        return toString(value);
    }

    @Override
    public String getName() {
        return "bitset";
    }
}
