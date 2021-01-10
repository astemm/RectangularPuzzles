package principal;

import java.awt.Button;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Validator { //class for automatic validation of manually composed puzzles
	JButton[] buttons;
	List<Edge> edges;
	List<Edge> initEdges;
	int width;
	int height;
	int count;
	double ratio12=1.10;
	
	public Validator(JButton[] buttons) {
		this.buttons = buttons;
		edges=new ArrayList<Edge>();
	}
	
	public Set<String> createEdgeSet() {
		Set<String> sidesSet=new HashSet<String>();
		sidesSet.add("west");sidesSet.add("south");sidesSet.add("east");sidesSet.add("north");
		return sidesSet;
	}
	
	public void buildEdges() {
		int k=1;
		int j=1;
		Edge side;
		Tile tile;
		int l=-1;
		while(true) {
			l++;
			if(buttons[l].getIcon()!=null) break;
		}
		edges=new ArrayList<Edge>();
		width=((BufferedImage)((ImageIcon)buttons[l].getIcon()).getImage()).getWidth();
		height=((BufferedImage)((ImageIcon)buttons[l].getIcon()).getImage()).getHeight();
		System.out.println("b:"+width+":"+height+"::"+buttons[l]);
		for(JButton b:buttons) {
			int sum=0;
			tile=new Tile(k++,b,createEdgeSet());
			//left-west side
			for(int i=0;i<height;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(0,i);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"west",tile,sum);
			System.out.println("EDDS77:"+side.getId()+"-"+side.getTile());
			edges.add(side);
			//bottom-south side
			sum=0;
			for(int i=0;i<width;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(i,height-1);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"south",tile,sum);
			System.out.println("EDDS77:"+side.getId()+"-"+side.getTile());
			edges.add(side);
			//right-east side
			sum=0;
			for(int i=0;i<height;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(width-1,i);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"east",tile,sum);
			System.out.println("EDDS77:"+side.getId()+"-"+side.getTile());
			edges.add(side);
			//upper-north side
			sum=0;
			for(int i=0;i<width;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(i,0);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"north",tile,sum);
			System.out.println("EDDS77:"+side.getId()+"-"+side.getTile());
			edges.add(side);
		}
		initEdges=new ArrayList<Edge>();;
		for(Edge e:edges) {
			initEdges.add(e);
		//	System.out.println("Edge:"+e.getSum());
		}
	}
	
	public String getReverse(String side) {
		String reverse=null; //sides: west-left; east-right; upper-north; bottom-south;
		if (side.equals("west")) {reverse="east";}
		else if (side.equals("south")) {reverse="north";}
		else if (side.equals("east")) {reverse="west";}
		else if (side.equals("north")) {reverse="south";}
		return reverse;
	}
	
	public Tile getTile(JButton button) {
		Tile tile=this.initEdges.stream().map(e->e.getTile()).distinct().filter(e->e.getButton()==button).findFirst().get();
		return tile;
	}
	
	public Edge getEdge(JButton button, String side) {
		Edge edge=this.initEdges.stream().filter(e->e.getSide().equals(side)).filter(e->e.getTile().getButton()==button).findFirst().get();
		//System.out.println("TILE-side-edge:"+getTile(button).getId()+":"+side+":"+edge.getId());
		System.out.println("side-edge: "+side+":"+edge.getId());
		return edge;
	}
	
	public Edge findClosest (Edge edge){
		String reverse=getReverse(edge.getSide());
		List<Edge> reverseEdges=this.initEdges.stream().filter(e->e.getSide().equals(reverse)).collect(Collectors.toList());
		//System.out.println("SIZE222: "+reverseEdges.size());
		if (reverseEdges.size()==0) {
		return null;
		}
		Map<Edge,Integer> edgeToDiff=new HashMap<Edge,Integer>();
		JButton button=edge.getTile().getButton();
		JButton button2=null;
		for(Edge e:reverseEdges) {
			button2=e.getTile().getButton();
			int sum=0;
			if (edge.getSide().equals("east")) {
			for(int i=0;i<height;i++) {
			int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(width-1,i);
			int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(0,i);
			int dif=Math.abs(rgb1-rgb2);
			sum+=dif;
			    }
			}
			else if (edge.getSide().equals("north")) {
				for(int i=0;i<width;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(i,0);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(i,height-1);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				}
			}
			else if (edge.getSide().equals("west")) {
				for(int i=0;i<height;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(0,i);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(width-1,i);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				}
			}
			else if (edge.getSide().equals("south")) {
				for(int i=0;i<width;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(i,height-1);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(i,0);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				}
			}
			edgeToDiff.put(e,sum);
		}
		List<Integer> sums=new ArrayList<Integer>(edgeToDiff.values());
		List<Integer> sums1=sums.stream().sorted().collect(Collectors.toList());
		int min=sums1.get(0); //get minimal difference
		//System.out.println("allSums22: "+sums1);
		//System.out.println("min22: "+min);
		Edge adjacent=null;
		Edge adjacent1=null;
		for(Edge ed:edgeToDiff.keySet()) {
			if (edgeToDiff.get(ed).equals(min)) adjacent=ed;
			if (edgeToDiff.get(ed).equals(sums1.get(1))) adjacent1=ed;
		}
		
		System.out.println("joinin33:"+edge.getId()+edge.getSide()+edge.getTile().getSidesSet());
		System.out.println("adjacen33:"+adjacent.getId()+adjacent.getSide()+adjacent.getTile().getSidesSet());
	    
		double ratio=(double)sums1.get(1)/min;
        if(ratio<=ratio12) {
        	System.out.println("RATIO::"+ratio);
        	System.out.println("CLOSEST00002:"+edge.getId()+":"+min+"::"+sums1.get(1)+":"+adjacent1.getId());
        	/*boolean one=checkMinimums(edge,adjacent);
        	boolean two=checkMinimums(edge,adjacent1);
        	if (two && !one) min=sums1.get(1);
        	System.out.println("ONE-TWO:"+one+":"+two); */
        }
        boolean reverseCheck;
		if (ratio<=ratio12) {
		reverseCheck=CheckReverse(adjacent1,edge,sums1.get(1)); }
		else reverseCheck=CheckReverse(adjacent,edge,min); //////
		System.out.println("REVERSE33:"+reverseCheck);
		if (reverseCheck && ratio<=ratio12) return adjacent1;
		else if (reverseCheck && ratio>ratio12) return adjacent;
		else return null;
	}
	
	public boolean CheckReverse(Edge reverseEdge, Edge directEdge, int suma) {
		String reverse=getReverse(reverseEdge.getSide());
		List<Edge> reverseEdges=initEdges.stream().filter(e->e.getSide().equals(reverse)).collect(Collectors.toList());
		System.out.println("SIZE_RE: "+reverseEdges.size());
		Map<Edge,Integer> edgeToDiff=new HashMap<Edge,Integer>();
		JButton button=reverseEdge.getTile().getButton();
		JButton button2=null;
		for(Edge e:reverseEdges) {
			button2=e.getTile().getButton();
			int sum=0;
			if (reverseEdge.getSide().equals("east")) {
			for(int i=0;i<height;i++) {
			int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(width-1,i);
			int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(0,i);
			int dif=Math.abs(rgb1-rgb2);  //Math.abs(rgb1-rgb2);
			sum+=dif;
			//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
			}
			}
			else if (reverseEdge.getSide().equals("north")) {
				for(int i=0;i<width;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(i,0);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(i,height-1);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
				}
			}
			else if (reverseEdge.getSide().equals("west")) {
				for(int i=0;i<height;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(0,i);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(width-1,i);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
				}
			}
			else if (reverseEdge.getSide().equals("south")) {
				for(int i=0;i<width;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(i,height-1);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(i,0);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
				}
			}
			edgeToDiff.put(e,sum);
		}
		List<Integer> sums=new ArrayList<Integer>(edgeToDiff.values());
		List<Integer> sums1=sums.stream().sorted().collect(Collectors.toList());
		int min=sums1.get(0); //get minimal difference
		System.out.println("diff_reverse: "+sums1);
		System.out.println("min_reverse: "+min);
		Edge adjacent=null;
		Edge adjacent1=null;
		for(Edge ed:edgeToDiff.keySet()) {
			if (edgeToDiff.get(ed).equals(min)) adjacent=ed;
			if (edgeToDiff.get(ed).equals(sums1.get(1))) adjacent1=ed;
		}
		
		double ratio=(double)sums1.get(1)/min;
        if(ratio<=ratio12) {
        	System.out.println("RATIO::"+ratio);
        	System.out.println("CLOSEST0002R:"+reverseEdge.getId()+":"+min+"::"+sums1.get(1)+":"+adjacent1.getId());
        }
		System.out.println("joining_reverse2:"+reverseEdge.getId()+reverseEdge.getSide()+"direc:"+directEdge.getId());
		System.out.println("adjacent_reverse2:"+adjacent.getId()+adjacent.getSide()+"-"+adjacent1.getId());
		if (directEdge==adjacent1 && ratio<=ratio12) {
			System.out.println("ADJACENT111::");
			return true;
		}
		else if (directEdge!=adjacent1 && ratio<=ratio12) {
			System.out.println("ADJACENT112::");
			return false;
		}
		else if (suma==min) {
			System.out.println("ADJACENT000::"+suma);
			return true;
		}
		else return false;
	}
	

}
