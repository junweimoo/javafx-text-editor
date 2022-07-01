import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
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
      controller.setStage(stage);

      Map<String, Parent> modulesMap = loadModules(controller);

      controller.initModules(modulesMap);
      
      stage.setTitle("Simple Text Editor");
      stage.setScene(new Scene(root, 800, 600));
      stage.show();
    } catch (MalformedURLException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  private Map<String, Parent> loadModules(ViewController controller) throws IOException {      
    FXMLLoader dialogFXMLLoader = new FXMLLoader(getClass().getResource("/FindDialog.fxml"));
    DialogPane findDialog = dialogFXMLLoader.load();
    ((FindDialogController) dialogFXMLLoader.getController()).setViewController(controller);

    Map<String, Parent> modulesMap = new HashMap<>();
    modulesMap.put("findDialog", findDialog);

    return modulesMap;
  }
}