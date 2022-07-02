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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser.ExtensionFilter;

public class ViewController implements Initializable {
  @FXML
  private CodeArea textArea;
  @FXML
  private BorderPane borderPane;
  private Stage stage;
  private FileChooser fileChooser;
  private File openFile;
  private Map<String, Parent> modulesMap;
  private int fontSize;

  private Dialog<String> findDialog;
  @FXML
  private TextField findTextField;

  @FXML
  private VBox tabsBar;
  private ObservableList<String> tabNames;
  private Map<String, CodeArea> tabsMap;

  @FXML
  private VBox topBar;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  private void initTextArea() {
    this.fontSize = 16;
    String styleStr = "-fx-font-size: " + fontSize + "px";
    textArea.setStyle(styleStr);

    borderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.isControlDown()) {
          switch (event.getCode()) {
            case EQUALS:
              increaseFont();
              break;
            case MINUS:
              decreaseFont();
              break;
            case F:
              showFindText();
              break;
            case S:
              save();
              break;
            default:
          }
        }
      }
    });

    textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
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
  }

  public void initModules(Map<String, Parent> map) {
    this.modulesMap = map;    
    tabNames = FXCollections.observableArrayList();
    tabsMap = new HashMap<>();

    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    ExtensionFilter extensionFilter = new ExtensionFilter("Text files (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(extensionFilter);
    fileChooser.setInitialFileName("*.txt");

    initTextArea();
    initFindDialog();
    initTabsBar();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML void newFile() {
    if (textArea.getLength() > 0) {
      Alert alert = new Alert(
        AlertType.NONE,
        "Save the current file?",
        ButtonType.YES,
        ButtonType.NO,
        ButtonType.CANCEL
      );

      alert.setTitle("Confirmation");
      alert.showAndWait();
      ButtonType result = alert.getResult();

      if (result == ButtonType.YES) {
        save();
      } 
      if (result == ButtonType.NO) {

      }
      if (result == ButtonType.CANCEL) {
        return;
      }
    }
    textArea.clear();
    stage.setTitle("New File");
  }

  @FXML 
  public void open() {
    try {
      fileChooser.setTitle("Open File");
      File file = fileChooser.showOpenDialog(stage);

      if (file != null) {
        FileReader fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String str = in.readLine();
        textArea.clear();

        while (str != null) {
          textArea.appendText(str);
          str = in.readLine();
          if (str != null) textArea.appendText("\n");
        }

        in.close();
        openFile = file;

        stage.setTitle(openFile.getName());
      }
    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    }
  }

  @FXML
  public void save() {
    if (openFile == null) {
      saveas();
    } else {
      try {
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
        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(textArea.getText());
        out.close();
        openFile = file;

        stage.setTitle(openFile.getName());
      }
    } catch (FileNotFoundException e) {
      
    } catch (IOException e) {

    }
  }

  @FXML
  public void increaseFont() {
    fontSize += 4;
    String styleStr = "-fx-font-size: " + fontSize + "px";
    textArea.setStyle(styleStr);
  }

  @FXML
  public void decreaseFont() {
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

  public int findNextAndHighlight(String searchStr, int indexFrom) {
    int index = findNext(searchStr, indexFrom);

    if (index != -1) {
      textArea.selectRange(index, index + searchStr.length());
      return index + searchStr.length();
    } else {
      return -1;
    }
  }

  public int findNext(String searchStr, int indexFrom) {
    int index = textArea.getText().indexOf(searchStr, indexFrom);

    if (index != -1) { // found
      return index;
    } else if (indexFrom != -1) { // reached last match
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

  public void switchTextArea(CodeArea nextTextArea, String nextTabName) {
    String styleStr = "-fx-font-size: " + fontSize + "px";
    nextTextArea.setStyle(styleStr);
    textArea = nextTextArea;
    borderPane.setCenter(new VirtualizedScrollPane<CodeArea>(nextTextArea));
    stage.setTitle(nextTabName);
  }

  public CodeArea getTextArea() {
    return textArea;
  }
}
