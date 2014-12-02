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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit test for the convenience converter.
 *
 * @author Michael Krotscheck
 */
public final class FileStreamConverterTest {

    /**
     * Unit test data.
     */
    private List<Map<String, Object>> data;

    /**
     * Setup the test data.
     */
    @Before
    public void setupTestData() {
        data = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("foo", i);
            data.add(row);
        }
    }

    /**
     * Make sure things throw up when we don't pass a decoder.
     *
     * @throws Exception Throws a runtime exception.
     */
    @Test(expected = RuntimeException.class)
    public void testConstructorWithoutDecoder() throws Exception {
        IDataEncoder encoder = mock(IDataEncoder.class);
        new FileStreamConverter(null, encoder);
    }

    /**
     * Make sure things throw up when we don't pass an encoder.
     *
     * @throws Exception Throws a runtime exception.
     */
    @Test(expected = RuntimeException.class)
    public void testConstructorWithoutEncoder() throws Exception {
        IDataDecoder decoder = mock(IDataDecoder.class);
        new FileStreamConverter(decoder, null);
    }

    /**
     * Test that a simple conversion can occur.
     *
     * @throws java.lang.Exception Unexpected Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSimpleConversion() throws Exception {
        IDataDecoder decoder = mock(IDataDecoder.class);
        IDataEncoder encoder = mock(IDataEncoder.class);

        when(decoder.iterator()).thenReturn(new TestIterator(data));
        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);

        // Run the test
        FileStreamConverter fsc = new FileStreamConverter(decoder, encoder);
        fsc.run();

        verify(encoder, times(data.size())).write(captor.capture());

        List<Map> capturedValues = captor.getAllValues();
        for (Map capture : capturedValues) {
            Assert.assertTrue(data.contains(capture));
        }
        for (Map item : data) {
            Assert.assertTrue(capturedValues.contains(item));
        }
    }

    /**
     * Test that a filtered conversion can occur.
     *
     * @throws java.lang.Exception Unexpected Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFilteredConversion() throws Exception {
        IDataDecoder decoder = mock(IDataDecoder.class);
        IDataEncoder encoder = mock(IDataEncoder.class);
        IDataFilter filter = mock(IDataFilter.class);

        when(decoder.iterator()).thenReturn(new TestIterator(data));
        for (Map<String, Object> row : data) {
            when(filter.apply(row)).thenReturn(row);
        }

        ArgumentCaptor<Map> filterCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map> finalCaptor = ArgumentCaptor.forClass(Map.class);

        // Run the test
        FileStreamConverter fsc = new FileStreamConverter(decoder, encoder);
        fsc.addFilter(filter);
        fsc.run();

        verify(filter, times(data.size())).apply(filterCaptor.capture());
        verify(encoder, times(data.size())).write(finalCaptor.capture());

        List<Map> capturedFilterValues = filterCaptor.getAllValues();
        for (Map capture : capturedFilterValues) {
            Assert.assertTrue(data.contains(capture));
        }
        for (Map item : data) {
            Assert.assertTrue(capturedFilterValues.contains(item));
        }

        List<Map> capturedFinalValues = finalCaptor.getAllValues();
        for (Map capture : capturedFinalValues) {
            Assert.assertTrue(data.contains(capture));
        }
        for (Map item : data) {
            Assert.assertTrue(capturedFinalValues.contains(item));
        }
    }

    /**
     * Test close with exception.
     *
     * @throws Exception Should throw no exceptions.
     */
    @Test
    public void testCloseWithDecoderException() throws Exception {
        IDataDecoder decoder = mock(IDataDecoder.class);
        IDataEncoder encoder = mock(IDataEncoder.class);

        doThrow(IOException.class).when(decoder).close();

        when(decoder.iterator()).thenReturn(new TestIterator(data));

        // Run the test
        FileStreamConverter fsc = new FileStreamConverter(decoder, encoder);
        fsc.run();

        Assert.assertTrue(true);
    }

    /**
     * Test close with exception.
     *
     * @throws Exception Should throw no exceptions.
     */
    @Test
    public void testCloseWithEncoderException() throws Exception {
        IDataDecoder decoder = mock(IDataDecoder.class);
        IDataEncoder encoder = mock(IDataEncoder.class);

        doThrow(IOException.class).when(encoder).close();

        when(decoder.iterator()).thenReturn(new TestIterator(data));

        // Run the test
        FileStreamConverter fsc = new FileStreamConverter(decoder, encoder);
        fsc.run();

        Assert.assertTrue(true);
    }

    /**
     * A test iterator to make sure our encoding works properly.
     */
    private final class TestIterator implements Iterator<Map<String, Object>> {

        /**
         * Inner data hash.
         */
        private final List<Map<String, Object>> innerData;

        /**
         * Current index.
         */
        private int index = 0;

        /**
         * Create a new instance.
         *
         * @param inputData The iterator data.
         */
        public TestIterator(final List<Map<String, Object>> inputData) {
            innerData = inputData;
        }

        /**
         * Returns whether there's more data to iterate over.
         *
         * @return true if there's data, otherwise false.
         */
        @Override
        public boolean hasNext() {
            return index < innerData.size();
        }

        /**
         * Return the next item.
         *
         * @return Data if there's a next item, otherwise false
         */
        @Override
        public Map<String, Object> next() {
            if (!hasNext()) {
                return null;
            }
            Map<String, Object> item = innerData.get(index);
            index++;
            return item;
        }

        /**
         * Remove things.
         */
        @Override
        public void remove() {
            // Do nothing.
        }
    }
}
