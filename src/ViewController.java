import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser.ExtensionFilter;

public class ViewController implements Initializable{
  @FXML
  private TextArea textArea;
  private Stage stage;
  private FileChooser fileChooser;
  private File openFile;
  private Map<String, Parent> modulesMap;
  private int fontSize;

  private Dialog<String> findDialog;
  @FXML
  private TextField findTextField;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    ExtensionFilter extensionFilter = new ExtensionFilter("Text files (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(extensionFilter);
    fileChooser.setInitialFileName("*.txt");

    initTextArea();
  }

  private void initTextArea() {
    this.fontSize = 12;
    increaseFont();

    textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
            default:
          }
        }
      }
    });
  }

  private void initFindDialog() {
    findDialog = new Dialog<>();
    DialogPane pane = (DialogPane) modulesMap.get("findDialog");
    findDialog.setDialogPane(pane);

    Window window = pane.getScene().getWindow();
    window.setOnCloseRequest(event -> window.hide());
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void initModules(Map<String, Parent> map) {
    this.modulesMap = map;

    initFindDialog();
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

  public void findAndHighlight(String searchStr) {
    System.out.println(searchStr);
  }
}
