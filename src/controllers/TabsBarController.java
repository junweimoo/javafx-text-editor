package controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class TabsBarController implements Initializable {
  @FXML
  private HBox tabsBar;
  private ObservableList<String> tabNames;
  private Map<String, CodeArea> tabsMap;
  private ViewController viewController;

  @Override
  public void initialize(URL url, ResourceBundle resources) {
    
  }

  public void init(ViewController viewController) {
    tabNames = FXCollections.observableArrayList();
    tabsMap = new HashMap<>();
    this.viewController = viewController;

    tabNames.addListener(new ListChangeListener<String>() {
      @Override
      public void onChanged(ListChangeListener.Change<? extends String> change) {
        updateTabs();
      }
    });

    tabsMap.put("New File 1", viewController.getTextArea());
    tabNames.add("New File 1");
  }
  
  public void addTab() {
    CodeArea newTextArea = new CodeArea();
    newTextArea.setParagraphGraphicFactory(LineNumberFactory.get(newTextArea));
    int newFileNumber = 1;
    while (tabsMap.containsKey("New File " + newFileNumber)) {
      newFileNumber++;
    }
    String newFileName = "New File " + newFileNumber;
    tabsMap.put(newFileName, newTextArea);
    tabNames.add(newFileName);
    switchTab(newFileName);
  }

  public void addTab(String fileName, CodeArea textArea) {
    tabsMap.put(fileName, textArea);
    tabNames.add(fileName);
    switchTab(fileName);
  }

  public void updateTabName(String oldName, String newName) {
    CodeArea oldTextArea = tabsMap.remove(oldName);
    tabsMap.put(newName, oldTextArea);
    for (int i = 0; i < tabNames.size(); i++) {
      if (tabNames.get(i).equals(oldName)) {
        tabNames.remove(i);
        tabNames.add(i, newName);

        break;
      }
    }
    updateTabs();
  }

  private void updateTabs() {
    tabsBar.getChildren().clear();
    for (String tabName : tabNames) {
      Button tabButton = new Button(tabName);
      tabButton.setOnAction((e) -> {
        switchTab(tabName);
      });
      tabsBar.getChildren().add(tabButton);
    }
    Button addTabButton = new Button("+");
    addTabButton.setOnAction((e) -> {
      addTab();
    });
    tabsBar.getChildren().add(addTabButton);
  }

  private void switchTab(String tabName) {
    CodeArea nextTextArea = tabsMap.get(tabName);
    viewController.switchTextArea(nextTextArea, tabName);
  }
}
