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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An abstract implementation of the data decoder, to consolidate some common
 * functionality and provide a simple extension point.
 *
 * @author Michael Krotscheck
 */
public abstract class AbstractDataDecoder
        extends AbstractFilteredDataStream
        implements IDataDecoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(AbstractDataDecoder.class);

    /**
     * The input stream.
     */
    private InputStream inputStream;

    /**
     * Returns an iterator for the file.
     *
     * @return An iterator.
     */
    public final Iterator<Map<String, Object>> iterator() {
        return new FilteredIterator(buildIterator(), getFilters());
    }

    /**
     * Internal iterator builder. Implement this for your own data decoder.
     *
     * @return An iterator from the provided file type.
     */
    protected abstract Iterator<Map<String, Object>> buildIterator();

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

    /**
     * An internal iterator that applies all of our filters.
     */
    private static final class FilteredIterator
            implements Iterator<Map<String, Object>> {

        /**
         * The internal list of filters to apply to each row.
         */
        private final List<IDataFilter> filters;

        /**
         * The internal iterator.
         */
        private final Iterator<Map<String, Object>> iterator;

        /**
         * Create a new filtered iterator.
         *
         * @param itr   The iterator to wrap.
         * @param fltrs The filters to apply.
         */
        public FilteredIterator(final Iterator<Map<String, Object>> itr,
                                final List<IDataFilter> fltrs) {
            this.iterator = itr;
            this.filters = fltrs;
        }

        /**
         * Do we have a next row?
         *
         * @return Whether there's another row.
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Apply all the configured filters.
         *
         * @return A filtered row.
         */
        @Override
        public Map<String, Object> next() {
            Map<String, Object> row = iterator.next();
            for (IDataFilter filter : filters) {
                row = filter.apply(row);
            }
            return row;
        }

        /**
         * Remove the current row.
         */
        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
