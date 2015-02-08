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

import net.krotscheck.dfr.IDataEncoder;

import java.io.Writer;

/**
 * This interface describes a data writer encoder, which adds additional methods
 * to managing data via Character Writers.
 *
 * @author Michael Krotscheck
 */
public interface ITextEncoder
        extends IDataEncoder {

    /**
     * Retrieve the output writer to which the encoder is writing its data.
     *
     * @return The writer.
     */
    Writer getWriter();

    /**
     * Set the output writer to which the encoder should write its data.
     *
     * @param stream The writer.
     */
    void setWriter(Writer stream);

}
