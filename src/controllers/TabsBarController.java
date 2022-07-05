package controllers;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import models.TextFile;

public class TabsBarController implements Initializable {
  @FXML
  private HBox tabsBar;
  private ViewController viewController;
  private ToggleGroup toggleGroup;
  private TextFile activeTextFile;
  @Override
  public void initialize(URL url, ResourceBundle resources) {
    
  }

  public void init(ViewController viewController) {
    this.viewController = viewController;
    toggleGroup = new ToggleGroup();
    toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null) oldVal.setSelected(true);
    });
  }
  
  public void addTab() {
    TextFile nextTextFile = new TextFile();
    switchTab(nextTextFile);
  }

  public void closeTab(TextFile textFile) {
    if (TextFile.getNumOpenFiles() == 1) System.exit(0);

    TextFile nextFile = TextFile.removeTextFile(textFile);
    switchTab(nextFile);
  }

  private void buildTabs() {
    tabsBar.getChildren().clear();
    Iterator<TextFile> it = TextFile.getIterator();
    while (it.hasNext()) {
      TextFile tf = it.next();
      ToggleButton tabButton = new ToggleButton(tf.getName());
      tabButton.setOnAction((e) -> {
        switchTab(tf);
      });
      tabButton.setToggleGroup(toggleGroup);
      if (tf == activeTextFile) tabButton.setSelected(true);
      tabsBar.getChildren().add(tabButton);
    }
    Button addTabButton = new Button("+");
    addTabButton.setOnAction((e) -> {
      addTab();
    });
    tabsBar.getChildren().add(addTabButton);
  }

  public void switchTab(TextFile textFile) {
    if (textFile == activeTextFile) return;
    activeTextFile = textFile;
    buildTabs();
    viewController.switchTextFile(textFile);
  }
}
