package woo.young.tobyproject.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import woo.young.tobyproject.service.TransactionHandler;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean implements FactoryBean<Object> {
    Object target;
    PlatformTransactionManager ptm;
    String pattern;
    Class<?> serviceInterface;

    public TxProxyFactoryBean(Object target, PlatformTransactionManager ptm, String pattern, Class<?> serviceInterface) {
        this.target = target;
        this.ptm = ptm;
        this.pattern = pattern;
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler th = new TransactionHandler(target,
                ptm, pattern);
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{serviceInterface},
                th
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
