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

package net.krotscheck.dfr.text;

import net.krotscheck.dfr.IDataDecoder;

import java.io.Reader;

/**
 * This interface describes a data stream decoder, which adds additional methods
 * to managing data via Character stream readers.
 *
 * @author Michael Krotscheck
 */
public interface ITextDecoder
        extends IDataDecoder {

    /**
     * Retrieve the reader from which the decoder is reading its data.
     *
     * @return The Reader.
     */
    Reader getReader();

    /**
     * Set the reader from which the decoder should read its data.
     *
     * @param reader The Reader.
     */
    void setReader(Reader reader);

}
