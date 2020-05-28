package interfaces;

import implementations.Vector;

public interface Avoider {

    void updateVelocity();

    void updatePosition(double dt, double vd);

    void calculateForce(Avoider other);

    boolean collides(Avoider other);

    void calculateWallForce(Wall wall);

    Vector getPosition();
    Vector getTarget();

    int getId();
    double getRadius();
}
