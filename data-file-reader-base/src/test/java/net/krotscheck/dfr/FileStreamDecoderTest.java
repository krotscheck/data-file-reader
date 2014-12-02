/*
 * Copyright (c) 2011-2013 Krotscheck.net, All Rights Reserved.
 */

package net.krotscheck.dfr;

import net.krotscheck.test.UnitTest;
import net.krotscheck.test.dfr.TestDataFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for the FileStream decoder factory.
 *
 * @author Michael Krotscheck
 */
@Category(UnitTest.class)
public final class FileStreamDecoderTest {

    /**
     * The mock mime type.
     */
    private String mockMimeType = "text/mock";

    /**
     * Assert that the decoder can be created.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test
    public void testConstructor() throws Exception {
        InputStream test = mock(InputStream.class);
        new FileStreamDecoder(test, mockMimeType);
        Assert.assertTrue(true);
    }

    /**
     * Test that an exception is thrown when the decoder is not found.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testNoDecoder() throws Exception {
        InputStream test = mock(InputStream.class);
        new FileStreamDecoder(test, "test/unavailable");
    }

    /**
     * Test that the iterator may be retrieved.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test
    public void testGetIterator() throws Exception {
        InputStream test = mock(InputStream.class);
        FileStreamDecoder decoder = new FileStreamDecoder(test, mockMimeType);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertNotNull(iterator);

        decoder.close();
    }

    /**
     * Assert that we can manipulate filters.
     *
     * @throws Exception An unexpected exception.
     */
    @Test
    public void testFilterOperations() throws Exception {
        InputStream test = mock(InputStream.class);
        FileStreamDecoder decoder = new FileStreamDecoder(test, mockMimeType);
        IDataFilter testFilter = new TestDataFilter();

        // Make sure it starts empty.
        Assert.assertEquals(0, decoder.getFilters().size());
        Assert.assertFalse(decoder.containsFilter(testFilter));

        decoder.addFilter(testFilter);
        Assert.assertEquals(1, decoder.getFilters().size());
        Assert.assertTrue(decoder.containsFilter(testFilter));

        // Try adding again, this should be a noop.
        decoder.addFilter(testFilter);
        Assert.assertEquals(1, decoder.getFilters().size());
        Assert.assertTrue(decoder.containsFilter(testFilter));

        // Remove the filter.
        decoder.removeFilter(testFilter);
        Assert.assertEquals(0, decoder.getFilters().size());
        Assert.assertFalse(decoder.containsFilter(testFilter));


        // Test multiple filters.
        List<IDataFilter> filters = new ArrayList<>();
        filters.add(new TestDataFilter());
        filters.add(new TestDataFilter());
        filters.add(new TestDataFilter());

        Assert.assertEquals(0, decoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertFalse(decoder.containsFilter(filter));
        }

        decoder.addFilters(filters);

        Assert.assertEquals(3, decoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertTrue(decoder.containsFilter(filter));
        }

        // Try adding them again, make sure we don't duplicate filters.
        decoder.addFilters(filters);

        Assert.assertEquals(3, decoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertTrue(decoder.containsFilter(filter));
        }

        decoder.clearFilters();

        Assert.assertEquals(0, decoder.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertFalse(decoder.containsFilter(filter));
        }

        // Test applyfilter.
        IDataFilter mockFilter1 = mock(IDataFilter.class);
        IDataFilter mockFilter2 = mock(IDataFilter.class);
        Map<String, Object> data0 = new HashMap<>();
        Map<String, Object> data1 = new HashMap<>();
        Map<String, Object> data2 = new HashMap<>();

        when(mockFilter1.apply(data0)).thenReturn(data1);
        when(mockFilter2.apply(data1)).thenReturn(data2);

        decoder.addFilter(mockFilter1);
        decoder.addFilter(mockFilter2);

        Map<String, Object> result = decoder.applyFilters(data0);

        Assert.assertEquals(result, data2);

        verify(mockFilter1, times(1)).apply(data0);
        verify(mockFilter2, times(1)).apply(data1);
    }
}
