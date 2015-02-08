/*
 * Copyright (c) 2015 Michael Krotscheck
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

package net.krotscheck.dfr.stream;

import net.krotscheck.test.dfr.TestStreamDecoder;
import net.krotscheck.util.ResourceUtil;
import org.apache.commons.io.input.NullInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the AbstractStreamDecoder.
 *
 * @author Michael Krotscheck
 */
public final class AbstractStreamDecoderTest {

    /**
     * Test data for our unit tests.
     */
    private List<Map<String, Object>> testData;

    /**
     * Set up some test data to work with.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Before
    public void setup() throws Exception {
        testData = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Map<String, Object> testRow = new HashMap<>();
            testRow.put("one", "column_one_row_" + i);
            testRow.put("two", "column_two_row_" + i);
            testRow.put("three", "column_three_row_" + i);
            testRow.put("four", "column_four_row_" + i);

            testData.add(testRow);
        }
    }

    /**
     * Assert that the input field may be set.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testGetSetInput() throws Exception {
        IStreamDecoder decoder = new TestStreamDecoder(testData);

        Assert.assertNull(decoder.getInputStream());

        // Test input stream
        InputStream stream = new NullInputStream(100);
        decoder.setInputStream(stream);
        Assert.assertEquals(stream, decoder.getInputStream());
    }

    /**
     * Test the close method without a stream.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testCloseWithoutStream() throws Exception {
        IStreamDecoder decoder = new TestStreamDecoder(testData);

        Assert.assertNull(decoder.getInputStream());

        // Make sure nothing happens
        decoder.close();
    }

    /**
     * Test that the stream is closeable.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testCloseWithStream() throws Exception {
        IStreamDecoder decoder = new TestStreamDecoder(testData);

        Assert.assertNull(decoder.getInputStream());
        InputStream stream = ResourceUtil.getResourceAsStream("test.bson");
        decoder.setInputStream(stream);
        Assert.assertEquals(stream, decoder.getInputStream());

        decoder.close();

        Assert.assertTrue(stream.available() == 0);
        Assert.assertNull(decoder.getInputStream());
    }


    /**
     * Test close with a stream that errors.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testCloseWithErrorStream() throws Exception {
        IStreamDecoder decoder = new TestStreamDecoder(testData);

        Assert.assertNull(decoder.getInputStream());
        InputStream stream = mock(InputStream.class);
        doThrow(IOException.class).when(stream).close();
        decoder.setInputStream(stream);
        Assert.assertEquals(stream, decoder.getInputStream());
        decoder.close();
        Assert.assertNull(decoder.getInputStream());
    }

    /**
     * Ensure the constructor is abstract.
     *
     * @throws Exception Tests throw exceptions.
     */
    @Test(expected = InstantiationException.class)
    public void testConstructorIsAbstract() throws Exception {
        Constructor<AbstractStreamDecoder> constructor =
                AbstractStreamDecoder.class.getDeclaredConstructor();

        // Try to create an instance
        constructor.newInstance();
    }
}
