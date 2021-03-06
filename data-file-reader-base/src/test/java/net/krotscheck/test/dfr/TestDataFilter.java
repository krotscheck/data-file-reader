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

import net.krotscheck.dfr.IDataFilter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A test data filter.
 *
 * @author Michael Krotscheck
 */
public class TestDataFilter implements IDataFilter {

    /**
     * Noop.
     *
     * @param row The data row to apply this filter to.
     * @return A clone of the previous row.
     */
    @Override
    public final Map<String, Object> apply(final Map<String, Object> row) {
        return new LinkedHashMap<>(row);
    }
}
