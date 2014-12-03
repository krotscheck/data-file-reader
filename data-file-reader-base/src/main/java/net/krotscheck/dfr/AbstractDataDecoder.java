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
     * The max rows.
     */
    private Long maxRows;

    /**
     * Returns an iterator for the file.
     *
     * @return An iterator.
     */
    public final Iterator<Map<String, Object>> iterator() {
        return new FilteredIterator(buildIterator(), getFilters(), maxRows);
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
     * Get the maximum rows.
     *
     * @return The max rows.
     */
    @Override
    public final Long getMaxRows() {
        return maxRows;
    }

    /**
     * Set the maximum rows.
     *
     * @param rows The number of rows, default null.
     */
    @Override
    public final void setMaxRows(final Long rows) {
        this.maxRows = rows;
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
         * The total number of rows to permit.
         */
        private final Long rows;

        /**
         * The current index.
         */
        private Long currentRow = (long) 0;

        /**
         * Create a new filtered iterator with a maximum number of allowed
         * rows.
         *
         * @param itr     The iterator to wrap.
         * @param fltrs   The filters to apply.
         * @param maxRows The number of rows to permit. If null will returns all
         *                rows.
         */
        public FilteredIterator(final Iterator<Map<String, Object>> itr,
                                final List<IDataFilter> fltrs,
                                final Long maxRows) {
            this.iterator = itr;
            this.filters = fltrs;
            this.rows = maxRows;
        }

        /**
         * Do we have a next row?
         *
         * @return Whether there's another row.
         */
        @Override
        public boolean hasNext() {
            if (rows == null || currentRow < rows) {
                return iterator.hasNext();
            }
            return false;
        }

        /**
         * Apply all the configured filters.
         *
         * @return A filtered row.
         */
        @Override
        public Map<String, Object> next() {
            // Confirm that we're not done yet.
            if (rows != null && currentRow >= rows) {
                return null;
            }

            Map<String, Object> row = iterator.next();
            for (IDataFilter filter : filters) {
                row = filter.apply(row);
            }
            currentRow++;
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
