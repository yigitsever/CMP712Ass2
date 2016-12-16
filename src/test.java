import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tokenize.WordTokenizer;
import tr.edu.hacettepe.tools.DocumentTermMatrixBuilder;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;

/**
 * Created by yigit on 12/14/16.
 */
public class test {
    public static Matrix matrix;

    public static void main(String[] args) {

        /*
         *  TokenizerFactory, PatriciaTreePerfectHash and DocumentTermMatrixBuilder provided by
         *  Gonenc Ercan for the second homework of BIL614 - Text Mining
         *  Those files can be found in the package tr.edu.hacettepe
         *  DirectoryCorpus is created for his project with respect to the implementation of
         *  SingleFileCorpus, again provided in the package
         */
        String trainingDirectory = "Training";
        File trainingFolder = new File(trainingDirectory);
        String categories[] = trainingFolder.list();

        String testDirectory = "Testing";
        TokenizerFactory factory = new TokenizerFactory();

        DirectoryCorpus corpus = new DirectoryCorpus(trainingDirectory);

        int corpusSize = 0;
        for (Document ignored : corpus) {
            corpusSize++;
        }
//        System.out.println("CorpusSize " + corpusSize);

        PatriciaTreePerfectHash dictionary = PatriciaTreePerfectHash.buildFromCorpus(corpus, factory, 0);

        matrix = DocumentTermMatrixBuilder.createMatrix(dictionary, corpus, factory, corpusSize);

        int vocabularySize = dictionary.size(); // Will be used for P(W_k | z) formula

//        System.out.println("RowSize: " + matrix.rowSize());
//        System.out.println("ColumnSize: " + matrix.columnSize());


        int docSizes[] = new int[matrix.rowSize()];
        for (int i = 0; i < matrix.rowSize(); i++) {
            Vector v = matrix.viewRow(i);

            Iterable<Vector.Element> elements = v.nonZeroes();

            for (Vector.Element ignored : elements) {
                docSizes[i]++;
            }
        }


        double p_z_w[][] = new double[corpusSize][vocabularySize];


        for (int z = 0; z < corpusSize; z++) {
            for (int w = 0; w < vocabularySize; w++) {
                p_z_w[z][w] = (matrix.get(z, w) + 1) / (docSizes[z] + vocabularySize);
            }
        }

        File testFolder = new File(testDirectory);
        File[] testFiles = testFolder.listFiles();
        int match = 0, miss = 0;


        for (File testFile : testFiles) {
            BigDecimal highscore = new BigDecimal(-1);
            int highest = -1;
            LinkedList<String> fileContent = new LinkedList<>();
            try {
                Reader testFileReader = new FileReader(testFile);
                WordTokenizer wordTokenizer = new WordTokenizer(testFileReader);

                String token;

                while ((token = wordTokenizer.nextToken()) != null) {
                    fileContent.add(token);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }   //End of populating fileContent

            for (int z = 0; z < corpusSize; z++) {
                BigDecimal score = new BigDecimal(1);
                BigDecimal kotarici = new BigDecimal(0.0000001);
                for (int w = 0; w < fileContent.size(); w++) {
                    int index = dictionary.findIndex(fileContent.get(w));
                    if (index == -1) {
                        score = score.multiply(kotarici);
                    } else {
                        BigDecimal tmp = new BigDecimal(p_z_w[z][index]);
                        score = score.multiply(tmp);
                    }
                }
//                score *= 1 / 5; // Well, redundant
                if (score.compareTo(highscore) == 1) {
                    highscore = score;
                    highest = z;
                }
            }
            try {
//                System.out.println("For file " + testFile.getName() + " classified as " + categories[highest] + " with " + format(highscore, 4));
//                System.out.println("=>" + testFile.getName() + "\t\t|\t" + categories[highest]);
                if (testFile.getName().contains(categories[highest])) {
                    System.out.printf("%-15s| %-15s| MATCH\n", testFile.getName(), categories[highest]);
                    match++;
                } else {
                    System.out.printf("%-15s| %-15s| MISS\n", testFile.getName(), categories[highest]);
                    miss++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("==RESULTS==");
        System.out.printf("%-6d Matched | Missed %-6d", match, miss);


    }

    private static String format(BigDecimal x, int scale) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }


}
