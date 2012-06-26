/* $Revision$ $Author$ $Date$
 *
 * Copyright (C) 2012   Syed Asad Rahman <asad@ebi.ac.uk>
 *           
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package utility;

import java.util.BitSet;
import pattern.IPatternFingerprinter;

/**
 *
 * @author Asad
 */
public class Similarity {

    /**
     *
     * @param fp1
     * @param fp2
     * @param weighted mode
     * @return reaction rawScore between two reaction based on the reactant product structure
     * @throws java.lang.Exception
     */
    public static double getSimilarity(IPatternFingerprinter fp1, IPatternFingerprinter fp2, boolean weighted) throws Exception {
        double score = 0.0;
        int size1 = fp1.getFingerprintSize();
        int size2 = fp2.getFingerprintSize();


        if (size1 != size2) {
            throw new Exception("Features vectors must be of the same length");

        } else if (weighted) {
            double[] structFeatures1 = fp1.getWeightedHashedFingerPrint();
            double[] structFeatures2 = fp2.getWeightedHashedFingerPrint();
            score = getSimilarity(structFeatures1, structFeatures2);
        } else {
            BitSet structFeatures1 = fp1.getHashedFingerPrint();
            BitSet structFeatures2 = fp2.getHashedFingerPrint();
            score = getSimilarity(structFeatures1, structFeatures2);
        }
        return score;
    }

    /**
     *
     * @param rawFeatures1
     * @param rawFeatures2
     * @return
     * @throws CDKException
     */
    private static double getSimilarity(double[] rawFeatures1, double[] rawFeatures2) throws Exception {
        double similarity = 0.0;

        if (rawFeatures1.length != rawFeatures2.length) {
            throw new Exception("Features vectors must be of the same length");
        }

        int n = rawFeatures1.length;
        double ab = 0.0;
        double a2 = 0.0;
        double b2 = 0.0;

        for (int i = 0; i < n; i++) {
            ab += rawFeatures1[i] * rawFeatures2[i];
            a2 += rawFeatures1[i] * rawFeatures1[i];
            b2 += rawFeatures2[i] * rawFeatures2[i];
        }

        if (a2 > 0.0 && b2 > 0.0) {
            similarity = ab / (a2 + b2 - ab);
        }
        return similarity;
    }

    /**
     *
     * @param rawFeatures1
     * @param rawFeatures2
     * @return
     * @throws CDKException
     */
    private static double getSimilarity(BitSet bitset1, BitSet bitset2) throws Exception {
        double similarity;

        if (bitset1.size() != bitset2.size()) {
            throw new Exception("Features vectors must be of the same length");
        }

        float _bitset1_cardinality = bitset1.cardinality();
        float _bitset2_cardinality = bitset2.cardinality();

        BitSet one_and_two = (BitSet) bitset1.clone();
        one_and_two.and(bitset2);
        float _common_bit_count = one_and_two.cardinality();
        double _tanimoto_coefficient = _common_bit_count / (_bitset1_cardinality + _bitset2_cardinality - _common_bit_count);
        similarity = _tanimoto_coefficient;
        return similarity;
    }
}
