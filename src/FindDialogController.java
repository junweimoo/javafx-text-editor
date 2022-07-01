import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class FindDialogController implements Initializable {
  @FXML
  private TextField searchTextField;
  @FXML 
  private Text infoText;
  private ViewController viewController;
  private int searchIndex;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    searchIndex = -1;
  }

  public void setViewController(ViewController viewController) {
    this.viewController = viewController;
  }

  @FXML
  public void findText() {
    String searchText = searchTextField.getText();
    searchIndex = viewController.findNextAndHighlight(searchText, searchIndex);
    if (searchIndex == -1) {
      infoText.setText(String.format("Can't find the text \"%s\"", searchText));
    }
  }

  @FXML
  public void findAllText() {
    List<Integer> results = new ArrayList<>();
    String searchText = searchTextField.getText();
    int searchTextLength = searchText.length();

    int curIndex = viewController.findNext(searchText, -1);
    if (curIndex == -1) {
      infoText.setText(String.format("Can't find the text \"%s\"", searchText));
      return;
    }
    int firstIndex = curIndex;
    results.add(curIndex);
    curIndex += searchTextLength;

    while ((curIndex = viewController.findNext(searchText, curIndex)) != firstIndex) {
      curIndex = viewController.findNext(searchText, curIndex);
      results.add(curIndex);
      curIndex += searchTextLength;
    }

    viewController.highlightAllText(results, searchTextLength);
  }
}
