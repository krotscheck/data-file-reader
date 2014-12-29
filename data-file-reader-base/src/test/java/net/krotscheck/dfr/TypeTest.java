/*
 * Copyright (c) 2014 Michael Krotscheck
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.krotscheck.dfr;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for our type detection utility.
 *
 * @author Michael Krotscheck
 */
public final class TypeTest {

    /**
     * Test Type Detection.
     */
    @Test
    public void testTypeDetection() {
        Assert.assertEquals(Type.STRING,
                Type.getTypeForValue("FOO"));
        Assert.assertEquals(Type.STRING,
                Type.getTypeForValue((String) "FOO"));
        Assert.assertEquals(Type.INTEGER,
                Type.getTypeForValue((int) 10));
        Assert.assertEquals(Type.INTEGER,
                Type.getTypeForValue((Integer) 10));
        Assert.assertEquals(Type.LONG,
                Type.getTypeForValue((long) 10));
        Assert.assertEquals(Type.LONG,
                Type.getTypeForValue((Long) (long) 10));
        Assert.assertEquals(Type.FLOAT,
                Type.getTypeForValue((float) 1.2));
        Assert.assertEquals(Type.FLOAT,
                Type.getTypeForValue((Float) (float) 1.2));
        Assert.assertEquals(Type.DOUBLE,
                Type.getTypeForValue((double) 1.2));
        Assert.assertEquals(Type.DOUBLE,
                Type.getTypeForValue((Double) (double) 1.2));
        Assert.assertEquals(Type.BOOLEAN,
                Type.getTypeForValue((boolean) true));
        Assert.assertEquals(Type.BOOLEAN,
                Type.getTypeForValue((Boolean) (boolean) true));

        // Check default
        Assert.assertEquals(Type.STRING,
                Type.getTypeForValue(Type.class));
    }
}
