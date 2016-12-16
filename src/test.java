import org.apache.mahout.math.Matrix;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tools.DocumentTermMatrixBuilder;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

/**
 * Created by yigit on 12/14/16.
 */
public class test {
    public static void main(String[] args) {

        TokenizerFactory factory = new TokenizerFactory();

        DirectoryCorpus corpus = new DirectoryCorpus("Training");

        int corpusSize = 0;
        for (Document document : corpus) {
            corpusSize++;
        }


        PatriciaTreePerfectHash dictionary = PatriciaTreePerfectHash.buildFromCorpus(corpus, factory, 0);

        Matrix matrix = DocumentTermMatrixBuilder.createMatrix(dictionary, corpus, factory, corpusSize);


    }


}
