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

package net.krotscheck.dfr.filter.column;

import net.krotscheck.test.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Unit tests for the column filter.
 *
 * @author Michael Krotscheck
 */
@Category(UnitTest.class)
public final class ColumnFilterTest {

    /**
     * Assert that our constructors work.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testConstructors() throws Exception {
        String[] columnsAsStrings = new String[]{"One", "Two"};
        List<String> columnsAsList = Arrays.asList(columnsAsStrings);
        Set<String> columnsAsSet = new HashSet<>(columnsAsList);

        // Test construction with string array.
        ColumnFilter filter1 = new ColumnFilter(columnsAsStrings);
        Set<String> columns1 = filter1.getColumns();

        Assert.assertTrue(columns1.contains("One"));
        Assert.assertTrue(columns1.contains("Two"));
        Assert.assertEquals(2, columns1.size());

        // Test construction with hash array.
        ColumnFilter filter2 = new ColumnFilter(columnsAsList);
        Set<String> columns2 = filter2.getColumns();

        Assert.assertTrue(columns2.contains("One"));
        Assert.assertTrue(columns2.contains("Two"));
        Assert.assertEquals(2, columns2.size());

        // Test construction with set array.
        ColumnFilter filter3 = new ColumnFilter(columnsAsSet);
        Set<String> columns3 = filter3.getColumns();

        Assert.assertTrue(columns3.contains("One"));
        Assert.assertTrue(columns3.contains("Two"));
        Assert.assertEquals(2, columns3.size());
    }

    /**
     * Assert that our constructors work with null input.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testNullConstructors() throws Exception {
        String[] columnsAsStrings = null;
        List<String> columnsAsList = null;
        Set<String> columnsAsSet = null;

        // Test construction with string array.
        ColumnFilter filter1 = new ColumnFilter(columnsAsStrings);
        Set<String> columns1 = filter1.getColumns();
        Assert.assertEquals(0, columns1.size());

        // Test construction with List.
        ColumnFilter filter2 = new ColumnFilter(columnsAsList);
        Set<String> columns2 = filter2.getColumns();
        Assert.assertEquals(0, columns2.size());

        // Test construction with Set.
        ColumnFilter filter3 = new ColumnFilter(columnsAsSet);
        Set<String> columns3 = filter3.getColumns();
        Assert.assertEquals(0, columns3.size());
    }

    /**
     * Test basic apply.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testBasicApply() throws Exception {
        ColumnFilter filter = new ColumnFilter(new String[]{"One", "Two"});

        Map<String, Object> testData = new LinkedHashMap<>();
        testData.put("One", "Test1");
        testData.put("Three", "Test3");
        testData.put("Two", "Test2");

        Map<String, Object> filteredData = filter.apply(testData);

        Assert.assertNotSame(filteredData, testData);

        Assert.assertTrue(filteredData.containsKey("One"));
        Assert.assertTrue(filteredData.containsKey("Two"));
        Assert.assertFalse(filteredData.containsKey("Three"));

        Assert.assertEquals("Test1", filteredData.get("One"));
        Assert.assertEquals("Test2", filteredData.get("Two"));
    }

    /**
     * Test apply on empty columns.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testNoColumnApply() throws Exception {
        ColumnFilter filter = new ColumnFilter(new String[]{});

        Map<String, Object> testData = new HashMap<>();
        testData.put("One", "Test1");
        testData.put("Three", "Test3");
        testData.put("Two", "Test2");

        Map<String, Object> filteredData = filter.apply(testData);

        Assert.assertNotSame(filteredData, testData);

        Assert.assertFalse(filteredData.containsKey("One"));
        Assert.assertFalse(filteredData.containsKey("Two"));
        Assert.assertFalse(filteredData.containsKey("Three"));

        Assert.assertEquals(0, filteredData.size());
    }

    /**
     * Test apply with no data.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testNoDataApply() throws Exception {
        ColumnFilter filter = new ColumnFilter(new String[]{"One", "Two"});

        Map<String, Object> testData = new HashMap<>();

        Map<String, Object> filteredData = filter.apply(testData);

        Assert.assertNotSame(filteredData, testData);

        Assert.assertTrue(filteredData.containsKey("One"));
        Assert.assertTrue(filteredData.containsKey("Two"));

        Assert.assertNull(filteredData.get("One"));
        Assert.assertNull(filteredData.get("Two"));

        Assert.assertEquals(2, filteredData.size());

    }

    /**
     * Test with null data.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testNullDataApply() throws Exception {
        ColumnFilter filter = new ColumnFilter(new String[]{"One", "Two"});

        Map<String, Object> testData = null;

        Map<String, Object> filteredData = filter.apply(testData);

        Assert.assertNotSame(filteredData, testData);

        Assert.assertTrue(filteredData.containsKey("One"));
        Assert.assertTrue(filteredData.containsKey("Two"));

        Assert.assertNull(filteredData.get("One"));
        Assert.assertNull(filteredData.get("Two"));

        Assert.assertEquals(2, filteredData.size());
    }

    /**
     * Test with columns that are not represented in the data.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testWithXorColumns() throws Exception {
        ColumnFilter filter = new ColumnFilter(new String[]{"One", "Two",
                "Four"});

        Map<String, Object> testData = new HashMap<>();
        testData.put("One", "Test1");
        testData.put("Three", "Test3");
        testData.put("Two", "Test2");

        Map<String, Object> filteredData = filter.apply(testData);

        Assert.assertNotSame(filteredData, testData);

        Assert.assertTrue(filteredData.containsKey("One"));
        Assert.assertTrue(filteredData.containsKey("Two"));
        Assert.assertTrue(filteredData.containsKey("Four"));
        Assert.assertFalse(filteredData.containsKey("Three"));

        Assert.assertEquals("Test1", filteredData.get("One"));
        Assert.assertEquals("Test2", filteredData.get("Two"));
        Assert.assertEquals(null, filteredData.get("Four"));
    }
}
