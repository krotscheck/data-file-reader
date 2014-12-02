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

import net.krotscheck.test.dfr.TestDataFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for our filtered datastreams.
 *
 * @author Michael Krotscheck
 */
public final class AbstractFilteredDataStreamTest {

    /**
     * Assert that we can add a filter.
     *
     * @throws Exception An unexpected exception.
     */
    @Test
    public void testSingleOperations() throws Exception {
        AbstractFilteredDataStream testStream = new TestFilteredDataStream();
        IDataFilter testFilter1 = new TestDataFilter();
        IDataFilter testFilter2 = new TestDataFilter();

        // Make sure it starts empty.
        Assert.assertEquals(0, testStream.getFilters().size());
        Assert.assertFalse(testStream.containsFilter(testFilter1));

        testStream.addFilter(testFilter1);
        Assert.assertEquals(1, testStream.getFilters().size());
        Assert.assertTrue(testStream.containsFilter(testFilter1));

        // Try adding again, this should be a noop.
        testStream.addFilter(testFilter1);
        Assert.assertEquals(1, testStream.getFilters().size());
        Assert.assertTrue(testStream.containsFilter(testFilter1));

        // Try to remove a filter that's not in the list.
        testStream.removeFilter(testFilter2);
        Assert.assertEquals(1, testStream.getFilters().size());
        Assert.assertTrue(testStream.containsFilter(testFilter1));

        // Remove the filter.
        testStream.removeFilter(testFilter1);
        Assert.assertEquals(0, testStream.getFilters().size());
        Assert.assertFalse(testStream.containsFilter(testFilter1));
    }

    /**
     * Test that we can add multiple filters, and that filters aren't repeated.
     *
     * @throws Exception An unexpected exception.
     */
    @Test
    public void testBatchOperations() throws Exception {
        AbstractFilteredDataStream testStream = new TestFilteredDataStream();
        List<IDataFilter> filters = new ArrayList<>();
        filters.add(new TestDataFilter());
        filters.add(new TestDataFilter());
        filters.add(new TestDataFilter());

        Assert.assertEquals(0, testStream.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertFalse(testStream.containsFilter(filter));
        }

        testStream.addFilters(filters);

        Assert.assertEquals(3, testStream.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertTrue(testStream.containsFilter(filter));
        }

        // Try adding them again, make sure we don't duplicate filters.
        testStream.addFilters(filters);

        Assert.assertEquals(3, testStream.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertTrue(testStream.containsFilter(filter));
        }

        testStream.clearFilters();

        Assert.assertEquals(0, testStream.getFilters().size());
        for (IDataFilter filter : filters) {
            Assert.assertFalse(testStream.containsFilter(filter));
        }
    }

    /**
     * Test class to use during testing.
     */
    private static class TestFilteredDataStream extends
            AbstractFilteredDataStream {

    }
}
