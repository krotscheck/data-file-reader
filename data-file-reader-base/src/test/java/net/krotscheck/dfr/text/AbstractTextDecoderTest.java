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

package net.krotscheck.dfr.text;

import net.krotscheck.test.dfr.TestTextDecoder;
import net.krotscheck.util.ResourceUtil;
import org.apache.commons.io.input.NullReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the AbstractTextDecoder.
 *
 * @author Michael Krotscheck
 */
public final class AbstractTextDecoderTest {

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
            Map<String, Object> testRow = new LinkedHashMap<>();
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
        ITextDecoder decoder = new TestTextDecoder(testData);

        Assert.assertNull(decoder.getReader());

        // Test input reader
        Reader reader = new NullReader(100);
        decoder.setReader(reader);
        Assert.assertEquals(reader, decoder.getReader());
    }

    /**
     * Test the close method without a reader.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testCloseWithoutStream() throws Exception {
        ITextDecoder decoder = new TestTextDecoder(testData);

        Assert.assertNull(decoder.getReader());

        // Make sure nothing happens
        decoder.close();
    }

    /**
     * Test that the reader is closeable.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testCloseWithReader() throws Exception {
        ITextDecoder decoder = new TestTextDecoder(testData);

        Assert.assertNull(decoder.getReader());
        Reader reader = new FileReader(ResourceUtil
                .getFileForResource("test.csv"));
        decoder.setReader(reader);
        Assert.assertEquals(reader, decoder.getReader());

        decoder.close();

        Assert.assertNull(decoder.getReader());
        try {
            Assert.assertFalse(reader.ready());
        } catch (IOException ioe) {
            Assert.assertFalse(false);
        }
    }


    /**
     * Test close with a stream that errors.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testCloseWithErrorStream() throws Exception {
        ITextDecoder decoder = new TestTextDecoder(testData);

        Assert.assertNull(decoder.getReader());
        Reader reader = mock(Reader.class);
        doThrow(IOException.class).when(reader).close();
        decoder.setReader(reader);
        Assert.assertEquals(reader, decoder.getReader());
        decoder.close();
        Assert.assertNull(decoder.getReader());
    }

    /**
     * Ensure the constructor is abstract.
     *
     * @throws Exception Tests throw exceptions.
     */
    @Test(expected = InstantiationException.class)
    public void testConstructorIsAbstract() throws Exception {
        Constructor<AbstractTextDecoder> constructor =
                AbstractTextDecoder.class.getDeclaredConstructor();

        // Try to create an instance
        constructor.newInstance();
    }
}
