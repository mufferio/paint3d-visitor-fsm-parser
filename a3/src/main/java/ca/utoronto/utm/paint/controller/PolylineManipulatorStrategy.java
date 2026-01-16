package ca.utoronto.utm.paint.controller;

import ca.utoronto.utm.paint.model.PaintModel;
import ca.utoronto.utm.paint.model.Point;
import ca.utoronto.utm.paint.model.PolylineCommand;
import javafx.scene.input.MouseEvent;

public class PolylineManipulatorStrategy extends ShapeManipulatorStrategy {


    PolylineManipulatorStrategy(PaintModel paintModel) {
        super(paintModel);
    }

    private PolylineCommand polylineCommand;


    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isSecondaryButtonDown()) {
            if (polylineCommand != null && polylineCommand.isDrawingPolyline()) {
                polylineCommand.endPolyline();
            }
            polylineCommand = null;
            return;
        }

        if (polylineCommand == null || !polylineCommand.isDrawingPolyline()) {
            this.polylineCommand = new PolylineCommand();


            this.addCommand(polylineCommand);
            polylineCommand.setDrawingPolyline(true);
        }
        int x = (int) e.getX();
        int y = (int) e.getY();
        polylineCommand.add(new Point(x, y));
        polylineCommand.setHoverPoint(new Point(x, y));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updateHover(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        updateHover(e);
    }

    private void updateHover(MouseEvent e) {
        if (polylineCommand != null && polylineCommand.isDrawingPolyline()) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            polylineCommand.setHoverPoint(new Point(x, y));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // no-op
    }
}
