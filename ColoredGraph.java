import java.util.*;
import java.io.*;

class Vertex {

	int value;
	String color = "UNCOLORED";
	Set<String> forbiddenColors;

	boolean isColored(){
		return color != "UNCOLORED";
	}

	boolean cantColor(String color){
		return forbiddenColors.contains(color);
	}

}

public class ColoredGraph{
	public static void main(String[] args){

	}

	public static boolean graphCanBeColored(List<Vertex> vertices, Map<Vertex, List<Vertex>> edges, List<String> colors){
		return graphCanBeColored(vertices.size(), vertices, edges, colors);
	}

	public static boolean graphCanBeColored(int uncoloredVertices, List<Vertex> vertices, Map<Vertex, List<Vertex>> e, List<String> c){
		//TODO
		if(v.size() == 0 && uncoloredVertices == 0){
			return true;
		}
		if(v.size() == 0){
			return false;
		}

		for(Vertex v : vertices){
			boolean canColor = false;
			for(String color: c){
				if(!v.cantColor(color)){
					v.color = color;
					setForbiddenColor(color, e.get(v));
					List<Vertex> currentVertices = new ArrayList<>(vertices);
					currentVertices.remove(v); // i
					canColor = canColor || graphCanBeColored(uncoloredVertices - 1, currentVertices, e, c);
					undoForbiddenColor(color, e.get(v));
					v.color = "UNCOLORED";
				}
			}
		}
	}

	public static void setForbiddenColor(String color, List<Vertex> vertices){
		for(Vertex v : vertices){
			v.forbiddenColors.add(color);
		}
	}

	public static void undoForbiddenColor(String color, List<Vertex> vertices){
		for(Vertex v : vertices){
			v.forbiddenColors.remove(color);
		}
	}
}