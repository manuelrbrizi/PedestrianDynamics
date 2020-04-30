package interfaces;

import java.util.List;
import java.util.Set;

public interface Particle {

    double calculateDistance(Particle p);
    double calculatePeriodicDistance(Particle p, double L);
    double calculateDistance(double newX, double newY);
    double getX();
    double getY();
    double getVelocity();
    double getAngle();
    double getNewAngle();
    void setNewAngle(double newAngle);
    void setAngle(double angle);
    void calculateNewPosition(double timeUnit, double L);
    double getYVelocity();
    double getXVelocity();
    int getId();
    Set<Particle> getNeighbours();
    void setNeighbours(Set<Particle> neighbours);
    double getRadius();
    boolean collides(Particle p);

    boolean collided();
    void setCollision(boolean collided);

    void updateEscape(Particle other);

}
