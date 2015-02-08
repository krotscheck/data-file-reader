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

import net.krotscheck.dfr.stream.AbstractStreamDecoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A test stream decoder.
 *
 * @author Michael Krotscheck
 */
public final class TestStreamDecoder extends AbstractStreamDecoder {

    /**
     * The internal data to 'decode'.
     */
    private final List<Map<String, Object>> data;

    /**
     * Create a new test stream decoder.
     */
    public TestStreamDecoder() {
        data = new ArrayList<>();
    }

    /**
     * Create a new test stream decoder.
     *
     * @param testData The test data to wrap.
     */
    public TestStreamDecoder(final List<Map<String, Object>> testData) {
        data = testData;
    }

    /**
     * The test mimetye.
     *
     * @return A test mimetype.
     */
    @Override
    public String getMimeType() {
        return "stream/mock";
    }

    /**
     * Dispose. Do nothing.
     */
    @Override
    protected void dispose() {

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
}
