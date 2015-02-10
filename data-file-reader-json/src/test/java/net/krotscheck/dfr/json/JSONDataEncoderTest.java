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

package net.krotscheck.dfr.json;

import net.krotscheck.dfr.IDataEncoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
 * Test our JSON Data Encoder.
 *
 * @author Michael Krotscheck
 */
public final class JSONDataEncoderTest {

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
            if (encoder instanceof JSONDataEncoder) {
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
        JSONDataEncoder encoder = new JSONDataEncoder();
        Assert.assertEquals("application/json", encoder.getMimeType());
    }

    /**
     * Do a simple test write.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testSimpleEncoder() throws Exception {
        JSONDataEncoder encoder = new JSONDataEncoder();
        OutputStreamWriter osw = new OutputStreamWriter(baos);
        encoder.setWriter(osw);
        for (Map<String, Object> row : testData) {
            encoder.write(row);
        }
        encoder.close();
        Assert.assertTrue(encoder.toString().length() > 0);

        JSONDataDecoder decoder = new JSONDataDecoder();
        InputStreamReader isr = new InputStreamReader(
                new ByteArrayInputStream(baos.toByteArray())
        );
        decoder.setReader(isr);

        Integer count = 0;
        for (Map<String, Object> resultRow : decoder) {
            Assert.assertEquals(count, resultRow.get("column_1"));
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
        JSONDataEncoder encoder = new JSONDataEncoder();
        Writer mockWriter = mock(Writer.class);
        encoder.setWriter(mockWriter);
        encoder.close();

        Assert.assertNull(encoder.getWriter());

        verify(mockWriter, times(1)).close();
    }

    /**
     * Assert close called with exception doesn't blow up in our faces.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testExceptionClose() throws Exception {
        JSONDataEncoder encoder = new JSONDataEncoder();
        Writer mockWriter = mock(Writer.class);
        doThrow(IOException.class).when(mockWriter).close();

        encoder.setWriter(mockWriter);
        encoder.close();

        Assert.assertNull(encoder.getWriter());

        // The json buffer is flushed on close and on write.
        verify(mockWriter, times(1)).close();
    }
}
