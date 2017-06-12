package mypackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DNS on 22.02.2017.
 */
public class Snake {
    private List<SnakeSection> sections;
    private boolean isAlive;
    private SnakeDirection direction;

    public void checkBody(SnakeSection head){
        if (sections.contains(head))
            isAlive = false;
    }

    public void checkBorders(SnakeSection head){
        if (head.getY() > Room.game.getHeight()-1 || head.getX() > Room.game.getWidth()-1 || head.getY() < 0 || head.getX() < 0)
            isAlive = false;
    }

    public List<SnakeSection> getSections(){
        return sections;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public SnakeDirection getDirection(){
        return direction;
    }

    public void setDirection(SnakeDirection a){
        direction = a;
    }

    public Snake(int x, int y){
        SnakeSection a = new SnakeSection(x, y);
        sections = new ArrayList<>();
        sections.add(a);
        isAlive = true;
    }

    public int getX(){
        return sections.get(0).getX();
    }

    public int getY(){
        return sections.get(0).getY();
    }

    public void move(){
        if (isAlive == false)
            return;
        if (direction == SnakeDirection.UP)
            move(0, -1);
        else if (direction == SnakeDirection.RIGHT)
            move(1, 0);
        else if (direction == SnakeDirection.DOWN)
            move(0, 1);
        else if (direction == SnakeDirection.LEFT)
            move(-1, 0);
    }

    public void move(int a, int b){
        SnakeSection go = new SnakeSection(sections.get(0).getX() + a, sections.get(0).getY() + b);
        checkBorders(go);
        checkBody(go);
        if (isAlive)
        sections.add(0, go);
        else
            return;
        if (!(go.getX() == Room.game.getMouse().getX() && go.getY() == Room.game.getMouse().getY()))
        sections.remove(sections.size()-1);
        else
            Room.game.eatMouse();
    }
}