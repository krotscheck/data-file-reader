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

import net.krotscheck.dfr.AbstractDataDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * An abstract implementation of the an input-stream based data decoder.
 *
 * @author Michael Krotscheck
 */
public abstract class AbstractStreamDecoder
        extends AbstractDataDecoder
        implements IStreamDecoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(AbstractStreamDecoder.class);

    /**
     * The input stream.
     */
    private InputStream inputStream;

    /**
     * Get the input stream for this decoder.
     *
     * @return The inputstream.
     */
    @Override
    public final InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Set the input stream for this decoder.
     *
     * @param stream The input stream.
     */
    @Override
    public final void setInputStream(final InputStream stream) {
        inputStream = stream;
    }

    /**
     * Close the input stream.
     */
    @Override
    public final void close() {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException ioe) {
                logger.error("Unable to close output stream.", ioe);
            } finally {
                this.inputStream = null;
            }
        }

        dispose();
    }

    /**
     * Protected close method, for child implementations.
     */
    protected abstract void dispose();
}
