package lu.svv.saa.linklaters.privacypolicies.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.uima.resource.ResourceInitializationException;

public class FileHandler {

  public static void writeToFile(String path, String content) {
    BufferedWriter writer = null;
    try {
      // BufferedWriter
      writer = new BufferedWriter(new FileWriter(path));
      writer.write(content);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void appendToFile(String path, String content) {
    BufferedWriter writer = null;
    try {
      // BufferedWriter
      writer = new BufferedWriter(new FileWriter(path, true));
      writer.write(content);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<String> readFromFile(String path) {
    List<String> lines = new ArrayList<String>();
    String line = null;
    try {
      // FileReader reads text files in the default encoding.
      FileReader fileReader = new FileReader(path);

      BufferedReader bufferedReader = new BufferedReader(fileReader);

      while ((line = bufferedReader.readLine()) != null) {
        lines.add(line);
      }
      bufferedReader.close();
    } catch (FileNotFoundException ex) {
      System.out.println("Unable to open file '" + path + "'");
    } catch (IOException ex) {
      System.out.println("Error reading file '" + path + "'");
    }
    return lines;
  }

  public static List<String> getFileNames(String path, final String extension)
      throws ResourceInitializationException {
    File directory = new File(path);
    if (!directory.isDirectory())
      throw new ResourceInitializationException(new IOException(
          "Document collection path " + directory + " is expected to be a directory!"));

    List<String> fileNames = new ArrayList<String>();
    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        String lowercaseName = name.toLowerCase();
        if (lowercaseName.endsWith(extension)) {
          return true;
        } else {
          return false;
        }
      }
    };

    String[] files = directory.list(filter);
    if (extension.equals("")) {
      files = directory.list();
    }
    for (final String filename : files) {
      fileNames.add(filename);

    }
    return fileNames;
  }

  public static boolean exists(String path) {
    return new File(path).exists();
  }

  public static List<String> readBatch(BufferedReader reader, int batchSize) throws IOException {
    List<String> result = new ArrayList<String>();
    for (int i = 0; i < batchSize; i++) {
      String line = reader.readLine();
      if (line != null) {
        result.add(line);
      } else {
        return result;
      }
    }
    return result;
  }

  public static String getArffHeaderMultiClass(String relation, int vectorDimension) {
    StringBuilder header = new StringBuilder();
    
      header.append("@relation " + relation + "\n\n");
      header.append("@attribute sentence STRING\n");
      header.append(getArffEmbeddingsAtt(vectorDimension));
      header.append("@attribute class"
          + "{N/A, DPO, PD_ORIGIN, PD_TIME_STORED, AUTO_DECISION_MAKING, "
          + "RECIPIENTS, DATA_SUBJECT_RIGHT, PD_CATEGORY, CONTROLLER, CONTROLLER_REPRESENTATIVE, "
          + "TRANSFER_OUTSIDE_EUROPE, PD_PROVISION_OBLIGED, PD_SECURITY, "
          + "PROCESSING_PURPOSES, CHILDREN}\n\n");
      header.append("@data\n");
    
    return header.toString();
  }
  
  public static String getArffHeaderBinaryClass(String relation, int vectorDimension, String classValue) {
    StringBuilder header = new StringBuilder();
    
      header.append("@relation " + relation + "\n\n");
      header.append("@attribute sentence STRING\n");
      header.append(getArffEmbeddingsAtt(vectorDimension));
      header.append("@attribute class"
          + "{" + classValue + ", NOT_" + classValue + "}\n\n");
      header.append("@data\n");
    
    return header.toString();
  }
  
  /*public static String getArffHeader(String relation, String variation) {
    StringBuilder header = new StringBuilder();
    if (variation.equals("perClass")) {
      header.append("@relation " + relation + "\n\n");
      header.append("@attribute sentence STRING\n");
      header.append("@attribute class"
          + "{N/A, DPO, PD_ORIGIN, PD_TIME_STORED, AUTO_DECISION_MAKING, "
          + "RECIPIENTS, DATA_SUBJECT_RIGHT, PD_CATEGORY, CONTROLLER, CONTROLLER_REPRESENTATIVE, "
          + "TRANSFER_OUTSIDE_EUROPE, PD_PROVISION_OBLIGED, PD_SECURITY, " // ONE_STOP_SHOP,
          + "PROCESSING_PURPOSES, CHILDREN}\n\n");
      header.append("@data\n");
    } else if (variation.equals("rawTxt")) {
      header.append("@relation " + relation + "\n\n");
      header.append("@attribute sentence STRING\n\n");
      header.append("@data\n");
    }
    return header.toString();
  }

  public static String getArffHeader(String relation, int vectorDim) {
    StringBuilder header = new StringBuilder();
    header.append("@relation " + relation + "\n\n");
    header.append("@attribute sentence STRING\n");
    for (int i = 0; i < vectorDim; ++i) {
      header.append(String.format("@attribute embedding-%d NUMERIC%n", i));
    }
    header
        .append("@attribute class" + "{N/A, DPO, PD_ORIGIN, PD_TIME_STORED, AUTO_DECISION_MAKING, "
            + "RECIPIENTS, DATA_SUBJECT_RIGHT, PD_CATEGORY, CONTROLLER, CONTROLLER_REPRESENTATIVE, "
            + "TRANSFER_OUTSIDE_EUROPE, PD_PROVISION_OBLIGED, PD_SECURITY, " // ONE_STOP_SHOP,
            + "PROCESSING_PURPOSES, CHILDREN}\n\n");
    header.append("@data\n");

    return header.toString();
  }*/

  public static String getArffEmbeddingsAtt(int vectorDim) {
    StringBuilder atts = new StringBuilder();
    for (int i = 0; i < vectorDim; ++i) {
      atts.append(String.format("@attribute embedding-%d NUMERIC%n", i));
    }

    return atts.toString();
  }

  public static Map<String, String> readIndex(String path) {
    Map<String, String> index = new HashMap<String, String>();
    String line = null;
    try {
      // FileReader reads text files in the default encoding.
      FileReader fileReader = new FileReader(path);

      BufferedReader bufferedReader = new BufferedReader(fileReader);

      while ((line = bufferedReader.readLine()) != null) {
        String key = line.split("#")[0];
        String value = line.split("#")[1];
        index.put(key, value);
      }
      bufferedReader.close();
    } catch (FileNotFoundException ex) {
      System.out.println("Unable to open file '" + path + "'");
    } catch (IOException ex) {
      System.out.println("Error reading file '" + path + "'");
    }

    return index;
  }

}
