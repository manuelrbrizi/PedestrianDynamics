import implementations.*;
import interfaces.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AvoidDynamics {

    public static void main(String args[]){

        PrintWriter writer = null;

        try {
            writer = new PrintWriter("outputOVITO.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();

        double dt = 0.05;


        List<Avoider> pedestrians = new ArrayList<Avoider>();
        pedestrians.add(new AvoiderImpl(1,new Vector(0,50),new Vector(0,0)));
        pedestrians.add(new AvoiderImpl(2,new Vector(0,0),new Vector(0,50)));
        pedestrians.add(new AvoiderImpl(3,new Vector(0,5),new Vector(0,50)));
        pedestrians.add(new AvoiderImpl(4,new Vector(0,10),new Vector(0,50)));

//        pedestrians.add(new AvoiderImpl(1,new Vector(50,50),new Vector(0,0)));
//        pedestrians.add(new AvoiderImpl(2,new Vector(0,0),new Vector(50,50)));
//        pedestrians.add(new AvoiderImpl(3,new Vector(0,50),new Vector(50,0)));
//        pedestrians.add(new AvoiderImpl(4,new Vector(50,0),new Vector(0,50)));


        List<Wall> walls = new ArrayList<Wall>();
        walls.add(new WallImpl(new Vector(-1,50),new Vector(-1,0)));
        walls.add(new WallImpl(new Vector(1,50),new Vector(1,0)));



        double vmax = 0.95;

        generateOvitoFile(pedestrians,walls);

        double time=0;
        int i = 0;
        while(time<= 100){
            for(Avoider p : pedestrians){
                for (Avoider other : pedestrians){
                    if(!p.equals(other) && p.collides(other) ){
                        System.out.printf("%d choco con %d\n",p.getId(),other.getId());
                    }
                    if(p.getId()==4){
                        System.out.println(1-p.getPosition().x-p.getRadius());
                    }
                }
            }
            i++;


            for (Avoider p : pedestrians){
                p.updateVelocity();
            }

            for(Avoider p : pedestrians){
                for(Avoider other : pedestrians){
                    if(!p.equals(other)){
                        p.calculateForce(other);
                    }
                }
            }

            for(Avoider p: pedestrians){
                for (Wall w : walls){
                    p.calculateWallForce(w);
                }
            }


            for (Avoider p : pedestrians){
                p.updatePosition(dt,vmax);
            }


            time+=dt;
            i++;
            generateOvitoFile(pedestrians,walls);
        }

    }

    private static void generateOvitoFile(List<Avoider> pedestrians,List<Wall> walls){
        StringBuilder sb = new StringBuilder();
        sb.append(pedestrians.size()+2*walls.size());
        sb.append("\n");
        sb.append("\n");

        for (Avoider p: pedestrians){
            sb.append(p.getPosition().x);
            sb.append("\t");
            sb.append(p.getPosition().y);
            sb.append("\t");
            sb.append(p.getRadius());
            sb.append("\n");
        }
        for (Wall w: walls){
            sb.append(w.getP1().x);
            sb.append("\t");
            sb.append(w.getP1().y);
            sb.append("\t");
            sb.append(0.4);
            sb.append("\n");
            sb.append(w.getP2().x);
            sb.append("\t");
            sb.append(w.getP2().y);
            sb.append("\t");
            sb.append(0.4);
            sb.append("\n");
        }


        try {
            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(new FileWriter("outputOVITO.txt", true));
            out.write(sb.toString());
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

}