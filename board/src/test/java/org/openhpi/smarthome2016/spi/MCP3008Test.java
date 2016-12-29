package org.openhpi.smarthome2016.spi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by essagl on 30.12.2016.
 */
public class MCP3008Test {
    @Test
    public void loadPropertiesTest() {
        double referenceVoltage = 2.25;
        MCP3008.setReferenceVoltage(referenceVoltage);
        Assertions.assertTrue(referenceVoltage == MCP3008.loadReferenceVoltage());
    }

}
