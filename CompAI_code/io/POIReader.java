package lu.svv.saa.linklaters.privacypolicies.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import org.apache.log4j.BasicConfigurator;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.uimafit.component.JCasCollectionReader_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class POIReader extends JCasCollectionReader_ImplBase {
  public static final String PARAM_INPUT_PATH = "inputPath";
  @ConfigurationParameter(name = PARAM_INPUT_PATH, mandatory = true,
      description = "Input path for dataset")
  private String inputPath;

  private File document;
  private XWPFDocument docx;
  private Iterator<IBodyElement> iterator = null;
  private StringBuilder documentText;
  private int counter;

  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    BasicConfigurator.configure();
    this.document = new File(inputPath);
    if (!this.document.isFile())
      throw new ResourceInitializationException(
          new IOException("Dataset path " + this.document + " is expected to be a file!"));
    try {
      this.docx = new XWPFDocument(new FileInputStream(document));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.iterator = docx.getBodyElementsIterator();
    this.counter = 0;
    this.documentText = new StringBuilder();
  }

  @Override
  public void getNext(JCas jCas) throws IOException, CollectionException {
    while (iterator.hasNext()) {
      IBodyElement element = iterator.next();
      String text = "";

      if (element instanceof XWPFParagraph) {
        text = ((XWPFParagraph) element).getParagraphText().replaceAll("\\s+", " ").trim();

      } else if (element instanceof XWPFTable) {
        text = ((XWPFTable) element).getText().replaceAll("\\s+", " ");

      }
      documentText.append(text + " ");
    }
    ++counter;
    setDocumentMetadata(jCas, documentText.toString());

  }

  public int getNumTables() {
    return this.docx.getTables().size();
  }

  public int getNumPictures() {
    return this.docx.getAllPictures().size();
  }

  @Override
  public void close() throws IOException {
    // nop
  }

  public boolean hasNext() throws IOException, CollectionException {
    return iterator.hasNext();
  }

  public Progress[] getProgress() {
    return new Progress[] {
        new ProgressImpl(counter, docx.getBodyElements().size(), "text segments")};
  }

  /**
   * Sets the metadata of the current document.
   *
   * @param jCas
   * @throws CollectionException
   */
  private void setDocumentMetadata(JCas jCas, String text) throws CollectionException {
    DocumentMetaData d = DocumentMetaData.create(jCas);
    String language = "en";
    String title = document.getName().substring(0, document.getName().indexOf("."));

    d.setDocumentTitle(title);
    d.setLanguage(language);
    jCas.setDocumentLanguage(language);
    jCas.setDocumentText(text);
  }

}
