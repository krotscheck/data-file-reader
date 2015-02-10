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

import net.krotscheck.test.dfr.TestDataDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doReturn;
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
     * Assert that we can change max rows.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    public void testGetSetMaxRows() throws Exception {
        IDataDecoder decoder = new TestDataDecoder(testData);

        Assert.assertNull(decoder.getMaxRows());

        decoder.setMaxRows((long) 100);
        Assert.assertEquals((long) 100, (long) decoder.getMaxRows());

        decoder.setMaxRows((long) 222);
        Assert.assertEquals((long) 222, (long) decoder.getMaxRows());

        decoder.setMaxRows(null);
        Assert.assertNull(decoder.getMaxRows());
    }

    /**
     * Assert that the input field may be filtered.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testFilteredIterator() throws Exception {
        Map<String, Object> mockData = new LinkedHashMap<>();

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
     * Assert that we can limit the number of rows returned.
     *
     * @throws Exception Should not throw an exception.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testLimitReturnedRows() throws Exception {

        // Start by asserting that our test decoder iterates over all 100 rows.
        IDataDecoder decoder = new TestDataDecoder(testData);

        int count = 0;
        for (Map<String, Object> row : decoder) {
            Assert.assertNotNull(row);
            Assert.assertEquals("column_one_row_" + count, row.get("one"));
            count++;
        }
        Assert.assertEquals(100, count);

        // Limit to 10
        decoder.setMaxRows((long) 10);
        count = 0;
        Iterator<Map<String, Object>> i = decoder.iterator();
        while (i.hasNext()) {
            Map<String, Object> row = i.next();
            Assert.assertNotNull(row);
            Assert.assertEquals("column_two_row_" + count, row.get("two"));
            count++;
        }
        Assert.assertEquals(10, count);

        // Check the leftover iterator.
        Assert.assertNull(i.next());
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
}
