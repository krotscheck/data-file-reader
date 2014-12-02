/*
 * Copyright (c) 2011-2013 Krotscheck.net, All Rights Reserved.
 */

package net.krotscheck.dfr.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.krotscheck.dfr.AbstractDataEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * JSON Data Encoder, allows us to write tuples to a data file in JSON format.
 *
 * @author Michael Krotscheck
 */
public final class JSONDataEncoder extends AbstractDataEncoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(JSONDataEncoder.class);

    /**
     * The JSON generation engine.
     */
    private JsonGenerator generator;

    /**
     * Return the decoding mimetype which this encoder supports.
     *
     * @return "application/json"
     */
    @Override
    public String getMimeType() {
        return "application/json";
    }

    /**
     * Write a row to the file.
     *
     * @param row A row of data.
     * @throws java.io.IOException Thrown if there's a problem writing to the
     *                             destination.
     */
    @Override
    protected void writeToStream(final Map<String, Object> row)
            throws IOException {
        if (generator == null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = new JsonFactory(mapper);
            generator = factory.createGenerator(getOutputStream());
            generator.writeStartArray(); // [
        }

        // Convert the row into a map
        generator.writeObject(row);
    }

    /**
     * Closes the output stream.
     */
    @Override
    public void dispose() {
        try {
            if (generator != null) {
                generator.writeEndArray();
                generator.close();
            }
            this.getOutputStream().close();
        } catch (IOException ioe) {
            logger.error("Unable to close stream", ioe);
            logger.trace(ioe.getMessage(), ioe);
        } finally {
            generator = null;
            this.setOutputStream(null);
        }
    }
}
