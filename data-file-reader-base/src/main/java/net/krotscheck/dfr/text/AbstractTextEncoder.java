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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;

/**
 * An abstract implementation of the data encoder, to consolidate some common
 * functionality and provide a simple extension point.
 *
 * @author Michael Krotscheck
 */
public abstract class AbstractTextEncoder
        extends AbstractDataEncoder
        implements ITextEncoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(AbstractTextEncoder.class);

    /**
     * The writer for our encoder.
     */
    private Writer outputWriter;

    /**
     * Get the writer for this encoder.
     *
     * @return The writer.
     */
    @Override
    public final Writer getWriter() {
        return outputWriter;
    }

    /**
     * Set the writer for this encoder.
     *
     * @param writer The writer.
     */
    @Override
    public final void setWriter(final Writer writer) {
        this.outputWriter = writer;
    }

    /**
     * Close the internal stream.
     */
    @Override
    public final void close() {
        dispose();

        if (this.outputWriter != null) {
            try {
                this.outputWriter.close();
            } catch (IOException ioe) {
                logger.error("Unable to close output stream.", ioe);
            } finally {
                this.outputWriter = null;
            }
        }
    }

    /**
     * Protected close method, for child implementations.
     */
    protected abstract void dispose();
}
