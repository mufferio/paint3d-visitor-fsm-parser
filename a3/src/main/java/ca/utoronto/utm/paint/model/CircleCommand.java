package ca.utoronto.utm.paint.model;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleCommand extends PaintCommand {
	private Point centre;
	private int radius;
	
	public CircleCommand(Point centre, int radius){
		this.centre = centre;
		this.radius = radius;
	}
	public Point getCentre() { return centre; }
	public void setCentre(Point centre) { 
		this.centre = centre; 
		this.setChanged();
		this.notifyObservers();
	}
	public int getRadius() { return radius; }
	public void setRadius(int radius) { 
		this.radius = radius; 
		this.setChanged();
		this.notifyObservers();
	}
    /**
     * Converts CircleCommand to the desired string format
     */
    @Override
    public String toString(){
        int r = (int) Math.round(this.getColor().getRed()* 255);
        int g = (int) Math.round(this.getColor().getGreen() * 255);
        int b = (int) Math.round(this.getColor().getBlue() * 255);

        String s = "Circle\n";
        s+="\tcolor:"+r+","+g+","+b+"\n";
        s+="\tfilled:"+this.isFill()+"\n";
        s+="\tCenter:("+this.getCentre().x + ", " + this.getCentre().y+")\n";
        s+="\tRadius:"+this.getRadius()+"\n";
        s+="End Circle";
        return s;
    }

	public void execute(GraphicsContext g){
		int x = this.getCentre().x;
		int y = this.getCentre().y;
		int radius = this.getRadius();
		if(this.isFill()){
			g.setFill(this.getColor());
			g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		} else {
			g.setStroke(this.getColor());
			g.strokeOval(x-radius, y-radius, 2*radius, 2*radius);
		}
	}
}
