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

import java.util.BitSet;
import java.util.Collection;

/**
 *
 * @author Syed Asad Rahman <asad@ebi.ac.uk> 2007-2011
 */
public interface IPatternFingerprinter extends Comparable<IPatternFingerprinter> {

    /**
     *
     * @return
     */
    public abstract double[] getValuesAsArray();

    /**
     *
     * @return Keys of the fingerprints
     */
    public abstract Collection<IFeature> getFeatures();

    /**
     *
     * @return values of the fingerprints
     */
    public abstract Collection<Double> getValues();

    /**
     *
     * @param index
     * @return
     * @throws Exception
     */
    public abstract IFeature getFeature(int index) throws Exception;

    /**
     *
     * @param pattern
     * @return
     */
    public abstract Double getWeight(String pattern);

    /**
     *
     * @param index
     * @return
     */
    public abstract Double getWeight(int index);

    /**
     * Size of the hashed fingerprint
     *
     * @return
     */
    public abstract int getFingerprintSize();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object feature);

    @Override
    public abstract String toString();

    /**
     *
     * @return
     */
    public abstract BitSet getHashedFingerPrint();

    /**
     *
     * @param fingerprint
     * @throws Exception
     */
    public abstract void add(BitSet fingerprint) throws Exception;

    /**
     *
     * @param fngp
     * @throws Exception
     */
    public abstract void add(IPatternFingerprinter fngp) throws Exception;

    /**
     * @return the fingerprintID
     */
    public abstract String getFingerprintID();

    /**
     * @param fingerprintID the fingerprintID to set
     */
    public abstract void setFingerprintID(String fingerprintID);

    /**
     * Number of unique features of this fingerprint
     *
     * @return
     */
    public abstract int getFeatureCount();

    public abstract boolean hasFeature(IFeature key);

    public abstract double[] getWeightedHashedFingerPrint();

    /**
     *
     * @param feature
     * @throws Exception
     */
    public abstract void add(IFeature feature) throws Exception;
}