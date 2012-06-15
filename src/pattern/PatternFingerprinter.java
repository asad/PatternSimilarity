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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Syed Asad Rahman <asad@ebi.ac.uk> 2007-2012
 */
public class PatternFingerprinter implements IPatternFingerprinter,
        Comparable<IPatternFingerprinter>,
        Comparator<IPatternFingerprinter>,
        Serializable {

    private static final long serialVersionUID = 0156306561546552043757L;
    private final Set<IFeature> featureSet;
    private String fingerprintID = "?";
    private int fingerprintSize;

    public PatternFingerprinter() {
        this(1024);
    }

    /**
     *
     * @param features
     */
    public PatternFingerprinter(Collection<IFeature> features) {
        this(features, 1024);
    }

    /**
     *
     * @param fingerprintSize
     */
    public PatternFingerprinter(int fingerprintSize) {
        this.fingerprintSize = fingerprintSize;
        featureSet = Collections.synchronizedSortedSet(new TreeSet<IFeature>());
    }

    /**
     *
     * @param features
     * @param fingerprintSize
     */
    public PatternFingerprinter(Collection<IFeature> features, int fingerprintSize) {
        this(fingerprintSize);
        for (final IFeature feature : features) {
            if (!this.featureSet.contains(feature)) {
                this.featureSet.add(new Feature(feature.getPattern()));
            } else {
                for (Iterator<IFeature> it = featureSet.iterator(); it.hasNext();) {
                    IFeature localFeature = it.next();
                    if (localFeature.getPattern().equals(feature.getPattern())) {
                        double newWeight = localFeature.getWeight() + feature.getWeight();
                        localFeature.setValue(newWeight);
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     * @param fingerprint
     * @throws Exception
     */
    @Override
    public synchronized void add(BitSet fingerprint) throws Exception {
        if (featureSet == null) {
            throw new Exception("Cannot perform PatternFingerprint.add() as Fingerprint not initialized");
        }
        for (int i = 0; i < fingerprint.size(); i++) {
            if (fingerprint.get(i)) {
                add(new Feature(String.valueOf(i), 1.0));
            }
        }
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public synchronized void add(IFeature feature) throws Exception {
        if (featureSet == null) {
            throw new Exception("Cannot perform PatternFingerprint.add() as Fingerprint not initialized");
        }

        if (!this.featureSet.contains(feature)) {
            this.featureSet.add(new Feature(feature.getPattern(), feature.getWeight()));
        } else {
            for (Iterator<IFeature> it = featureSet.iterator(); it.hasNext();) {
                IFeature localFeature = it.next();
                if (localFeature.getPattern().equals(feature.getPattern())) {
                    double newWeight = localFeature.getWeight() + feature.getWeight();
                    localFeature.setValue(newWeight);
                    break;
                }
            }
        }
    }

    /**
     *
     * @param fngp
     * @throws Exception
     */
    @Override
    public synchronized void add(IPatternFingerprinter fngp) throws Exception {
        if (featureSet == null || fngp == null) {
            throw new Exception("Cannot perform PatternFingerprint.add() as Fingerprint not initialized");
        }
        if (fngp.getFingerprintSize() != this.fingerprintSize) {
            throw new Exception("Cannot perform PatternFingerprint.add() as Fingerprint size not equal");
        }
        for (Iterator<IFeature> it1 = fngp.getFeatures().iterator(); it1.hasNext();) {
            IFeature feature = it1.next();
            if (!this.featureSet.contains(feature)) {
                this.featureSet.add(new Feature(feature.getPattern(), feature.getWeight()));
            } else {
                for (Iterator<IFeature> it2 = featureSet.iterator(); it2.hasNext();) {
                    IFeature localFeature = it2.next();
                    if (localFeature.getPattern().equals(feature.getPattern())) {
                        double newWeight = localFeature.getWeight() + feature.getWeight();
                        localFeature.setValue(newWeight);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public double[] getValuesAsArray() {
        int pos = 0;
        double[] res = new double[featureSet.size()];
        for (Iterator<IFeature> it = featureSet.iterator(); it.hasNext();) {
            IFeature feature = it.next();
            res[pos] = feature.getWeight();
            pos += 1;
        }
        return res;
    }

    @Override
    public Collection<IFeature> getFeatures() {
        return Collections.unmodifiableCollection(featureSet);
    }

    @Override
    public Collection<Double> getValues() {
        List<Double> collection = new ArrayList<Double>(featureSet.size());
        int i = 0;
        for (Iterator<IFeature> it = featureSet.iterator(); it.hasNext();) {
            IFeature feature = it.next();
            collection.add(i, feature.getWeight());
            i += 1;
        }
        return collection;
    }

    @Override
    public int getFeatureCount() {
        return featureSet.size();
    }

    @Override
    public BitSet getHashedFingerPrint() {
        double[] weightedHashedFingerPrint = getWeightedHashedFingerPrint();
        BitSet binary = new BitSet(this.fingerprintSize);
        for (int i = 0; i < weightedHashedFingerPrint.length; i++) {
            if (weightedHashedFingerPrint[i] > 0.) {
                binary.set(i, true);
            } else {
                binary.set(i, false);
            }
        }
        return binary;
    }

    @Override
    public double[] getWeightedHashedFingerPrint() {
        double[] hashedFingerPrint = new double[this.fingerprintSize];
        for (int i = 0; i < hashedFingerPrint.length; i++) {
            hashedFingerPrint[i] = 0.;
        }
        Collection<IFeature> features = this.getFeatures();
        for (final IFeature feature : features) {
            long hashCode = feature.hashCode();
            int randomNumber = (int) RandomNumber.generateMersenneTwisterRandomNumber(this.fingerprintSize, hashCode);
            hashedFingerPrint[randomNumber] += feature.getWeight();
        }
        return hashedFingerPrint;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        DecimalFormat df = new DecimalFormat();
        result.append("ID=").append(this.fingerprintID);
        result.append(" (").append(this.featureSet.size()).append(") ");
        List<IFeature> list = new ArrayList<IFeature>(this.featureSet);
        for (Iterator<IFeature> it = list.iterator(); it.hasNext();) {
            IFeature feature = it.next();
            result.append(" ").append(feature.getPattern()).
                    append(":").
                    append(df.format(feature.getWeight())).append("; ");
        }
        result.append(NEW_LINE);
        return result.toString();
    }

    @Override
    public IFeature getFeature(int index) throws Exception {
        if (featureSet.size() >= index) {
            int i = 0;
            for (final IFeature key : featureSet) {
                if (i == index) {
                    return key;
                }
                i++;
            }
        }
        return null;
    }

    @Override
    public Double getWeight(String pattern) {
        if (!featureSet.isEmpty()) {
            int i = 0;
            for (final IFeature key : featureSet) {
                if (key.getPattern().equals(pattern)) {
                    return key.getWeight();
                }
                i++;
            }
        }
        return new Double(-1.0);
    }

    @Override
    public Double getWeight(int index) {
        if (featureSet.size() >= index) {
            int i = 0;
            for (final IFeature value : featureSet) {
                if (i == index) {
                    return value.getWeight();
                }
                i++;
            }
        }
        return new Double(-1.0);
    }

    /**
     * @return the fingerprintID
     */
    @Override
    public String getFingerprintID() {
        return fingerprintID;
    }

    /**
     * @param fingerprintID the fingerprintID to set
     */
    @Override
    public void setFingerprintID(String fingerprintID) {
        this.fingerprintID = fingerprintID;
    }

    /**
     * Returns 0 if two fingerprints are equal and if they share same labels it
     * returns difference in their weight
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public synchronized int compare(IPatternFingerprinter o1, IPatternFingerprinter o2) {
        Comparator<IPatternFingerprinter> comparator = new IPatternComparators(this).overallComparator();
        return comparator.compare(o1, o2);
    }

    /**
     * Returns 0 if two fingerprints are equal and if they share same labels it
     * returns difference in their weight
     *
     * @param t
     * @return
     */
    @Override
    public synchronized int compareTo(IPatternFingerprinter t) {
        return compare(this, t);
    }

    /**
     * Return true if two Fingerprints are equal
     *
     * @param object
     * @return
     */
    @Override
    public synchronized boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IPatternFingerprinter)) {
            return false;
        }
        IPatternFingerprinter obj = (IPatternFingerprinter) object;

        if (!this.fingerprintID.equals(obj.getFingerprintID())) {
            return false;
        }
        int len1 = obj.getFeatureCount();
        int len2 = this.getFeatureCount();
        int n = Math.min(len1, len2);
        if (len1 == len2) {
            int pos = 0;
            while (n-- != 0) {
                try {
                    if (!this.getFeature(pos).equals(obj.getFeature(pos))) {
                        return false;
                    } else if (this.getFeature(pos).equals(obj.getFeature(pos))) {
                        double v1 = this.getWeight(pos).doubleValue();
                        double v2 = obj.getWeight(pos).doubleValue();
                        if (v1 != v2) {
                            return false;
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(PatternFingerprinter.class.getName()).log(Level.SEVERE, null, ex);
                }
                pos++;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.featureSet != null ? this.featureSet.hashCode() : 0);
        hash = 31 * hash + (this.fingerprintID != null ? this.fingerprintID.hashCode() : 0);
        hash = 31 * hash + this.fingerprintSize;
        return hash;
    }

    @Override
    public int getFingerprintSize() {
        return fingerprintSize;
    }

    @Override
    public boolean hasFeature(IFeature key) {
        return this.featureSet.contains(key);
    }
}
