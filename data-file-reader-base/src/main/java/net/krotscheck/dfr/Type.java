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

/**
 * A list of value types that can be casting purpose.
 *
 * @author Michael Krotscheck
 */
public enum Type {

    /**
     * String Type.
     */
    STRING,

    /**
     * Integer Type.
     */
    INTEGER,

    /**
     * Float Type.
     */
    FLOAT,

    /**
     * Double Type.
     */
    DOUBLE,

    /**
     * Long Type.
     */
    LONG,

    /**
     * Boolean Type.
     */
    BOOLEAN;

    /**
     * Given a value, will return the associated type.
     *
     * @param value The value to resolve the type for.
     * @return The type.
     */
    public static Type getTypeForValue(final Object value) {
        if (value instanceof String) {
            return Type.STRING;
        } else if (value instanceof Boolean) {
            return Type.BOOLEAN;
        } else if (value instanceof Long) {
            return Type.LONG;
        } else if (value instanceof Integer) {
            return Type.INTEGER;
        } else if (value instanceof Double) {
            return Type.DOUBLE;
        } else if (value instanceof Float) {
            return Type.FLOAT;
        }

        // String will be our default.
        return Type.STRING;
    }

}
