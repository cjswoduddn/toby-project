package woo.young.tobyproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectionTest {

    @Test
    public void invokeMetho() throws Exception{
        String name = "spring";

        assertEquals(name.length(), 6);
        Method method = String.class.getMethod("length");
        assertEquals(name.length(), method.invoke(name));

    }
}
