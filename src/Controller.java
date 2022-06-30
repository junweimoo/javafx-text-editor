import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable{
  @FXML
  private TextArea textArea;
  private Stage stage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void save() {
    System.out.println("Saved: ");
    System.out.println(textArea.getText());

    FileChooser fileChooser = new FileChooser();

    try {
      fileChooser.setTitle("Save As");
      File file = fileChooser.showSaveDialog(stage);

      if (file != null) {
        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(textArea.getText());
        out.close();
      }
    } catch (FileNotFoundException e) {
      
    } catch (IOException e) {

    }
  }
}
