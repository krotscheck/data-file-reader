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
 * Our runtime cache of discovered IDataDecoders.
 *
 * @author Michael Krotscheck
 */
public final class DecoderCache {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(DecoderCache.class);

    /**
     * The static cache of discovered decoders.
     */
    private static Map<String, Class<? extends IDataDecoder>> cache;

    /**
     * This is a utility class, therefore a private constructor.
     */
    private DecoderCache() {

    }

    /**
     * Populates the cache.
     */
    private static void populateCache() {
        if (cache == null) {
            cache = new HashMap<>();

            logger.info("IDataDecoders found:");

            ServiceLoader<IDataDecoder> loader =
                    ServiceLoader.load(IDataDecoder.class);

            // Logging.
            for (IDataDecoder discoveredDecoder : loader) {
                String name = discoveredDecoder.getClass().getCanonicalName();
                String decoderMimeType = discoveredDecoder.getMimeType();
                logger.info(String.format("    %s -> %s",
                        decoderMimeType, name));
                cache.put(decoderMimeType, discoveredDecoder.getClass());
            }
        }
    }

    /**
     * Return a set of all supported mimetypes.
     *
     * @return A set of the mimetypes supported by discovered IDataDecoders.
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
     * Retrieve a decoder for a specified mime type.
     *
     * @param mimeType The mimetype to scan for.
     * @return An instance of the decoder.
     * @throws ClassNotFoundException Thrown when no decoder for a mimetype is
     *                                found.
     * @throws IllegalAccessException Thrown when the decoder's constructor is
     *                                not accessible. Never thrown, as the
     *                                service loader would throw it first.
     * @throws InstantiationException Thrown when the decoder's constructor is
     *                                not accessible. Never thrown, as the
     *                                service loader would throw it first.
     */
    public static IDataDecoder getDecoder(final String mimeType)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {

        populateCache();

        if (!cache.containsKey(mimeType)) {
            throw new ClassNotFoundException(
                    String.format("IDataDecoder for"
                            + " mimeType [%s] not found.", mimeType));
        }

        Class<? extends IDataDecoder> decoderClass = cache.get(mimeType);

        return decoderClass.newInstance();
    }
}
