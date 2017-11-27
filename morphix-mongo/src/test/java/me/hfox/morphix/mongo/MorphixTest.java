package me.hfox.morphix.mongo;

import junit.framework.TestCase;
import me.hfox.morphix.mongo.examples.ABCTestClass;
import me.hfox.morphix.mongo.examples.DEFTestClass;
import me.hfox.morphix.mongo.examples.FakeClass;
import me.hfox.morphix.mongo.exception.MorphixException;
import me.hfox.morphix.mongo.helper.entity.EntityHelper;

public class MorphixTest extends TestCase {

    private Morphix morphix;

    public void setUp() throws Exception {
        morphix = new Morphix(null, null);
    }

    public void testCreate() {
        assertNotNull(morphix.getOptions());
        assertNotNull(morphix.getMapper());
    }

    public void testUnlabelledEntity() {
        EntityHelper helper = morphix.getEntityHelper();
        assertEquals("abc_test_class", helper.getCollectionName(ABCTestClass.class));
    }

    public void testLabelledEntity() {
        EntityHelper helper = morphix.getEntityHelper();
        assertEquals("test_classes", helper.getCollectionName(DEFTestClass.class));
    }

    public void testFakeEntity() {
        EntityHelper helper = morphix.getEntityHelper();

        try {
            helper.getCollectionName(FakeClass.class);
            fail("Expected a MorphixException to be thrown");
        } catch (MorphixException ex) {
            // passed
        }
    }

}