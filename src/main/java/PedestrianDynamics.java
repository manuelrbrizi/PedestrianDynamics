import implementations.CellImpl;
import implementations.GridImpl;
import interfaces.Cell;
import interfaces.Grid;
import interfaces.Parser;
import interfaces.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PedestrianDynamics {

    public static void main(String args[]){
        System.out.println("HOLA MUNDO!\n");
    }

    private static Grid fillGrid(Parser parser){
        double L = parser.getL();
        double M = parser.getM();
        double Rc = parser.getRc();
        int N = parser.getN();
        Grid grid = new GridImpl(L, M, Rc);
        int xCellPosition = 0, yCellPosition = 0;
        int cellQuantity =  (int) (L/M) * (int)(L/M);
        List<Cell> cellList = new ArrayList<Cell>();
        Cell cell = new CellImpl(0, 0);
        cellList.add(cell);

        for(int i = 1; i < cellQuantity; i++){
            int cellsPerRow = (int) (L/M);
            if(i % cellsPerRow == 0){
                xCellPosition = 0;
                yCellPosition++;
                cell = new CellImpl(xCellPosition, yCellPosition);
                cellList.add(cell);
            }
            else{
                xCellPosition++;
                cell = new CellImpl(xCellPosition, yCellPosition);
                cellList.add(cell);
            }
        }

        List<Particle> particles = parser.getParticles();
        Particle p;
        int cellNumber;

        for(int i = 0; i < N; i++){
            p = particles.get(i);
            cellNumber = calculateCellNumber(p.getX(), p.getY(), M, L);
            cellList.get(cellNumber).addParticle(p);
        }

        grid.setCells(cellList);
        return grid;
    }

    private static int calculateCellNumber(double x, double y, double M, double L){
        return (int) (Math.min(Math.floor(x/M), (L/M)-1) + (int)(L/M) * Math.min(Math.floor(y/M), (L/M)-1));
    }

    private static void cellIndexMethod(Grid grid){
        for(Cell c : grid.getCells()){
            for(Particle p : c.getParticles()){
                getNeighbours(p,c.getX(),c.getY(),grid);
                getNeighbours(p,c.getX(),c.getY()+1,grid);
                getNeighbours(p,c.getX()+1,c.getY()+1,grid);
                getNeighbours(p,c.getX()+1,c.getY(),grid);
                getNeighbours(p,c.getX()+1,c.getY()-1,grid);
            }
        }
    }

    private static void getNeighbours(Particle p, int x, int y, Grid grid) {
        int cellsPerRow = (int) (grid.getL()/grid.getM());

        if(y==cellsPerRow){
            y = 0;
        }
        else if(y==-1){
            y += cellsPerRow;
        }

        if(x == cellsPerRow){
            x = 0;
        }

        Cell c = grid.getCells().get((int)(x+y*cellsPerRow));

        for(Particle other : c.getParticles()){
            if(p.getId() != other.getId() && p.calculatePeriodicDistance(other,grid.getL()) < grid.getRc()){
                p.getNeighbours().add(other);
                other.getNeighbours().add(p);
            }
        }
    }

    private static void testGridCreation(Grid grid){
        Cell c;
        Particle p;

        for(int i = 0; i < grid.getCells().size(); i++){
            c = grid.getCells().get(i);
            for(int j = 0; j < c.getParticles().size(); j++){
                p = c.getParticles().get(j);
                System.out.println(String.format("(%f, %f) ID = %d - CELL = %d, NEIGH = %d\n", p.getX(), p.getY(), p.getId(), i, p.getNeighbours().size()));
            }
        }
    }

    private static void generateOvitoFile(Grid grid){
        StringBuilder sb = new StringBuilder();
        sb.append(grid.getParticles().size());
        sb.append("\n");
        sb.append("\n");

        for (Particle p: grid.getParticles()){
            sb.append(p.getX());
            sb.append("\t");
            sb.append(p.getY());
            sb.append("\t");
            sb.append(p.getXVelocity());
            sb.append("\t");
            sb.append(p.getYVelocity());
            sb.append("\t");
            sb.append(0.03);
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

    private static void generateInputFile(int quantity, double L, double Rc, double Nu, double vel){
        StringBuilder sb = new StringBuilder();
        sb.append(quantity);
        sb.append("\n");
        sb.append(L);
        sb.append("\n");
        sb.append(Rc);
        sb.append("\n");
        sb.append(Nu);
        sb.append("\n");
        Random rand = new Random();
        for (int i = 0; i < quantity; i++){
            sb.append(rand.nextDouble()*L);
            sb.append(" ");
            sb.append(rand.nextDouble()*L);
            sb.append(" ");
            sb.append(vel);
            sb.append(" ");
            sb.append(rand.nextDouble()* Math.PI*2);
            sb.append("\n");
        }

        try {
            FileWriter myWriter = new FileWriter("input.txt");
            myWriter.write(sb.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}