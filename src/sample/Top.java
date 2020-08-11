package sample;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Top {
    static HBox root;
    static Button newGame;
    static Label score;

    public static Parent createTop()
    {
        root = new HBox();

        newGame = new Button();

        Main.score = 0;

        score = new Label("Score: " + Main.score);

        newGame.setText("New Game");

        newGame.setOnAction(e -> Game.resetGame());

        root.getChildren().addAll(newGame, score);

        return root;
    }

    public static Parent updateTop()
    {
        root.getChildren().clear();

        score = new Label("Score: " + Main.score);

        root.getChildren().addAll(newGame, score);

        return root;
    }
}
