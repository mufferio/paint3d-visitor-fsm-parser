package ca.utoronto.utm.paint.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PolylineCommand extends PaintCommand {
    private final ArrayList<Point> points=new ArrayList<Point>();
    private boolean drawingPolyline = false;
    private Point hoverPoint = null;


    public void add(Point p){
        this.points.add(p);
        this.setChanged();
        this.notifyObservers();
    }
    public ArrayList<Point> getPoints(){ return this.points; }


    /**
     * Enables or disables polyline drawing mode.
     * @param drawingPolyline true to enable, false to disable
     */
    public void setDrawingPolyline(boolean drawingPolyline) {
        this.drawingPolyline = drawingPolyline;
    }

    /**
     * @return whether a polyline is currently being drawn
     */
    public boolean isDrawingPolyline() {
        return this.drawingPolyline;
    }

    /**
     * Converts PolylineCommand to the desired string format
     */
    @Override
    public String toString(){
        int r = (int) Math.round(this.getColor().getRed()* 255);
        int g = (int) Math.round(this.getColor().getGreen() * 255);
        int b = (int) Math.round(this.getColor().getBlue() * 255);

        String s="Polyline\n";
        s+="\tcolor:"+r+","+g+","+b+"\n";
        s+="\tfilled:"+this.isFill()+"\n";
        s+="\tpoints\n";
        for (Point p : this.points){
            s+="\t\tpoint:("+p.x+","+p.y+")\n";
        }
        s+="\tend points\n";
        s+="End Polyline";
        return s;
    }

    /**
     * set Point p on mouse to activate line hover
     * @param p
     */
    public void setHoverPoint(Point p) {
        if (!drawingPolyline) return;
        this.hoverPoint = p;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * removes hover point
     */
    public void clearHoverPoint() {
        this.hoverPoint = null;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Ends polyline drawing mode by removing the temporary final point and notifying observers.
     */
    public void endPolyline() {
        if (this.isDrawingPolyline()) {
            this.setDrawingPolyline(false);
            clearHoverPoint();
        }
    }

    @Override
    public void execute(GraphicsContext g) {
        ArrayList<Point> points = this.getPoints();
        for (int i = 1; i < points.size(); i++) {
            Point last = points.get(i - 1);
            Point current = points.get(i);

            // add color and thickness here setStroke setLineWidth
            g.setStroke(this.getColor());
            g.strokeLine(last.x, last.y, current.x, current.y);
        }

        if (hoverPoint != null && !points.isEmpty()) {
            Point last = points.get(points.size() - 1);
            g.strokeLine(last.x, last.y, hoverPoint.x, hoverPoint.y);
        }

    }
}