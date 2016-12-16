import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

/**
 * Created by yigit on 12/14/16.
 */
public class test {
    public static void main(String[] args) {

        TokenizerFactory factory = new TokenizerFactory();

        DirectoryCorpus corpus = new DirectoryCorpus("Training");


        PatriciaTreePerfectHash dictionary = PatriciaTreePerfectHash.buildFromCorpus(corpus, factory, 0);

        for (int i = 0; i < dictionary.size(); i++) {
            System.out.println(dictionary.findWord(i));
        }


    }


}
