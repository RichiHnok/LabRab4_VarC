import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.geom.*;

public class MyGraphicsDisplay extends JPanel{

    private Double[][] graphicsData;

    private boolean showAxis = true;
    private boolean showDots = true;
    private boolean showTest = true;
    private boolean showIntegrals = false;

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private double scale;

    private BasicStroke axisStroke;
    private BasicStroke graphicsStroke;
    private BasicStroke markerStroke;
    
    private Font axisFont;
    private Font squareFont;

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
            3f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f,
            null,
            0.0f
        );
        axisFont = new Font("Serif", Font.BOLD, 36);
        squareFont = new Font("Calibri", Font.BOLD, 36);
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

    public void showTest(boolean test){
        this.showTest = test;
        repaint();
    }

    public void setShowIntegrals(boolean showIntegrals){
        this.showIntegrals = showIntegrals;
        repaint();
    }


    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    protected void paintGraphics(Graphics2D canvas){
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);
        GeneralPath graphics = new GeneralPath();

        // graphics.lineTo(
        //     xyToPoint(graphicsData[0][0],graphicsData[0][1]).getX(),
        //     xyToPoint(graphicsData[0][0], graphicsData[0][1]).getY()
        // );

        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }

        // for(int i = 0; i < graphicsData.length; i++){
        //     Point2D.Double point = xyToPoint (graphicsData[i][0], graphicsData[i][1]);
        //     graphics.moveTo(point.getX(), point.getY());
        // }
        canvas.draw(graphics);
    }

    protected void paintAxis(Graphics2D canvas){
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();

        if(minX <= 0.0 && maxX >= 0.0){
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();

            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());

            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);

            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);

            canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
        }

        if(minY <= 0.0 && maxY >= 0){
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();

            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 20);
            arrow.closePath();

            canvas.draw(arrow);
            canvas.fill(arrow);

            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);

            canvas.drawString(
                "x",
                (float) (labelPos.getX() - bounds.getWidth() - 10),
                (float) (labelPos.getY() + bounds.getY())
            );
        }
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

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (graphicsData==null || graphicsData.length==0)
            return;

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

        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        
        scale = Math.min(scaleX, scaleY);
        
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight()/scale - (maxY -minY))/2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale==scaleY) {
            double xIncrement = (getSize().getWidth()/scale - (maxX -minX))/2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (showAxis)
            paintAxis(canvas);
        
        paintGraphics(canvas);

        if (showDots)
            paintDots(canvas);
        
        if(showTest)
            test(canvas);

        // if(showIntegrals){
        //     paintArea(canvas);
        // }

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);    
    }

    public void test(Graphics2D canvas){
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.BLUE);
        canvas.setPaint(Color.BLUE);

        // GeneralPath areaToPaint = new GeneralPath();
        // Point2D point = xyToPoint(graphicsData[5][0], graphicsData[5][1]);
        // areaToPaint.moveTo(point.getX(), point.getY());
        // areaToPaint.lineTo(point.getX() + 30, point.getY() + 30);
        // areaToPaint.lineTo(point.getX(), point.getY() + 30);
        // areaToPaint.closePath();
        // canvas.draw(areaToPaint);
        // canvas.fill(areaToPaint);
        
        // System.out.println(graphicsData.length);
        ArrayList<ArrayList<ArrayList<Double>>> pairs = new ArrayList<>();
        pairs = findTransitionPairs();
        // for(ArrayList<ArrayList<Double>> m: pairs){
        //     System.out.println(m.toString());
        // }
        
        ArrayList<ArrayList<Double>> pointsMinMax = new ArrayList<>();
        pointsMinMax = findExtremePoints();
        
        ArrayList<Double> zeros = new ArrayList<>();
        zeros = findZeros(pairs);
        
        // if(zeros.size() < 2) return;
        
        int u = 0;
        while(graphicsData[u][0] < pairs.get(0).get(1).get(0)){
            u++;
        }
        // u++;
        // System.out.println(u);

        // double step = Math.abs(graphicsData[0][0]) - Math.abs(graphicsData[1][0]);

        // ArrayList<GeneralPath> figures = new ArrayList<>();

        for(int i = 0; i < pairs.size() - 1; i++){
            // System.out.println(zeros.get(i) + ", " + zeros.get(i+1));
            // int upperLine;
            // if(pairs.get(i).get(0).get(1) < 0 ){
            //     upperLine = 1;
            // }else{
            //     upperLine = -1;
            // }            

            GeneralPath ar = new GeneralPath();
            Point2D point1 = xyToPoint(zeros.get(i), 0);
            Point2D point2 = xyToPoint(zeros.get(i + 1), 0);
            Point2D pointMM = xyToPoint(pointsMinMax.get(i).get(0), pointsMinMax.get(i).get(1));

            ar.moveTo(point1.getX(), point1.getY());
            // ar.lineTo(pointMM.getX(), pointMM.getY() + 10);
            
            while(graphicsData[u][0] < zeros.get(i+1)){
                // area += upperLine*(graphicsData[u][1] + graphicsData[u+1][1])*step/2;
                Point2D pnt = xyToPoint(graphicsData[u][0], graphicsData[u][1]);
                ar.lineTo(pnt.getX(), pnt.getY());
                u++;
            }
            ar.lineTo(point2.getX(), point2.getY());
            // areaToPaint.lineTo(point.getX() - zeros.get(i+1), point.getY());
            // ar.closePath();
            canvas.draw(ar);
            canvas.fill(ar);
            // area += upperLine*part2OfStep*graphicsData[u][1]/2;
        }
    }
    
    public void paintArea(Graphics2D canvas){
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);
        canvas.setPaint(Color.RED);
        canvas.setFont(squareFont);
        // ArrayList<ArrayList<Double>> transitions = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> pairs = new ArrayList<>();
        pairs = findTransitionPairs();
        
        ArrayList<ArrayList<Double>> pointsMinMax = new ArrayList<>();
        pointsMinMax = findExtremePoints();
        
        ArrayList<Double> zeros = new ArrayList<>();
        zeros = findZeros(pairs);
        
        if(zeros.size() < 2) return;
        
        int u = 0;
        while(graphicsData[u][1] != pairs.get(0).get(1).get(1)){
            u++;
        }

        double step = Math.abs(graphicsData[0][0]) - Math.abs(graphicsData[1][0]);

        ArrayList<GeneralPath> figures = new ArrayList<>();

        for(int i = 0; i < pairs.size() - 1; i++){
            int upperLine;
            if(pairs.get(i).get(0).get(1) < 0 ){
                upperLine = 1;
            }else{
                upperLine = -1;
            }

            Double area = .0;
            Double part1OfStep;
            Double part2OfStep;
            
            part1OfStep = Math.abs(zeros.get(i) - graphicsData[u][0]);
            part2OfStep = step - part1OfStep;
            area += upperLine*part1OfStep*graphicsData[u][1]/2;


            GeneralPath areaToPaint = new GeneralPath();
            Point2D point = xyToPoint(zeros.get(i), 0);

            areaToPaint.moveTo(point.getX() - zeros.get(i+1), point.getY());
            areaToPaint.lineTo(point.getX(), point.getY());
            
            while(graphicsData[u+1][0] < zeros.get(i+1)){
                area += upperLine*(graphicsData[u][1] + graphicsData[u+1][1])*step/2;
                areaToPaint.lineTo(point.getX() + graphicsData[u][0], point.getY() + graphicsData[u][1]);
                u++;
            }
            // areaToPaint.lineTo(point.getX() - zeros.get(i+1), point.getY());
            areaToPaint.closePath();
            figures.add(areaToPaint);
            area += upperLine*part2OfStep*graphicsData[u][1]/2;
        }
    }

    private ArrayList<Double> findZeros(ArrayList<ArrayList<ArrayList<Double>>> pairs){
        ArrayList<Double> zeros = new ArrayList<>();
        for(int i = 0; i < pairs.size(); i++){
            Double x1;
            x1 = pairs.get(i).get(0).get(0);
            Double y1;
            y1 = pairs.get(i).get(0).get(1);

            Double x2;
            x2 = pairs.get(i).get(1).get(0);
            Double y2;
            y2 = pairs.get(i).get(1).get(1);
            
            Double zero = (x1*(y1 - y2) - y1*(x1 - x2))/(y1 - y2);

            zeros.add(zero);
        }


        return zeros;
    }

    private ArrayList<ArrayList<ArrayList<Double>>> findTransitionPairs(){
        ArrayList<ArrayList<ArrayList<Double>>> pairs = new ArrayList<>();
        // System.out.println(graphicsData.length);
        // System.out.println(graphicsData[0].length);
        for(int i = 0; i < graphicsData.length - 1; i++){
            // System.out.print(graphicsData[i][1] + "  " + graphicsData[i+1][1] + " 1| ");
            if(graphicsData[i][1] / graphicsData[i+1][1] < 0){
                
                ArrayList<Double> point1 = new ArrayList<>();
                point1.add(graphicsData[i][0]);
                point1.add(graphicsData[i][1]);
        //         System.out.print(point1.toString() + " 2| ");

                ArrayList<Double> point2 = new ArrayList<>();
                point2.add(graphicsData[i+1][0]);
                point2.add(graphicsData[i+1][1]);
        //         System.out.print(point2.toString() + " 3| ");

                ArrayList<ArrayList<Double>> pair = new ArrayList<>();
                pair.add(point1);
                pair.add(point2);

                pairs.add(pair);
            }
            // System.out.println();
        }
        // System.out.println(pairs.toString());

        return pairs;
    }

    private ArrayList<ArrayList<Double>> findExtremePoints(){
        ArrayList<ArrayList<Double>> pointsMinMax = new ArrayList<>();
        for(int i = 1; i < graphicsData.length - 1; i++){
            if(Math.abs(graphicsData[i][1]) > Math.abs(graphicsData[i-1][1]) && Math.abs(graphicsData[i][1]) > Math.abs(graphicsData[i+1][1])){
                ArrayList<Double> extr = new ArrayList<>();
                extr.add(graphicsData[i][0]);
                extr.add(graphicsData[i][1]);
                pointsMinMax.add(extr);
            }
        }

        return pointsMinMax;
    }
}
