package noodle.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller;//controller variable defined for controller


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));//fxml loader
        GridPane grid = loader.load();
        controller = loader.getController();
        controller.createplayground();
        MenuBar menubar = createmenu();
        menubar.prefWidthProperty().bind(primaryStage.widthProperty());//for Stretching the menu bar, Whatever is the width of the primary stage.
        Pane menupane = (Pane) grid.getChildren().get(0);
        menupane.getChildren().add(menubar);
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    private MenuBar createmenu() {
        Menu filemenu = new Menu("File");
        MenuItem newgame = new MenuItem("New Game");//tick event on new game using lambda
        newgame.setOnAction(event -> controller.resetgame());

        MenuItem resetgame = new MenuItem("Reset Game");//tick event on reset game using lambda
        resetgame.setOnAction(event -> controller.resetgame());

        SeparatorMenuItem separator = new SeparatorMenuItem();

        MenuItem exitgame = new MenuItem("Exit Game");//tick action for exit game using lambda
        exitgame.setOnAction(event -> exitgame());

        filemenu.getItems().addAll(newgame, resetgame, separator, exitgame);
        Menu helpmenu = new Menu("Help");
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem aboutme = new MenuItem("About Me");
        aboutme.setOnAction(event -> aboutcreator());
        MenuItem aboutgame = new MenuItem("About Connect4");
        aboutgame.setOnAction(event -> aboutconnect4());//tick action for about connect4 using lamda
        helpmenu.getItems().addAll(aboutgame, sep, aboutme);

        MenuBar menubar = new MenuBar();
        menubar.getMenus().addAll(filemenu, helpmenu);
        return menubar;
    }

    private void aboutcreator() {
        Alert aboutdialog = new Alert(Alert.AlertType.INFORMATION);
        aboutdialog.setTitle("About The Creator");
        aboutdialog.setHeaderText("Ashwaq Ahmed");
        aboutdialog.setContentText("I Love video-games, I also like Star Gazing");
        aboutdialog.show();
    }

    // ABOUT CONNECT4 DESCRIPTION FUNCTION
    private void aboutconnect4() {
        Alert alertdialog = new Alert(Alert.AlertType.INFORMATION);
        alertdialog.setTitle("About Connect4 Game");
        alertdialog.setHeaderText("How to Play?");
        alertdialog.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alertdialog.show();
    }

    // EXIT GAME CLICK EVENT FUNCTION
    private void exitgame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetgame() {

    }


    public static void main(String[] args) {
        launch(args);
    }
}
