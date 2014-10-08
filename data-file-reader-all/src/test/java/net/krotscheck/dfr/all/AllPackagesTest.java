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

package net.krotscheck.dfr.all;

import net.krotscheck.dfr.IDataDecoder;
import net.krotscheck.dfr.IDataEncoder;
import net.krotscheck.dfr.bson.BSONDataDecoder;
import net.krotscheck.dfr.bson.BSONDataEncoder;
import net.krotscheck.dfr.csv.CSVDataDecoder;
import net.krotscheck.dfr.csv.CSVDataEncoder;
import net.krotscheck.dfr.json.JSONDataDecoder;
import net.krotscheck.dfr.json.JSONDataEncoder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ServiceLoader;

/**
 * Test that all packages are registered.
 *
 * @author Michael Krotscheck
 */
public final class AllPackagesTest {


    /**
     * Make sure BSONDecoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testBSONDecoderDiscovery() {
        ServiceLoader<IDataDecoder> loader
                = ServiceLoader.load(IDataDecoder.class);

        for (IDataDecoder decoder : loader) {
            if (decoder instanceof BSONDataDecoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }

    /**
     * Make sure JSONDecoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testJSONDecoderDiscovery() {
        ServiceLoader<IDataDecoder> loader
                = ServiceLoader.load(IDataDecoder.class);

        for (IDataDecoder decoder : loader) {
            if (decoder instanceof JSONDataDecoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }

    /**
     * Make sure CSVDecoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testCSVDecoderDiscovery() {
        ServiceLoader<IDataDecoder> loader
                = ServiceLoader.load(IDataDecoder.class);

        for (IDataDecoder decoder : loader) {
            if (decoder instanceof CSVDataDecoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }


    /**
     * Make sure BSONEncoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testBSONEncoderDiscovery() {
        ServiceLoader<IDataEncoder> loader
                = ServiceLoader.load(IDataEncoder.class);

        for (IDataEncoder encoder : loader) {
            if (encoder instanceof BSONDataEncoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }

    /**
     * Make sure JSONEncoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testJSONEncoderDiscovery() {
        ServiceLoader<IDataEncoder> loader
                = ServiceLoader.load(IDataEncoder.class);

        for (IDataEncoder encoder : loader) {
            if (encoder instanceof JSONDataEncoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }

    /**
     * Make sure CSVEncoder is autodiscoverable by the java ServiceLoader.
     */
    @Test
    public void testCSVEncoderDiscovery() {
        ServiceLoader<IDataEncoder> loader
                = ServiceLoader.load(IDataEncoder.class);

        for (IDataEncoder encoder : loader) {
            if (encoder instanceof CSVDataEncoder) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.fail();
    }
}
