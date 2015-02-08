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

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * Unit tests for our data encoder cache.
 *
 * @author Michael Krotscheck
 */
public final class EncoderCacheTest {

    /**
     * The mock mime type.
     */
    private final String mockMimeType = "text/mock";

    /**
     * Assert that the encoder can be created.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test
    public void testKnownMimeType() throws Exception {
        IDataEncoder encoder = EncoderCache.getEncoder(mockMimeType);
        Assert.assertNotNull(encoder);
    }

    /**
     * Test that an exception is thrown when the encoder is not found.
     *
     * @throws Exception Thrown when we can't find the encoder.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testNoEncoder() throws Exception {
        EncoderCache.getEncoder("test/unavailable");
    }

    /**
     * Assert that the constructor is private.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void assertPrivateConstructor() throws Exception {
        Constructor<EncoderCache> constructor =
                EncoderCache.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * Assert that encoders are reconstructed.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void assertEncodersThreadsafe() throws Exception {
        IDataEncoder encoder1 = EncoderCache.getEncoder(mockMimeType);
        IDataEncoder encoder2 = EncoderCache.getEncoder(mockMimeType);
        Assert.assertNotSame(encoder1, encoder2);
    }

    /**
     * Assert that all mimetypes are listed.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testGetAllMimeTypes() throws Exception {
        Set<String> mimeTypes = EncoderCache.supportedMimeTypes();

        Assert.assertEquals(3, mimeTypes.size());
        Assert.assertTrue(mimeTypes.contains(mockMimeType));
    }

    /**
     * Assert that mimetypes can be checked.
     *
     * @throws Exception Unexpected exceptions.
     */
    @Test
    public void testIsMimetypeSupported() throws Exception {
        Assert.assertFalse(
                EncoderCache.isMimeTypeSupported("test/unavailable")
        );
        Assert.assertTrue(
                EncoderCache.isMimeTypeSupported(mockMimeType)
        );
    }
}
