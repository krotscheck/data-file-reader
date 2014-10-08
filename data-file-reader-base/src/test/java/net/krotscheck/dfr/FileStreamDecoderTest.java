/*
 * Copyright (c) 2011-2013 Krotscheck.net, All Rights Reserved.
 */

package net.krotscheck.dfr;

import net.krotscheck.test.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.Mockito.mock;

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
}
