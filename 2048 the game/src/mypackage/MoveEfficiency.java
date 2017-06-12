package mypackage;

/**
 * Created by DNS on 23.04.2017.
 */
public class MoveEfficiency implements Comparable<MoveEfficiency> {

    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {

        if (numberOfEmptyTiles != o.numberOfEmptyTiles)
            return numberOfEmptyTiles > o.numberOfEmptyTiles ? 1 : -1;

        else if (score != o.score)
            return score > o.score ? 1 : -1;

        return 0;
    }
}
