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

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the abstract data decoder.
 *
 * @author Michael Krotscheck
 */
public final class AbstractDataEncoderTest {

    /**
     * Ensure the constructor is abstract.
     *
     * @throws Exception Tests throw exceptions.
     */
    @Test(expected = InstantiationException.class)
    public void testConstructorIsAbstract() throws Exception {
        Constructor<AbstractDataEncoder> constructor =
                AbstractDataEncoder.class.getDeclaredConstructor();


        // Try to create an instance
        constructor.newInstance();
    }

    /**
     * Assert that the write method passes to the internal outputstream method.
     *
     * @throws Exception Any unexpected exception.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testWrite() throws Exception {
        AbstractDataEncoder encoder = mock(AbstractDataEncoder.class);

        Map<String, Object> testMap = new LinkedHashMap<>();

        encoder.write(testMap);

        verify(encoder, times(1)).write(testMap);
        verify(encoder, times(1)).writeToOutput(testMap);
    }

}
