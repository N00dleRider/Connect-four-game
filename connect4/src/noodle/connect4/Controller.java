package noodle.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int COLUMNS=7;
    private static final int ROWS=6;
    private static final int circledia=80;
    private static String player1="Player One";
    private static String player2="Player Two";
    private static final String disccolor1="#24303E";
    private static final String disccolor2="#4CAA88";
    private boolean isplay1=true;
    private Disc[][] insertedDiscArray=new Disc[ROWS][COLUMNS];//for devolopers


    @FXML
    public GridPane grid;
    @FXML
    public Pane discpane;
    @FXML
    public Label playernamelabel;
    private boolean isallowedtoinsert=true;


    public void createplayground(){
        Shape recwithholes=createstructuregrid();
        grid.add(recwithholes,0,1);
        List<Rectangle> rectangleList=clickablecolumns();
        for (Rectangle rectangle:rectangleList) {
            grid.add(rectangle,0,1);
        }


    }
    private Shape createstructuregrid(){
        Shape recwithholes= new Rectangle((COLUMNS+1)*circledia,(ROWS+1)*circledia);//+1 for margins on right and bottom
        for(int row=0;row<ROWS;row++){
            for(int column=0;column<COLUMNS;column++){
                Circle circle=new Circle();
                circle.setRadius(circledia/2);//here we create spacing after the translation has been done so...
                circle.setCenterX(circledia/2);// we only use circledia2 and thus the circles touch with each other.
                circle.setCenterY(circledia/2);
                circle.setSmooth(true);
                circle.setTranslateX(column*(circledia+5)+circledia/4);//+5 for spacing between discs,/4 for margin at top
                circle.setTranslateY(row*(circledia+5)+circledia/4);//+5 for spacing btween discs,/4 for margin at the left
                recwithholes=Shape.subtract(recwithholes,circle);


            }
        }

        recwithholes.setFill(Color.WHITE);
        return recwithholes;


    }
    private List<Rectangle> clickablecolumns() {
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int col = 0; col < COLUMNS; col++) {

            Rectangle rectangle = new Rectangle(circledia, (ROWS+ 1) * circledia);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(circledia+5)+circledia/4);
            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee35")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            final int column=col;

            rectangle.setOnMouseClicked(event -> {
                if(isallowedtoinsert) {
                    isallowedtoinsert=false;//when disk is being dropped more disks cannot be dropped
                    insertdisk(new Disc(isplay1), column);
                }
            });
            rectangleList.add(rectangle);

        }
        return rectangleList;
    }
    //changing the rows to be filled on multiple clicks on same column


    private void insertdisk(Disc disc,int columns) {
        int row=ROWS-1;
        while(row>=0){
            if(getdiscispresent(row,columns)==null){
                break;
            }
            row--;

        }
        if (row<0)
            return;
        insertedDiscArray[row][columns]=disc;//for devolopers
        discpane.getChildren().add(disc);
        disc.setTranslateX(columns*(circledia+5)+circledia/4);//to make the disc fall on the first row of the selected column
        int currentrow=row;
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row*(circledia+5)+circledia/4);//to make the disc fall on the last row of the selected column
        translateTransition.setOnFinished(event -> {
            isallowedtoinsert=true;//finally when the disk is dropped completely,allow the next player to drop the disk
            if(gameEnded(currentrow,columns)){
                gameOver();
                return;

            }
            isplay1=!isplay1;
            playernamelabel.setText(isplay1?player1:player2);//Assignment!!!!!!!!!!!!!!!!!!!!!
        });
        translateTransition.play();
    }
    private boolean gameEnded(int row,int column){
        List<Point2D> verticalpoints=IntStream.rangeClosed(row-3,row+3)
                .mapToObj(r -> new Point2D(r,column))
                .collect(Collectors.toList());
        List<Point2D> horizontalpoints=IntStream.rangeClosed(column-3,column+3)
                .mapToObj(col -> new Point2D(row,col))
                .collect(Collectors.toList());
        Point2D startpoint1=new Point2D(row-3,column+3);
        List<Point2D> diagonalpoints=IntStream.rangeClosed(0,6)
                .mapToObj(i -> startpoint1.add(i,-i))
                .collect(Collectors.toList());
        Point2D startpoint2=new Point2D(row-3,column-3);
        List<Point2D> diagona2points=IntStream.rangeClosed(0,6)
                .mapToObj(i -> startpoint2.add(i,i))
                .collect(Collectors.toList());
        boolean isended=checkcombinations(verticalpoints)||checkcombinations(horizontalpoints)||checkcombinations(diagonalpoints)||checkcombinations(diagona2points);
        return isended;

    }

    private boolean checkcombinations(List<Point2D> points){
        int chain=0;
        for (Point2D point:points) {

            int rowindexforarray= (int) point.getX();
            int columnindexforarray= (int) point.getY();
            Disc disc=getdiscispresent(rowindexforarray,columnindexforarray);

            if(disc!=null && disc.isp1move==isplay1){//if the last inserted disc belongs to current player
                chain++;
                if(chain==4){
                    return true;
                }
            }
                else{
                    chain=0;
                }
        }
     return false;
    }
    private Disc getdiscispresent(int row,int column) {
        if(row>=ROWS||row<0||column>=COLUMNS||column<0)
            return null;
        return insertedDiscArray[row][column];

    }

    private void gameOver(){
        String winner=isplay1?player1:player2;
        System.out.println("Winner is: "+winner);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is: "+winner);
        alert.setContentText("Want to Play agan?");
        ButtonType yesbutton=new ButtonType("Yes");
        ButtonType nobutton=new ButtonType("No, Exit");
        alert.getButtonTypes().setAll(yesbutton, nobutton);
        Platform.runLater(()->{//helps to resolve illegal State exception.only to be executed while animation is done running
                Optional<ButtonType> buttonclicked=alert.showAndWait();//store the button value and return the optional
        if(buttonclicked.isPresent()&&buttonclicked.get()==yesbutton){
            resetgame();
        }else{
            Platform.exit();
            System.exit(0);
        }
        });

    }

    public void resetgame() {
       discpane.getChildren().clear();//removes all the disks from pane
        for(int row=0;row<insertedDiscArray.length;row++){//structurally make all the elements of disk array to null
            for(int col=0;col<insertedDiscArray[row].length;col++){
                insertedDiscArray[row][col]=null;
            }
        }
        isplay1=true;//always p1 starts game
        playernamelabel.setText(player1);//set his name
        createplayground();//prepare fresh playground
    }

    private static class Disc extends Circle{
        private final boolean isp1move;
        public Disc(boolean isp1move){
            this.isp1move=isp1move;
            setRadius(circledia/2);
            setFill(isp1move?Color.valueOf(disccolor1):Color.valueOf(disccolor2));
            setCenterX(circledia/2);
            setCenterY(circledia/2);
        }

}

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
