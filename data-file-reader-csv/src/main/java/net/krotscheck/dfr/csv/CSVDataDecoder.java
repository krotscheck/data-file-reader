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

package net.krotscheck.dfr.csv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import net.krotscheck.dfr.AbstractDataDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This data decoder will stream in a CSV file and generate objects for every
 * row found. Due to the nature of java, it is assumed that column labels act as
 * schema indicators and are contained in the first row of the file.
 *
 * @author Michael Krotscheck
 */
public final class CSVDataDecoder extends AbstractDataDecoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(CSVDataDecoder.class);

    /**
     * Returns an iterator for the file.
     *
     * @return A row iterator.
     */
    @Override
    public Iterator<Map<String, Object>> iterator() {
        return new InnerRowIterator(getInputStream());
    }

    /**
     * Return the decoding mimetype which this decoder supports.
     *
     * @return "text/csv"
     */
    @Override
    public String getMimeType() {
        return "text/csv";
    }

    /**
     * Dispose of the decoder.
     */
    @Override
    protected void dispose() {
        // Do nothing, this decoder is handled by the parent class.
    }

    /**
     * Internal iterator class, which wraps a Jackson parser.
     */
    private static final class InnerRowIterator
            implements Iterator<Map<String, Object>> {

        /**
         * A mapping iterator from the CSV decoder.
         */
        private MappingIterator<Map<String, Object>> innerIterator;

        /**
         * Create a new iterator from the given inputstream.
         *
         * @param csvStream An InputStream of CSV rows.
         */
        public InnerRowIterator(final InputStream csvStream) {
            // Construct our schema.
            CsvSchema schema = CsvSchema.emptySchema().withUseHeader(true);
            CsvMapper mapper = new CsvMapper();
            TypeReference<HashMap<String, Object>> typeRef =
                    new TypeReference<HashMap<String, Object>>() {

                    };
            try {
                ObjectReader reader = mapper.reader(schema).withType(typeRef);
                innerIterator = reader.readValues(csvStream);
            } catch (IOException ioe) {
                logger.error("CSV File does not exist.");
            }
        }

        /**
         * Do we have another item in the file?
         *
         * @return true if there's another one, otherwise false.
         */
        @Override
        public boolean hasNext() {
            if (innerIterator != null) {
                return innerIterator.hasNext();
            } else {
                return false;
            }
        }

        /**
         * Return the next item.
         *
         * @return The next item.
         */
        @Override
        public Map<String, Object> next() {
            if (hasNext()) {
                return innerIterator.next();
            }
            return null;
        }

        /**
         * We cannot remove from a stream.
         */
        @Override
        public void remove() {
            // Does nothing.
        }
    }
}
