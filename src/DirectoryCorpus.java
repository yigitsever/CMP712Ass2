import tr.edu.hacettepe.document.Corpus;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.document.StringDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by yigit on 12/16/16.
 */
public class DirectoryCorpus implements Corpus {
    private final String corpusPath;

    public DirectoryCorpus(String corpusPath) {
        this.corpusPath = corpusPath;
    }

    @Override
    public Iterator<Document> iterator() {
        DocIterator docIter;

        try {
            docIter = new DocIterator();
        } catch (IOException e) {
            throw new RuntimeException("Problem reading from corpus directory", e);
        }

        return docIter;
    }


    private class DocIterator implements Iterator<Document> {
        private File[] directory;

        private int index;
        private Document document;

        private DocIterator() throws IOException {
            File folder = new File(corpusPath);
            directory = folder.listFiles();
            index = 0;
            document = nextDoc();

        }

        @Override
        public boolean hasNext() {
            return this.directory.length > index;
        }

        private Document nextDoc() {
            File tmp = this.directory[index];

            StringBuilder fileContent = new StringBuilder();
            String lineSeperator = System.getProperty("line.separator");
            try {
                Scanner scanner = new Scanner(tmp);

                while (scanner.hasNextLine()) {
                    fileContent.append(scanner.nextLine() + lineSeperator);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return new StringDocument(fileContent.toString());

        }

        @Override
        public Document next() {
            Document tmp = document;
            document = nextDoc();
            index++;
            return tmp;
        }
    }
}
