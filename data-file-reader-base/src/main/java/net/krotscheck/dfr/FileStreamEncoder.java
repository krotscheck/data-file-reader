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
import java.io.OutputStream;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * This class provides a convenience class by which files may be quickly
 * encoded. It uses the Java ServiceLoader to discover all appropriate
 * implementations of IDataEncoder, and creates one based on your passed
 * mimetype.
 *
 * @author Michael Krotscheck
 */
public final class FileStreamEncoder {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(FileStreamEncoder.class);

    /**
     * Our service discovery loader.
     */
    private static ServiceLoader<IDataEncoder> loader;

    /**
     * The output encoder used for this instance.
     */
    private final IDataEncoder outputEncoder;

    /**
     * Creates a new instance of the FileStreamEncoder.
     *
     * @param destination The destination output stream.
     * @param mimeType    The explicit MimeType to use when encoding.
     * @throws java.lang.ClassNotFoundException If the encoder cannot be
     *                                          found.ø
     */
    public FileStreamEncoder(final OutputStream destination,
                             final String mimeType)
            throws ClassNotFoundException {
        outputEncoder = getEncoder(mimeType);
        outputEncoder.setOutputStream(destination);
    }

    /**
     * Write a row to the underlying encoder.
     *
     * @param row A row of data, mapped as column:row.
     * @throws java.io.IOException Thrown when there's a problem writing to the
     *                             destination.
     */
    public void write(final Map<String, Object> row) throws IOException {
        outputEncoder.write(row);
    }

    /**
     * Closes the encoder stream.
     *
     * @throws java.io.IOException Exception if the wrapped encoder cannot
     *                             close.
     */
    public void close() throws IOException {
        outputEncoder.close();
    }

    /**
     * Use service discovery to find our data stream encoder.
     *
     * @param mimeType The mimetype to scan for.
     * @return An instance of the encoder.
     * @throws ClassNotFoundException Thrown when no encoder for a mimetype is
     *                                found.
     */
    private IDataEncoder getEncoder(final String mimeType)
            throws ClassNotFoundException {
        if (loader == null) {
            logger.info("IDataEncoders found:");
            loader = ServiceLoader.load(IDataEncoder.class);
            // Debug logging.
            for (IDataEncoder discoveredEncoder : loader) {
                String name = discoveredEncoder.getClass().getCanonicalName();
                String encoderMimeType = discoveredEncoder.getMimeType();
                logger.info(String.format("    %s -> %s", encoderMimeType,
                        name));
            }
        }

        for (IDataEncoder encoder : loader) {
            if (encoder.getMimeType().equals(mimeType)) {
                return encoder;
            }
        }

        throw new ClassNotFoundException(
                String.format("IDataEncoder for"
                        + " mimeType [%s] not found.", mimeType));
    }
}
