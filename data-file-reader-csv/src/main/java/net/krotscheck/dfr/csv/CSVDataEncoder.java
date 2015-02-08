/*
 * Copyright (c) 2011-2013 Krotscheck.net, All Rights Reserved.
 */

package net.krotscheck.dfr.csv;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import net.krotscheck.dfr.text.AbstractTextEncoder;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * CSV Data Encoder, allows us to write tuples to a data file in CSV format.
 *
 * @author Michael Krotscheck
 */
public final class CSVDataEncoder extends AbstractTextEncoder {

    /**
     * The writer.
     */
    private ObjectWriter writer;

    /**
     * Return the decoding mimetype which this encoder supports.
     *
     * @return "text/csv"
     */
    @Override
    public String getMimeType() {
        return "text/csv";
    }

    /**
     * Write a row to the destination.
     *
     * @param row The row to write.
     * @throws java.io.IOException Exception thrown when there's problems
     *                             writing to the output.
     */
    @Override
    protected void writeToOutput(final Map<String, Object> row)
            throws IOException {
        if (writer == null) {
            CsvMapper mapper = new CsvMapper();
            mapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
            mapper.getFactory()
                    .configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,
                            false);
            CsvSchema schema = buildCsvSchema(row);
            writer = mapper.writer(schema);
            writer.writeValue(getWriter(), row.keySet());
        }
        writer.writeValue(getWriter(), row.values());
    }

    /**
     * Closes the output stream.
     */
    @Override
    public void dispose() {
        writer = null;
    }

    /**
     * Extrapolate the CSV columns from the row keys.
     *
     * @param row A row.
     * @return A constructed CSV schema.
     */
    public CsvSchema buildCsvSchema(final Map<String, Object> row) {
        CsvSchema.Builder builder = CsvSchema.builder();
        Set<String> fields = row.keySet();

        for (String field : fields) {
            builder.addColumn(field);
        }

        return builder.build();
    }
}
