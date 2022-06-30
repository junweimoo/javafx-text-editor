import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller implements Initializable{
  @FXML
  private TextArea textArea;
  private Stage stage;
  private FileChooser fileChooser;
  private File openFile;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    ExtensionFilter extensionFilter = new ExtensionFilter("Text files (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(extensionFilter);
    fileChooser.setInitialFileName("*.txt");
  }

  public void setStage(Stage stage) {
    this.stage = stage;
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
      }
    } catch (FileNotFoundException e) {
      
    } catch (IOException e) {

    }
  }
}
