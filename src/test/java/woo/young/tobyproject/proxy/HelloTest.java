package woo.young.tobyproject.proxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;

public class HelloTest {

    @Test
    public void simpleProxy(){
        Hello hello = new HelloTarget();
        assertEquals(hello.sayHello("yw"), "Hello yw");
        assertEquals(hello.sayHi("yw"), "Hi yw");
        assertEquals(hello.sayThankYou("yw"), "Thank You yw");
    }

    @Test
    public void uppercase() throws Exception{
        //given
        Hello hello = new HelloUppercase(new HelloTarget());
        assertEquals(hello.sayHello("yw"), "HELLO YW");
        assertEquals(hello.sayHi("yw"), "HI YW");
        assertEquals(hello.sayThankYou("yw"), "THANK YOU YW");
    }

    /**
     * in dynamic proxy instance
     * void sayHello(String str){
     *     invocationHandler.invock(..., method, args)
     *
     * }
     */
    static class UppercaseAdvice implements MethodInterceptor{

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return ((String)(invocation.proceed())).toUpperCase();
        }
    }
    @Test
    void proxyTest(){
//        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
//                getClass().getClassLoader(),
//                new Class[]{Hello.class},
//                new UppercaseHandler(new HelloTarget())
//        );
        ProxyFactoryBean pf = new ProxyFactoryBean();
        NameMatchMethodPointcut pc = new NameMatchMethodPointcut();
        pc.setMappedName("sayH*");
        pf.setTarget(new HelloTarget());
        pf.addAdvisor(new DefaultPointcutAdvisor(pc, new UppercaseAdvice()));

        Hello hello = (Hello) pf.getObject();

        String monky = hello.sayThankYou("monky");
        System.out.println("monky = " + monky);
        String monky1 = hello.sayHello("monky");
        System.out.println("monky1 = " + monky1);

    }

    @Test
    void classNamePointcutAdvisor(){
        NameMatchMethodPointcut classMehtodPointCut = new NameMatchMethodPointcut(){
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        classMehtodPointCut.setMappedName("sayH*");
    }

}
