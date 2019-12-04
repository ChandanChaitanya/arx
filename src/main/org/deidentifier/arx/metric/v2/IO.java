/*
 * ARX: Powerful Data Anonymization
 * Copyright 2012 - 2018 Fabian Prasser and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.deidentifier.arx.metric.v2;

import com.carrotsearch.hppc.LongDoubleHashMap;
import com.carrotsearch.hppc.LongObjectHashMap;
import org.apache.commons.math3.fraction.BigFraction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigInteger;
import org.apache.commons.math3.fraction.BigFraction;
/**
 * This class implements serialization for maps
 *
 * @author Fabian Prasser
 */
public class IO {

    /**
     * Reads a LongDoubleHashMap from the stream.
     *
     * @param stream
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static LongDoubleHashMap readLongDoubleHashMap(ObjectInputStream stream) throws ClassNotFoundException, IOException {

        LongDoubleHashMap result = new LongDoubleHashMap();
        // Read
        String mapAsString = (String) stream.readObject();
        if (mapAsString != null) {
            Map<String, String> map = Arrays.stream(mapAsString.substring(1, mapAsString.length() - 1).split(","))
                    .map(entry -> entry.split("=>"))
                    .collect(Collectors.toMap(entry -> ((String)entry[0]).trim(), entry -> ((String)entry[1]).trim()));

            // Set
            for (Map.Entry<String, String> entry : map.entrySet()) {
                result.put(Long.parseLong(entry.getKey()), Double.parseDouble(entry.getValue()));
            }
        }

        // Return
        return result;
    }

    /**
     * Reads a LongObjectHashMap of the type BigFraction from the stream.
     *
     * @param stream
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static LongObjectHashMap<BigFraction> readLongBigFractionOpenHashMap(ObjectInputStream stream) throws ClassNotFoundException, IOException {

        LongObjectHashMap<BigFraction> result = new LongObjectHashMap<BigFraction>();
        // Read
        String mapAsString = (String) stream.readObject();
        if (mapAsString != null) {
            Map<String, String> map = Arrays.stream(mapAsString.substring(1, mapAsString.length() - 1).split(","))
                    .map(entry -> entry.split("=>"))
                    .collect(Collectors.toMap(entry -> ((String)entry[0]).trim(), entry ->((String)entry[1]).trim()));

            // Set
            for (Map.Entry<String, String> entry : map.entrySet()) {
                result.put(Long.parseLong(entry.getKey()), new BigFraction(new BigInteger(entry.getValue())));
            }
        }
        // Return
        return result;
    }

    /**
     * Writes a LongDoubleHashMap to the stream.
     *
     * @param stream
     * @param hashmap
     * @throws IOException
     */
    public static void writeLongDoubleHashMap(ObjectOutputStream stream, LongDoubleHashMap hashmap) throws IOException {
        // Write
        stream.writeObject(hashmap.toString());
    }

    /**
     * Writes a LongObjectHashMap of the type BigFraction to the stream.
     *
     * @param stream
     * @param hashmap
     * @throws IOException
     */
    public static void writeLongBigFractionOpenHashMap(ObjectOutputStream stream, LongObjectHashMap<BigFraction> hashmap) throws IOException {
        // Write
        stream.writeObject(hashmap.toString());
    }
}
