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

package net.krotscheck.test.dfr;

import net.krotscheck.dfr.stream.AbstractStreamEncoder;

import java.util.Map;

/**
 * A test stream encoder.
 *
 * @author Michael Krotscheck
 */
public final class TestStreamEncoder extends AbstractStreamEncoder {

    /**
     * Mock mimetype.
     *
     * @return "text/mock"
     */
    @Override
    public String getMimeType() {
        return "stream/mock";
    }

    /**
     * Write a new row to the aether.
     *
     * @param row A row of data.
     */
    @Override
    protected void writeToOutput(final Map<String, Object> row) {

    }

    /**
     * Dispose of this encoder.
     */
    @Override
    protected void dispose() {

    }
}
