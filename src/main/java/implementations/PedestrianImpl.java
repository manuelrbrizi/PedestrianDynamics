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
    public int id;


    public int getId() {
        return id;
    }

    public  PedestrianImpl(Vector position, double radius, int id){
        this.position = position;
        this.radius = radius;
        this.escapeVel = new Vector(0,0);
        this.collided = false;
        this.targetReached = false;
        this.id = id;
    }

    public boolean collides(Pedestrian other) {
        return (position.distance(other.getPosition()) - radius - other.getRadius()) <=0;
    }

    public void updateEscape(Pedestrian other, double rmin) {
        collided = true;
        radius = rmin;
        Vector en = (position.substract(other.getPosition()).divide(position.distance(other.getPosition())));
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


        velocity = target.substract(position).divide(target.substract(position).length());
    }

    public void updatePosition(double dt,double vmax, Vector target) {
        Vector vel;
        double v;
        if(collided){
            double mod = escapeVel.length();
            vel = escapeVel.divide(mod);
            v = vmax;
        }
        else{
            vel = velocity;
            v = vd;
        }

        position = position.getAdded(vel.multiply(dt*v));


        if(!targetReached && crossDoor(target)) {
            targetReached = true;
        }
        if(!targetReached && position.y<0){
            position.y = radius;
        }
        collided = false;
        escapeVel = new Vector(0,0);
        velocity = new Vector(0,0);

    }

    private boolean crossDoor(Vector target) {
        if(Math.abs(target.x-position.x)<Math.abs(radius-0.6)){
            if(Math.abs(target.y-position.y)<radius){
                return true;
            }
        }

        return false;
    }

    public void wallCollision(double rmin) {

        if((position.y-radius)<=0 && !targetReached){

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

    public Vector getVelocity() {
        return escapeVel;
    }

    public void calculateForce(Vector otherPosition, Vector target){
        double A = 2000.0, Bp = 0.08, Bw = 0.04;

        Vector toTarget = target.substract(position).divide(target.substract(position)
                .length());
        Vector toParticle = otherPosition.substract(position).divide(otherPosition
                .substract(position).length());

        double angle = toParticle.angle() - toTarget.angle();
        double mod;

        if((position.y-radius)<=0 && !targetReached){
            mod = A*Math.exp(-position.distance(otherPosition)/Bw)*Math.cos(angle);
        }
        else{
            mod = A*Math.exp(-position.distance(otherPosition)/Bp)*Math.cos(angle);
        }

        Vector newVel = (position.substract(otherPosition).divide(position.
                distance(otherPosition))).multiply(mod).perp();

        System.out.printf("NEW_VEL_X_BEF = %f, NEW_VEL_Y_BEF = %f\n", newVel.x, newVel.y);
        System.out.printf("VEL_X_BEF = %f, VEL_Y_BEF = %f\n", velocity.x, velocity.y);
        velocity = velocity.getAdded(newVel);
        System.out.printf("VEL_X_AFT = %f, VEL_Y_AFT = %f\n", velocity.x, velocity.y);
    }
}
