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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
        CSVDataEncoder encoder = new CSVDataEncoder();
        encoder.setOutputStream(baos);

        for (int i = 0; i < 10; i++) {
            Map<String, Object> data = new HashMap<>();
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
     * Do a simple test write.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testSimpleDecoder() throws Exception {

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setInputStream(bais);

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

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setInputStream(bais);

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

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setInputStream(bais);

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
        FileInputStream fis = mock(FileInputStream.class);
        when(fis.available()).thenReturn(1000);
        when(fis.read()).thenThrow(new IOException());

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setInputStream(fis);

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

        InputStream input = mock(InputStream.class);
        decoder.setInputStream(input);
        decoder.close();

        verify(input, times(1)).close();

        Assert.assertNull(decoder.getInputStream());
    }

    /**
     * Make sure that throwing an exception still exits cleanly.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testCloseException() throws Exception {
        CSVDataDecoder decoder = new CSVDataDecoder();

        InputStream input = mock(InputStream.class);
        doThrow(IOException.class).when(input).close();

        decoder.setInputStream(input);
        decoder.close();

        verify(input, times(1)).close();

        Assert.assertNull(decoder.getInputStream());
    }

    /**
     * Test with an empty input stream.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testEmptyInputStream() throws Exception {
        CSVDataDecoder decoder = new CSVDataDecoder();

        InputStream input = mock(InputStream.class);
        decoder.setInputStream(input);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertNull(iterator.next());
    }

    /**
     * Test with an invalid json string.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testBadJson() throws Exception {

        // Generate a bad json here. We take the existing one and slice it in
        // half.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CSVDataEncoder encoder = new CSVDataEncoder();
        encoder.setOutputStream(baos);

        Map<String, Object> data = new HashMap<>();
        data.put("column_1", 0);
        data.put("column_2", String.format("String %s", 0));
        data.put("column_3", "foo");
        encoder.write(data);
        encoder.close();

        byte[] oldData = baos.toByteArray();
        byte[] badData = Arrays.copyOfRange(oldData, 0, oldData.length / 2);
        InputStream badInputStream = new ByteArrayInputStream(badData);

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setInputStream(badInputStream);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertNull(iterator.next());
    }

    /**
     * Test with an invalid object structure.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testBadJsonWithoutArray() throws Exception {

        // Generate a bad json here. We take the existing one and slice it in
        // half.
        String badCSV = "{}";
        InputStream badInputStream = new ByteArrayInputStream(badCSV
                .getBytes(StandardCharsets.UTF_8));

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setInputStream(badInputStream);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertNull(iterator.next());
    }

    /**
     * Test with an empty object structure.
     *
     * @throws Exception Any unexpected exceptions.
     */
    @Test
    public void testBadJsonEmptyArray() throws Exception {

        // Generate a bad json here. We take the existing one and slice it in
        // half.
        String badCSV = "[]";
        InputStream badInputStream = new ByteArrayInputStream(badCSV
                .getBytes(StandardCharsets.UTF_8));

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setInputStream(badInputStream);

        Iterator<Map<String, Object>> iterator = decoder.iterator();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertNull(iterator.next());
    }
}
