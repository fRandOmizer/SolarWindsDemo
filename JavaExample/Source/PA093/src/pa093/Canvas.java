package pa093;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * This is a painting class 
 * and here is written the whole logic of my application.
 * 
 * @author Zan Richard
 */
public class Canvas extends JPanel {
   
    private Points points;
    private Boolean giftWrapping; 
    private Boolean grahamScan; 
    private Boolean triangulation;
    private Boolean kdTree;
    private Boolean delaunayTriangulation;
    private Boolean voronoiDiag;
    
    public Canvas() {
        this.points = new Points();
        giftWrapping = false;
        grahamScan = false;
        triangulation = false;
        kdTree = false;
        delaunayTriangulation = false;
        voronoiDiag = false;
    }

    // <editor-fold desc="Setters">
    public void SetPoints(Points points){
        this.points = points;
    }
    
    public void EnableGiftWrapping(){
        if (giftWrapping == false){
            giftWrapping = true;
        } else {
            giftWrapping = false;
        }
    }
    
    public void EnableGrahamScan(){
        if (grahamScan == false){
            grahamScan = true;
        } else {
            grahamScan = false;
        }
    }
    
    public void EnableTriangulation(){
        if (triangulation == false){
            triangulation = true;
        } else {
            triangulation = false;
        }
    }
    
    public void EnableKDTree(){
        if (kdTree == false){
            kdTree = true;
        } else {
            kdTree = false;
        }
    }
    
    public void EnableDelaunayTrian(){
        if (delaunayTriangulation == false){
            delaunayTriangulation = true;
        } else {
            delaunayTriangulation = false;
        }
    }
    public void EnableVoronoiDiag(){
        if (voronoiDiag == false){
            voronoiDiag = true;
        } else {
            voronoiDiag = false;
        }
    }
    // </editor-fold>
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (points.GetPoints().size()>0){
            for (Point2D point : points.GetPoints()){
                g.fillOval((int) point.getX()-5, (int) point.getY()-5, 10, 10);
            }
            try {
                if (giftWrapping){
                    this.giftWrapping(g);
                }

                if (grahamScan){
                    this.grahamScan(g);
                }
                
                if (triangulation){
                    this.grahamScan(g);
                    this.triangulation(g);
                }
                
                if (kdTree){
                    this.kDtree(g);
                }
                
                if (delaunayTriangulation){
                    this.delaunayTrian(g,true);
                }
                
                if (voronoiDiag){
                    List<Edge> edge = delaunayTrian(g,false);
                    this.voronoiDiag(g, edge);
                }
            } 
            catch (Exception e) {
            }
        }
    }
    
    // <editor-fold desc="Geometric algorithms">
    private void giftWrapping(Graphics g){
        int pivot = smallestX(points.GetPoints());
        int currentIndex = pivot;
        int previousIndex = pivot;
       
        do {
            //find the clossest point by angle to the given point
            int nextIndex = getMinimalAnglePoint(points.GetPoints(), currentIndex, previousIndex);

            Point2D a = points.GetPoints().get(currentIndex);
            Point2D b = points.GetPoints().get(previousIndex);
            
            if (currentIndex != previousIndex){
                g.setColor(new Color((int)(Math.random() * 0x1000000)));
                g.drawLine((int) a.getX(),
                           (int) a.getY(),
                           (int) b.getX(),
                           (int) b.getY());
            }
            previousIndex = currentIndex;
            currentIndex = nextIndex;
        } while (pivot != currentIndex);
        
        //connect last point with first point
        Point2D a = points.GetPoints().get(currentIndex);
        Point2D b = points.GetPoints().get(previousIndex);

        if (currentIndex != previousIndex){
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.drawLine((int) a.getX(),
                       (int) a.getY(),
                       (int) b.getX(),
                       (int) b.getY());
        }
        
    }
    
    private void grahamScan(Graphics g){
        int pivotIndex = smallestX(points.GetPoints());
        List<Point2D> orderedPoints = orderPointsByAngle(pivotIndex, points.GetPoints());
        List<Point2D> choosenPoints = chooseLeftSidePoints(orderedPoints);
        
        //connect the last choosen point with the fisrt one
        Point2D pointA = choosenPoints.get(0);
        Point2D pointB = choosenPoints.get(choosenPoints.size()-1);
        g.setColor(new Color((int)(Math.random() * 0x1000000)));
        g.drawLine((int) pointA.getX(),
                   (int) pointA.getY(),
                   (int) pointB.getX(),
                   (int) pointB.getY());
        
        int i = 1;
        while (i < choosenPoints.size()){
            pointA = choosenPoints.get(i-1);
            pointB = choosenPoints.get(i);
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.drawLine((int) pointA.getX(),
                       (int) pointA.getY(),
                       (int) pointB.getX(),
                       (int) pointB.getY());
            i++;
        }
    }
    
    private void triangulation(Graphics g){
        int pivotIndex = smallestX(points.GetPoints());
        List<Point2D> orderedPoints = orderPointsByAngle(pivotIndex, points.GetPoints());
        List<Point2D> choosenPoints = chooseLeftSidePoints(orderedPoints);
        List<Point2D> lexicoPoints = lexicalOrderPoints(choosenPoints);
        
        //remember index of each point in lexicoPoints
        List<Integer> leftPath = getLeftPoints(lexicoPoints);
        List<Integer> rightPath = getRightPoints(lexicoPoints);
        
        List<Integer> stack = new ArrayList<Integer>();
        stack.add(0);
        stack.add(1);
        
        Point2D first = lexicoPoints.get(0);
        Point2D second = lexicoPoints.get(1);
        
        for (int i = 2; i < lexicoPoints.size(); i++){
            //checking if point is on the same side as the point on the top of stack
            Boolean conditionLeft = leftPath.contains(i) && leftPath.contains(stack.get(stack.size()-1));
            Boolean conditionRight = rightPath.contains(i) && rightPath.contains(stack.get(stack.size()-1));

            //get I point from lexicographicaly ordered points
            Point2D point = lexicoPoints.get(i);
            
            //checking if the points are making convex rotations for given right/left path
            Boolean condition = (directionOfRotation(first, second, point) < 0 && leftPath.contains(i)) ||
                                (directionOfRotation(first, second, point) > 0 && rightPath.contains(i));

            if (conditionLeft || conditionRight){
                while (condition){
                    //drawing line
                    Point2D pointA = lexicoPoints.get(stack.get(stack.size()-2));
                    Point2D pointB = point;
                    g.setColor(new Color((int)(Math.random() * 0x1000000)));
                    g.drawLine((int) pointA.getX(),
                               (int) pointA.getY(),
                               (int) pointB.getX(),
                               (int) pointB.getY());

                    //removing top element from stack
                    stack.remove(stack.size()-1);
                    
                    //updating pointers to the top elements of stack
                    if (stack.size() > 1){
                        first = lexicoPoints.get(stack.size()-2);
                        second = lexicoPoints.get(stack.size()-1);
                    }
                    
                    //recalculating condition
                    condition = ((directionOfRotation(first, second, point) < 0 && leftPath.contains(i)) ||
                                (directionOfRotation(first, second, point) > 0 && rightPath.contains(i))) &&
                                stack.size() > 1;
                }
                //adding point to stack
                stack.add(i);
            } else {
                //point is on the different path, therefore connect all points from stack with this point
                for (int j = 0; j < stack.size(); j++){
                    Point2D pointA = lexicoPoints.get(stack.get(j));
                    Point2D pointB = point;
                    g.setColor(new Color((int)(Math.random() * 0x1000000)));
                    g.drawLine((int) pointA.getX(),
                               (int) pointA.getY(),
                               (int) pointB.getX(),
                               (int) pointB.getY());
                }
                //saving last item from stack
                int tempIndex = stack.get(stack.size()-1);
                //clearing stack
                stack = new ArrayList<Integer>();
                
                //adding items to stack
                stack.add(tempIndex);
                stack.add(i);
                
                //updating pointers to top 2 points in stack
                first = lexicoPoints.get(stack.size()-2);
                second = lexicoPoints.get(stack.size()-1);
            }
        }  
    }
    
    private void kDtree(Graphics g) {
        KdNode root = buildKdTree(deepCopyPoint2DArray(points.GetPoints()), 0);
        
        drawKDTree(g, root, this.getWidth(), this.getHeight());
    }
    
    private List<Edge> delaunayTrian(Graphics g, boolean draw) {
        DelaunayTrian delaunayTrian = new DelaunayTrian();
        
        List<Point2D> pointsDelanayTrian = deepCopyPoint2DArray(points.GetPoints());
        Point2D a = pointsDelanayTrian.get(0);
        Point2D b = getClosestPoint(pointsDelanayTrian, a);
        
        Edge edge = new Edge();
        edge.setEdge(a, b);
        
        Point2D c = minimalDelaunayDistance(a, b, pointsDelanayTrian);
        if (c==null){
            edge.setEdge(b, a);
            Point2D tempPoint = a;
            a = b;
            b = tempPoint;
            c = minimalDelaunayDistance(a, b, pointsDelanayTrian);
        }
        
        Edge edge2 = new Edge();
        Edge edge3 = new Edge();
        
        edge2.setEdge(b, c);
        edge3.setEdge(c, a);
        
        delaunayTrian.addEdge(edge);
        delaunayTrian.addEdge(edge2);
        delaunayTrian.addEdge(edge3);
        
        while (delaunayTrian.getEdges().size() > 0){
            edge = new Edge();
            edge2 = new Edge();
            edge3 = new Edge();
            
            a=delaunayTrian.getEdges().get(0).getA();
            b=delaunayTrian.getEdges().get(0).getB();
            
            edge.setEdge(b, a);
            
            c = minimalDelaunayDistance(b, a, pointsDelanayTrian);
            
            if (c!=null){
                edge2.setEdge(a, c);
                edge3.setEdge(c, b);
                
                delaunayTrian.addEdge(edge2);
                delaunayTrian.addEdge(edge3);
            }
            
            //delaunayTrian.addEdgeDT(edge1);
            delaunayTrian.removeEdge();
        }
        
        List<Edge> edgesDT = delaunayTrian.getEdgesDT();
        if (draw){
            for (Edge edgeTemp : edgesDT){
            a=edgeTemp.getA();
            b=edgeTemp.getB();
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.drawLine((int) a.getX(),
                       (int) a.getY(),
                       (int) b.getX(),
                       (int) b.getY());
            }
        }

        return edgesDT;
    }
    
    private void voronoiDiag(Graphics g, List<Edge> edges) {
        List<Edge> edgeList = inicializeEdges(deepCopyEdgeArray(edges), this.getWidth(), this.getHeight());
        
        edgeList = calculateVoronoiDiag(deepCopyEdgeArray(edges), points);
        for (Edge edge : edgeList){
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.drawLine( (int)edge.getIntersection1().getX(), 
                        (int)edge.getIntersection1().getY(), 
                        (int)edge.getIntersection2().getX(), 
                        (int)edge.getIntersection2().getY());
            g.setColor(Color.BLACK);
            
        }
    }
    // </editor-fold>

    // <editor-fold desc="Static functions">
    private static void drawKDTree(Graphics g, KdNode node, int width, int height){
        Point2D point = node.getPoint();
        if (node.getParent()==null){
            g.drawLine((int)point.getX(), 0, (int)point.getX(), height);
        } else {
            if ((node.getDepth()%2)==0){
                int parentY = (int)node.getParent().getPoint().getY();
                int ancestorY = getAncestorBorderEven(node, node.getParent(), height);
                int X = (int)node.getPoint().getX();
                g.setColor(new Color((int)(Math.random() * 0x1000000)));
                g.drawLine(X, parentY, X, ancestorY);
            } else {
                int parentX = (int)node.getParent().getPoint().getX();
                int ancestorX = getAncestorBorderOdd(node, node.getParent(), width);
                int Y = (int)node.getPoint().getY();
                g.setColor(new Color((int)(Math.random() * 0x1000000)));
                g.drawLine(parentX, Y, ancestorX, Y);
            }
        }
        if (node.getLesser() != null){
            drawKDTree(g, node.getLesser(), width, height);
        }
        if (node.getGreater()!= null){
            drawKDTree(g, node.getGreater(), width, height);
        }
    }
    
    private static int getAncestorBorderEven(KdNode node, KdNode ancestor, int height){
        int result = -1;
        KdNode parent = node.getParent();
        int parentResult = (int)Math.signum(node.getPoint().getY()-parent.getPoint().getY());
        int ancestorResult = (int)Math.signum(node.getPoint().getY()-ancestor.getPoint().getY());
        
        if (parentResult != ancestorResult && (ancestor.getDepth()%2)!=0){
            result = (int)ancestor.getPoint().getY();
            return result;
        }
        
        if (ancestor.getParent()==null){
            if (node.getPoint().getY()>parent.getPoint().getY()){
                result = height;
                return result;
            }
            else {
                result = 0;
                return result;
            }
        } else {
            result = getAncestorBorderEven(node, ancestor.getParent(), height);
        }
        return result;
    }
    
    private static int getAncestorBorderOdd(KdNode node, KdNode ancestor, int width){
        int result = -1;
        KdNode parent = node.getParent();
        int parentResult = (int)Math.signum(node.getPoint().getX()-parent.getPoint().getX());
        int ancestorResult = (int)Math.signum(node.getPoint().getX()-ancestor.getPoint().getX());
        
        if (parentResult != ancestorResult && (ancestor.getDepth()%2)!=1){
            result = (int)ancestor.getPoint().getX();
            return result;
        }
        
        if (ancestor.getParent()==null){
            if (node.getPoint().getX()>parent.getPoint().getX()){
                result = width;
                return result;
            } else {
                result = 0;
                return result;
            }
        } else {
            result = getAncestorBorderOdd(node, ancestor.getParent(), width);
        }

        return result;
    }
    
    private static KdNode buildKdTree(List<Point2D> points, int depth){
        KdNode node = null;
        if (points.size()==0){
            return node;
        }
        
        if (points.size()==1){
            node = new KdNode(points.get(0),depth);
            
            return node;
        } 
        
        Point2D median = getMedian(points, depth);
        KdNode left = buildKdTree(divideByMedianLeft(points, depth, median), depth + 1);
        KdNode right = buildKdTree(divideByMedianRight(points, depth, median), depth + 1);
        node = new KdNode(median, depth);
        if (left != null){
            left.setParent(node);
        }
        if (right != null){
            right.setParent(node);
        }

        node.setLesserGreater(left, right);
        
        return node;
    }
    
    private static List<Point2D> divideByMedianLeft(List<Point2D> points, int depth, Point2D median){
        List<Point2D> leftPoints = new ArrayList<>();
        
        if ((depth%2)==0){
            for (int i = 0; i < points.size(); i++ ){
                if (points.get(i).getX() < (int)median.getX()){
                    leftPoints.add(points.get(i));
                }
            }
        } else {
            for (int i = 0; i < points.size(); i++ ){
                if (points.get(i).getY() < (int)median.getY()){
                    leftPoints.add(points.get(i));
                }
            }
        }
        
        return leftPoints;
    }
    
    private static List<Point2D> divideByMedianRight(List<Point2D> points, int depth, Point2D median){
        List<Point2D> rightPoints = new ArrayList<>();

        if ((depth%2)==0){
            for (int i = 0; i < points.size(); i++ ){
                if (points.get(i).getX() > (int)median.getX()){
                    rightPoints.add(points.get(i));
                }
            }
        } else {
            for (int i = 0; i < points.size(); i++ ){
                if (points.get(i).getY() > (int)median.getY()){
                    rightPoints.add(points.get(i));
                }
            }
        }
        
        return rightPoints;
    }
    
    private static Point2D getMedian(List<Point2D> points, int depth){
        points = deepCopyPoint2DArray(points);
        if ((depth%2)==0){
            for (int i = 0; i < points.size() - 1; i++)
            {
                int index = i;
                for (int j = i + 1; j < points.size(); j++)
                    if (points.get(j).getX() < points.get(index).getX()) 
                        index = j;

                Point2D smallerPoint = points.get(index);  
                points.set(index, points.get(i));
                points.set(i, smallerPoint);
            }
        } else {
            for (int i = 0; i < points.size() - 1; i++)
            {
                int index = i;
                for (int j = i + 1; j < points.size(); j++)
                    if (points.get(j).getY() < points.get(index).getY()) 
                        index = j;

                Point2D smallerPoint = points.get(index);  
                points.set(index, points.get(i));
                points.set(i, smallerPoint);
            }
        }
        int middle = points.size()/2;
        if (points.size()%2 == 1) {
            return points.get(middle);
        } else {
            return points.get(middle-1);
        }
    }
    
    /**
     * Calculate if three points have left or right rotation
     * @param a first point
     * @param b second point 
     * @param c third point - tested point
     * @return 0> => left rotation, 0== =>  on the line, 0< => right rotation
     */
    public static double directionOfRotation(Point2D a, Point2D b, Point2D c){
        return (b.getX() - a.getX())*(c.getY() - a.getY()) -
               (b.getY() - a.getY())*(c.getX() - a.getX());
    }
    
    public static double distanceOf2Points(Point2D a, Point2D b){
        return (a.getX() - b.getX())*(a.getX() - b.getX()) +
               (a.getY() - b.getY())*(a.getY() - b.getY());
    }
    
    private static List<Point2D> deepCopyPoint2DArray(List<Point2D> points){
        List<Point2D> result = new ArrayList();
        
         for (Point2D point : points){
            result.add(point);
         }
        
        return result;
    }
    
    private static List<Edge> deepCopyEdgeArray(List<Edge> edges){
        List<Edge> result = new ArrayList();
        
         for (Edge point : edges){
            result.add(point);
         }
        
        return result;
    }
    
    private List<Point2D> chooseLeftSidePoints(List<Point2D> orderedPoints) {
        List<Point2D> result = new ArrayList();
        
        Point2D firstPoint = orderedPoints.get(0);
        Point2D secondPoint = orderedPoints.get(1);
        
        result.add(firstPoint);
        result.add(secondPoint);
        
        int index = 2;
        while( index < orderedPoints.size()){
            
            Point2D point = orderedPoints.get(index);
            
            if (directionOfRotation(firstPoint, secondPoint, point) < 0){
                
                result.add(point); 
                index++;
                
                firstPoint = result.get(result.size()-2);
                secondPoint = result.get(result.size()-1);
                
            } else {
                result.remove(result.size()-1);
                
                firstPoint = result.get(result.size()-2);
                secondPoint = result.get(result.size()-1);
            }
        }
        return result;
    }
    
    private static double angleBetween2Lines(Line2D line1, Line2D line2){
        
        Point2D a = new Point2D.Double(line1.getX1() - line1.getX2(), 
                                       line1.getY1() - line1.getY2());
        Double aNorm =  Math.sqrt(a.getX()*a.getX()+a.getY()*a.getY());
        a = new Point2D.Double(a.getX()/aNorm, 
                               a.getY()/aNorm);
        
        Point2D b = new Point2D.Double(line2.getX1() - line2.getX2(), 
                                       line2.getY1() - line2.getY2());
        Double bNorm =  Math.sqrt(b.getX()*b.getX()+b.getY()*b.getY());
        b = new Point2D.Double(b.getX()/bNorm, 
                               b.getY()/bNorm);
        
        Double dot = (a.getX()*b.getX()) + (a.getY()*b.getY());
        return Math.abs(Math.toDegrees(Math.acos(dot)));
    }
    
    private static List<Point2D> orderPointsByAngle(int pivotIndex, List<Point2D> points){
        
        //containers inicialization
        List<Point2D> tempPoints = deepCopyPoint2DArray(points);
        List<Point2D> orderedPoints = new ArrayList();
        List<Double> angles = new ArrayList();
        
        //pivot inicialization
        Point2D pivot = points.get(pivotIndex);
        orderedPoints.add(pivot);
        angles.add(0.0);
        tempPoints.remove(pivotIndex);
        
        //axis inicialization
        Point2D pivotAxisPoint = new Point2D.Double(pivot.getX(), 
                                                    pivot.getY()-100);
        Line2D line1 = new Line2D.Double(pivotAxisPoint.getX()
                                       , pivotAxisPoint.getY()
                                       , pivot.getX()
                                       , pivot.getY());
        
        //calculating angle for points
        for (int i = 0; i < tempPoints.size(); i++){
            Point2D point = tempPoints.get(i);
            Line2D line2 = new Line2D.Double(pivot.getX()
                                           , pivot.getY()
                                           , point.getX()
                                           , point.getY());
            orderedPoints.add(point);
            angles.add(angleBetween2Lines(line1, line2));
        }
        
        //Select sort for angles and pointas
        int current = 0;
        Point2D tempPoint;  
        Double tempAngle;
        for (int i = angles.size() - 1; i > 0; i-- ) {
            current = 0;   
            for(int j = 1; j <= i; j ++)   
            {
                if( angles.get(j) > angles.get(current)){
                    current = j;
                }        
            }
            tempPoint = orderedPoints.get(current);   
            tempAngle = angles.get(current);  

            orderedPoints.set(current, orderedPoints.get(i));   
            angles.set(current, angles.get(i));   

            orderedPoints.set(i, tempPoint);   
            angles.set(i, tempAngle);  
        }           
        
        //looking for duplicate points with same angle
        int index = 0;
        while (index != orderedPoints.size()-2){
            if(Math.abs(angles.get(index) - angles.get(index+1)) <= 0.01){
                
                if(distanceOf2Points(pivot, orderedPoints.get(index)) < 
                   distanceOf2Points(pivot, orderedPoints.get(index+1))){
                    
                    orderedPoints.remove(index);
                    angles.remove(index);
                } else {
                    orderedPoints.remove(index + 1);
                    angles.remove(index + 1);
                }
            }
            index++;
        }
        return orderedPoints;
    }
    
    private static int smallestX(List<Point2D> points){
        int index = -1;
        int pomIndex = -1;
        Double smallestX = 5000.0;
        
        for (Point2D point : points){
            pomIndex++;
            if (point.getX()<smallestX){
                smallestX = point.getX();
                index = pomIndex;
            }
        }
        return index;
    }
    
    private static int getMinimalAnglePoint(List<Point2D> points, int currentIndex, int previousIndex){
        Point2D currentPoint = points.get(currentIndex) ; 
        Point2D previousPoint = points.get(previousIndex);
        
        //setting inicial line
        if (currentIndex == previousIndex){
            previousPoint = new Point2D.Double(previousPoint.getX(), previousPoint.getY()+100);
        }
        
        Line2D line1 = new Line2D.Double(previousPoint.getX()
                                       , previousPoint.getY()
                                       , currentPoint.getX()
                                       , currentPoint.getY());

        //inicializing loop
        int index = -1;
        int tempIndex = -1;
        Double minimalAngle = 5000.0;
        
        //looking for point that have minimal angle to the two previous points
        for (Point2D point : points){
            tempIndex++;
            
            if (tempIndex != currentIndex && tempIndex != previousIndex){
                
                Line2D line2 = new Line2D.Double(currentPoint.getX()
                                               , currentPoint.getY()
                                               , point.getX()
                                               , point.getY());
                
                Double  pomMinimalAngle = angleBetween2Lines(line1, line2);

                if (pomMinimalAngle<minimalAngle){
                    minimalAngle = pomMinimalAngle;
                    index = tempIndex;
                }
            }
        }
        return index;
    }
    
    private static List<Point2D> lexicalOrderPoints(List<Point2D> points) {
        List<Point2D> result = deepCopyPoint2DArray(points);
        
        Point2D tempPoint;  
        for (int i = result.size() - 1; i > 0; i-- ) {
            int current = 0;   
            for(int j = 1; j <= i; j ++)   
            {
                if( (result.get(j).getY() > result.get(current).getY()) || 
                        ( result.get(j).getY() == result.get(current).getY() 
                        && result.get(j).getX() < result.get(current).getX())){
                    current = j;
                }  
            }
            tempPoint = result.get(current);   
            result.set(current, result.get(i));   
            result.set(i, tempPoint);   
            
        }       
        
        return result;
    }
    
    private static List<Integer> getLeftPoints(List<Point2D> points) {
        List<Integer> result = new ArrayList<Integer>();
        
        Point2D start = points.get(0);
        Point2D end = points.get(points.size() - 1);
        
        for (int i  = 1; i < points.size()-1; i++){
            Point2D point = points.get(i);
            if (directionOfRotation(start, end, point) > 0){
                result.add(i);
            }
        }
        
        return result;
    }
    
    private static List<Integer> getRightPoints(List<Point2D> points) {
        List<Integer> result = new ArrayList();
        
        Point2D start = points.get(0);
        Point2D end = points.get(points.size() - 1);
        
        for (int i  = 1; i < points.size()-1; i++){
            Point2D point = points.get(i);
            if (directionOfRotation(start, end, point) <= 0){
                result.add(i);
            }
        }
        
        return result;
    }
    
    private static double crossProduct(Point2D a, Point2D b, Point2D c){
        double u1 = a.getX() - b.getX();
        double v1 = a.getY() - b.getY();
        double u2 = c.getX() - a.getX();
        double v2 = c.getY() - a.getY();
        return u1*v2 - u2*v1;
    }

    private static Point2D getClosestPoint(List<Point2D> points, Point2D testedPoint){
        Point2D result = new Point2D.Double();
        double distance = 1000000;
        
        for (Point2D point : points){
            double tempDistance = distanceOf2Points(point, testedPoint);
            if( tempDistance < distance && !testedPoint.equals(point)){
                distance = tempDistance;
                result = point;
            }
        }

        return result;
    }

    public static boolean isPointOnSamePlane(Point2D a, Point2D b, Point2D P, Point2D center) {
        boolean result = true;

        double uX = (-1)*(b.getY() - a.getY());
        double uY = b.getX() - a.getX();
        
        Point2D centerPoint1 = new Point2D.Double(center.getX()+5*uX, center.getY()+5*uY);
        Point2D centerPoint2 = new Point2D.Double(center.getX()-5*uX, center.getY()-5*uY);
        
        
        if (Math.signum(directionOfRotation(centerPoint1, centerPoint2, P))==Math.signum(directionOfRotation(centerPoint1, centerPoint2, a))){
            result = true;
        } else {
            result = false;
        } 
        return result;
    }

    private static List<Edge> inicializeEdges(List<Edge> edges, double width, double height){
        
        List<Edge> result = new ArrayList<>();
        
        
        for (Edge edge : edges){
            
            edge.initVoronoiEdge(width, height);
            result.add(edge);

        }

        return result;
    }
    
    private static List<Edge> calculateVoronoiDiag(List<Edge> edges, Points points){
        List<Edge> result = new ArrayList<>();
        
        for (int i = 0; i < edges.size(); i++){
            Edge edge1 = edges.get(i);
            for (int j = 0; j < edges.size(); j++){
                
                Edge edge2 = edges.get(j);
                Point2D neightbourPoint = edge1.getNeightbourPoint(edge2);
                
                if (neightbourPoint != null){
                    for (int k = 0; k < edges.size(); k++){
                        
                        Edge edge3 = edges.get(k);
                        Edge opaqueEdge3 = new Edge();
                        opaqueEdge3.setEdge(edge3.getB(), edge3.getA());
                        
                        if ((!edge2.equals(edge3)) && (!edge1.equals(edge3)) && (!edge2.equals(opaqueEdge3)) && (!edge1.equals(opaqueEdge3))) {
                            if (neightbourPoint.equals(edge1.getNeightbourPoint(edge3))){ //&& neightbourPoint.equals(edge2.getNeightbourPoint(edge3))
                                if (!pointsInTriangle(points, edge1.getA(), edge1.getB(), neightbourPoint)) {
                                    edge1.setVoronoiEdge(edge2);
                                    edge2.setVoronoiEdge(edge1);
                                    result.add(edge1);
                                    result.add(edge2);
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
        return result;
    }
    
    

    private static boolean pointsInTriangle (Points pt, Point2D p1, Point2D p2, Point2D p3) {
        boolean b1, b2, b3;
        for (Point2D point : pt.GetPoints()){
            b1 = directionOfRotation(point, p1, p2) < 0.0f;
            b2 = directionOfRotation(point, p2, p3) < 0.0f;
            b3 = directionOfRotation(point, p3, p1) < 0.0f;
            
            if (((b1 == b2) && (b2 == b3))){
                return true;
            }
        }
        return false;
    }

    private static Point2D minimalDelaunayDistance(Point2D p1, Point2D p2, List<Point2D> points) {
        double minDelaunayDistance = 100000000;
        Point2D minDelaunayDistancePoint = null;
        for (Point2D p3 : points) {
            if(p3 == p1 || p3 == p2) continue;
            double cp = crossProduct(p1, p2, p3);
            if (cp > 0)
            {
                double p1Sq = p1.getX() * p1.getX() + p1.getY() * p1.getY();
                double p2Sq = p2.getX() * p2.getX() + p2.getY() * p2.getY();
                double p3Sq = p3.getX() * p3.getX() + p3.getY() * p3.getY();
                double num = p1Sq * (-p2.getY() + p3.getY()) + p2Sq * (-p3.getY() + p1.getY()) + p3Sq * (-p1.getY() + p2.getY());
                double cx = num / (2.0 * cp);
                num = p1Sq * (p3.getX() - p2.getX()) + p2Sq * (p1.getX() - p3.getX()) + p3Sq * (p2.getX() - p1.getX());
                double cy = -num / (2.0 * cp);
                Point2D center = new Point2D.Double(cx, cy);

                double radius = Math.sqrt((cx-p3.getX())*(cx-p3.getX()) + (cy-p3.getY())*(cy-p3.getY()));
                double delaunayDistance;
                if (crossProduct(p1, p2, center) > 0)
                {
                    delaunayDistance = radius;
                }
                else
                {
                    delaunayDistance = -radius;
                }

                if (delaunayDistance < minDelaunayDistance)
                {
                    minDelaunayDistance = delaunayDistance;
                    minDelaunayDistancePoint = p3;
                }
            }
        }

        return minDelaunayDistancePoint;
    }
    // </editor-fold>
}
