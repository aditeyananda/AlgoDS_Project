// Graph.java
// Graph code, modified from Graph Quiz code.
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.PrintStream;
import java.util.ArrayList;

//class vertex is used here to indicate a vertex of the graph. The variables mentioned tell about the previous node, distance, adjacent Edges etc.
//The color White indicates that this vertex has not been yet traversed.
class Vertex
{   public String strName;
    public LinkedList<Edge> adjEdges; 
    public Vertex prv;                
    public double dist;
    public int hPos;               
    public String clr, status="";               
    public Vertex(String nm) 
    { strName=nm; adjEdges=new LinkedList<>(); reset();}
    public void reset() 
    { dist=weightGraph.inf; prv=null; clr="White";}
}

//class Edge indicates the edges of the graph. here, we have source and destination of the edge. Also, the weight between these nodes.
class Edge 
{   public Vertex src, dst;
    public double wVal;
    public String status="";

    public Edge(Vertex src, Vertex dst, double wVal)
    {   super();
        this.src=src;
        this.dst=dst;
        this.wVal=wVal;
    }
}

//class weightGraph is used here to form the initial graph and then call functions to make necessary changes as per the query.
//This class contains all the necessary functions required for the queries such as - addEdge, deleteEdge, edgeUp, edgeDown, vertexUp, vertexDown. 
//It also contains the imlementation of Dijkstra Algorithm for finding shortest path. and the required function for printing the graph
class weightGraph
{   public final static double inf=Double.MAX_VALUE;
    protected Map<String, Vertex> vMap=new HashMap<>();
    @SuppressWarnings("unchecked")
    public void dijkstraAlgo(String head)
    {   for (Vertex q : vMap.values())
        {
            q.dist = inf;
            q.prv = null;
        }
        Vertex beg=vMap.get(head);
        if(beg == null)
        {   System.out.println("Start vertex not found!");
            return;
        }
        if(beg.status.equals("DOWN"))
        {
            System.out.println("Start vertex is DOWN!");
            return;
        }

        beg.dist = 0.0;
        minHeap x1 = new minHeap(new ArrayList(vMap.values()));
        while (!x1.isEmpty()) {
            Vertex p = x1.getMin();
            for (Edge edge : p.adjEdges)
            {   if(!edge.dst.status.equals("DOWN") && !edge.status.equals("DOWN") && p.dist != inf)
                {   double temp=p.dist+edge.wVal;
                    temp = (double)Math.round(temp*100)/100;
                    if(temp < edge.dst.dist)
                    {   edge.dst.dist = temp;
                        edge.dst.prv = p;
                        x1.lowKeyPri(edge.dst.hPos, temp);
                    }
                }
            }
        }
    }
    public void addEdge(String src, String dest, double wVal)
    {   Boolean pivot=false;
        if (vMap.get(src)!=null && vMap.get(dest)!=null)
        {   Vertex p = getVertex(src);
            Vertex q = getVertex(dest);
            for (Edge edge : p.adjEdges) 
            {   if (edge.dst.strName.equals(dest))
                {
                    edge.wVal = wVal;
                    pivot = true;
                }
            }
            if(!pivot)
            {
                Edge pq = new Edge(p, q, wVal);
                p.adjEdges.add(pq);
            }
        } 
        else 
        {   Vertex p = getVertex(src);
            Vertex q = getVertex(dest);
            Edge pq = new Edge(p, q, wVal);
            p.adjEdges.add(pq);
        }
    }
    public void deleteEdge(String srcVertex, String dstVertex) 
    {   if (vMap.get(srcVertex)!=null && vMap.get(dstVertex)!=null) 
        {   Vertex p = getVertex(srcVertex);
            for (Edge edge : p.adjEdges)
            {   if(edge.dst.strName.equals(dstVertex)) 
                {   p.adjEdges.remove(edge);
                    break;
                }
            }
        }
    }
    public void edgeUp(String srcVertex, String dstVertex) 
    {   if(vMap.get(srcVertex)!=null && vMap.get(dstVertex)!=null)
        {   Vertex p = getVertex(srcVertex);
            for (Edge edge : p.adjEdges)
            {   if (edge.dst.strName.equals(dstVertex))
                    edge.status = "";
            }
        }
    }
    public void edgeDown(String srcVertex, String dstVertex) 
    {   if(vMap.get(srcVertex)!= null && vMap.get(dstVertex)!=null)
        {   Vertex p = getVertex(srcVertex);
            for(Edge edge : p.adjEdges)
            {  if (edge.dst.strName.equals(dstVertex)) 
                    edge.status = "DOWN";
            }
        }
    }
    public void vertexUp(String vertex)
    {   if (vMap.get(vertex)!=null)
        {
            Vertex v = getVertex(vertex);
            v.status = "";
        }
    }
    public void vertexDown(String vertex)
    {   if (vMap.get(vertex)!=null)
        {
            Vertex q = getVertex(vertex);
            q.status = "DOWN";
        }
    }
    private Vertex getVertex(String vertexName)
    {   Vertex w = vMap.get(vertexName);
        if (w == null)
        {
            w = new Vertex(vertexName);
            vMap.put(vertexName, w);
        }
        return w;
    }
    public void printweightGraph()
    {
        ArrayList<String> sortVert = new ArrayList<>(vMap.keySet());
        Collections.sort(sortVert);
        for (String key : sortVert) 
        {   Vertex w=vMap.get(key);
            System.out.println(w.strName+" "+w.status);
            ArrayList<String> adjList = new ArrayList<>();
            for (Edge edge : w.adjEdges)
            {
                adjList.add(edge.dst.strName+" "+edge.wVal+" "+edge.status);
            }
            Collections.sort(adjList);                      
            for (String neighbor : adjList)
                System.out.println("  " + neighbor);
        }
    }
    public void addEdges(String srcName, String dstName, double wVal)
    {
        Vertex p = getVertex(srcName);
        Vertex q =getVertex(dstName);
        Edge ptoq = new Edge(p, q, wVal);
        Edge qtop = new Edge(q, p, wVal);
        p.adjEdges.add(ptoq);
        q.adjEdges.add(qtop);
    }
    public void printPath(String dstName)
    {   Vertex w=vMap.get(dstName);
        if(w==null)
        {
            System.out.println("Destination vertex not found");
        }
        else if(w.dist == inf)
        {
            System.out.println(dstName + " is unReachable");
        }
        else
        {
            printShortestPath(w);
            System.out.print(" " + w.dist);
            System.out.println();
        }
    }
    private void printShortestPath(Vertex end)
    {   if (end.prv != null)
        {
            printShortestPath(end.prv);
            System.out.print(" ");
        }
        System.out.print(end.strName);
    }
}

//This class contains the impementation of Heap and MinHeapify
class minHeap
{
    private ArrayList<Vertex> seQueue;
    public minHeap(ArrayList<Vertex> arr) {
        this.seQueue = arr;
        make(this.seQueue, seQueue.size());
    }
    private void make(ArrayList<Vertex> unseQueue, int num) 
    {
        for(int i=(num/2)-1; i>=0; i--)
        {
            minHeapify(unseQueue, i, num);
        }
    }
    private void minHeapify(ArrayList<Vertex> unseQueue, int i, int num)
    {   int l = leftChild(i);
        int r = rightChild(i);
        int smin;
        if(l<= num-1)
        {
            if(getDist(unseQueue.get(l))<getDist(unseQueue.get(i)))
            {   smin = l;
                unseQueue.get(l).hPos = i;
                unseQueue.get(i).hPos = l;
            } 
            else 
            {   unseQueue.get(l).hPos = l;
                unseQueue.get(i).hPos = i;
                smin = i;
            }
        } 
        else 
        {   unseQueue.get(i).hPos = i;
            smin = i;
        }
        if(r<= num-1)
        {   if(getDist(unseQueue.get(r))<getDist(unseQueue.get(smin)))
            {   unseQueue.get(r).hPos = i;
                unseQueue.get(i).hPos = r;
                smin = r;
            }
            else 
            {   unseQueue.get(r).hPos = r;
                unseQueue.get(i).hPos = i;
            }
        }
        if(smin!=i)
        {   Vertex temp=unseQueue.get(i);
            unseQueue.set(i, unseQueue.get(smin));
            unseQueue.set(smin, temp);
            minHeapify(unseQueue, smin, num);
        }
    }
    public Vertex getMin(ArrayList<Vertex> seQueue, int num) 
    {   if(num<1)
        {
            System.out.println("Heap underflow...");
            return null;
        }
        Vertex min = seQueue.get(0);
        seQueue.set(0, seQueue.get(num-1));
        seQueue.remove(num-1);
        num = num-1;
        if(num>0)
        {
            minHeapify(seQueue, 0, num);
        }
        return min;
    }
    public void lowKeyPri(int i, double key)
    {   if(seQueue.get(i).dist<key)
            return;
        seQueue.get(i).dist=key;
        while(i>0 && seQueue.get(parent(i)).dist>seQueue.get(i).dist)
        {
            seQueue.get(parent(i)).hPos = i;
            seQueue.get(i).hPos = parent(i);
            Vertex temp = seQueue.get(i);
            seQueue.set(i, seQueue.get(parent(i)));
            seQueue.set(parent(i), temp);
            i = parent(i);
        }
    }
    private int parent(int i){ return (i - 1) / 2; }
    private int leftChild(int i){ return 2 * i + 1; }
    private int rightChild(int i){ return 2 * i + 2; }
    public boolean isEmpty(){ return seQueue.isEmpty(); }
    public Vertex getMin(){ return getMin(seQueue,seQueue.size()); }
    private double getDist(Vertex v){ return v.dist; }
}

class Reachable 
{   public static void reachableVertices(weightGraph g)
    {   ArrayList<String> vertList = new ArrayList<>(g.vMap.keySet());
        Collections.sort(vertList);
        for (String vName : vertList)
        {   resetVertices(g);
            Vertex s = g.vMap.get(vName);
            if(s.status.equals("DOWN"))
                continue;
            s.clr = "Grey";
            s.dist = 0.0;
            s.prv = null;
            Queue<Vertex> q=new LinkedList<>();
            q.add(s);

            while(!q.isEmpty())
            {
                Vertex v = q.remove();
                for (Edge e : v.adjEdges)
                {
                    if(!e.dst.status.equals("DOWN") && !e.status.equals("DOWN") && e.dst.dist == weightGraph.inf && e.dst.clr.equals("White")) 
                    {   e.dst.clr = "Grey";
                        e.dst.dist = v.dist + 1.0;
                        e.dst.prv = v;
                        q.add(e.dst);
                    }
                }
                v.clr = "Black";
            }
            System.out.println(vName);
            displayReachable(vertList, g, vName);
        }
    }

    private static void displayReachable(ArrayList<String> vertList, weightGraph g, String source)
    {
        for (String vName : vertList)
        {   if (g.vMap.get(vName).clr.equals("Black") && !vName.equals(source))
                System.out.println("  " + vName);
        }
    }

    private static void resetVertices(weightGraph g) 
    {   for (Vertex v : g.vMap.values()) 
        {   v.clr = "White";
            v.dist = weightGraph.inf;
            v.prv = null;
        }
    }
}

//This is the main class for the program that reads the input processes it and then gives the output
class Graph 
{    public static void main(String[] args)
    {   weightGraph g = new weightGraph();
        try 
        {
            FileReader netFile = new FileReader(args[0]);
            FileReader qFile = new FileReader(args[1]);
            PrintStream outFile = new PrintStream(args[2]);
            System.setOut(outFile);  
            Scanner weightGraphFile = new Scanner(netFile);
            Scanner queries = new Scanner(qFile);

            while (weightGraphFile.hasNextLine())
            {
                String[] values =weightGraphFile.nextLine().split(" ");
                String source=values[0];
                String dest=values[1];
                double wVal=Double.parseDouble(values[2]);
                g.addEdges(source, dest, wVal);
            }
            while (queries.hasNextLine()) 
            {
                String[] buff = queries.nextLine().split(" ");
                if (buff[0].equals("print")) 
                {
                    g.printweightGraph();
                    System.out.print("\n");
                } 
                else if (buff[0].equals("addedge")) 
                {
                    g.addEdge(buff[1], buff[2], Double.parseDouble(buff[3]));
                } 
                else if (buff[0].equals("deleteedge")) 
                {
                    g.deleteEdge(buff[1], buff[2]);
                } 
                else if (buff[0].equals("edgedown")) 
                {
                    g.edgeDown(buff[1], buff[2]);
                } 
                else if (buff[0].equals("edgeup")) 
                {
                    g.edgeUp(buff[1], buff[2]);
                } 
                else if (buff[0].equals("vertexdown")) 
                {
                    g.vertexDown(buff[1]);
                } 
                else if (buff[0].equals("vertexup")) 
                {
                    g.vertexUp(buff[1]);
                } 
                else if (buff[0].equals("path")) 
                {
                    g.dijkstraAlgo(buff[1]);
                    g.printPath(buff[2]);
                    System.out.println("\n");
                } 
                else if (buff[0].equals("Reachable") || buff[0].equals("reachable")) 
                {
                    Reachable.reachableVertices(g);
                    System.out.print("\n");
                } 
                else if (buff[0].equals("exit")) 
                {
                    System.out.println("Exiting...");
                    System.exit(0);
                } 
                else
                    System.out.println("Invalid Command!");
            }

            netFile.close();
            qFile.close();
            outFile.close();
        } 
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.err.println("Invalid Command!");
        } 
        catch (IOException e) 
        {
            System.err.println(e);
        }
    }
}