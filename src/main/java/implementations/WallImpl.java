package implementations;

import interfaces.Avoider;
import interfaces.Wall;

public class WallImpl implements Wall {

    private Vector p1;
    private Vector p2;

    public WallImpl(Vector p1, Vector p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public Vector getMinPoint(Avoider avoider){
        Vector pos = avoider.getPosition();

        if(p1.y < pos.y){
            return p1;
        }
        else if(p2.y > pos.y){
            return p2;
        }
        else {
            return new Vector(p1.x,pos.y);
        }

    }

    public Vector getP1() {
        return p1;
    }

    public Vector getP2() {
        return p2;
    }

}
