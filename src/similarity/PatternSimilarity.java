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
import pattern.IPatternFingerprinter;
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
        if (args.length < 2) {
            System.out.println("Error: Missing input pattern files");
            System.out.println("Usage: java -jar PatternSimilarity.jar file1.fp file2.fp -TAB");
            System.out.println("Usage: java -jar PatternSimilarity.jar file1.fp file2.fp -CSV");
            System.exit(1);
        }


        File fileQ = new File(args[0]);
        File fileT = new File(args[1]);

        if (!fileQ.isFile()) {
            System.out.println("Error: Query file not found!");
            System.exit(1);
        }
        if (!fileT.isFile()) {
            System.out.println("Error: Target file not found!");
            System.exit(1);
        }

        if (args.length < 3) {
            System.out.println("Error: Input pattern file type is not defined");
            System.out.println("Usage: java -jar PatternSimilarity.jar file1.fp file2.fp -TAB");
            System.out.println("Usage: java -jar PatternSimilarity.jar file1.fp file2.fp -CSV");
            System.exit(1);
        }

        IPatternFingerprinter q = null;
        IPatternFingerprinter t = null;

        if (args[2].equalsIgnoreCase("-TAB")) {
            q = ReaderTABFile.Read(fileQ);
            t = ReaderTABFile.Read(fileT);
        } else if (args[2].equalsIgnoreCase("-CSV")) {
            q = ReaderCSVFile.Read(fileQ);
            t = ReaderCSVFile.Read(fileT);
        } else {
            System.out.println("Error: Input pattern file type not supported");
            System.out.println("Usage: java -jar PatternSimilarity.jar file1.fp file2.fp -TAB");
            System.out.println("Usage: java -jar PatternSimilarity.jar file1.fp file2.fp -CSV");
            System.exit(1);
        }

        double score = Similarity.getSimilarity(q, t);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String a = df.format(score);

        System.out.println("Tanimoto: " + a);
    }
}
