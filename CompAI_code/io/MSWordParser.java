package lu.svv.saa.linklaters.privacypolicies.io;

import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.uimafit.component.JCasCollectionReader_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Row;
import com.aspose.words.Section;
import com.aspose.words.Table;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

public class MSWordParser extends JCasCollectionReader_ImplBase {

  public static final String PARAM_INPUT_PATH = "inputPath";
  @ConfigurationParameter(name = PARAM_INPUT_PATH, mandatory = true,
      description = "Input path for dataset")
  private String inputPath;

  public static final String PARAM_LICENSE_PATH = "licensePath";
  @ConfigurationParameter(name = PARAM_LICENSE_PATH, mandatory = true,
      description = "Input path for dataset")
  private String licensePath;

  private Document docx;
  private StringBuilder documentText;
  private int counter;
  private int id;

  @Override
  public void initialize(final UimaContext context) throws ResourceInitializationException {
    super.initialize(context);
    // Apply the license
    id = 1;
    License license = new License();
    try {
      license.setLicense(licensePath + "/Aspose.Words.lic");
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    try {
      this.docx = new Document(inputPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (this.docx == null)
      throw new ResourceInitializationException(
          new IOException("Word document in " + this.inputPath + " could not be parsed!"));

    // this.logger.info("Reading \"" + FilenameUtils.getName(docx.getOriginalFileName()) + "\"");
    System.out.println("Reading \"" + FilenameUtils.getName(docx.getOriginalFileName()) + "\"");
    this.counter = 0;
    this.documentText = new StringBuilder();

  }

  public boolean hasNext() throws IOException, CollectionException {
    return counter < docx.getSections().getCount();
  }

  public Progress[] getProgress() {
    return new Progress[] {new ProgressImpl(counter,
        docx.getChildNodes(NodeType.PARAGRAPH, true).getCount(), "text segments")};
  }

  @SuppressWarnings({"static-access", "rawtypes", "unchecked"})
  @Override
  public void getNext(JCas jCas) throws IOException, CollectionException {
    int offset = 0;
    for (Section section : docx.getSections()) {
      NodeCollection children = section.getBody().getChildNodes();
      for (Node child : (Iterable<Node>) children) {
        String text = "";
        if (child.nodeTypeToString(child.getNodeType()).equalsIgnoreCase("paragraph")) {
          Paragraph paragraph = (Paragraph) child;
          text = paragraph.getText().trim().replaceAll("\\s+", " ");

          if (text.length() > 1) {
            documentText.append(text + " ");
            newParagraph(jCas, text, offset);
            offset += text.length() + 1;
          }
        }

        else if (child.nodeTypeToString(child.getNodeType()).equalsIgnoreCase("table")) {
          Table table = (Table) child;
          for (Row row : table.getRows()) {
            text = "";
            for (Cell cell : row.getCells()) {
              NodeCollection cellParagraphs = cell.getChildNodes(NodeType.PARAGRAPH, true);
              for (Paragraph paragraph : (Iterable<Paragraph>) cellParagraphs) {
                text += paragraph.getText().trim().replaceAll("\t", " ") + " ";
              }
            }
            if (text.length() > 1) {
              documentText.append(text + " ");
              newParagraph(jCas, text, offset);
              offset += text.length() + 1;
            }
          }
        }
      }
      ++counter;
    }
    setDocumentMetadata(jCas, documentText.toString());
  }

  private void newParagraph(JCas jcas, String text, int offset) {
    de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph para =
        new de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph(jcas);
    para.setBegin(offset);
    para.setEnd(offset + text.length() + 1);
    para.setId("para" + id);
    id++;
    para.addToIndexes();
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
    String title = FilenameUtils
        .getName(docx.getOriginalFileName().substring(0, docx.getOriginalFileName().indexOf(".")));

    d.setDocumentTitle(title);
    d.addToIndexes();

    jCas.setDocumentLanguage(language);
    jCas.setDocumentText(text);
  }


}
