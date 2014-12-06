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

import net.krotscheck.test.UnitTest;
import net.krotscheck.test.dfr.TestDataFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Unit test for the FileStream encoder factory.
 *
 * @author Michael Krotscheck
 */

@PrepareForTest(EncoderCache.class)
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@Category(UnitTest.class)
public final class FileStreamEncoderTest {

    /**
     * The mock mime type.
     */
    private String mockMimeType = "text/mock";

    /**
     * Assert that the encoder can be created.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test
    public void testConstructor() throws Exception {
        OutputStream test = mock(OutputStream.class);
        new FileStreamEncoder(test, mockMimeType);
        Assert.assertTrue(true);
    }

    /**
     * Test that an exception is thrown when the encoder is not found.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testNoEncoder() throws Exception {
        OutputStream test = mock(OutputStream.class);
        new FileStreamEncoder(test, "test/unavailable");
    }

    /**
     * Assert that an exception is thrown when the service loader goes belly
     * up.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testServiceLoaderBoom() throws Exception {

        mockStatic(EncoderCache.class);
        when(EncoderCache.getEncoder(anyString()))
                .thenThrow(InstantiationException.class);

        OutputStream test = mock(OutputStream.class);
        new FileStreamEncoder(test, "test/unavailable");
    }

    /**
     * Test that the iterator may be retrieved.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test
    public void testGetIterator() throws Exception {
        OutputStream test = mock(OutputStream.class);
        FileStreamEncoder encoder = new FileStreamEncoder(test, mockMimeType);

        Map<String, Object> row = new HashMap<>();
        encoder.write(row);
        encoder.close();
    }

    /**
     * Assert that we can manipulate filters.
     *
     * @throws Exception An unexpected exception.
     */
    @Test
    public void testFilterOperations() throws Exception {
        OutputStream test = mock(OutputStream.class);
        FileStreamEncoder encoder = new FileStreamEncoder(test, mockMimeType);
        IDataFilter testFilter = new TestDataFilter();

        // Make sure it starts empty.
        Assert.assertEquals(0, encoder.getFilters().size());
        Assert.assertFalse(encoder.containsFilter(testFilter));

        encoder.addFilter(testFilter);
        Assert.assertEquals(1, encoder.getFilters().size());
        Assert.assertTrue(encoder.containsFilter(testFilter));

        // Try adding again, this should be a noop.
        encoder.addFilter(testFilter);
        Assert.assertEquals(1, encoder.getFilters().size());
        Assert.assertTrue(encoder.containsFilter(testFilter));

        // Remove the filter.
        encoder.removeFilter(testFilter);
        Assert.assertEquals(0, encoder.getFilters().size());
        Assert.assertFalse(encoder.containsFilter(testFilter));


        // Test multiple filters.
        List<IDataFilter> filters = new ArrayList<>();
        filters.add(new TestDataFilter());
        filters.add(new TestDataFilter());
        filters.add(new TestDataFilter());

        Assert.assertEquals(0, encoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertFalse(encoder.containsFilter(filter));
        }

        encoder.addFilters(filters);

        Assert.assertEquals(3, encoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertTrue(encoder.containsFilter(filter));
        }

        // Try adding them again, make sure we don't duplicate filters.
        encoder.addFilters(filters);

        Assert.assertEquals(3, encoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertTrue(encoder.containsFilter(filter));
        }

        encoder.clearFilters();

        Assert.assertEquals(0, encoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertFalse(encoder.containsFilter(filter));
        }

        // Test applyfilter.
        IDataFilter mockFilter1 = mock(IDataFilter.class);
        IDataFilter mockFilter2 = mock(IDataFilter.class);
        Map<String, Object> data0 = new HashMap<>();
        Map<String, Object> data1 = new HashMap<>();
        Map<String, Object> data2 = new HashMap<>();

        when(mockFilter1.apply(data0)).thenReturn(data1);
        when(mockFilter2.apply(data1)).thenReturn(data2);

        encoder.addFilter(mockFilter1);
        encoder.addFilter(mockFilter2);

        Map<String, Object> result = encoder.applyFilters(data0);

        Assert.assertEquals(result, data2);

        verify(mockFilter1, times(1)).apply(data0);
        verify(mockFilter2, times(1)).apply(data1);
    }
}
