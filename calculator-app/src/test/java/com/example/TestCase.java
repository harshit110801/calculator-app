package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCase {

    @Test
    void testHelloWorld() {
        Testing t = new Testing();
        assertEquals("Hello World !!", t.helloWorld());
    }
}