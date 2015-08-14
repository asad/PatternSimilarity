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
package similarity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pattern.IFeature;
import pattern.IPatternFingerprinter;
import pattern.PatternFingerprinter;
import utility.ReaderCSVFile;
import utility.ReaderTABFile;
import utility.Similarity;

/**
 *
 * @author Syed Asad Rahman <asad@ebi.ac.uk> 2007-2012
 */
public class PatternSimilarity {

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 6) {
            help();
        }

        List<String> command = Arrays.asList(args);

        File fileQ = null;
        File fileT = null;
        File fileP = null;
        String inputType = "TAB";
        boolean weighted = false;

        if (command.contains("-q")) {
            int indexOf = command.indexOf("-q") + 1;
            if (command.size() > indexOf) {
                String file = command.get(indexOf);
                fileQ = new File(file);
            }
        }
        if (command.contains("-t")) {
            int indexOf = command.indexOf("-t") + 1;
            if (command.size() > indexOf) {
                String file = command.get(indexOf);
                fileT = new File(file);
            }
        }

        if (command.contains("-p")) {
            int indexOf = command.indexOf("-p") + 1;
            if (command.size() > indexOf) {
                String file = command.get(indexOf);
                fileP = new File(file);
            }
        }

        if (command.contains("-f")) {
            int indexOf = command.indexOf("-f") + 1;
            if (command.size() > indexOf) {
                inputType = command.get(indexOf);
            }
        }

        if (command.contains("-B")) {
            weighted = true;
        }

        if (fileQ == null || !fileQ.isFile()) {
            System.out.println("Error: Query file not found!");
            System.exit(1);
        }
        if (fileT == null || !fileT.isFile()) {
            System.out.println("Error: Target file not found!");
            System.exit(1);
        }

        IPatternFingerprinter q = null;
        IPatternFingerprinter t = null;
        IPatternFingerprinter p = null;

        boolean fileReadingSuccess = false;

        if (inputType.equalsIgnoreCase("-TAB")) {
            q = ReaderTABFile.Read(fileQ);
            t = ReaderTABFile.Read(fileT);
            if (fileP != null && !fileP.isFile()) {
                p = ReaderTABFile.Read(fileP);
            }
            fileReadingSuccess = true;
        } else if (inputType.equalsIgnoreCase("-CSV")) {
            q = ReaderCSVFile.Read(fileQ);
            t = ReaderCSVFile.Read(fileT);
            if (fileP != null && !fileP.isFile()) {
                p = ReaderCSVFile.Read(fileP);
            }
            fileReadingSuccess = true;
        }

        if (!fileReadingSuccess) {
            help();
        }

        /*
         Consider only predefined patterns
         */
        if (q != null && t != null && p != null && p.getFeatureCount() > 0) {
            Set<IFeature> featuresQ = new HashSet<IFeature>(q.getFeatures());
            Set<IFeature> featuresT = new HashSet<IFeature>(t.getFeatures());
            Collection<IFeature> patterns = p.getFeatures();

            q = new PatternFingerprinter();
            t = new PatternFingerprinter();

            for (IFeature patternQ : featuresQ) {
                if (patterns.contains(patternQ)) {
                    q.add(patternQ);
                }
            }
            for (IFeature patternT : featuresT) {
                if (patterns.contains(patternT)) {
                    t.add(patternT);
                }
            }
        }

        double score = Similarity.getSimilarity(q, t, weighted);;

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String a = df.format(score);

        System.out.println("Tanimoto: " + a);
    }

    static void help() {
        System.out.println("Error: Missing input pattern files");
        System.out.println("Usage: java -jar PatternSimilarity.jar -f TAB/CSV -q file1.fp -t file2.fp -p pattern.fp");
        System.out.println("Usage: java -jar PatternSimilarity.jar -f TAB/CSV -q file1.fp -t file2.fp -B ");
        System.out.println("Usage: input file in TAB/CSV delimted format");
        System.out.println("Usage: -B for binary mode");
        System.out.println("Usage: -p file with defined patterns to be considered");
        System.exit(1);
    }
}
