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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import pattern.Feature;
import pattern.IFeature;
import pattern.IPatternFingerprinter;
import pattern.PatternFingerprinter;
import similarity.PatternSimilarity;

/**
 *
 * @author Syed Asad Rahman <asad@ebi.ac.uk> 2007-2012
 */
public class ReaderCSVFile {

    /**
     * Returns Pattern FP from the file
     *
     * @param file
     * @return
     */
    public static IPatternFingerprinter Read(File file) {
        TextReader textReader = null;
        IPatternFingerprinter q = new PatternFingerprinter();
        try {
            textReader = new TextReader(new BufferedReader(new FileReader(file)));
            int lineCounter = 0;
            while (!textReader.eof()) {
                String line = textReader.getln().trim();
                String[] tokenize = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                lineCounter += 1;
                if (tokenize.length != 2) {
                    System.err.println("Error: in parsing line " + lineCounter + " in File " + file.getName());
                }
                String pattern = tokenize[0];
                double weight = Double.parseDouble(tokenize[1]);
                IFeature feature = new Feature(pattern, weight);
                try {
                    q.add(feature);
                } catch (Exception ex) {
                    Logger.getLogger(PatternSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PatternSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (textReader != null) {
                textReader.close();
            }
        }
        return q;
    }
}
