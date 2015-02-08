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

package net.krotscheck.dfr.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.krotscheck.dfr.text.AbstractTextDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

/**
 * This data decoder will read in a Json encoded file and generate objects for
 * every row found. Note that no significant error checking is performed here,
 * the file is assumed to be an array of un-nested objects.
 *
 * @author Michael Krotscheck
 */
public final class JSONDataDecoder extends AbstractTextDecoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(JSONDataDecoder.class);

    /**
     * Create the iterator for the JSON file.
     *
     * @return An iterator.
     */
    @Override
    protected Iterator<Map<String, Object>> buildIterator() {
        return new InnerRowIterator(getReader());
    }

    /**
     * Return the decoding mimetype which this decoder supports.
     *
     * @return "application/json"
     */
    @Override
    public String getMimeType() {
        return "application/json";
    }

    /**
     * Dispose of this decoder.
     */
    @Override
    protected void dispose() {
        // Do nothing.
    }

    /**
     * Internal iterator class, which wraps a Jackson parser.
     */
    private static final class InnerRowIterator
            implements Iterator<Map<String, Object>> {

        /**
         * The JSON Parser.
         */
        private JsonParser parser;

        /**
         * Type reference, for object mapping.
         */
        private TypeReference<Map<String, Object>> mapReference =
                new TypeReference<Map<String, Object>>() {

                };

        /**
         * Create a new instance of the iterator, wrapping an reader that is
         * assumed to be pointed at a properly formatted json data file.
         *
         * @param jsonReader The reader.
         */
        private InnerRowIterator(final Reader jsonReader) {

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = new JsonFactory(mapper);
                parser = factory.createParser(jsonReader);

                if (parser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IOException(
                            "File does not contains an array of objects.");
                }

                parser.nextToken();
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());
                parser = null;
            }
        }

        /**
         * Do we have another row?
         *
         * @return True if we have one, otherwise false.
         */
        @Override
        public boolean hasNext() {
            if (parser != null) {
                JsonToken token = parser.getCurrentToken();
                return token == JsonToken.START_OBJECT;
            } else {
                return false;
            }
        }

        /**
         * Retrieve the next item.
         *
         * @return The next row.
         */
        @Override
        public Map<String, Object> next() {
            if (parser != null && hasNext()) {
                try {
                    Map<String, Object> next = parser.readValueAs(mapReference);
                    parser.nextToken();
                    return next;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            return null;
        }

        /**
         * Unimplemented.
         */
        @Override
        public void remove() {
            // Do nothing- we can't remove from a reader.
        }
    }
}
