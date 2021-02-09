package woo.young.tobyproject.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler {

    private Object target;
    private PlatformTransactionManager ptm;
    private String pattern;

    public TransactionHandler(Object target, PlatformTransactionManager ptm, String pattern) {
        this.target = target;
        this.ptm = ptm;
        this.pattern = pattern;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return invokeInTransaction(method, args);
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = ptm.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = method.invoke(target, args);
            ptm.commit(status);
            return ret;
        }catch (RuntimeException e){
            ptm.rollback(status);
            throw e;
        }
    }
}
