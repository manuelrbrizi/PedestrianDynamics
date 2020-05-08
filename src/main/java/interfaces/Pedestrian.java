package interfaces;

import implementations.Vector;


public interface Pedestrian {


    boolean collides(Pedestrian other);

    void updateEscape(Pedestrian other, double rmin);

    void updateRadius(double rmax, double tao, double dt);

    void updateVelocity(double rmin, double rmax, double beta, double vmax, Vector target);

    void updatePosition(double dt, double vmax, Vector target);

    void wallCollision(double rmin);

    Vector getPosition();
    double getRadius();
    boolean targetReached();
    Vector getVelocity();
    int getId();

    void calculateForce(Vector otherPosition, Vector target);
    void clear();
}
