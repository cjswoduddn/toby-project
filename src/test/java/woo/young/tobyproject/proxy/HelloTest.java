package woo.young.tobyproject.proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
