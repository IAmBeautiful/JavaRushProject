package mypackage;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by DNS on 21.04.2017.
 */
public class Controller extends KeyAdapter {

    Model model = new Model();
    View view;
    private final static int WINNING_TILE = 2048;

    public View getView() {
        return view;
    }

    public Tile[][] getGameTiles(){
        return model.getGameTiles();
    }

    public int getScore(){
        return model.score;
    }

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    public void resetGame(){

        model.score = 0;
        view.isGameLost = false;
        view.isGameWon = false;
        model.resetGameTiles();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent){

        if (keyEvent.getKeyCode() == keyEvent.VK_ESCAPE)
            resetGame();

        if (!model.canMove())
            view.isGameLost = true;

        if (!view.isGameWon && !view.isGameLost) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT)
                model.left();
            else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
                model.right();
            else if (keyEvent.getKeyCode() == KeyEvent.VK_UP)
                model.up();
            else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN)
                model.down();
            else if (keyEvent.getKeyCode() == KeyEvent.VK_Z)
                model.rollback();
            else if (keyEvent.getKeyCode() == KeyEvent.VK_R)
                model.randomMove();
            else if (keyEvent.getKeyCode() == KeyEvent.VK_A)
                model.autoMove();
        }

        if (model.maxTile == WINNING_TILE)
            view.isGameWon = true;

        view.repaint();
    }
}
