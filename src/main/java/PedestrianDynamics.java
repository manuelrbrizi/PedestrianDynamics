import implementations.*;
import interfaces.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PedestrianDynamics {

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


        List<Pedestrian> pedestrians = new ArrayList<Pedestrian>();
        Vector base = new Vector(5,5);
        Random random = new Random(System.currentTimeMillis());

        /*for (int i = 0; i<10;i++){
            for(int j = 0; j<10;j++){
                Pedestrian p1 = new PedestrianImpl(base.getAdded(new Vector(i,random.nextDouble())),0.10);
                pedestrians.add(p1);
            }
        }*/

        pedestrians.add(new PedestrianImpl(base.getAdded(new Vector(1,1)), 0.10, 1));
        pedestrians.add(new PedestrianImpl(base.getAdded(new Vector(6,6)), 0.10, 2));

        double rmax = 0.37;
        double rmin = 0.10;
        double vmax = 0.95;
        double tao = 0.5;
        double beta = 0.9;

        Vector door = new Vector(10,0);
        Vector secondary = new Vector(10,-100);
        generateOvitoFile(pedestrians);

        double time=0;
        int i = 0;
        while(time<= 100){
            for(Pedestrian p : pedestrians){
                p.wallCollision(rmin);
                for (Pedestrian other : pedestrians){
                    if(!p.equals(other) && p.collides(other) ){
                        p.updateEscape(other, rmin);
                        other.updateEscape(p, rmin);
                        //other.clear();
                        System.out.println("CHOQUE");
                    }
                }
            }

            for (Pedestrian p : pedestrians){
                if(p.getId() == 1){
                    p.updateVelocity(rmin, rmax, beta, vmax, base.getAdded(new Vector(18, 18)));
                }
                else {
                    p.updateVelocity(rmin, rmax, beta, vmax, base.getAdded(new Vector(-10, -10)));
                }
            }

            for(Pedestrian p : pedestrians){
                for(Pedestrian other : pedestrians){
                    if(!p.equals(other) && p.getId() != 2){
                        p.calculateForce(other.getPosition(), base.getAdded(new Vector(18, 18)));
                    }
                    else if(!p.equals(other)){
                        p.calculateForce(other.getPosition(), base.getAdded(new Vector(-10, -10)));

                    }
                }
            }

            for(Pedestrian p : pedestrians){
//                if(p.getId() == 1){
                    p.updateRadius(rmax, tao, dt);
//                }
            }



            for (Pedestrian p : pedestrians){
//                if(p.getId() == 1){
                    p.updatePosition(dt, vmax, door);
//                }
            }


            time+=dt;
            i++;
            generateOvitoFile(pedestrians);
        }

    }






    private static void generateOvitoFile(List<Pedestrian> pedestrians){
        StringBuilder sb = new StringBuilder();
        sb.append(pedestrians.size()+2);
        sb.append("\n");
        sb.append("\n");

        for (Pedestrian p: pedestrians){
            sb.append(p.getPosition().x);
            sb.append("\t");
            sb.append(p.getPosition().y);
            sb.append("\t");
            sb.append(p.getRadius());
            sb.append("\n");
        }

        sb.append(9.3);
        sb.append("\t");
        sb.append(0);
        sb.append("\t");
        sb.append(0.1);
        sb.append("\n");

        sb.append(10.7);
        sb.append("\t");
        sb.append(0);
        sb.append("\t");
        sb.append(0.1);
        sb.append("\n");

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