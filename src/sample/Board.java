package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.security.PublicKey;
import java.util.ArrayList;

public class Board {

    static boolean isSelected = false;
    static Tile selectedTile;

    public static Pane createBoard() {
        Board.selectedTile = null;
        Board.isSelected = false;

        Pane root = new Pane();

        root.setPrefSize(Main.WIDTH, Main.HEIGHT);

        Main.grid = new Tile[Main.Y_TILES][Main.X_TILES];

        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES; x++) {
                Main.grid[y][x] = new Tile(y, x, Main.TILE_SIZE);

                boolean flag = (x > 1 && Main.grid[y][x - 1].token == Main.grid[y][x - 2].token && Main.grid[y][x - 1].token == Main.grid[y][x].token) ||
                        (y > 1 && Main.grid[y - 1][x].token == Main.grid[y - 2][x].token && Main.grid[y - 1][x].token == Main.grid[y][x].token);

                while (flag) {
                    System.out.println(y + "," + x + " : " + Main.grid[y][x].token);
                    reRoll(new Coordinate(y, x));
                    System.out.println(y + "," + x + " : " + Main.grid[y][x].token);

                    flag = (x > 1 && Main.grid[y][x - 1].token == Main.grid[y][x - 2].token && Main.grid[y][x - 1].token == Main.grid[y][x].token) ||
                            (y > 1 && Main.grid[y - 1][x].token == Main.grid[y - 2][x].token && Main.grid[y - 1][x].token == Main.grid[y][x].token);
                }

                root.getChildren().add(Main.grid[y][x]);
            }


        while (!threeMatchPossible())
            root = createBoard();

        //getting neighbors
        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES; x++)
                Main.grid[y][x].neighbors = Tile.getNeighbors(Main.grid[y][x]);


        return root;
    }

    public static boolean threeMatchPossible() {
        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES; x++) {
                if (y > 1 && Main.grid[y - 2][x].token == Main.grid[y - 1][x].token && getMatchNeighbors("UP", Main.grid[y - 2][x]).size() > 0) //up
                    return true;

                if (y + 2 < Main.Y_TILES && Main.grid[y + 2][x].token == Main.grid[y + 1][x].token && getMatchNeighbors("DOWN", Main.grid[y + 2][x]).size() > 0) //down
                    return true;

                if (x + 2 < Main.X_TILES && Main.grid[y][x + 2].token == Main.grid[y][x + 1].token && getMatchNeighbors("RIGHT", Main.grid[y][x + 2]).size() > 0) //right
                    return true;

                if (x > 1 && Main.grid[y][x - 2].token == Main.grid[y][x - 1].token && getMatchNeighbors("LEFT", Main.grid[y][x - 2]).size() > 0) //left
                    return true;
            }
        return false;
    }

    public static boolean madeThreeMatch() {
        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES - 2; x++)
                if (Main.grid[y][x].token != Tile.Token.NULL && Main.grid[y][x].token == Main.grid[y][x + 1].token && Main.grid[y][x].token == Main.grid[y][x + 2].token)
                    return true;

        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 0; x < Main.X_TILES; x++)
                if (Main.grid[y][x].token != Tile.Token.NULL && Main.grid[y][x].token == Main.grid[y + 1][x].token && Main.grid[y][x].token == Main.grid[y + 2][x].token)
                    return true;

        return false;
    }

    public static boolean findThreeMatch() {

        boolean flag = false;

        if (findThreeMatchRow()) {
            flag = true;
            Main.score += 3;
        }

        if (findThreeMatchColumn()) {
            flag = true;
            Main.score += 3;
        }

        return flag;
    }

    public static boolean findThreeMatchRow() {
        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL && Main.grid[y][x].token == Main.grid[y][x + 1].token && Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    return true;
                }

            }

        return false;
    }

    public static boolean findThreeMatchColumn() {
        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 0; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL && Main.grid[y][x].token == Main.grid[y + 1][x].token && Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }
            }

        return false;
    }

    public static boolean findFourMatch() {
        boolean flag = false;

        if (findFourMatchColumn()) {
            flag = true;
            Main.score += 4;
        }

        if (findFourMatchRow()) {
            flag = true;
            Main.score += 4;
        }

        return flag;
    }

    public static boolean findFourMatchRow() {
        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES - 3; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 3].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    Main.grid[y][x + 3] = clearTile(Main.grid[y][x + 3]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findFourMatchColumn() {
        for (int y = 0; y < Main.Y_TILES - 3; y++)
            for (int x = 0; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 3][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    Main.grid[y + 3][x] = clearTile(Main.grid[y + 3][x]);

                    return true;
                }
            }
        return false;
    }

    public static boolean findFiveMatch() {
        boolean flag = false;

        if (findFiveMatchColumn()) {
            flag = true;
            Main.score += 5;
        }

        if (findFiveMatchRow()) {
            flag = true;
            Main.score += 5;
        }
        return flag;
    }

    public static boolean findFiveMatchRow() {
        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES - 4; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 3].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 4].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    Main.grid[y][x + 3] = clearTile(Main.grid[y][x + 3]);
                    Main.grid[y][x + 4] = clearTile(Main.grid[y][x + 4]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findFiveMatchColumn() {
        for (int y = 0; y < Main.Y_TILES - 4; y++)
            for (int x = 0; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 3][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 4][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    Main.grid[y + 3][x] = clearTile(Main.grid[y + 3][x]);
                    Main.grid[y + 4][x] = clearTile(Main.grid[y + 4][x]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findLs() {
        boolean flag = false;

        if (findTopLeftL()) {
            flag = true;
            Main.score += 5;
        }

        if (findTopMidL()) {
            flag = true;
            Main.score += 5;
        }

        if (findTopRightL()) {
            flag = true;
            Main.score += 5;
        }

        if (findMidLeftL()) {
            flag = true;
            Main.score += 5;
        }

        if (findMidRightL()) {
            flag = true;
            Main.score += 5;
        }

        if (findBottomLeftL()) {
            flag = true;
            Main.score += 5;
        }

        if (findBottomMidL()) {
            flag = true;
            Main.score += 5;
        }

        if (findBottomLeftR()) {
            flag = true;
            Main.score += 5;
        }

        return flag;
    }

    public static boolean findTopLeftL() {
        /*
            X X X
            X
            X
        */
        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 0; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findTopMidL() {
        /*
            X X X
              X
              X
        */
        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 1; x < Main.X_TILES - 1; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }

            }
        return false;
    }

    public static boolean findTopRightL() {
        /*
            X X X
                X
                X
        */

        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 2; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findMidLeftL() {
        /*
            X
            X X X
            X
        */

        for (int y = 1; y < Main.Y_TILES - 1; y++)
            for (int x = 0; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findMidRightL() {
        /*
                X
            X X X
                X
        */

        for (int y = 1; y < Main.Y_TILES - 1; y++)
            for (int x = 2; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findBottomLeftL() {
        /*
            X
            X
            X X X
        */
        for (int y = 2; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    return true;
                }

            }
        return false;
    }

    public static boolean findBottomMidL() {
        /*
              X
              X
            X X X
        */
        for (int y = 2; y < Main.Y_TILES; y++)
            for (int x = 1; x < Main.X_TILES - 1; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findBottomLeftR() {
        /*
                X
                X
            X X X
        */
        for (int y = 2; y < Main.Y_TILES; y++)
            for (int x = 2; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findFourTs() {
        return findSidewaysFourTs() || findRegularFourTs();
    }

    public static boolean findSidewaysFourTs() {
        boolean flag = false;

        if (findTopLeftSidewaysFourT()) {
            flag = true;
            Main.score += 6;
        }

        if (findTopRightSidewaysFourT()) {
            flag = true;
            Main.score += 6;
        }

        if (findBottomLeftSidewaysFourT()) {
            flag = true;
            Main.score += 6;
        }

        if (findBottomRightSidewaysFourT()) {
            flag = true;
            Main.score += 6;
        }

        return flag;
    }

    public static boolean findTopLeftSidewaysFourT() {
        /*
            X
            X X X
            X
            X
        */

        for (int y = 1; y < Main.Y_TILES - 2; y++)
            for (int x = 0; x < Main.Y_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findTopRightSidewaysFourT() {
        /*
                X
            X X X
                X
                X
        */

        for (int y = 1; y < Main.Y_TILES - 2; y++)
            for (int x = 2; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    return true;

                }
            }
        return false;
    }

    public static boolean findBottomLeftSidewaysFourT() {
        /*
            X
            X
            X X X
            X
        */

        for (int y = 2; y < Main.Y_TILES - 1; y++)
            for (int x = 0; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);

                    return true;
                }
            }
        return false;
    }

    public static boolean findBottomRightSidewaysFourT() {
        /*
                X
                X
            X X X
                X
        */

        for (int y = 2; y < Main.Y_TILES - 1; y++)
            for (int x = 2; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);

                    return true;
                }
            }
        return false;
    }

    public static boolean findRegularFourTs() {
        boolean flag = false;

        if (findTopLeftFourT()) {
            flag = true;
            Main.score += 6;
        }

        if (findTopRightFourT()) {
            flag = true;
            Main.score += 6;
        }

        if (findBottomLeftFourT()) {
            flag = true;
            Main.score += 6;
        }

        if (findBottomRightFourT()) {
            flag = true;
            Main.score += 6;
        }

        return flag;
    }

    public static boolean findTopLeftFourT() {
        /*
            X X X X
              X
              X
        */

        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 1; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findTopRightFourT() {
        /*
            X X X X
                X
                X
        */

        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 2; x < Main.X_TILES - 1; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findBottomLeftFourT() {
        /*
              X
              X
            X X X X
        */

        for (int y = 2; y < Main.Y_TILES; y++)
            for (int x = 1; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findBottomRightFourT() {
         /*
                X
                X
            X X X X
        */

        for (int y = 2; y < Main.Y_TILES; y++)
            for (int x = 2; x < Main.X_TILES - 1; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findFiveTs() {
        return findRegularFiveTs() || findSidewaysFiveTs();
    }

    public static boolean findRegularFiveTs() {
        boolean flag = false;

        if (findTopRegularFiveT()) {
            flag = true;
            System.out.println("findRegularFiveTs()");
            Main.score += 7;
        }

        if (findBottomRegularFiveT()) {
            flag = true;
            System.out.println("findBottomRegularFiveT()");
            Main.score += 7;
        }

        return flag;
    }

    public static boolean findTopRegularFiveT() {
        /*
            X X X X X
                X
                X
        */

        for (int y = 0; y < Main.Y_TILES - 2; y++)
            for (int x = 2; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findBottomRegularFiveT() {
        /*
                X
                X
            X X X X X
        */

        for (int y = 2; y < Main.Y_TILES; y++)
            for (int x = 2; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    return true;
                }
            }
        return false;
    }


    public static boolean findSidewaysFiveTs() {
        boolean flag = false;

        if (findLeftSidewaysFiveT()) {
            flag = true;
            System.out.println("findLeftSidewaysFiveT()");
            Main.score += 7;
        }

        if (findRightSidewaysFiveT()) {
            flag = true;
            System.out.println("findRightSidewaysFiveT()");
            Main.score += 7;
        }

        return flag;
    }

    public static boolean findLeftSidewaysFiveT() {
        /*
           X
           X
           X X X
           X
           X
        */

        for (int y = 2; y < Main.Y_TILES - 2; y++)
            for (int x = 0; x < Main.X_TILES - 2; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x + 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x + 1].token &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x + 2] = clearTile(Main.grid[y][x + 2]);
                    Main.grid[y][x + 1] = clearTile(Main.grid[y][x + 1]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }
            }
        return false;
    }

    public static boolean findRightSidewaysFiveT() {
        /*
                X
                X
            X X X
                X
                X
        */

        for (int y = 2; y < Main.Y_TILES - 2; y++)
            for (int x = 2; x < Main.X_TILES; x++) {
                if (Main.grid[y][x].token != Tile.Token.NULL &&
                        Main.grid[y][x].token == Main.grid[y][x - 2].token &&
                        Main.grid[y][x].token == Main.grid[y][x - 1].token &&
                        Main.grid[y][x].token == Main.grid[y - 2][x].token &&
                        Main.grid[y][x].token == Main.grid[y - 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 1][x].token &&
                        Main.grid[y][x].token == Main.grid[y + 2][x].token) {
                    Main.grid[y][x] = clearTile(Main.grid[y][x]);
                    Main.grid[y][x - 2] = clearTile(Main.grid[y][x - 2]);
                    Main.grid[y][x - 1] = clearTile(Main.grid[y][x - 1]);
                    Main.grid[y - 2][x] = clearTile(Main.grid[y - 2][x]);
                    Main.grid[y - 1][x] = clearTile(Main.grid[y - 1][x]);
                    Main.grid[y + 1][x] = clearTile(Main.grid[y + 1][x]);
                    Main.grid[y + 2][x] = clearTile(Main.grid[y + 2][x]);
                    return true;
                }
            }
        return false;
    }

    public static Tile clearTile(Tile tile) {
        tile.token = Tile.Token.NULL;
        tile.fill.setFill(tile.setColor(tile.token));

        return tile;
    }

    public static ArrayList<Tile> getMatchNeighbors(String direction, Tile target) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        switch (direction.toUpperCase()) {
            case "UP":
                if (validCoordinate(new Coordinate(target.coordinate.y - 1, target.coordinate.x)) && Main.grid[target.coordinate.y + 1][target.coordinate.x].token == target.token) //down
                    neighbors.add(Main.grid[target.coordinate.y + 1][target.coordinate.x]);

                if (validCoordinate(new Coordinate(target.coordinate.y, target.coordinate.x - 1)) && Main.grid[target.coordinate.y][target.coordinate.x - 1].token == target.token) //left
                    neighbors.add(Main.grid[target.coordinate.y][target.coordinate.x - 1]);

                if (validCoordinate(new Coordinate(target.coordinate.y, target.coordinate.x + 1)) && Main.grid[target.coordinate.y][target.coordinate.x + 1].token == target.token) //right
                    neighbors.add(Main.grid[target.coordinate.y][target.coordinate.x + 1]);

                break;

            case "RIGHT":
                if (validCoordinate(new Coordinate(target.coordinate.y - 1, target.coordinate.x)) && Main.grid[target.coordinate.y + 1][target.coordinate.x].token == target.token) //down
                    neighbors.add(Main.grid[target.coordinate.y + 1][target.coordinate.x]);

                if (validCoordinate(new Coordinate(target.coordinate.y, target.coordinate.x - 1)) && Main.grid[target.coordinate.y][target.coordinate.x - 1].token == target.token) //left
                    neighbors.add(Main.grid[target.coordinate.y][target.coordinate.x - 1]);

                if (validCoordinate(new Coordinate(target.coordinate.y - 1, target.coordinate.x)) && Main.grid[target.coordinate.y - 1][target.coordinate.x].token == target.token) //up
                    neighbors.add(Main.grid[target.coordinate.y - 1][target.coordinate.x]);
                break;

            case "DOWN":
                if (validCoordinate(new Coordinate(target.coordinate.y, target.coordinate.x + 1)) && Main.grid[target.coordinate.y][target.coordinate.x + 1].token == target.token) //right
                    neighbors.add(Main.grid[target.coordinate.y][target.coordinate.x + 1]);

                if (validCoordinate(new Coordinate(target.coordinate.y, target.coordinate.x - 1)) && Main.grid[target.coordinate.y][target.coordinate.x - 1].token == target.token) //left
                    neighbors.add(Main.grid[target.coordinate.y][target.coordinate.x - 1]);

                if (validCoordinate(new Coordinate(target.coordinate.y - 1, target.coordinate.x)) && Main.grid[target.coordinate.y - 1][target.coordinate.x].token == target.token) //up
                    neighbors.add(Main.grid[target.coordinate.y - 1][target.coordinate.x]);
                break;

            case "LEFT":
                if (validCoordinate(new Coordinate(target.coordinate.y, target.coordinate.x + 1)) && Main.grid[target.coordinate.y][target.coordinate.x + 1].token == target.token) //right
                    neighbors.add(Main.grid[target.coordinate.y][target.coordinate.x + 1]);

                if (validCoordinate(new Coordinate(target.coordinate.y - 1, target.coordinate.x)) && Main.grid[target.coordinate.y + 1][target.coordinate.x].token == target.token) //down
                    neighbors.add(Main.grid[target.coordinate.y + 1][target.coordinate.x]);

                if (validCoordinate(new Coordinate(target.coordinate.y - 1, target.coordinate.x)) && Main.grid[target.coordinate.y - 1][target.coordinate.x].token == target.token) //up
                    neighbors.add(Main.grid[target.coordinate.y - 1][target.coordinate.x]);
                break;
        }

        return neighbors;
    }

    public static boolean findEmpty()
    {
        boolean flag = false;
        for(int y = Main.Y_TILES - 1; y > -1; y--)
            for(int x = 0; x < Main.X_TILES - 1; x++)
            {
                if(Main.grid[y][x].token == Tile.Token.NULL && y == 0)
                {
                    int rand = (int) (Math.random() * 4);
                    Main.grid[y][x].fill.setFill(Main.grid[y][x].setColor(rand));
                    Main.grid[y][x].token = Main.grid[y][x].setToken(Main.grid[y][x].fill.getFill());
                    flag = true;
                }

                else if(Main.grid[y][x].token == Tile.Token.NULL)
                {
                    swap(Main.grid[y][x], Main.grid[y - 1][x]);
                    flag = true;
                }
            }
        return flag;
    }


    public static void swap(Tile tile1, Tile tile2)
    {
        Tile.Token temp = tile1.token;

        Main.grid[tile1.coordinate.y][tile1.coordinate.x].token = tile2.token;
        Main.grid[tile1.coordinate.y][tile1.coordinate.x].fill.setFill(Main.grid[tile1.coordinate.y][tile1.coordinate.x].setColor(Main.grid[tile1.coordinate.y][tile1.coordinate.x].token));

        Main.grid[tile2.coordinate.y][tile2.coordinate.x].token = temp;
        Main.grid[tile2.coordinate.y][tile2.coordinate.x].fill.setFill(Main.grid[tile2.coordinate.y][tile2.coordinate.x].setColor(Main.grid[tile2.coordinate.y][tile2.coordinate.x].token));

        for (int y = 0; y < Main.Y_TILES; y++)
            for (int x = 0; x < Main.X_TILES; x++)
                Main.grid[y][x].neighbors = Tile.getNeighbors(Main.grid[y][x]);
    }

    public static boolean validCoordinate(Coordinate coordinate)
    {
        return coordinate.y >= 0 && coordinate.y < Main.Y_TILES && coordinate.x >= 0 && coordinate.x < Main.X_TILES;
    }

    public static Tile reRoll(Coordinate coordinate)
    {
        int rand = (int) (Math.random() * 7);
        Tile tile = Main.grid[coordinate.y][coordinate.x];

        tile.fill.setFill(tile.setColor(rand));

        tile.token = tile.setToken(tile.fill.getFill());

        return tile;
    }
}
