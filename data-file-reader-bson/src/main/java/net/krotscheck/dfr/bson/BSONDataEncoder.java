/*
 * Copyright (c) 2011-2013 Krotscheck.net, All Rights Reserved.
 */

package net.krotscheck.dfr.bson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.krotscheck.dfr.stream.AbstractStreamEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonGenerator;

/**
 * BSON Data Encoder, allows us to write tuples to a data file in BSON format.
 *
 * @author Michael Krotscheck
 */
public final class BSONDataEncoder extends AbstractStreamEncoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(BSONDataEncoder.class);

    /**
     * JSON Generator, which writes our BSON for us.
     */
    private JsonGenerator generator;

    /**
     * Return the decoding mimetype which this encoder supports.
     *
     * @return "application/bson"
     */
    @Override
    public String getMimeType() {
        return "application/bson";
    }

    /**
     * Write a row to the file.
     *
     * @param row A row of data.
     * @throws java.io.IOException Thrown when there are problems writing to the
     *                             destination.
     */
    @Override
    protected void writeToOutput(final Map<String, Object> row)
            throws IOException {
        if (generator == null) {
            ObjectMapper mapper = new ObjectMapper();
            BsonFactory factory = new BsonFactory(mapper);
            factory.enable(BsonGenerator.Feature.ENABLE_STREAMING);
            generator = factory.createJsonGenerator(getOutputStream());
            generator.writeStartArray(); // [
        }

        // Convert the tuple into a map
        generator.writeObject(row);
    }

    /**
     * Closes the output stream.
     */
    @Override
    protected void dispose() {
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
