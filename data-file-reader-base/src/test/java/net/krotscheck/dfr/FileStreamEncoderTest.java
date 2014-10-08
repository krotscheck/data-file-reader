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
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * Unit test for the FileStream encoder factory.
 *
 * @author Michael Krotscheck
 */
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
     * Test that the iterator may be retrieved.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test
    public void testGetIterator() throws Exception {
        OutputStream test = mock(OutputStream.class);
        FileStreamEncoder encoder = new FileStreamEncoder(test, mockMimeType);

        Map<String, Object> row = new HashMap<String, Object>();
        encoder.write(row);
        encoder.close();
    }
}
