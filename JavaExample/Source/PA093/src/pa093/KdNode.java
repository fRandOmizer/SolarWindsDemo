package pa093;

import java.awt.geom.Point2D;

/**
 * KDNode storing class
 * 
 * @author Zan Richard
 */
public class KdNode {
    private int depth; 
    private Point2D id; 
    private KdNode parent;
    private KdNode lesser; 
    private KdNode greater; 
    
    public KdNode(Point2D point, int depth){
        id = point;
        parent = null;
        lesser = null;
        greater = null; 
        this.depth = depth;
    }
    
    public void setParent(KdNode parent){
        this.parent = parent;
    }
    
    public void setLesserGreater(KdNode lesser, KdNode greater){
        this.lesser = lesser;
        this.greater = greater; 
    }
    
    public Point2D getPoint(){
        return id;
    }
    
    public KdNode getParent(){
        return parent;
    }
    
    public KdNode getLesser(){
        return lesser;
    }
    
    public KdNode getGreater(){
        return greater;
    }
    
    public int getDepth(){
        return depth;
    }
}
