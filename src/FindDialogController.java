import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class FindDialogController implements Initializable {
  @FXML
  private TextField searchTextField;
  private ViewController viewController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  public void setViewController(ViewController viewController) {
    this.viewController = viewController;
  }

  @FXML
  public void findText() {
    String searchText = searchTextField.getText();
    viewController.findAndHighlight(searchText);
  }
}
