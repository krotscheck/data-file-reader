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

import java.util.Map;

/**
 * This interface describes a data filter, allowing custom transformations on
 * data streams.
 *
 * @author Michael Krotscheck
 */
public interface IDataFilter {

    /**
     * Apply a filter to the specified row.
     *
     * @param row The data row to apply this filter to.
     * @return The mimetype, as a string.
     */
    Map<String, Object> apply(final Map<String, Object> row);

}
