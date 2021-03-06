
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
import java.util.Comparator;

/**
 *
 * @author Syed Asad Rahman <asad@ebi.ac.uk> 2007-2012
 */
public final class Feature implements IFeature,
        Comparable<IFeature>,
        Comparator<IFeature>,
        Serializable {

    private static final long serialVersionUID = 0xe6c5aecf276L;
    private final String pattern;
    private double weight;

    /**
     *
     * @param feature
     * @param weight
     */
    public Feature(String feature, double weight) {
        this.pattern = feature;
        this.weight = weight;
    }

    /**
     *
     * @param feature
     */
    public Feature(String feature) {
        this(feature, 1.0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Feature other = (Feature) obj;
        return !((this.pattern == null) ? (other.pattern != null) : !this.pattern.equals(other.pattern));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.pattern != null ? this.pattern.hashCode() : 0);
        return hash;
    }
    
    /**
     * Return weighted fingerprint
     *
     * @return
     */
    @Override
    public synchronized String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);
        result.append(getPattern());
        return result.toString();
    }

    /**
     * @return the pattern
     */
    @Override
    public String getPattern() {
        return pattern;
    }

    /**
     * @return the weight
     */
    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(IFeature feature) {
        return this.pattern.compareTo(feature.getPattern());
    }

    @Override
    public int compare(IFeature o1, IFeature o2) {
        return o1.getPattern().compareTo(o2.getPattern());
    }

    /**
     *
     * @param weight
     */
    @Override
    public void setValue(double weight) {
        this.weight = weight;
    }
}
