package sample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Game {

    public static BorderPane createGame()
    {
        Main.tileSelected = false;
        Main.score = 0;

        BorderPane game = new BorderPane();

        Main.board = Board.createBoard();
        game.setCenter(Main.board);

        game.setTop(Top.createTop());

        return game;
    }

    public static void resetGame()
    {
        Main.tileSelected = false;
        Board.selectedTile = null;


        Main.game = new BorderPane();
        Main.game.setTop(Top.createTop());
        Main.board = Board.createBoard();
        Main.game.setCenter(Main.board);
        Main.game.setTop(Top.createTop());

        Main.window.setScene(new Scene(Main.game));

    }
}
