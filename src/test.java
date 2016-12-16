import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tools.DocumentTermMatrixBuilder;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

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
        TokenizerFactory factory = new TokenizerFactory();

        DirectoryCorpus corpus = new DirectoryCorpus(trainingDirectory);

        int corpusSize = 0;
        for (Document document : corpus) {
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

            for (Vector.Element element : elements) {
                docSizes[i]++;
            }
        }


        double p_z_w[][] = new double[corpusSize][vocabularySize];


        for (int z = 0; z < corpusSize; z++) {
            for (int w = 0; w < vocabularySize; w++) {
                p_z_w[z][w] = (matrix.get(z, w) + 1) / (docSizes[z] + vocabularySize);
            }
        }



    }


}
