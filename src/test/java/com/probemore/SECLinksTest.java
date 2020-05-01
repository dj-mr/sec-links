/**
 * JUnits for SECLinks class.
 */
package com.probemore;

import com.probemore.controller.SECLinks;
import com.probemore.controller.processor.SECLinksProcessor;
import com.probemore.controller.processor.SECLinksProcessorImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SECLinks.class, SECLinksProcessorImpl.class})
public class SECLinksTest {

    /**
     * SECLinks dependency injection.
     */
    @Autowired
    private SECLinks secLinks;

    /**
     * SECLinksProcessor dependency injection.
     */
    @Autowired
    private SECLinksProcessor secLinksProcessor;

    /**
     * Test to check if SECLinks bean is created.
     */
    @Test
    public void testSECLinksBean_BeanMustExist() {
        Assert.assertNotNull(secLinks);
    }

    /**
     * Test to check if SECLinksProcessor bean is created.
     */
    @Test
    public void testSECLinksProcessorBean_BeanMustExist() {
        Assert.assertNotNull(secLinksProcessor);
    }

}
