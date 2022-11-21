import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
// import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
// import java.awt.geom.*;
import java.text.DecimalFormat;;

public class MyGraphicsDisplay extends JPanel{

    private Double[][] graphicsData;

    private boolean showAxis = true;
    private boolean showDots = true;
    private boolean showIntegrals = false;
    private boolean showRotate = false;

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private double scale;

    private BasicStroke axisStroke;
    private BasicStroke graphicsStroke;
    private BasicStroke markerStroke;
    
    private Font axisFont;

    public MyGraphicsDisplay(){
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(
            2.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND,
            10.0f,
            new float[] {4*6,1*6,1*6,1*6,1*6,1*6,2*6,1*6,2*6},
            0.0f
        );
        axisStroke = new BasicStroke(
            2.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f,
            null,
            0.0f
        );
        markerStroke = new BasicStroke(
            1f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f,
            null,
            0.0f
        );
        axisFont = new Font("Serif", Font.BOLD, 36);
        // squareFont = new Font("Calibri", Font.BOLD, 36);
    }

    public void showGraphics(Double[][] graphicsData){
        this.graphicsData = graphicsData;
        repaint();
    }

    public void setShowAxis(boolean showAxis){
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowDots(boolean showDots){
        this.showDots = showDots;
        repaint();
    }

    public void setShowIntegrals(boolean showIntegrals){
        this.showIntegrals = showIntegrals;
        repaint();
    }

    public void setShowRotate(boolean showRotate){
        this.showRotate = showRotate;
        repaint();
    }
    
    protected void paintGraphics(Graphics2D canvas){
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);
        GeneralPath graphics = new GeneralPath();

        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }

        canvas.draw(graphics);
    }

    protected void paintAxis(Graphics2D canvas){
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();

        System.out.println("minX:  " + minX);
        System.out.println("minY:  " + minY);
        System.out.println("maxX:  " + maxX);
        System.out.println("maxY:  " + maxY);

        Double beginOfY = .0;
        Double endOfY = .0;
        int dirY = 0;
        Double beginOfX = .0;
        Double endOfX = .0;
        int dirX = 0;
        if(!showRotate){
            beginOfY = maxY;
            endOfY = minY;
            dirY = 1;
            
            beginOfX = maxX;
            endOfX = minX;
            dirX = 2;
        }else{
            beginOfY = -minX;
            endOfY = -getSize().getWidth();
            dirY = 4;

            beginOfX = maxY;
            endOfX = minY;
            dirX = 1;
        }

        if(minX <= 0.0 && maxX >= 0.0){
            // canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            canvas.draw(new Line2D.Double(xyToPoint(0, beginOfY), xyToPoint(0, endOfY)));
            // canvas.draw(new Line2D.Double(xyToPoint(5, 13), xyToPoint(5, 1)));
            // canvas.draw(new Line2D.Double(xyToPoint(0, -minX), xyToPoint(0, maxX)));
            Point2D.Double lineEnd = xyToPoint(0, beginOfY);
            drawArrow(canvas, lineEnd, dirY);
            
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, beginOfY);

            canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
        }
        
        if(minY <= 0.0 && maxY >= 0){
            // canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            canvas.draw(new Line2D.Double(xyToPoint(beginOfX, 0), xyToPoint(endOfX, 0)));

            Point2D.Double lineEnd = xyToPoint(beginOfX, 0);
            drawArrow(canvas, lineEnd, dirX);
            // arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            // arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY()-5);
            // arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            // arrow.closePath();

            // canvas.draw(arrow);
            // canvas.fill(arrow);

            // Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(beginOfX, 0);

            canvas.drawString("x", (float)labelPos.getX() - 25, (float)labelPos.getY() + 25);
            // canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
            // canvas.drawString("x", (float)(labelPos.getX() - bounds.getWidth() - 10),(float) (labelPos.getY() + bounds.getY()));
        }
    }

    private void drawArrow(Graphics2D canvas, Point2D.Double lineEnd, int direction){
        GeneralPath arrow = new GeneralPath();
        arrow.moveTo(lineEnd.getX(), lineEnd.getY());
        
        if(direction == 4){
            arrow.lineTo(arrow.getCurrentPoint().getX() + 20, arrow.getCurrentPoint().getY() + 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() - 10);    
        }
        if(direction == 1){
            arrow.lineTo(arrow.getCurrentPoint().getX() - 5, arrow.getCurrentPoint().getY() +20);
            arrow.lineTo(arrow.getCurrentPoint().getX() + 10, arrow.getCurrentPoint().getY());
        }
        if(direction == 2){
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() + 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() - 10);    
        }

        arrow.closePath();
        canvas.draw(arrow);
        canvas.fill(arrow);
    }

    protected void paintDots(Graphics2D canvas){
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.BLACK);
        // canvas.setPaint(Color.RED);
        GeneralPath dot = new GeneralPath();

        for(int i = 0; i < graphicsData.length; i++){
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);

            dot.moveTo(point.getX() + 5.5, point.getY());
            dot.lineTo(point.getX() - 5.5, point.getY());
            
            dot.moveTo(point.getX(), point.getY() + 5.5);
            dot.lineTo(point.getX(), point.getY() - 5.5);


            dot.moveTo(point.getX() - 5.5, point.getY() - 5.5);
            dot.lineTo(point.getX() + 5.5, point.getY() + 5.5);

            dot.moveTo(point.getX() - 5.5, point.getY() + 5.5);
            dot.lineTo(point.getX() + 5.5, point.getY() - 5.5);
        }
        canvas.draw(dot);
    }
    
    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX;
        double deltaY;
        if(!showRotate){
            deltaX = x - minX;
            deltaY = maxY - y;
        }else{
            deltaX = (-1)*y - minX;
            deltaY = maxY - x;
        }
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }

    // protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
    //     Point2D.Double dest = new Point2D.Double();
        
    //     dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
    //     return dest;
    // }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (graphicsData==null || graphicsData.length==0)
            return;
        // for(int i = 0; i < graphicsData.length; i++){
        //     System.out.println(graphicsData[i][0] + "  " + graphicsData[i][1]);
        // }
        
        if(!showRotate){
            minX = graphicsData[0][0];
            maxX = graphicsData[graphicsData.length-1][0];
            minY = graphicsData[0][1];
            maxY = minY;
            for (int i = 1; i < graphicsData.length; i++) {
                if (graphicsData[i][1] < minY) {
                    minY = graphicsData[i][1];
                }
                if (graphicsData[i][1] > maxY) {
                    maxY = graphicsData[i][1];
                }
            }
        }else{
            minX = -graphicsData[0][1];
            maxX = minX;
            minY = graphicsData[0][0];
            maxY = graphicsData[graphicsData.length-1][0];
            for (int i = 1; i < graphicsData.length; i++) {
                if (-graphicsData[i][0] < minX) {
                    minX = -graphicsData[i][0];
                }
                if (-graphicsData[i][1] > maxX) {
                    maxX = -graphicsData[i][1];
                }
            }
        }

        // System.out.println("minX:  " + minX);
        // System.out.println("minY:  " + minY);
        // System.out.println("maxX:  " + maxX);
        // System.out.println("maxY:  " + maxY);

        double scaleX;
        double scaleY;

        scaleX = getSize().getWidth() / (maxX - minX);
        scaleY = getSize().getHeight() / (maxY - minY);
        
        scale = Math.min(scaleX, scaleY);
        
        if (scale == scaleX) {
            // System.out.println("МасштабX: " + scale);
            double yIncrement = (getSize().getHeight()/scale - (maxY -minY))/2;
            // System.out.println("инкремент: " + yIncrement);
                maxY += yIncrement;
                minY -= yIncrement;
        }
        if (scale == scaleY) {
            // System.out.println("МасштабY: " + scale);
            double xIncrement = (getSize().getWidth()/scale - (maxX -minX))/2;
            // System.out.println("инкремент: " + xIncrement);
            maxX += xIncrement;
            minX -= xIncrement;
        }

        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if(showIntegrals)
            paintSquare(canvas);

        if (showAxis)
            paintAxis(canvas);
        
        paintGraphics(canvas);    

        if (showDots)
            paintDots(canvas);
            
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);    
    }

    public void paintSquare(Graphics2D canvas){
        canvas.setStroke(markerStroke);
        
        ArrayList<Double> zeros = new ArrayList<>();
        ArrayList<ArrayList<Double>> pointsMinMax = new ArrayList<>();
        
        for(int i = 0; i < graphicsData.length - 1; i++){
            if(graphicsData[i][1] == 0){
                zeros.add(graphicsData[i][0]);
                i++;
                continue;
            }

            if(graphicsData[i][1] / graphicsData[i+1][1] < 0){
                
                Double x1;
                x1 = graphicsData[i][0];
                Double y1;
                y1 = graphicsData[i][1];
                Double x2;
                x2 = graphicsData[i+1][0];
                Double y2;
                y2 = graphicsData[i+1][1];
                
                Double zero = (x1*(y1 - y2) - y1*(x1 - x2))/(y1 - y2);
    
                zeros.add(zero);
                i++;
            }
        }
        if(zeros.size() < 2) return;
        
        for(int i = 1; i < graphicsData.length - 1; i++){
            if(
                Math.abs(graphicsData[i][1]) > Math.abs(graphicsData[i-1][1]) && 
                Math.abs(graphicsData[i][1]) > Math.abs(graphicsData[i+1][1])
                ){
                ArrayList<Double> extr = new ArrayList<>();
                extr.add(graphicsData[i][0]);
                extr.add(graphicsData[i][1]);
                pointsMinMax.add(extr);
            }
        }
        
        
        int u = 0;
        while(graphicsData[u][0] < zeros.get(0)) { u++; }
        
        Double step = Math.abs(graphicsData[0][0]) - Math.abs(graphicsData[1][0]);

        for(int i = 0; i < zeros.size() - 1; i++){
            canvas.setColor(Color.BLUE);
            canvas.setPaint(Color.BLUE);
            Double area = .0;
            
            Double part1OfStep;
            Double part2OfStep;

            if(graphicsData[u][1] == 0){u++;}
            
            part1OfStep = Math.abs(zeros.get(i) - graphicsData[u][0]);
            Double area1 = .0;
            if(part1OfStep == 0){
                area1 += Math.abs(step*graphicsData[u][1]/2);
            }else{
                area1 += Math.abs(part1OfStep*graphicsData[u][1]/2);
            }            
            
            GeneralPath ar = new GeneralPath();
            Point2D point1 = xyToPoint(zeros.get(i), 0);
            Point2D point2 = xyToPoint(zeros.get(i + 1), 0);
            Point2D pointMM = xyToPoint(pointsMinMax.get(i).get(0), pointsMinMax.get(i).get(1)/(3));
            
            ar.moveTo(point1.getX(), point1.getY());
            
            while(graphicsData[u][0] < zeros.get(i+1)){
                if(graphicsData[u+1][0] > zeros.get(i+1)){
                    Double area2 = .0;
                    
                    part2OfStep = Math.abs(graphicsData[u][0] - zeros.get(i+1));
                    
                    if(part2OfStep == 0){
                        area2 += Math.abs(step*graphicsData[u][1]/2);
                    }else{
                        area2 += Math.abs(part2OfStep*graphicsData[u][1]/2);
                    }
                    area += area2;
                    break;
                }
                area += Math.abs((graphicsData[u][1] + graphicsData[u+1][1]))*step/2;
                Point2D pnt = xyToPoint(graphicsData[u][0], graphicsData[u][1]);
                ar.lineTo(pnt.getX(), pnt.getY());
                u++;
            }
            ar.lineTo(point2.getX(), point2.getY());
            
            area += area1;

            canvas.draw(ar);
            canvas.fill(ar);
            canvas.setColor(Color.WHITE);
            canvas.setPaint(Color.WHITE);
            DecimalFormat df = new DecimalFormat("#.###");
            canvas.drawString(df.format(area), (float)pointMM.getX() - 10, (float)pointMM.getY());
        }
    }
}
