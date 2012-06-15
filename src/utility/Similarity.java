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
     * @return reaction rawScore between two reaction based on the reactant product structure
     * @throws java.lang.Exception
     */
    public static double getSimilarity(IPatternFingerprinter fp1, IPatternFingerprinter fp2) throws Exception {
        double score = 0.0;
        int size1 = fp1.getFingerprintSize();
        int size2 = fp2.getFingerprintSize();


        if (size1 != size2) {
            throw new Exception("Features vectors must be of the same length");

        } else {

            double[] structFeatures1 = fp1.getWeightedHashedFingerPrint();
            double[] structFeatures2 = fp2.getWeightedHashedFingerPrint();
            score = getSimilarity(structFeatures1, structFeatures2);
        }
        return score;
    }

    /**
     *
     * @param bondFeatures1
     * @param bondFeatures2
     * @return
     * @throws CDKException
     */
    private static double getSimilarity(double[] bondFeatures1, double[] bondFeatures2) throws Exception {
        double similarity = 0.0;

        if (bondFeatures1.length != bondFeatures2.length) {
            throw new Exception("Features vectors must be of the same length");
        }

        int n = bondFeatures1.length;
        double ab = 0.0;
        double a2 = 0.0;
        double b2 = 0.0;

        for (int i = 0; i < n; i++) {
            ab += bondFeatures1[i] * bondFeatures2[i];
            a2 += bondFeatures1[i] * bondFeatures1[i];
            b2 += bondFeatures2[i] * bondFeatures2[i];
        }

        if (a2 > 0.0 && b2 > 0.0) {
            similarity = ab / (a2 + b2 - ab);
        }
        return similarity;
    }
}
