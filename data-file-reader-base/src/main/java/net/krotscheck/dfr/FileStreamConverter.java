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
import java.util.Map;

/**
 * This class simplifies converting a data file from one type to another. To
 * use, provide a decoder and an encoder, and call <code>run()</code>. This
 * class implements <code>java.lang.Runnable</code>, for use with threads.
 *
 * @author Michael Krotscheck
 */
public final class FileStreamConverter implements Runnable {

    /**
     * Logger instance.
     */
    private static Logger logger =
            LoggerFactory.getLogger(FileStreamConverter.class);

    /**
     * The data encoder.
     */
    private IDataEncoder dataEncoder;

    /**
     * The data decoder.
     */
    private IDataDecoder dataDecoder;

    /**
     * Create a new instance of the filestream converter. Accepts an encoder and
     * a decoder.
     *
     * @param decoder The data decoder.
     * @param encoder The data encoder.
     */
    public FileStreamConverter(final IDataDecoder decoder,
                               final IDataEncoder encoder) {
        if (decoder == null) {
            throw new RuntimeException("Data decoder is null");
        }
        if (encoder == null) {
            throw new RuntimeException("Data encoder is null");
        }

        this.dataEncoder = encoder;
        this.dataDecoder = decoder;
    }

    /**
     * Run the converter.
     */
    @Override
    public void run() {
        try {
            for (Map<String, Object> row : dataDecoder) {
                dataEncoder.write(row);
            }

            dataDecoder.close();
            dataEncoder.close();
        } catch (IOException ioe) {
            logger.error("Unable to complete conversion.", ioe);
        }

    }
}
