package implementations;

import interfaces.Particle;

import java.util.HashSet;
import java.util.Set;

public class ParticleImpl implements Particle {
    private double x;
    private double y;
    private double radius;
    private int id;
    private Set<Particle> neighbours;

    private double velocity;
    private double angle;
    private double newAngle;
    private boolean collided;


    public ParticleImpl(double x, double y, int id, double velocity, double angle) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.neighbours = new HashSet<Particle>();
        this.velocity = velocity;
        this.angle = angle;

    }

    public boolean collided(){
        return collided;
    }

    public void setCollision(boolean collided){
        this.collided = true;
    }

    public void updateEscape(Particle other){

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public double getRadius(){
        return radius;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getNewAngle() {
        return newAngle;
    }

    public void setNewAngle(double newAngle) {
        this.newAngle = newAngle;
    }

    public Set<Particle> getNeighbours() {
        return neighbours;
    }

    public double getXVelocity(){
        return velocity * Math.cos(newAngle);
    }

    public double getYVelocity(){
        return velocity * Math.sin(newAngle);
    }

    public void setNeighbours(Set<Particle> neighbours) {
        this.neighbours = neighbours;
    }

    /* Now considering the border of the particle */
    public double calculateDistance(Particle p) {
        double toReturn = Math.sqrt(Math.pow(p.getX()-getX(),2) + Math.pow(p.getY()-getY(),2)) ;
        return toReturn < 0 ? 0 : toReturn;
    }

    public double calculateDistance(double newX, double newY){
        double toReturn = Math.sqrt(Math.pow(newX-getX(),2) + Math.pow(newY-getY(),2));
        return toReturn < 0 ? 0 : toReturn;
    }

    public double calculatePeriodicDistance(Particle other,double L){
        double distance;
        double xdist = Math.abs(getX()-other.getX());
        double ydist = Math.abs(getY()-other.getY());
        if(xdist > L/2){
            xdist = L-xdist;
        }
        if(ydist >L/2){
            ydist = L-ydist;
        }

        return Math.sqrt(Math.pow(xdist,2) + Math.pow(ydist,2));
    }

    public void calculateNewPosition(double timeUnit, double L){
        x += getXVelocity() * timeUnit;
        y += getYVelocity() * timeUnit;

        if(x > L){
            x -= L;
        }
        else if(x < 0){
            x += L;
        }

        if(y > L){
            y -= L;
        }
        else if(y < 0){
            y += L;
        }
    }

    public boolean collides(Particle p){
        double distance = Math.sqrt(Math.pow(x-p.getX(),2) + Math.pow(y-p.getY(),2));

        return distance - radius - p.getRadius() <=0;

    }
}
