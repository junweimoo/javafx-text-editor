package models;

import java.util.HashMap;
import java.util.Map;

public class TabHistory {
  class TabNode {
    TabNode prev, next;
    TextFile textFile;

    void remove() {
      if (first == this) {
        first = next;
      } else {
        prev.next = next;
      }
      if (last == this) {
        last = prev;
      } else {
        next.prev = prev;
      }
      next = null;
      prev = null;
    }

    TabNode(TextFile textFile) {
      this.textFile = textFile;
    }
  }

  private TabNode first, last;
  private Map<TextFile, TabNode> added;

  public TabHistory() {
    added = new HashMap<>();
  }

  public void add(TextFile tf) {
    TabNode node = new TabNode(tf);

    if (added.containsKey(tf)) {
      TabNode oldNode = added.get(tf);
      oldNode.remove();
    } 

    if (first == null) {
      first = node;
      last = node;
    } else {
      first.prev = node;
      node.next = first;
      first = node;
    }
    added.put(tf, node);

    // printSeq();
  }

  public TextFile getLastOpened() {
    TextFile ret = first.textFile;
    added.remove(ret);
    first.remove();

    // printSeq();
    return ret;
  }

  public void remove(TextFile tf) {
    TabNode node = added.get(tf);
    if (node == null)
      return;
    node.remove();
    added.remove(tf);
  }

  /*
  private void printSeq() {
    TabNode n = first;
    while (n != null) {
      System.out.print(n.textFile.getName() + " ");
      n = n.next;
    }
    System.out.println("");
  }
  */
}
