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

package net.krotscheck.dfr.csv;

import net.krotscheck.dfr.IDataDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the CSV Data Decoder.
 *
 * @author Michael Krotscheck
 */
public final class CSVDataDecoderTest {

    /**
     * A list of pregenerated test data for our encoder.
     */
    private ByteArrayInputStream bais;

    /**
     * Reset our test data.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Before
    public void setup() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos);
        CSVDataEncoder encoder = new CSVDataEncoder();
        encoder.setWriter(writer);

        for (int i = 0; i < 10; i++) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("column_1", i);
            data.put("column_2", String.format("String %s", i));
            data.put("column_3", "foo");
            encoder.write(data);
        }
        encoder.close();

        bais = new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Make sure the encoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testDecoderDiscovery() {
        ServiceLoader<IDataDecoder> loader
                = ServiceLoader.load(IDataDecoder.class);

        for (IDataDecoder encoder : loader) {
            if (encoder instanceof CSVDataDecoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }

    /**
     * Assert that the mimetype is correct.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testMimeType() throws Exception {
        CSVDataDecoder decoder = new CSVDataDecoder();
        Assert.assertEquals("text/csv", decoder.getMimeType());
    }

    /**
     * Do a simple test read.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testSimpleDecoder() throws Exception {

        InputStreamReader reader = new InputStreamReader(bais);
        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(reader);

        Integer count = 0;
        for (Map<String, Object> resultRow : decoder) {
            Assert.assertEquals(String.valueOf(count),
                    resultRow.get("column_1"));
            Assert.assertEquals(String.format("String %s", count),
                    resultRow.get("column_2"));
            Assert.assertEquals("foo", resultRow.get("column_3"));
            count++;
        }
        Assert.assertEquals((int) count, 10);
    }

    /**
     * Make sure the remove() method does nothing.
     */
    @Test
    public void testRemove() {

        InputStreamReader reader = new InputStreamReader(bais);
        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(reader);

        Iterator<Map<String, Object>> iterator = decoder.iterator();

        // This should do nothing, we should have 10 rows even after multiple
        // remove calls.
        for (Integer i = 0; i < 10; i++) {
            iterator.remove();
            Assert.assertTrue(iterator.hasNext());
            iterator.next();
        }
    }

    /**
     * Make sure hasNext returns false at the end.
     */
    @Test
    public void testHasNext() {

        InputStreamReader reader = new InputStreamReader(bais);
        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(reader);

        Iterator<Map<String, Object>> iterator = decoder.iterator();

        // This should do nothing, we should have 10 rows even after multiple
        // remove calls.
        for (Integer i = 0; i < 10; i++) {
            Assert.assertTrue(iterator.hasNext());
            iterator.next();
        }

        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Assert that a constructor with a bad input stream still behaves like an
     * iterator, albeit an empty one.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testIteratorException() throws Exception {
        Reader reader = mock(Reader.class);
        when(reader.read()).thenThrow(new IOException());

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(reader);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Make sure that we can close things.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testClose() throws Exception {
        CSVDataDecoder decoder = new CSVDataDecoder();

        Reader reader = mock(Reader.class);
        decoder.setReader(reader);
        decoder.close();

        verify(reader, times(1)).close();

        Assert.assertNull(decoder.getReader());
    }

    /**
     * Make sure that throwing an exception still exits cleanly.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testCloseException() throws Exception {
        CSVDataDecoder decoder = new CSVDataDecoder();

        Reader reader = mock(Reader.class);
        doThrow(IOException.class).when(reader).close();

        decoder.setReader(reader);
        decoder.close();

        verify(reader, times(1)).close();

        Assert.assertNull(decoder.getReader());
    }

    /**
     * Test with an empty input stream.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testEmptyInputStream() throws Exception {
        CSVDataDecoder decoder = new CSVDataDecoder();

        Reader reader = mock(Reader.class);
        decoder.setReader(reader);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertNull(iterator.next());
    }

    /**
     * Assert that reading from a CSV file returns the schema in order.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testOrderedColumnRead() throws Exception {

        // Valid data.
        StringReader orderedReader = new StringReader("one,two,three,four,"
                + "five,six\n"
                + "1,2,3,4,5,6\n"
                + "1,2,3,4,5,6");

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(orderedReader);

        for (Map<String, Object> row : decoder) {
            Assert.assertTrue(row instanceof LinkedHashMap);
        }
    }

    /**
     * Test with an invalid csv string.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testBadCsv() throws Exception {

        // Generate a bad csv here. We take the existing one and slice it in
        // half.
        StringReader badReader = new StringReader("1';3;4;");

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(badReader);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertNull(iterator.next());
    }

    /**
     * Test with an invalid object structure.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testBadCsvWithoutArray() throws Exception {

        // Generate a bad csv here, a CSV file with headers but no content.
        StringReader badReader = new StringReader("1,3,4,");

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(badReader);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertNull(iterator.next());
    }
}
