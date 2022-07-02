package controllers;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

import org.fxmisc.richtext.model.StyleSpansBuilder;

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
    String searchText = searchTextField.getText();
    int searchTextLength = searchText.length();
    StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

    int curIndex = viewController.findNext(searchText, -1);

    if (curIndex == -1) {
      infoText.setText(String.format("Can't find the text \"%s\"", searchText));
      return;
    }

    int firstIndex = curIndex;
    int prev = 0;  

    spansBuilder.add(Collections.emptyList(), curIndex - prev);
    spansBuilder.add(Collections.singleton("found-text"), searchTextLength);
    curIndex += searchTextLength;
    prev = curIndex;

    while ((curIndex = viewController.findNext(searchText, curIndex)) != firstIndex) {
      spansBuilder.add(Collections.emptyList(), curIndex - prev);
      spansBuilder.add(Collections.singleton("found-text"), searchTextLength);
      curIndex += searchTextLength;
      prev = curIndex;
    }

    spansBuilder.add(Collections.emptyList(), viewController.getTextArea().getLength() - 1 - prev);

    viewController.setTextStyleSpans(spansBuilder.create());
  }
}
