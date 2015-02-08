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

import net.krotscheck.dfr.AbstractDataDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * An abstract implementation of a characterstream based data decoder.
 *
 * @author Michael Krotscheck
 */
public abstract class AbstractTextDecoder
        extends AbstractDataDecoder
        implements ITextDecoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(AbstractTextDecoder.class);

    /**
     * The input reader.
     */
    private Reader inputReader;

    /**
     * Get the reader for this decoder.
     *
     * @return The reader.
     */
    @Override
    public final Reader getReader() {
        return inputReader;
    }

    /**
     * Set the reader for this decoder.
     *
     * @param reader The reader.
     */
    @Override
    public final void setReader(final Reader reader) {
        this.inputReader = reader;
    }

    /**
     * Close the input stream.
     */
    @Override
    public final void close() {
        if (this.inputReader != null) {
            try {
                this.inputReader.close();
            } catch (IOException ioe) {
                logger.error("Unable to close input reader.", ioe);
            } finally {
                this.inputReader = null;
            }
        }

        dispose();
    }

    /**
     * Protected close method, for child implementations.
     */
    protected abstract void dispose();
}
