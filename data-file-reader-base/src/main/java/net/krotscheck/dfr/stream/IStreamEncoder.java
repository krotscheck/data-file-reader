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

import net.krotscheck.dfr.IDataEncoder;

import java.io.OutputStream;

/**
 * This interface describes a data stream encoder, which adds additional methods
 * to managing data via OutputStreams.
 *
 * @author Michael Krotscheck
 */
public interface IStreamEncoder extends IDataEncoder {

    /**
     * Retrieve the output stream to which the encoder is writing its data.
     *
     * @return The stream.
     */
    OutputStream getOutputStream();

    /**
     * Set the output stream to which the encoder should write its data.
     *
     * @param stream The stream.
     */
    void setOutputStream(OutputStream stream);

}
