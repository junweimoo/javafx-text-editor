import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import controllers.FindDialogController;
import controllers.TabsBarController;
import controllers.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    try {
      launch(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Stage stage) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View.fxml"));

      Parent root = fxmlLoader.load();
      ViewController controller = fxmlLoader.getController();
      Map<String, Parent> modulesMap = loadModules(controller);

      controller.init(stage, modulesMap);
      
      stage.setTitle("JavaFX Text Editor");
      stage.setScene(new Scene(root, 1000, 750));
      stage.show();
    } catch (MalformedURLException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private Map<String, Parent> loadModules(ViewController controller) throws IOException {      
    FXMLLoader dialogFxmlLoader = new FXMLLoader(getClass().getResource("/FindDialog.fxml"));
    DialogPane findDialog = dialogFxmlLoader.load();
    ((FindDialogController) dialogFxmlLoader.getController()).init(controller);

    FXMLLoader tabsFxmlLoader = new FXMLLoader(getClass().getResource("/TabsBar.fxml"));
    HBox tabsBar = tabsFxmlLoader.load();
    ((TabsBarController) tabsFxmlLoader.getController()).init(controller);

    Map<String, Parent> modulesMap = new HashMap<>();
    modulesMap.put("findDialog", findDialog);
    modulesMap.put("tabsBar", tabsBar);

    return modulesMap;
  }
}