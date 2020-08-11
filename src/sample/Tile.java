package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.MissingFormatArgumentException;
import java.util.Timer;
import java.util.TimerTask;

public class Tile extends StackPane {
    Coordinate coordinate;
    Rectangle fill;
    boolean isSelected;
    enum Token {FLIRTATION, SEXUALITY, ROMANCE, TALENT, PASSION, SENTIMENT, JOY, NULL}
    Token token;
    Timer timer;
    ArrayList <Tile> neighbors = new ArrayList<>();


    public Tile(int y, int x, int tileSize)
    {
        coordinate = new Coordinate(y,x);
        isSelected = false;
        int rand = (int) (Math.random() * 7);

        fill = new Rectangle(tileSize - 2, tileSize - 2);
        fill.setFill(setColor(rand));
        fill.setStroke(Color.BLACK);

        token = setToken(fill.getFill());

        getChildren().add(fill);

        setTranslateX(x * tileSize);
        setTranslateY(y * tileSize);

        setOnMouseClicked( e ->
        {
            if(!isSelected && !Board.isSelected) //selecting the tile
            {
                isSelected = true;

                Board.isSelected = true;

                Board.selectedTile = this;

                timer = new Timer();

                TimerTask task = new TimerTask()
                {
                    @Override
                    public void run() {
                        setSelected();
                    }
                };

                timer.schedule(task, 0, 750);

                System.out.println("Selecting a tile");
            }

            else if(isSelected && Board.isSelected && equal(Board.selectedTile, this)) //deselecting a tile
            {
                isSelected = false;

                Board.isSelected = false;

                Board.selectedTile = null;

                timer.cancel();
                fill.setFill(setColor(token));

                System.out.println("Deselecting a tile");

            }

            else if(!isSelected && Board.isSelected && isNeighbor(this)) //switching tiles
            {
                Board.selectedTile.timer.cancel();
                Board.selectedTile.fill.setFill(setColor(token));

                Board.swap(this, Board.selectedTile);
                System.out.println("Swapped tiles");

                if (!Board.madeThreeMatch())
                {
                    System.out.println("Swap didn't make match, swapping back");
                    Board.swap(this, Board.selectedTile);
                }

                Board.selectedTile.isSelected = false;
                Board.isSelected = false;
                Board.selectedTile = null;

                //
                while (Board.findFiveTs() || Board.findFourTs() || Board.findLs() || Board.findFiveMatch() || Board.findFourMatch() || Board.findThreeMatch())
                {
                    Top.updateTop();
                }

                while (Board.findEmpty())
                {
                    Board.findFiveTs();
                    Board.findFourTs();
                    Board.findLs();
                    Board.findFiveMatch();
                    Board.findFourMatch();
                    Board.findThreeMatch();
                }
            }

            else
                System.out.println("Nothing");

        });
    }

    public Paint setColor(int num)
    {

        switch (num)
        {
            case 0:
                return Color.RED;

            case 1:
                return Color.ORANGE;

            case 2:
                return Color.GREEN;

            case 3:
                return Color.BLUE;

            case 4:
                return Color.PINK;

            case 5:
                return Color.CYAN;

            case 6:
                return Color.YELLOW;
        }

        return Color.BLACK;
    }

    public Paint setColor(Token token)
    {

        switch (token)
        {
            case SEXUALITY:
                return Color.RED;

            case ROMANCE:
                return Color.ORANGE;

            case FLIRTATION:
                return Color.GREEN;

            case TALENT:
                return Color.BLUE;

            case PASSION:
                return Color.PINK;

            case SENTIMENT:
                return Color.CYAN;

            case JOY:
                return Color.YELLOW;
        }

        return Color.WHITE;
    }

    public Token setToken (Paint color)
    {
        if(color == Color.RED)
            return Token.SEXUALITY;

        else if(color == Color.ORANGE)
            return Token.ROMANCE;

        else if(color == Color.GREEN)
            return Token.FLIRTATION;

        else if(color == Color.BLUE)
            return Token.TALENT;

        else if(color == Color.PINK)
            return Token.PASSION;

        else if(color == Color.CYAN)
            return Token.SENTIMENT;

        else if(color == Color.YELLOW)
            return Token.JOY;

        else
            return Token.NULL;
    }

    public void setSelected()
    {
        if(fill.getFill() == Color.WHITE)
            fill.setFill(setColor(token));

        else
            fill.setFill(Color.WHITE);
    }

    public static ArrayList <Tile> getNeighbors(Tile tile)
    {
        ArrayList <Tile> neighbors = new ArrayList<>();

        if(Board.validCoordinate(new Coordinate(tile.coordinate.y - 1,tile.coordinate.x))) //down
            neighbors.add(Main.grid[tile.coordinate.y - 1][tile.coordinate.x]);

        if(Board.validCoordinate(new Coordinate(tile.coordinate.y + 1,tile.coordinate.x))) //up
            neighbors.add(Main.grid[tile.coordinate.y + 1][tile.coordinate.x]);

        if(Board.validCoordinate(new Coordinate(tile.coordinate.y,tile.coordinate.x + 1))) //right
            neighbors.add(Main.grid[tile.coordinate.y][tile.coordinate.x + 1]);

        if(Board.validCoordinate(new Coordinate(tile.coordinate.y,tile.coordinate.x - 1))) //left
            neighbors.add(Main.grid[tile.coordinate.y][tile.coordinate.x - 1]);


        return neighbors;
    }

    public boolean isNeighbor(Tile selectedTile)
    {
        boolean flag = false;
        for(int i = 0; i < Board.selectedTile.neighbors.size(); i++)
        {
            flag = equal(Board.selectedTile.neighbors.get(i), selectedTile);

            if (flag)
                return true;
        }

        return false;
    }

    public boolean equal(Tile tile1, Tile tile2)
    {
        return tile1.coordinate.x == tile2.coordinate.x && tile1.coordinate.y == tile2.coordinate.y;
    }
}
