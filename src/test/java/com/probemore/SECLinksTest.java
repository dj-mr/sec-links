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

    @Autowired
    private SECLinks secLinks;

    @Autowired
    private SECLinksProcessor secLinksProcessor;

    @Test
    public void tesSECLinksBean_BeanMustExist() {
        Assert.assertNotNull(secLinks);
    }

    @Test
    public void tesSECLinksProcessorBean_BeanMustExist() {
        Assert.assertNotNull(secLinksProcessor);
    }

}
