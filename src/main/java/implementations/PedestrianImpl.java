package implementations;

import interfaces.Pedestrian;

public class PedestrianImpl implements Pedestrian {
    private Vector position;
    private Vector velocity;
    private double vd;
    private Vector escapeVel;
    private double radius;
    private boolean collided;
    private boolean targetReached;


    public  PedestrianImpl(Vector position, double radius){
        this.position = position;
        this.radius = radius;
        this.escapeVel = new Vector(0,0);
        this.collided = false;
        this.targetReached = false;
    }

    public boolean collides(Pedestrian other) {
        return (position.distance(other.getPosition()) - radius - other.getRadius()) <=0;
    }

    public void updateEscape(Pedestrian other, double rmin) {
        collided = true;
        radius = rmin;
        Vector en = (position.substract(other.getPosition()).divide(position.substract(other.getPosition()).length()));
        escapeVel = escapeVel.getAdded(en);
    }

    public void updateRadius(double rmax, double tao, double dt) {
        radius += (rmax*dt)/tao;
        if(radius>rmax){
            radius = rmax;
        }
    }

    public void updateVelocity(double rmin, double rmax, double beta, double vmax, Vector target) {
        double a = (radius-rmin)/(rmax-rmin);
        vd = vmax*Math.pow(a,1);

        Vector en = (target.substract(position).divide(target.substract(position).length()));


        velocity = en;
    }

    public void updatePosition(double dt,double vmax, Vector target) {
        Vector vel;
        double v;
        if(collided){
            double mod = escapeVel.distance(new Vector(0,0));
            vel = escapeVel.divide(mod);
            v = vmax;
        }
        else{
            vel = velocity;
            v = vd;
        }

        position = position.getAdded(vel.multiply(dt*v));


        if(!targetReached && (position.distance(target)-radius) <=0) {
            targetReached = true;
        }
        collided = false;

    }

    public void wallCollision(double rmin) {
        if(position.y-radius<=0){

            Vector wall = new Vector(position.x,0);
            collided = true;
            radius = rmin;
            Vector en = (position.substract(wall).divide(position.substract(wall).length()));
            escapeVel = escapeVel.getAdded(en);
        }
    }

    public Vector getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public boolean targetReached() {
        return targetReached;
    }
}
