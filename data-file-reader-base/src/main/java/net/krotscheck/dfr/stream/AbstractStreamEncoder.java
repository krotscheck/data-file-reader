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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An abstract implementation of the data encoder, to consolidate some common
 * functionality and provide a simple extension point.
 *
 * @author Michael Krotscheck
 */
public abstract class AbstractStreamEncoder
        extends AbstractDataEncoder
        implements IStreamEncoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(AbstractStreamEncoder.class);

    /**
     * The output stream to write to.
     */
    private OutputStream outputStream;

    /**
     * Get the destination for our output stream.
     *
     * @return The destination.
     */
    @Override
    public final OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Set the destination for our output stream.
     *
     * @param stream The destination.
     */
    @Override
    public final void setOutputStream(final OutputStream stream) {
        this.outputStream = stream;
    }

    /**
     * Close the internal stream.
     */
    @Override
    public final void close() {
        dispose();

        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (IOException ioe) {
                logger.error("Unable to close output stream.", ioe);
            } finally {
                this.outputStream = null;
            }
        }
    }

    /**
     * Protected close method, for child implementations.
     */
    protected abstract void dispose();
}
