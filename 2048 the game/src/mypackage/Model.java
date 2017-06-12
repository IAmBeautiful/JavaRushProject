package mypackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by DNS on 21.04.2017.
 */
public class Model {

    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    private Stack previousStates = new Stack();
    private Stack previousScores = new Stack();

    private boolean isSaveNeeded = true;

    private Model model;
    private View view;

    protected int score;
    protected int maxTile;

    public void autoMove(){

        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4, Collections.<MoveEfficiency>reverseOrder());

        queue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                left();
            }
        }));

        queue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                right();
            }
        }));

        queue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                up();
            }
        }));

        queue.offer(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                down();
            }
        }));

        queue.peek().getMove().move();
    }

    public boolean hasBoardChanged(){

        int weight1 = 0;
        int weight2 = 0;

        Tile[][] test = (Tile[][]) previousStates.peek();

        for (int i = 0; i < gameTiles.length; i++)
            for (int j = 0; j < gameTiles[i].length; j++){

                weight1 += gameTiles[i][j].value;
                weight2 += test[i][j].value;
            }

        return weight1 != weight2;
    }

    public MoveEfficiency getMoveEfficiency(Move move){

        move.move();
        MoveEfficiency efficiency;

        if (hasBoardChanged()) {
            efficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        }
        else{
            efficiency = new MoveEfficiency(-1, 0, move);
        }

        rollback();
        return efficiency;
    }


    public void randomMove(){

        int n = ((int) (Math.random() * 100)) % 4;

        switch (n){
            case 0: left(); break;
            case 1: right(); break;
            case 2: up(); break;
            case 3: down(); break;
        }
    }


    private void saveState(Tile[][] tiles){

        Tile[][] saveIt = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[i].length; j++)
                saveIt[i][j] = new Tile(tiles[i][j].value);

        previousStates.push(saveIt);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback(){

        if (!previousScores.isEmpty() && !previousStates.isEmpty()) {

            gameTiles = (Tile[][]) previousStates.pop();
            score = (int) previousScores.pop();
        }
    }

    public Model() {
        resetGameTiles();
    }

    public Tile[][] getGameTiles(){
        return gameTiles;
    }

    public int getScore(){
        return score;
    }

    public boolean canMove(){

        Tile[][] test = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for(int i = 0; i < gameTiles.length; i++)
            for (int j = 0; j < gameTiles[i].length; j++)
                test[i][j] = new Tile(gameTiles[i][j].value);


        for (int i = 0; i < 5; i++){

            for (int j = 0; j < test.length; j++){

                if (compressTiles(test[j]))
                    return true;
                if (testMergeTiles(test[j]))
                    return true;
            }
            test = testTurnArray(test);
        }

        return false;
    }

    private Tile[][] testTurnArray(Tile[][] test){

        Tile[][] matrix = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        int cont1 = 0;
        int cont2 = 0;

        for (int j = matrix.length-1; j >= 0; j--){
            for (int i = 0; i < matrix.length; i++){
                matrix[i][j] = test[cont1][cont2++];
            }
            cont1++;
            cont2 = 0;
        }

        return matrix;
    }

    private boolean testMergeTiles(Tile[] tiles) {
        boolean changeTiles = false;

        for (int i = 0; i < tiles.length - 1; i++) {
            int q = tiles[i].value;
            int w = tiles[i + 1].value;

            if (q == w && w != 0) {
                tiles[i].value = q + w;
                tiles[i + 1].value = 0;
                compressTiles(tiles);
                changeTiles = true;
            }
        }
        return changeTiles;
    }


    private void addTile() {
        
        if (getEmptyTiles().size() > 0) {

            int i = (int) (Math.random() * getEmptyTiles().size());

            getEmptyTiles().get(i).setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    private ArrayList<Tile> getEmptyTiles() {

        ArrayList<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].isEmpty())
                    tiles.add(gameTiles[i][j]);
            }
        }

        return tiles;
    }

    protected void resetGameTiles() {

        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        score = 0;
        maxTile = 2;

        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }

        addTile();
        addTile();
    }

    private boolean compressTiles(Tile[] tiles) {

        boolean changeTiles = false;

        for (int j = 0; j < tiles.length; j++) {
            for (int i = 0; i < tiles.length - 1; i++) {

                if (tiles[i].value == 0 && tiles[i + 1].value != 0) {
                    Tile changed = tiles[i];
                    tiles[i] = tiles[i + 1];
                    tiles[i + 1] = changed;
                    changeTiles = true;
                }
            }
        }

        return changeTiles;
    }

    private boolean mergeTiles(Tile[] tiles) {

        boolean changeTiles = false;

            for (int i = 0; i < tiles.length - 1; i++) {

                int q = tiles[i].value;
                int w = tiles[i + 1].value;

                if (q == w && w != 0) {

                    score += q + w;
                    if (maxTile < q + w)
                        maxTile = q + w;

                    tiles[i].value = q + w;
                    tiles[i + 1].value = 0;
                    compressTiles(tiles);

                    changeTiles = true;
                }
            }

            return changeTiles;
    }

    protected void left(){

        if (isSaveNeeded){
            saveState(gameTiles);
        }

        boolean changeTiles = false;

        for (int i = 0; i < gameTiles.length; i++){

            if (compressTiles(gameTiles[i]))
                changeTiles = true;

            if (mergeTiles(gameTiles[i]))
                changeTiles = true;
        }

        if (changeTiles)
            addTile();

        isSaveNeeded = true;
    }

    private void turnArray(){

        Tile[][] matrix = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        int cont1 = 0;
        int cont2 = 0;

        for (int j = matrix.length-1; j >= 0; j--){
            for (int i = 0; i < matrix.length; i++){
                matrix[i][j] = gameTiles[cont1][cont2++];
            }
            cont1++;
            cont2 = 0;
        }

        gameTiles = matrix;
    }

    protected void down(){

        saveState(gameTiles);

        turnArray();
        left();
        turnArray();
        turnArray();
        turnArray();
    }

    protected void up(){

        saveState(gameTiles);

        turnArray();
        turnArray();
        turnArray();
        left();
        turnArray();
    }

    protected void right(){

        saveState(gameTiles);

        turnArray();
        turnArray();
        left();
        turnArray();
        turnArray();
    }

}