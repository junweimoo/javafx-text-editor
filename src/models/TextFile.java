package models;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class TextFile {
  private static Set<TextFile> openTextFiles = new LinkedHashSet<>();
  private static Map<String, TextFile> nameMap = new HashMap<>();

  private File file;
  private String name;
  private CodeArea textArea;  

  public static Iterator<TextFile> getIterator() {
    return openTextFiles.iterator();
  }

  public static int getNumOpenFiles() {
    return openTextFiles.size();
  }

  public static TextFile getTextFileByPath(String path) {
    return nameMap.get(path);
  }

  public static TextFile removeTextFile(TextFile textFile) {
    if (openTextFiles.size() == 1 || !openTextFiles.contains(textFile)) 
      return null;

    Iterator<TextFile> it = getIterator();
    TextFile nextFile = null;
    TextFile tf = it.next();

    if (tf == textFile) {
      nextFile = it.next();
    } else {
      while (it.hasNext()) {
        TextFile nf = it.next();
        if (nf == textFile) break;
        else tf = nf;
      }
      nextFile = tf;
    }

    if (textFile.getFile() == null) {
      nameMap.remove(textFile.getName());
    } else {
      nameMap.remove(textFile.getFile().getAbsolutePath());
    }
    openTextFiles.remove(textFile);
    return nextFile;
  }

  public TextFile(File file, String name) {
    this.file = file;
    this.name = name;
    this.textArea = new CodeArea();
    textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
    nameMap.put(file.getAbsolutePath(), this);
    openTextFiles.add(this);
  }

  public TextFile() {
    int newFileNumber = 1;
    while (nameMap.containsKey("New File " + newFileNumber)) {
      newFileNumber++;
    }
    String newFileName = "New File " + newFileNumber;

    this.file = null;
    this.name = newFileName;
    this.textArea = new CodeArea();
    textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
    nameMap.put(name, this);
    openTextFiles.add(this);
  }

  public File getFile() {
    return this.file;
  }

  public void setFile(File newFile) {
    if (this.file != null) nameMap.remove(this.file.getAbsolutePath());
    nameMap.put(newFile.getAbsolutePath(), this);
    this.file = newFile;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public CodeArea getTextArea() {
    return this.textArea;
  }

  public void setTextArea(CodeArea textArea) {
    this.textArea = textArea;
  }
}
