package pa093;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Main purpose is to maintain edges
 * @author Zan Richard
 */
public class DelaunayTrian {
    private List<Edge> AELedges;
    private List<Edge> DTedges;
    
    public DelaunayTrian(){
        AELedges = new ArrayList<>();
        DTedges = new ArrayList<>();
    }
    
    public void addEdge(Edge edge){
        Edge tempEdge = new Edge();
        tempEdge.setEdge(edge.getB(), edge.getA());
        if (!DTedges.contains(edge) && !AELedges.contains(edge) && !AELedges.contains(tempEdge) && !DTedges.contains(tempEdge)){ 
            AELedges.add(edge);
        }else {
            int index = AELedges.indexOf(edge);
            if (index != -1){
                AELedges.remove(index);
            }
        }
        DTedges.add(edge);
    }
    
    public void addEdgeDT(Edge edge){
        DTedges.add(edge);
    }
    
    public void removeEdge(){
        AELedges.remove(0);
    }
    
    public List<Edge> getEdges(){
        return AELedges;
    }
    
    public List<Edge> getEdgesDT(){
        return DTedges;
    }
}
