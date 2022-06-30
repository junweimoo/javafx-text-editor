import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
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
  }
}
