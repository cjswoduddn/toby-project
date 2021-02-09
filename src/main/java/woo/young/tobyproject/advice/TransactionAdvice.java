package woo.young.tobyproject.advice;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RequiredArgsConstructor
public class TransactionAdvice implements MethodInterceptor {

    private final PlatformTransactionManager ptm;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = ptm.getTransaction(new DefaultTransactionDefinition());
        try{
            Object ret = invocation.proceed();
            ptm.commit(status);
            return ret;
        }catch (RuntimeException e){
            ptm.rollback(status);
            throw e;
        }

    }
}
