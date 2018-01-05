package me.hfox.morphix.helper.lifecycle;

import me.hfox.morphix.exception.MorphixException;
import me.hfox.morphix.exception.support.InvalidTimeLibraryException;

public enum TimeLibrary {

    DEFAULT("java.util.Date"),
    JODA("org.joda.time.DateTime");

    private boolean tested;
    private String testClass;

    TimeLibrary(String testClass) {
        this.testClass = testClass;
    }

    public void test() throws MorphixException {
        if (tested) {
            return;
        }

        try {
            Class<?> cls = Class.forName(testClass);
            // System.out.println("Loaded " + name());
            tested = true;
        } catch (ClassNotFoundException ex) {
            throw new InvalidTimeLibraryException(this, "Could not load the '" + name() + "' time library", ex);
        }
    }

}