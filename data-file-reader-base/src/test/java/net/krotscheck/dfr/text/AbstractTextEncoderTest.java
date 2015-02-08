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

package net.krotscheck.dfr.text;

import net.krotscheck.dfr.AbstractDataEncoder;
import net.krotscheck.test.dfr.TestTextEncoder;
import org.apache.commons.io.output.NullWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the abstract data decoder.
 *
 * @author Michael Krotscheck
 */
public final class AbstractTextEncoderTest {

    /**
     * Assert that we can change the destination.
     *
     * @throws Exception Should not be thrown.
     */
    @Test
    public void testGetSetDestination() throws Exception {
        ITextEncoder encoder = new TestTextEncoder();

        Assert.assertNull(encoder.getWriter());

        // Test input stream
        Writer writer = new NullWriter();
        encoder.setWriter(writer);
        Assert.assertEquals(writer, encoder.getWriter());
    }

    /**
     * Assert that we can call close without an output stream.
     *
     * @throws Exception Should not be thrown.
     */
    @Test
    public void testCloseWithoutStream() throws Exception {
        ITextEncoder encoder = new TestTextEncoder();

        Assert.assertNull(encoder.getWriter());

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
        ITextEncoder encoder = new TestTextEncoder();

        Assert.assertNull(encoder.getWriter());
        Writer writer = new NullWriter();
        encoder.setWriter(writer);
        Assert.assertEquals(writer, encoder.getWriter());

        encoder.close();

        Assert.assertNull(encoder.getWriter());
    }


    /**
     * Assert that we can call close with an output stream that errors.
     *
     * @throws Exception Should not be thrown.
     */
    @Test
    public void testCloseWithErrorStream() throws Exception {
        ITextEncoder encoder = new TestTextEncoder();

        Assert.assertNull(encoder.getWriter());
        Writer writer = mock(Writer.class);
        doThrow(IOException.class).when(writer).close();
        encoder.setWriter(writer);
        Assert.assertEquals(writer, encoder.getWriter());
        encoder.close();
        Assert.assertNull(encoder.getWriter());
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
