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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract implementation of the data encoder, to consolidate some common
 * functionality and provide a simple extension point.
 *
 * @author Michael Krotscheck
 */
public abstract class AbstractFilteredDataStream
        implements IFilteringDataStream {

    /**
     * List of filters that should be applied to each row during conversion.
     */
    private List<IDataFilter> filters;

    /**
     * Create a new instance.
     */
    public AbstractFilteredDataStream() {
        this.filters = new ArrayList<>();
    }

    /**
     * Add a filter to this converter.
     *
     * @param filter The filter to add.
     */
    public final void addFilter(final IDataFilter filter) {
        if (!containsFilter(filter)) {
            filters.add(filter);
        }
    }

    /**
     * Add many filters.
     *
     * @param newFilters The filters to add.
     */
    public final void addFilters(final List<IDataFilter> newFilters) {
        for (IDataFilter filter : newFilters) {
            addFilter(filter);
        }
    }

    /**
     * Retrieve all the filters.
     *
     * @return The filters.
     */
    public final List<IDataFilter> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    /**
     * Check to see if a filter is contained in this converter.
     *
     * @param filter The filter to check.
     * @return True if the filter is contained, otherwise false.
     */
    public final Boolean containsFilter(final IDataFilter filter) {
        if (filters == null) {
            filters = new ArrayList<>();
        }

        return this.filters.contains(filter);
    }

    /**
     * Remove a filter from the list.
     *
     * @param filter The filter to remove.
     */
    public final void removeFilter(final IDataFilter filter) {
        if (containsFilter(filter)) {
            this.filters.remove(filter);
        }
    }

    /**
     * Remove all filters.
     */
    public final void clearFilters() {
        this.filters.clear();
    }

    /**
     * Apply the filters.
     *
     * @param row The row to filter.
     * @return The filtered row.
     */
    public final Map<String, Object> applyFilters(
            final Map<String, Object> row) {
        Map<String, Object> filteringRow = new HashMap<>(row);

        for (IDataFilter filter : getFilters()) {
            filteringRow = filter.apply(filteringRow);
        }

        return filteringRow;
    }
}
