package test.test.impl;

import test.api.SampleService;

/**
 * SampleServiceImpl
 *
 * @author ����� ������ (ielkin@sitronics.com)
 */
public class SampleServiceImpl implements SampleService {

    public String getGreeting(String name) {
        return "Hello, " + name + "!";
    }
}
