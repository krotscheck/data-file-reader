/*
 * Copyright (c) 2015 Michael Krotscheck
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

package net.krotscheck.dfr.stream;

import net.krotscheck.dfr.IDataDecoder;

import java.io.InputStream;

/**
 * This interface describes a data stream decoder, which adds additional methods
 * to managing data via InputStreams.
 *
 * @author Michael Krotscheck
 */
public interface IStreamDecoder
        extends IDataDecoder {

    /**
     * Retrieve the input stream from which the decoder is reading its data.
     *
     * @return The input stream.
     */
    InputStream getInputStream();

    /**
     * Set the input stream from which the decoder should read its data.
     *
     * @param stream The input stream.
     */
    void setInputStream(InputStream stream);

}
