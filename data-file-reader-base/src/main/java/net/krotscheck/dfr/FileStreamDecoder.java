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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * This class provides a convenience class by which files may be quickly
 * decoded. It uses the Java ServiceLoader to discover all appropriate
 * implementations of IDataDecoder, and creates one based on your passed
 * mimetype.
 *
 * @author Michael Krotscheck
 */
public final class FileStreamDecoder implements Iterable<Map<String,
        Object>>, Closeable {

    /**
     * Our service discovery loader.
     */
    private static ServiceLoader<IDataDecoder> loader;

    /**
     * Use service discovery to find our data stream decoder.
     *
     * @param mimeType The mimetype to scan for.
     * @return An instance of the decoder.
     * @throws ClassNotFoundException Thrown when no encoder for a mimetype is
     *                                found.
     */
    private static IDataDecoder getDecoder(final String mimeType)
            throws ClassNotFoundException {
        if (loader == null) {
            logger.info("IDataDecoders found:");
            loader = ServiceLoader.load(IDataDecoder.class);

            // Logging.
            for (IDataDecoder discoveredDecoder : loader) {
                String name = discoveredDecoder.getClass().getCanonicalName();
                String encoderMimeType = discoveredDecoder.getMimeType();
                logger.info(String.format("    %s -> %s", encoderMimeType,
                        name));
            }
        }

        for (IDataDecoder decoder : loader) {
            if (decoder.getMimeType().equals(mimeType)) {
                return decoder;
            }
        }

        throw new ClassNotFoundException(
                String.format("IDataDecoder for"
                        + " mimeType [%s] not found.", mimeType));
    }

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(FileStreamDecoder.class);

    /**
     * Our input stream reader.
     */
    private IDataDecoder inputDecoder = null;

    /**
     * Creates a new instance of the FileStreamDecoder.
     *
     * @param source   The source input stream.
     * @param mimeType The mimetype.
     * @throws ClassNotFoundException Thrown when a decoder is not found.
     */
    public FileStreamDecoder(final InputStream source, final String mimeType)
            throws ClassNotFoundException {
        inputDecoder = getDecoder(mimeType);
        inputDecoder.setInputStream(source);
    }

    /**
     * Returns the iterator from the underlying data stream.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<Map<String, Object>> iterator() {
        return inputDecoder.iterator();
    }

    /**
     * Closes the file input stream.
     *
     * @throws java.io.IOException IO Exception thrown by the wrapped encoder.
     */
    public void close() throws IOException {
        inputDecoder.close();
    }
}
