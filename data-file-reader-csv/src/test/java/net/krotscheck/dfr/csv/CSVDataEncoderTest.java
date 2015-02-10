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

import net.krotscheck.dfr.IDataEncoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test our CSV Data Encoder.
 *
 * @author Michael Krotscheck
 */
public final class CSVDataEncoderTest {

    /**
     * A list of pregenerated test data for our encoder.
     */
    private List<Map<String, Object>> testData;

    /**
     * Test output stream.
     */
    private ByteArrayOutputStream baos;

    /**
     * Reset our test data.
     */
    @Before
    public void setup() {
        baos = new ByteArrayOutputStream();

        testData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("column_1", i);
            data.put("column_2", String.format("String %s", i));
            data.put("column_3", "foo");
            testData.add(data);
        }
    }

    /**
     * Make sure the encoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testEncoderDiscovery() {
        ServiceLoader<IDataEncoder> loader
                = ServiceLoader.load(IDataEncoder.class);

        for (IDataEncoder encoder : loader) {
            if (encoder instanceof CSVDataEncoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }

    /**
     * Assert that the mimetype is correct.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testMimeType() throws Exception {
        CSVDataEncoder encoder = new CSVDataEncoder();
        Assert.assertEquals("text/csv", encoder.getMimeType());
    }

    /**
     * Do a simple test write.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testSimpleEncoder() throws Exception {
        CSVDataEncoder encoder = new CSVDataEncoder();
        StringWriter writer = new StringWriter();
        encoder.setWriter(writer);
        for (Map<String, Object> row : testData) {
            encoder.write(row);
        }
        encoder.close();
        Assert.assertTrue(encoder.toString().length() > 0);

        CSVDataDecoder decoder = new CSVDataDecoder();
        decoder.setReader(new StringReader(writer.toString()));

        Integer count = 0;
        for (Map<String, Object> resultRow : decoder) {
            Assert.assertEquals(String.valueOf(count),
                    resultRow.get("column_1"));
            Assert.assertEquals(String.format("String %s", count),
                    resultRow.get("column_2"));
            Assert.assertEquals("foo", resultRow.get("column_3"));
            count++;
        }
    }

    /**
     * Assert close called prematurely.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testPrematureClose() throws Exception {
        CSVDataEncoder encoder = new CSVDataEncoder();
        Writer writer = mock(Writer.class);
        encoder.setWriter(writer);
        encoder.close();

        Assert.assertNull(encoder.getWriter());

        verify(writer, times(1)).close();
    }

    /**
     * Assert close called with exception doesn't blow up in our faces.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testExceptionClose() throws Exception {
        CSVDataEncoder encoder = new CSVDataEncoder();
        Writer writer = mock(Writer.class);
        encoder.setWriter(writer);
        encoder.write(testData.get(0)); // Open the stream

        doThrow(IOException.class).when(writer).close();

        encoder.close();

        Assert.assertNull(encoder.getWriter());

        // Write for header and data.
        verify(writer, times(1)).close();
    }
}
