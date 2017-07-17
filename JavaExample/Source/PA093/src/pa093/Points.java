package pa093;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class mainly stores points and have basic functions for points management.
 * @author Zan Richard
 */
public class Points {
    private List<Point2D> Points;
    
    public Points(){
        Points = new ArrayList();
    }
    
    public void AddPoint(Double x, Double y){
        Point2D point = new Point2D.Double(x, y);
        Points.add(point);
    }
    
    public void RemovePoint(Double x, Double y){
        int index = this.getFocusedPoint(x, y);
        if (index != -1){
            Points.remove(index);
        }
    }
    
    public void MovePoint(Double x, Double y){
        int index = this.getFocusedPoint(x, y);
        if (index != -1){
            Points.set(index, new Point2D.Double(x, y)); 
        }
    }

    public List<Point2D> GetPoints(){
        return Points;
    }
    
    private int getFocusedPoint(Double x, Double y){
        int index = -1;
        int pom = -1;
        for (Point2D point : Points){
            pom++;
            double distance = ((point.getX()-x)*(point.getX()-x))+
                    ((point.getY()-y)*(point.getY()-y));
            if (distance <= 125){
                index = pom;
            }
        }
        return index;
    }
}
