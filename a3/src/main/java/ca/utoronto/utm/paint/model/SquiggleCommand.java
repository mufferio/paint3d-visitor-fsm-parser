package ca.utoronto.utm.paint.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class SquiggleCommand extends PaintCommand {
	private final ArrayList<Point> points=new ArrayList<Point>();


	public void add(Point p){ 
		this.points.add(p); 
		this.setChanged();
		this.notifyObservers();
	}
	public ArrayList<Point> getPoints(){ return this.points; }

    /**
     * Converts SquiggleCommand to the desired string format
     */
    @Override
    public String toString(){
        int r = (int) Math.round(this.getColor().getRed()* 255);
        int g = (int) Math.round(this.getColor().getGreen() * 255);
        int b = (int) Math.round(this.getColor().getBlue() * 255);

        String s="Squiggle\n";
        s+="\tcolor:"+r+","+g+","+b+"\n";
        s+="\tfilled:"+this.isFill()+"\n";
        s+="\tpoints\n";
        for (Point p : this.points){
            s+="\t\tpoint:("+p.x+","+p.y+")\n";
        }
        s+="\tend points\n";
        s+="End Squiggle";
        return s;
    }
	
	@Override
	public void execute(GraphicsContext g) {

		ArrayList<Point> points = this.getPoints();
		g.setStroke(this.getColor());
		for(int i=0;i<points.size()-1;i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
		
	}
}
