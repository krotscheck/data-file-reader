/*
 * Copyright (c) 2011-2013 Krotscheck.net, All Rights Reserved.
 */

package net.krotscheck.dfr.bson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.krotscheck.dfr.AbstractDataDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import de.undercouch.bson4jackson.BsonFactory;

/**
 * This data decoder will stream in a BSON encoded file and generate objects for
 * every row found.
 *
 * @author Michael Krotscheck
 */
public final class BSONDataDecoder extends AbstractDataDecoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(BSONDataDecoder.class);

    /**
     * Returns an iterator for the file.
     *
     * @return An iterator.
     */
    @Override
    public Iterator<Map<String, Object>> iterator() {
        return new InnerRowIterator(getInputStream());
    }

    /**
     * Return the decoding mimetype which this decoder supports.
     *
     * @return "application/bson"
     */
    @Override
    public String getMimeType() {
        return "application/bson";
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
         * Our jackson json parser - which is going to write BSON for us.
         */
        private JsonParser parser;

        /**
         * Type reference, everything's a string/object pair.
         */
        private TypeReference<Map<String, Object>> mapReference =
                new TypeReference<Map<String, Object>>() {

                };

        /**
         * Create a new iterator.
         *
         * @param bsonStream The BSON Input stream.
         */
        private InnerRowIterator(final InputStream bsonStream) {

            try {
                // Construct our iterator.
                ObjectMapper mapper = new ObjectMapper();
                BsonFactory factory = new BsonFactory(mapper);
                parser = factory.createParser(bsonStream);

                // BSON formats arrays as { 0: {obj}, 1: {obj}...}
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException(
                            "File does not contain an array of objects.");
                }

                parser.nextToken();
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());
            }
        }

        /**
         * Does this iterator have a next item?
         *
         * @return true if there's an extra item, otherwise false.
         */
        @Override
        public boolean hasNext() {
            JsonToken token = parser.getCurrentToken();
            return token != null && token == JsonToken.FIELD_NAME;
        }

        /**
         * Retrieve the next row in the file.
         *
         * @return The next row.
         */
        @Override
        public Map<String, Object> next() {
            if (hasNext()) {
                try {
                    parser.nextToken();
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
         * We cannot remove from a stream.
         */
        @Override
        public void remove() {
            // Do nothing- we can't remove from a stream.
        }
    }
}
