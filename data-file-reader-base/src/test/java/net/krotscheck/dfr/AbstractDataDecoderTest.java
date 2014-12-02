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

import net.krotscheck.util.ResourceUtil;
import org.apache.commons.io.input.NullInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the AbstractDataDecoder.
 *
 * @author Michael Krotscheck
 */
public final class AbstractDataDecoderTest {

    /**
     * Assert that the input field may be set.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testGetSetInput() throws Exception {
        IDataDecoder decoder = new TestDataDecoder();

        Assert.assertNull(decoder.getInputStream());

        // Test input stream
        InputStream stream = new NullInputStream(100);
        decoder.setInputStream(stream);
        Assert.assertEquals(stream, decoder.getInputStream());
    }

    /**
     * Assert that the input field may be set.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFilteredIterator() throws Exception {
        Map<String, Object> mockData = new HashMap<>();

        Iterator mockIterator = mock(Iterator.class);
        doReturn(mockData).when(mockIterator).next();

        AbstractDataDecoder decoder = mock(AbstractDataDecoder.class);
        doReturn(mockIterator).when(decoder).buildIterator();

        IDataFilter filter = mock(IDataFilter.class);
        decoder.addFilter(filter);

        Iterator<Map<String, Object>> decoderIterator = decoder.iterator();

        // Assert that calling the iterator invokes the filters.
        decoderIterator.next();
        verify(mockIterator, times(1)).next();
        verify(filter, times(1)).apply(anyMap());

        // Assert that remove calls the underlying iterator.
        decoderIterator.remove();
        verify(mockIterator, times(1)).remove();

        // Assert that hasNext is passed through.
        decoderIterator.hasNext();
        verify(mockIterator, times(1)).hasNext();
    }

    /**
     * Test the close method without a stream.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testCloseWithoutStream() throws Exception {
        IDataDecoder decoder = new TestDataDecoder();

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
        IDataDecoder decoder = new TestDataDecoder();

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
        IDataDecoder decoder = new TestDataDecoder();

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
     * @throws java.lang.Exception Tests throw exceptions.
     */
    @Test(expected = InstantiationException.class)
    public void testConstructorIsAbstract() throws Exception {
        Constructor<AbstractDataDecoder> constructor =
                AbstractDataDecoder.class.getDeclaredConstructor();

        // Try to create an instance
        constructor.newInstance();
    }


    /**
     * A test class to use for this unit test.
     */
    public static final class TestDataDecoder extends AbstractDataDecoder {

        /**
         * Do nothing.
         *
         * @return Nothing
         */
        @Override
        public String getMimeType() {
            return null;
        }

        /**
         * Do nothing.
         *
         * @return Nothing
         */
        @Override
        @SuppressWarnings("unchecked")
        protected Iterator<Map<String, Object>> buildIterator() {
            return null;
        }

        /**
         * Do nothing.
         */
        @Override
        protected void dispose() {

        }
    }
}
