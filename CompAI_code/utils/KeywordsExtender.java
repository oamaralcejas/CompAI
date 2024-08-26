package lu.svv.saa.linklaters.privacypolicies.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;

public class KeywordsExtender {
  private Dictionary d = null;

  public KeywordsExtender() {
   try {
      d = Dictionary.getDefaultResourceInstance();
    } catch (JWNLException e) {
      e.printStackTrace();
    }
  }
  
  public String extendKeywords(String phrase, boolean firstSense) {
    List<String> keywords = new ArrayList<String>(Arrays.asList(phrase.split(" ")));
    //List<String> extendedSet = new ArrayList<String>(); 
    String result = phrase;
    for (String word : keywords) {
      IndexWordSet wdset;
      try {
        wdset = d.lookupAllIndexWords(word);
        for (IndexWord w : wdset.getIndexWordCollection()) {
          for (Synset s : w.getSenses()) {
            for (Word lem : s.getWords()) {
              if(!result.contains(lem.getLemma()))
                result = result + " " + lem.getLemma();
              /*if (!extendedSet.contains(lem.getLemma()))
                extendedSet.add(lem.getLemma());*/
            }
            if(firstSense)
              break;
          }
        }
      } catch (JWNLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }
}
