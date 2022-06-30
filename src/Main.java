import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View.fxml"));
  
      Parent root = fxmlLoader.load();
      Controller controller = fxmlLoader.getController();
      controller.setStage(stage);
      
      stage.setTitle("Simple Text Editor");
      stage.setScene(new Scene(root, 700, 500));
      stage.show();
    } catch (MalformedURLException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}