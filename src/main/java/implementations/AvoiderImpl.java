package implementations;

import interfaces.Avoider;
import interfaces.Pedestrian;
import interfaces.Wall;

public class AvoiderImpl implements Avoider {


    Vector velocity;
    Vector position;
    Vector myTarget;
    double radius;
    int id;


    public AvoiderImpl(int id,Vector position, Vector myTarget){
        this.id = id;
        this.position = position;
        this.myTarget = myTarget;
        radius = 0.3;

        velocity = new Vector(0,0);
    }

    public void updateVelocity() {
        velocity = myTarget.substract(position).divide(myTarget.substract(position).length());
    }

    public void updatePosition(double dt, double vd) {

        position = position.getAdded(velocity.multiply(dt*vd));

        velocity = new Vector(0,0);

    }


    public void calculateForce(Avoider other) {
        double A = 2, Bp = 1;
        Vector otherPosition = other.getPosition();

        double angle;
        double mod;

        angle =  Math.atan2(position.x*otherPosition.y-position.y*otherPosition.x,position.x*otherPosition.x+position.y*otherPosition.y);

        mod = A*Math.exp(-(position.distance(otherPosition)-radius-other.getRadius())/Bp)*Math.cos(angle);

        Vector newVel = (position.substract(otherPosition).divide(position.distance(otherPosition))).multiply(mod).perp();

        velocity = velocity.getAdded(newVel);

        velocity = velocity.divide(velocity.length());

    }

    public void calculateWallForce(Wall wall){
        double A = 5, Bp = 0.01;
        Vector otherPosition = wall.getMinPoint(this);

        double angle;
        double mod;

        angle =  Math.atan2(position.x*otherPosition.y-position.y*otherPosition.x,position.x*otherPosition.x+position.y*otherPosition.y);

        mod = A*Math.exp(-(position.distance(otherPosition)-radius)/Bp)*Math.cos(angle);

        Vector newVel = (position.substract(otherPosition).divide(position.distance(otherPosition))).multiply(mod).perp();

        velocity = velocity.getAdded(newVel);

        velocity = velocity.divide(velocity.length());
    }


    public  Vector getPosition(){
        return position;
    }


    public boolean collides(Avoider other) {
        return (position.distance(other.getPosition()) - radius - other.getRadius()) <=0;
    }

    public int getId(){
        return id;
    }

    public double getRadius(){
        return radius;
    }


}
