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

package net.krotscheck.test.dfr;

import net.krotscheck.dfr.AbstractDataDecoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A test data decoder.
 *
 * @author Michael Krotscheck
 */
public final class TestDataDecoder extends AbstractDataDecoder {

    /**
     * The internal data to 'decode'.
     */
    private final List<Map<String, Object>> data;

    /**
     * Create a new test data decoder.
     */
    public TestDataDecoder() {
        data = new ArrayList<>();
    }

    /**
     * Create a new test data decoder.
     *
     * @param testData The test data to wrap.
     */
    public TestDataDecoder(final List<Map<String, Object>> testData) {
        data = testData;
    }

    /**
     * The test mimetye.
     *
     * @return A test mimetype.
     */
    @Override
    public String getMimeType() {
        return "raw/mock";
    }

    /**
     * Do nothing.
     *
     * @return Nothing
     */
    @Override
    @SuppressWarnings("unchecked")
    protected Iterator<Map<String, Object>> buildIterator() {
        if (data != null) {
            return data.iterator();
        } else {
            return null;
        }
    }

    /**
     * Close. Do nothing.
     */
    @Override
    public void close() {

    }
}
