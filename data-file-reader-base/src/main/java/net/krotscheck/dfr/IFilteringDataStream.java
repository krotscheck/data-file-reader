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

import java.util.List;
import java.util.Map;

/**
 * This interface describes a data stream (whether a converter, encoder or
 * decoder) that is capable of filtering its throughput.
 *
 * @author Michael Krotscheck
 */
public interface IFilteringDataStream {

    /**
     * Add a filter to this converter.
     *
     * @param filter The filter to add.
     */
    void addFilter(final IDataFilter filter);

    /**
     * Add a list of filters to this converter.
     *
     * @param filters The filters to add.
     */
    void addFilters(List<IDataFilter> filters);

    /**
     * Check to see if a filter is contained in this converter.
     *
     * @param filter The filter to check.
     * @return True if the filter is loaded, otherwise false.
     */
    Boolean containsFilter(final IDataFilter filter);

    /**
     * Return all the filters.
     *
     * @return All the filters.
     */
    List<IDataFilter> getFilters();

    /**
     * Remove a filter from the list.
     *
     * @param filter The filter to remove.
     */
    void removeFilter(final IDataFilter filter);

    /**
     * Remove all filters.
     */
    void clearFilters();

    /**
     * Apply the filters to the given row.
     *
     * @param row The row to filter.
     * @return A row that has all filters applied to it.
     */
    Map<String, Object> applyFilters(final Map<String, Object> row);
}
