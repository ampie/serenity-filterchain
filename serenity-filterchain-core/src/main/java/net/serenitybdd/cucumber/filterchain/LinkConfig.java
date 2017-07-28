package net.serenitybdd.cucumber.filterchain;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public abstract class LinkConfig<T extends TestOutcomeLink> {
    private String name;
    private String implementationClass;
    private Properties properties;

    protected LinkConfig() {
    }

    protected LinkConfig(String name, String implementationClass) {
        setName(name);
        setImplementationClass(implementationClass);
    }

    public String getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(String implementationClass) {
        this.implementationClass = implementationClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T buildLink() {
        T result = instantiate();
        result.setName(name);
        try {
            Object implementation = Class.forName(getImplementationClass()).newInstance();
            if (this.properties != null) {
                Set<Map.Entry<Object, Object>> entries = this.properties.entrySet();
                PropertyDescriptor[] props = Introspector.getBeanInfo(implementation.getClass()).getPropertyDescriptors();
                for (PropertyDescriptor prop : props) {
                    Method writeMethod = prop.getWriteMethod();
                    if (writeMethod != null && getProperties().containsKey(prop.getName())) {
                        Class<?> targetType = writeMethod.getParameterTypes()[0];
                        Converter converter = ConvertUtils.lookup(String.class, targetType);
                        Object propValue;
                        if(converter==null){
                            if( targetType.isEnum()){
                                propValue=Enum.valueOf((Class<? extends Enum>) targetType,getProperties().getProperty(prop.getName()));
                            }else{
                                throw new IllegalStateException("No converter for type " + targetType.getName());
                            }
                        }else{
                            propValue = converter.convert(targetType, getProperties().getProperty(prop.getName()));

                        }
                        writeMethod.invoke(implementation, propValue);
                    }
                }
            }
            result.setImplementation(implementation);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public abstract T instantiate();

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
