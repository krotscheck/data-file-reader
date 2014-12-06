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

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Our runtime cache of discovered IDataEncoders.
 *
 * @author Michael Krotscheck
 */
public final class EncoderCache {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(EncoderCache.class);

    /**
     * The static cache of discovered encoders.
     */
    private static Map<String, Class<? extends IDataEncoder>> cache;

    /**
     * This is a utility class, therefore a private constructor.
     */
    private EncoderCache() {

    }

    /**
     * Populates the cache.
     */
    private static void populateCache() {
        if (cache == null) {
            cache = new HashMap<>();

            logger.info("IDataEncoders found:");

            ServiceLoader<IDataEncoder> loader =
                    ServiceLoader.load(IDataEncoder.class);

            // Logging.
            for (IDataEncoder discoveredDecoder : loader) {
                String name = discoveredDecoder.getClass().getCanonicalName();
                String encoderMimeType = discoveredDecoder.getMimeType();
                logger.info(String.format("    %s -> %s",
                        encoderMimeType, name));
                cache.put(encoderMimeType, discoveredDecoder.getClass());
            }
        }
    }

    /**
     * Return a set of all supported mimetypes.
     *
     * @return A set of the mimetypes supported by discovered IDataEncoders.
     */
    public static Set<String> supportedMimeTypes() {
        populateCache();

        return cache.keySet();
    }

    /**
     * Returns whether a given mimetype is supported.
     *
     * @param mimeType The mimetype to check.
     * @return True if the mimetype is supported, otherwise false.
     */
    public static Boolean isMimeTypeSupported(final String mimeType) {
        populateCache();

        return cache.containsKey(mimeType);
    }

    /**
     * Retrieve a encoder for a specified mime type.
     *
     * @param mimeType The mimetype to scan for.
     * @return An instance of the encoder.
     * @throws ClassNotFoundException Thrown when no encoder for a mimetype is
     *                                found.
     * @throws IllegalAccessException Thrown when the encoder's constructor is
     *                                not accessible. Never thrown, as the
     *                                service loader would throw it first.
     * @throws InstantiationException Thrown when the encoder's constructor is
     *                                not accessible. Never thrown, as the
     *                                service loader would throw it first.
     */
    public static IDataEncoder getEncoder(final String mimeType)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {

        populateCache();

        if (!cache.containsKey(mimeType)) {
            throw new ClassNotFoundException(
                    String.format("IDataEncoder for"
                            + " mimeType [%s] not found.", mimeType));
        }

        Class<? extends IDataEncoder> encoderClass = cache.get(mimeType);

        return encoderClass.newInstance();
    }
}
