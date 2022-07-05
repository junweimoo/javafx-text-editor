package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.TextFile;

public class ViewController implements Initializable {
  @FXML
  private CodeArea textArea;
  @FXML
  private BorderPane borderPane;
  @FXML
  private TextField findTextField;
  @FXML
  private VBox topBar;
  private Stage stage;
  private Dialog<String> findDialog;
  private FileChooser fileChooser;
  private TabsBarController tabsBarController;
  private Map<String, Parent> modulesMap;
  private int fontSize;
  private TextFile openTextFile;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  private void initFindDialog() {
    findDialog = new Dialog<>();
    DialogPane pane = (DialogPane) modulesMap.get("findDialog");
    findDialog.setDialogPane(pane);
    findDialog.setTitle("Find Text");
    findDialog.initModality(Modality.NONE);

    Stage window = (Stage) pane.getScene().getWindow();

    pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        switch (event.getCode()) {
          case ESCAPE:
            window.hide();
            textArea.clearStyle(0, textArea.getLength());
            break;
          default:
        }
      }
    });

    window.setAlwaysOnTop(true);
    window.setOnCloseRequest(event -> {
      window.hide();
      textArea.clearStyle(0, textArea.getLength());
    });
  }

  private void initTabsBar() {
    HBox loadedTabsBar = (HBox) modulesMap.get("tabsBar");
    topBar.getChildren().add(loadedTabsBar);
    tabsBarController = (TabsBarController) loadedTabsBar.getUserData();
  }

  /*
   * private void initHotkeys() {
   * borderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
   * 
   * @Override
   * public void handle(KeyEvent event) {
   * if (event.isControlDown()) {
   * switch (event.getCode()) {
   * case EQUALS:
   * increaseFont();
   * break;
   * case MINUS:
   * decreaseFont();
   * break;
   * case F:
   * showFindText();
   * break;
   * case S:
   * save();
   * break;
   * case T:
   * tabsBarController.addTab();
   * break;
   * case W:
   * tabsBarController.closeTab(openTextFile);
   * default:
   * }
   * }
   * }
   * });
   * }
   */

  public void initFileChooser() {
    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    ExtensionFilter extensionFilter = new ExtensionFilter("Text files (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(extensionFilter);
    fileChooser.setInitialFileName("*.txt");
  }

  public void init(Stage stage, Map<String, Parent> modulesMap) {
    this.stage = stage;
    this.modulesMap = modulesMap;
    this.fontSize = 16;
    this.openTextFile = new TextFile();

    initFindDialog();
    // initHotkeys();
    initFileChooser();
    initTabsBar();

    tabsBarController.switchTab(openTextFile);
  }

  @FXML
  public void open() {
    try {
      fileChooser.setTitle("Open File");
      File file = fileChooser.showOpenDialog(stage);

      if (file == null)
        return;

      TextFile openTextFile = TextFile.getTextFileByPath(file.getAbsolutePath());
      if (openTextFile != null) {
        tabsBarController.switchTab(openTextFile);
        return;
      }

      TextFile newTextFile = new TextFile(file, file.getName());

      FileReader fr = new FileReader(file);
      BufferedReader in = new BufferedReader(fr);
      String str = in.readLine();
      CodeArea newTextArea = newTextFile.getTextArea();

      while (str != null) {
        newTextArea.appendText(str);
        str = in.readLine();
        if (str != null)
          newTextArea.appendText("\n");
      }

      in.close();

      openTextFile = newTextFile;
      tabsBarController.switchTab(openTextFile);
    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    }
  }

  @FXML
  public void close() {
    tabsBarController.closeTab(openTextFile);
  }

  @FXML
  public void save() {
    if (openTextFile.getFile() == null) {
      saveas();
    } else {
      try {
        File openFile = openTextFile.getFile();
        FileWriter fw = new FileWriter(openFile);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(textArea.getText());
        out.close();
      } catch (IOException e) {

      }
    }
  }

  @FXML
  public void saveas() {
    try {
      fileChooser.setTitle("Save As");
      File file = fileChooser.showSaveDialog(stage);

      if (file != null && !file.getName().contains(".")) {
        file = new File(file.getAbsolutePath() + ".txt");
      }

      if (file != null) {
        openTextFile.setFile(file);
        openTextFile.setName(file.getName());

        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(textArea.getText());
        out.close();

        tabsBarController.switchTab(openTextFile);
      }
    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    }
  }

  @FXML
  public void newtab() {
    tabsBarController.addTab();
  }

  @FXML
  public void increaseFont() {
    if (fontSize > 100)
      return;
    fontSize += 4;
    String styleStr = "-fx-font-size: " + fontSize + "px";
    textArea.setStyle(styleStr);
  }

  @FXML
  public void decreaseFont() {
    if (fontSize < 8)
      return;
    fontSize -= 4;
    String styleStr = "-fx-font-size: " + fontSize + "px";
    textArea.setStyle(styleStr);
  }

  @FXML
  public void showFindText() {
    findDialog.showAndWait();
  }

  @FXML
  public void toggleWrapText() {
    textArea.setWrapText(!textArea.isWrapText());
  }

  @FXML
  public void toUppercase() {
    IndexRange range = textArea.getSelection();
    String original = textArea.getSelectedText();
    String lower = original.toUpperCase();
    textArea.replaceSelection(lower);
    textArea.selectRange(range.getStart(), range.getEnd());
  }

  @FXML 
  void toLowercase() {
    IndexRange range = textArea.getSelection();
    String original = textArea.getSelectedText();
    String lower = original.toLowerCase();
    textArea.replaceSelection(lower);
    textArea.selectRange(range.getStart(), range.getEnd());
  }

  @FXML
  void toRandomcase() {
    IndexRange range = textArea.getSelection();
    String original = textArea.getSelectedText().toLowerCase();
    Random rng = new Random();
    StringBuilder sb = new StringBuilder();
    for (char c : original.toCharArray()) {
      if (c >= 97 && c <= 122 && rng.nextInt(2) == 1) {
        c -= 32;
      }
      sb.append(c);
    }
    textArea.replaceSelection(sb.toString());
    textArea.selectRange(range.getStart(), range.getEnd());
  }

  public int findNextAndHighlight(String searchStr, int indexFrom) {
    int index = findNext(searchStr, indexFrom);

    if (index != -1) {
      textArea.selectRange(index, index + searchStr.length());
      return index + searchStr.length();
    } else {
      return -1;
    }
  }

  public int findNextAndReplace(String searchStr, String replString, int indexFrom) {
    int index = findNext(searchStr, indexFrom);

    if (index != -1) {
      textArea.replaceText(index, index + searchStr.length(), replString);
      textArea.selectRange(index, index + replString.length());
      return index;
    } else {
      return -1;
    }
  }

  public int findNext(String searchStr, int indexFrom) {
    if (textArea.getLength() == 0)
      return -1;

    int index = textArea.getText().indexOf(searchStr, indexFrom);

    if (index != -1) { // found
      return index;
    } else if (indexFrom != -1) { // try searching from top
      indexFrom = -1;
      index = textArea.getText().indexOf(searchStr, indexFrom);
      return index;
    } else { // not found
      return -1;
    }
  }

  public void setTextStyleSpans(StyleSpans<Collection<String>> styleSpans) {
    textArea.setStyleSpans(0, styleSpans);
  }

  public CodeArea getTextArea() {
    return textArea;
  }

  public void switchTextFile(TextFile nextTextFile) {
    openTextFile = nextTextFile;
    String styleStr = "-fx-font-size: " + fontSize + "px";
    nextTextFile.getTextArea().setStyle(styleStr);
    textArea = nextTextFile.getTextArea();
    File nextFile = nextTextFile.getFile();
    String newTitle = nextFile != null ? nextFile.getAbsolutePath() : nextTextFile.getName();
    stage.setTitle(newTitle);
    borderPane.setCenter(new VirtualizedScrollPane<CodeArea>(textArea));
  }
}
