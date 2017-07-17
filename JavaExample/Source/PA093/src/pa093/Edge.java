
package pa093;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Main purpose of this class is to store edge and calculate its axis.
 * @author Zan Richard
 */
public class Edge {
    //main points
    private Point2D a;
    private Point2D b;
    
    //used for storing axis
    private Point2D normal;
    private Point2D middle;

    private Point2D end1;
    private Point2D end2;
    
    private Point2D intersection1;
    private Point2D intersection2;
    
    public Edge(){
        a = new Point2D.Double();
        b = new Point2D.Double();
        
        normal = new Point2D.Double();
        middle = new Point2D.Double();
        
        end1 = new Point2D.Double();
        end2 = new Point2D.Double();
        
        intersection1 = new Point2D.Double();
        intersection2 = new Point2D.Double();
    }
    
    /**
     * Setting the axis values to the edges of Canvas
     * @param width
     * @param height 
     */
    public void initVoronoiEdge(double width, double height){
        middle.setLocation((a.getX() + b.getX())/2, (a.getY() + b.getY())/2);
        normal.setLocation((-1)*(b.getY() - a.getY()), b.getX() - a.getX());
        
        end1.setLocation(middle.getX()+15*normal.getX(), middle.getY()+15*normal.getY());
        end2.setLocation(middle.getX()-15*normal.getX(), middle.getY()-15*normal.getY());
        
        Line2D x0 = new Line2D.Double(0, 0, width, 0);
        Line2D x1 = new Line2D.Double(0, height, width, height);
        Line2D y0 = new Line2D.Double(0, 0, 0, height);
        Line2D y1 = new Line2D.Double(width, 0, width, height);
        
        Line2D line = new Line2D.Double(end1.getX(), end1.getY(), end2.getX(), end2.getY());
        
        end1 = intersectionOfTwoLines(x0, line);
        end2 = intersectionOfTwoLines(x1, line);
        
        intersection1 = intersectionOfTwoLines(x0, line);
        intersection2 = intersectionOfTwoLines(x1, line);
        
        if (end1.getX()<0 || end1.getX()>width){
            end1 = intersectionOfTwoLines(y0, line);
            end2 = intersectionOfTwoLines(y1, line);
            
            intersection1 = intersectionOfTwoLines(y0, line);
            intersection2 = intersectionOfTwoLines(y1, line);
        }
    }
    
    public Point2D getNeightbourPoint(Edge edge){
        if (this.equals(edge)){
            return null;
        }
        Point2D p1 = edge.getA();
        Point2D p2 = edge.getB();
        if (a.equals(p1)&&!b.equals(p2)){
            return p2;
        }
        if (b.equals(p1)&&!a.equals(p2)){
            return p2;
        }
        if (a.equals(p2)&&!b.equals(p1)){
            return p1;
        }
        if (b.equals(p2)&&!a.equals(p1)){
            return p1;
        }
        return null;
    }
    
    // <editor-fold desc="Setters">
    public void setEdge(Point2D a, Point2D b){
        this.a = a;
        this.b = b;
    }
    
    /**
     * Calculating intersection and choosing witch end of axis to change
     * @param edge edge with intersection
     */
    public void setVoronoiEdge(Edge edge){
        
        Line2D line1 = new Line2D.Double(end1.getX(), end1.getY(), end2.getX(), end2.getY());
        Line2D line2 = new Line2D.Double(edge.getIntersection1().getX(), 
                                         edge.getIntersection1().getY(), 
                                         edge.getIntersection2().getX(), 
                                         edge.getIntersection2().getY());
        Point2D intersection = intersectionOfTwoLines(line1, line2);
        
        Point2D dirPoint = chooseDirection(edge);

        boolean isSameDirrectionForEnd1 = Math.signum(Canvas.directionOfRotation(a, b, dirPoint)) == Math.signum(Canvas.directionOfRotation(a, b, end1));
        boolean isSameDirrectionForEnd2 = Math.signum(Canvas.directionOfRotation(a, b, dirPoint)) == Math.signum(Canvas.directionOfRotation(a, b, end2));
        
        if (isSameDirrectionForEnd1){
            intersection1 = intersection;
            return;
        } 
        
        if (isSameDirrectionForEnd2){
            intersection2 = intersection;
            return;
        }
    }
    // </editor-fold>
    
    // <editor-fold desc="Getters">
    public Point2D getA(){
        return a;
    }
    
    public Point2D getB(){
        return b;
    }
    
    public Point2D getMiddle(){
        return middle;
    }
    
    public Point2D getIntersection1(){
        return intersection1;
    }
    
    public Point2D getIntersection2(){
        return intersection2;
    }
    // </editor-fold>
    
    // <editor-fold desc="Private methods">
    /**
     * Looking for intersection's direction
     * @param edge adjacent edge to this edge 
     * @return direction
     */
    private Point2D chooseDirection(Edge edge){
        Point2D result = new Point2D.Double();
        
        if (!edge.getA().equals(a) && !edge.getA().equals(b)){
            result = edge.getA();
        }
        
        if (!edge.getB().equals(a) && !edge.getB().equals(b)){
            result = edge.getB();
        }
        
        return result;
    }
    
    private static Point2D intersectionOfTwoLines(Line2D a, Line2D b) {
        double x1 = a.getX1();
        double x2 = a.getX2();
        double x3 = b.getX1();
        double x4 = b.getX2();
        
        double y1 = a.getY1();
        double y2 = a.getY2();
        double y3 = b.getY1();
        double y4 = b.getY2();
        
        double pX = ((x1*y2-x2*y1)*(x3-x4)-(x3*y4-x4*y3)*(x1-x2))/((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
        double pY = ((x1*y2-x2*y1)*(y3-y4)-(x3*y4-x4*y3)*(y1-y2))/((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
        
        return new Point2D.Double(pX, pY);
    }
    
    private static boolean isTheClosesOne(Point2D center, Point2D oldValue, Point2D newValue){
        return (Canvas.distanceOf2Points(center, oldValue)>Canvas.distanceOf2Points(center, newValue));
    }
    // </editor-fold>
    
    // <editor-fold desc="Hash, Equals, toString">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.a);
        hash = 89 * hash + Objects.hashCode(this.b);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Edge{" + "int1=" + intersection1 + ", int2=" + intersection2 + '}';
    }
    // </editor-fold>
}
