/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import pattern.Feature;
import pattern.IFeature;
import pattern.IPatternFingerprinter;
import pattern.PatternFingerprinter;
import utility.TextReader;

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
            System.out.println("Usage: java -jar PatternSimilarity.jar file1.fp file2.fp");
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
        TextReader textReaderQ = null;
        IPatternFingerprinter q = new PatternFingerprinter();
        try {
            textReaderQ = new TextReader(new BufferedReader(new FileReader(fileQ)));
            while (!textReaderQ.eof()) {
                IFeature feature = new Feature(textReaderQ.getWord().trim(), textReaderQ.getDouble());
                try {
                    q.add(feature);
                } catch (Exception ex) {
                    Logger.getLogger(PatternSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PatternSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (textReaderQ != null) {
                textReaderQ.close();
            }
        }

        TextReader textReaderT = null;
        IPatternFingerprinter t = new PatternFingerprinter();
        try {
            textReaderT = new TextReader(new BufferedReader(new FileReader(fileT)));
            while (!textReaderT.eof()) {
                IFeature feature = new Feature(textReaderT.getWord().trim(), textReaderT.getDouble());
                try {
                    t.add(feature);
                } catch (Exception ex) {
                    Logger.getLogger(PatternSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PatternSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (textReaderT != null) {
                textReaderT.close();
            }
        }
        double score = getSimilarity(q, t);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String a = df.format(score);

        System.out.println("Tanimoto: " + a);
    }

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
