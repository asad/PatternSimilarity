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
package pattern;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asad
 */
class IPatternComparators {

    public static Comparator<IPatternFingerprinter> overallComparator() {
        return new Comparator<IPatternFingerprinter>() {

            @Override
            public int compare(IPatternFingerprinter o1, IPatternFingerprinter o2) {
                int len1 = o1.getFeatureCount();
                int len2 = o2.getFeatureCount();
                if (!o1.getFingerprintID().equals(o2.getFingerprintID())) {
                    return o1.getFingerprintID().compareTo(o2.getFingerprintID());
                }
                int n = Math.min(len1, len2);
                if (len1 == len2) {
                    int pos = 0;
                    while (n-- != 0) {
                        try {
                            if (!o1.getFeature(pos).equals(o2.getFeature(pos))) {
                                return o1.getFeature(pos).compareTo(o2.getFeature(pos));
                            } else if (!o1.getFeature(pos).equals(o2.getFeature(pos))) {
                                double v1 = o1.getWeight(pos).doubleValue();
                                double v2 = o2.getWeight(pos).doubleValue();
                                if (v1 != v2) {
                                    return (int) (Math.max(v1, v2) - Math.min(v1, v2));
                                }
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(PatternFingerprinter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pos++;
                    }
                }
                return Math.max(len1, len2) - n;
            }
        };
    }

    public static Comparator<IPatternFingerprinter> dataComparator() {
        return new Comparator<IPatternFingerprinter>() {

            @Override
            public synchronized int compare(IPatternFingerprinter o1, IPatternFingerprinter o2) {
                int len1 = o1.getFeatureCount();
                int len2 = o2.getFeatureCount();
                if (!o1.getFingerprintID().equals(o2.getFingerprintID())) {
                    return o1.getFingerprintID().compareTo(o2.getFingerprintID());
                }
                int n = Math.min(len1, len2);
                if (len1 == len2) {
                    int pos = 0;
                    while (n-- != 0) {
                        double v1 = o1.getWeight(pos).doubleValue();
                        double v2 = o2.getWeight(pos).doubleValue();
                        if (v1 != v2) {
                            return (int) (Math.max(v1, v2) - Math.min(v1, v2));
                        }
                        pos++;
                    }
                }
                return Math.max(len1, len2) - n;
            }
        };
    }
}
