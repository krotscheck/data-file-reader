/*
 * Copyright (c) 2015 Michael Krotscheck
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

package net.krotscheck.dfr.stream;

import net.krotscheck.dfr.AbstractDataEncoder;
import net.krotscheck.test.dfr.TestStreamEncoder;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the abstract data decoder.
 *
 * @author Michael Krotscheck
 */
public final class AbstractStreamEncoderTest {

    /**
     * Assert that we can change the destination.
     *
     * @throws Exception Should not be thrown.
     */
    @Test
    public void testGetSetDestination() throws Exception {
        IStreamEncoder encoder = new TestStreamEncoder();

        Assert.assertNull(encoder.getOutputStream());

        // Test input stream
        OutputStream stream = new NullOutputStream();
        encoder.setOutputStream(stream);
        Assert.assertEquals(stream, encoder.getOutputStream());
    }

    /**
     * Assert that we can call close without an output stream.
     *
     * @throws Exception Should not be thrown.
     */
    @Test
    public void testCloseWithoutStream() throws Exception {
        IStreamEncoder encoder = new TestStreamEncoder();

        Assert.assertNull(encoder.getOutputStream());

        // Make sure nothing happens
        encoder.close();
    }

    /**
     * Assert that we can call close with an output stream.
     *
     * @throws Exception Should not be thrown.
     */
    @Test
    public void testCloseWithStream() throws Exception {
        IStreamEncoder encoder = new TestStreamEncoder();

        Assert.assertNull(encoder.getOutputStream());
        OutputStream stream = new NullOutputStream();
        encoder.setOutputStream(stream);
        Assert.assertEquals(stream, encoder.getOutputStream());

        encoder.close();

        Assert.assertNull(encoder.getOutputStream());
    }


    /**
     * Assert that we can call close with an output stream that errors.
     *
     * @throws Exception Should not be thrown.
     */
    @Test
    public void testCloseWithErrorStream() throws Exception {
        IStreamEncoder encoder = new TestStreamEncoder();

        Assert.assertNull(encoder.getOutputStream());
        OutputStream stream = mock(OutputStream.class);
        doThrow(IOException.class).when(stream).close();
        encoder.setOutputStream(stream);
        Assert.assertEquals(stream, encoder.getOutputStream());
        encoder.close();
        Assert.assertNull(encoder.getOutputStream());
    }

    /**
     * Ensure the constructor is abstract.
     *
     * @throws Exception Tests throw exceptions.
     */
    @Test(expected = InstantiationException.class)
    public void testConstructorIsAbstract() throws Exception {
        Constructor<AbstractDataEncoder> constructor =
                AbstractDataEncoder.class.getDeclaredConstructor();

        // Try to create an instance
        constructor.newInstance();
    }
}
