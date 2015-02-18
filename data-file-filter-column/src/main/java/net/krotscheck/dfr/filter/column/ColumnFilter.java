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

package net.krotscheck.dfr.filter.column;

import net.krotscheck.dfr.IDataFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The column filter is there if you want to pick and choose which columns
 * should be included in your data stream. Provided a string[] list of column
 * headers, only the data in these columns will be included in the output from
 * the filter.
 *
 * @author Michael Krotscheck
 */
public final class ColumnFilter implements IDataFilter {

    /**
     * The columns that we're filtering.
     */
    private final Set<String> columns;

    /**
     * Retrieve the set of columns in this filter.
     *
     * @return The current set of columns.
     */
    public Set<String> getColumns() {
        return Collections.unmodifiableSet(columns);
    }

    /**
     * Create a new instance of the column filter.
     *
     * @param columnNames The list of column headers which should pass this
     *                    filter.
     */
    public ColumnFilter(final String[] columnNames) {
        if (columnNames == null) {
            columns = new LinkedHashSet<>();
        } else {
            columns = new LinkedHashSet<>(Arrays.asList(columnNames));
        }
    }

    /**
     * Create a new instance of the column filter.
     *
     * @param columnNames The list of column headers which should pass this
     *                    filter.
     */
    public ColumnFilter(final List<String> columnNames) {
        if (columnNames == null) {
            columns = new LinkedHashSet<>();
        } else {
            columns = new LinkedHashSet<>(columnNames);
        }
    }

    /**
     * Create a new instance of the column filter.
     *
     * @param columnNames The set of column headers which should pass this
     *                    filter.
     */
    public ColumnFilter(final Set<String> columnNames) {
        if (columnNames == null) {
            columns = new LinkedHashSet<>();
        } else {
            columns = new LinkedHashSet<>(columnNames);
        }
    }

    /**
     * Apply the filter.
     *
     * @param row The data row to apply this filter to.
     * @return A row that only contains the configured columns.
     */
    @Override
    public Map<String, Object> apply(final Map<String, Object> row) {
        Map<String, Object> cleanRow, filteredRow;

        // Make sure the row isn't null.
        if (row == null) {
            cleanRow = new LinkedHashMap<>();
        } else {
            cleanRow = new LinkedHashMap<>(row);
        }

        // Copy the row so we avoid concurrent modifications.
        filteredRow = new LinkedHashMap<>();

        // Iterate through the requested columns and add/override the ones
        // the user wants to see.
        for (String column : columns) {
            if (cleanRow.containsKey(column)) {
                filteredRow.put(column, cleanRow.get(column));
            } else {
                filteredRow.put(column, null);
            }
        }

        return filteredRow;
    }
}
