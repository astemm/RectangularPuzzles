package principal;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Queue;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Builder { //class for automatic assembly of puzzles
	JButton[] buttons;  //PuzButton[] buttons;
	Set<Tile> addedTiles;
	JButton currentButton=null;
	Queue<Tile> currentTiles;
	List<Edge> edges;
	List<Edge> initEdges;
	int width;
	int height;
	int count;
	double ratio12=1.10;
	TilesGridMapper mapper;
	boolean first=true;

	public Builder(JButton[] buttons, JPanel panel) {
		this.buttons = buttons;
		addedTiles=new HashSet<Tile>();
		currentTiles=new LinkedList<Tile>();
		edges=new ArrayList<Edge>();
		mapper=new TilesGridMapper(panel);
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
		edges=new ArrayList<Edge>();
		System.out.println(buttons[0]);
		width=((BufferedImage)((ImageIcon)buttons[0].getIcon()).getImage()).getWidth();
		height=((BufferedImage)((ImageIcon)buttons[0].getIcon()).getImage()).getHeight();
		for(JButton b:buttons) {
			int sum=0;
			tile=new Tile(k++,b,createEdgeSet());
			//left-west side
			for(int i=0;i<height;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(0,i);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"west",tile,sum);
			edges.add(side);
			//bottom-south side
			sum=0;
			for(int i=0;i<width;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(i,height-1);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"south",tile,sum);
			edges.add(side);
			//right-east side
			sum=0;
			for(int i=0;i<height;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(width-1,i);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"east",tile,sum);
			edges.add(side);
			//upper-north side
			sum=0;
			for(int i=0;i<width;i++) {
				int rgb=((BufferedImage)((ImageIcon)b.getIcon()).getImage()).getRGB(i,0);
			sum+=rgb;}
			//System.out.println(sum);
			side=new Edge(j++,"north",tile,sum);
			edges.add(side);
		}
		initEdges=new ArrayList<Edge>();;
		for(Edge e:edges) {
			initEdges.add(e);
		//	System.out.println("Edge:"+e.getSum());
		}
	}
	
	public Edge findEdgeBySide(Tile tile,String side) {
		count++;
		System.out.println(count);
		//System.out.println(this.edges.size());
		List<Edge> edges1=this.edges.stream().filter(e->e.getTile()==tile).collect(Collectors.toList());
		System.out.println("edges11"+edges1);
		System.out.println("edges11size"+edges1.size());
		Edge edge=this.edges.stream().filter(e->e.getTile()==tile).filter(s->s.getSide().equals(side)).findAny().orElse(null);
		return edge;
	}
	
	public Edge getEdgeByTile(Tile tile) {
		Edge edge=null;
		String side=tile.getSide();
		if(side==null) { System.out.println("poll:"+currentTiles.peek());
		currentTiles.poll();
		System.out.println("peek:"+currentTiles.peek());
		Tile newTile=currentTiles.peek();
		edge=getEdgeByTile(newTile);
		}
		else edge=findEdgeBySide(tile,side);
		return edge;
	}
	
	public Edge findNextEdge() {
		Tile thisTile=currentTiles.peek();
		Edge nextEdge=null;
		nextEdge=getEdgeByTile(thisTile);
		return nextEdge;
	}
	
	public void traverseEdges(){ //main method
		int iter=0;
		Edge firstEdge=findMidEdge();
		System.out.println("id: "+firstEdge.getId());
		System.out.println("side: "+firstEdge.getSide());
		processEdge(firstEdge);
		int size=this.edges.size();
		while(this.edges.size()>2) {
			iter++;
			System.out.println("iter: "+iter);
			if(iter>29) return;
		Edge nextEdge=findNextEdge();
		processEdge(nextEdge);
		System.out.println("left: "+this.edges.size());
		}
	}
	
	public Edge findMidEdge() { 
		List<Edge> sortedEdges=this.edges.stream().sorted(Comparator.comparing(Edge::getSum)).collect(Collectors.toList());
		System.out.println("MIDD:"+sortedEdges.get(edges.size()/2));
		return sortedEdges.get(edges.size()/2);
	}
	
	public String getReverse(String side) {
		String reverse=null; //sides: west-left; east-right; upper-north; bottom-south;
		if (side.equals("west")) {reverse="east";}
		else if (side.equals("south")) {reverse="north";}
		else if (side.equals("east")) {reverse="west";}
		else if (side.equals("north")) {reverse="south";}
		return reverse;
	}
	
	public boolean CheckReverse(Edge reverseEdge, Edge directEdge, int suma) {
		String reverse=getReverse(reverseEdge.getSide());
		List<Edge> reverseEdges=this.edges.stream().filter(e->e.getSide().equals(reverse)).collect(Collectors.toList());
		System.out.println("SIZE_RE: "+reverseEdges.size());
		//if (reverseEdges.size()==0) return true;
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
			int dif=Math.abs(rgb1-rgb2); 
			sum+=dif;
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
        	System.out.println("CLOSEST0001R:"+reverseEdge.getId()+":"+min+"::"+sums1.get(1)+":"+adjacent1.getId());
        	/*boolean one=checkMinimums(reverseEdge,adjacent);
        	boolean two=checkMinimums(reverseEdge,adjacent1);
        	if (two && !one) min=sums1.get(1);
        	System.out.println("ONE-TWO:"+one+":"+two); */
        }
		
		System.out.println("joining_reverse:"+reverseEdge.getId()+reverseEdge.getSide());
		System.out.println("adjacent_reverse:"+adjacent.getId()+adjacent.getSide());
		System.out.println("adjacent1:"+adjacent1.getId());
		System.out.println("directEdge:"+directEdge.getId());
		if (ratio<=ratio12 && directEdge==adjacent1) { ////
			return true;
		}
		else if (ratio<=ratio12 && directEdge!=adjacent1) { ////
			return false;
		}
		else if (suma==min ) {
			return true;
		}
		else return false;
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
	
	public Edge findClosest (Edge edge){  //this.edges.stream().sorted(Comparator.comparing(Edge::getSum))
		String reverse=getReverse(edge.getSide());
		List<Edge> sortedEdges=this.edges.stream().filter(e->e.getSide().equals(reverse)).sorted(Comparator.comparing(e->((Edge)e).getDifference(edge))).collect(Collectors.toList());
		//System.out.println("CL:"+sortedEdges.stream().map(e->e.getDifference(edge)).collect(Collectors.toList()));
		if (edge.getId()==29) for (Edge e:sortedEdges) {
		System.out.println("CLOSEST29:"+e.getId()+":"+e.getDifference(edge)); }
		System.out.println("CLOSEST:"+sortedEdges.get(0).getId());
		return sortedEdges.get(0);
	}
	
	public boolean isAdjacentLineOut(Point current, String side) {
		System.out.println("isADJACENT:"+current+":"+side);
		boolean adjacentOut=true;
		List<JButton> buttons=mapper.newButtons;
		List<JButton> filteredButtons=null;
		int east=current.getX()+1; int west=current.getX()-1;
		int north=current.getY()-1; int south=current.getY()+1;
		if (side.equals("east")) {
			filteredButtons=buttons.stream().filter(b->((Point)b.getClientProperty("index")).getX()==east).collect(Collectors.toList());
			//if (filteredButtons.size()>0) { 
			//	for(JButton b:filteredButtons)
			//	{System.out.println("EASssT_SIZE"+b.getClientProperty("index"));
			//System.out.println("EASssT_SIZE"+b.getClientProperty("index")); } }
			if (filteredButtons.size()>0) return false; //{System.out.println("OUSIDE"); return false;}
		}
		if (side.equals("west")) {
			filteredButtons=buttons.stream().filter(b->((Point)b.getClientProperty("index")).getX()==west).collect(Collectors.toList());
			if (filteredButtons.size()>0) return false;
		}
		if (side.equals("north")) {
			filteredButtons=buttons.stream().filter(b->((Point)b.getClientProperty("index")).getY()==north).collect(Collectors.toList());
			if (filteredButtons.size()>0) return false;
		}
		if (side.equals("south")) {
			filteredButtons=buttons.stream().filter(b->((Point)b.getClientProperty("index")).getY()==south).collect(Collectors.toList());
			if (filteredButtons.size()>0) return false;
		}
		//System.out.println("isAdjacentOutttt:"+adjacentOut);
		return adjacentOut;
	}
	
	public boolean isOutside2(Edge current, Edge adjacent) {
		int iter=0;
		System.out.println("CURRENT_Adjacent:"+current.getId()+"-"+current.getSide()+":"+adjacent.getId()+"-"+adjacent.getSide());
		boolean isOutSide=true; int w=5;
		boolean isLineFull=false;
		Point point=null;
		if (currentButton==null) point=new Point(1,1); //mapper.currentButton
		else point=(Point)currentButton.getClientProperty("index");
		//int curIndex=point.getX()+point.getY()*w;
		
		//if (isAdjacentLineOut(point, current.getSide())==false) return false;
		
		Edge currentEdge=null; Edge nextEdge=null;
		
		if(current.getSide().equals("east")) {
			currentEdge=getEdge(current.getTile().getButton(),"west");
			for(int i=0; i<2;i++) {
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("Edgess:"+currentEdge.getId()+":"+nextEdge.getId());
			    //reverseCheck=CheckReverse(nextEdge,currentEdge,0);//////
			    //System.out.println("REVERSE_CHECK22:"+reverseCheck);
				if (nextEdge==null) {isOutSide=false; break;}
				//getEdge(southToNorth.getTile().getButton(),current.getSide()); 
				currentEdge=getEdge(nextEdge.getTile().getButton(),"west");
			}
			nextEdge=findClosest2 (currentEdge);
			if (nextEdge!=null) {isLineFull=CheckFullLine(current, adjacent);
			System.out.println("IsLineFull::"+isLineFull);
			isOutSide=isLineFull;}
		}
		if(current.getSide().equals("west")) {
			currentEdge=getEdge(current.getTile().getButton(),"east");
			for(int i=0; i<2;i++) {
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("Edgess:"+currentEdge.getId()+":"+nextEdge.getId());
			    //reverseCheck=CheckReverse(nextEdge,currentEdge,0);//////
			    //System.out.println("REVERSE_CHECK22:"+reverseCheck);
				if (nextEdge==null) {isOutSide=false; break;}
				currentEdge=getEdge(nextEdge.getTile().getButton(),"east");
			}
			nextEdge=findClosest2 (currentEdge);
			if (nextEdge!=null) {isLineFull=CheckFullLine(current, adjacent);
			System.out.println("IsLineFull::"+isLineFull);
			isOutSide=isLineFull;}
		}
		if(current.getSide().equals("north")) {
			currentEdge=getEdge(current.getTile().getButton(),"south");
			for(int i=0; i<3;i++) {
				iter++;
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("Edgess:"+currentEdge.getId()+":"+nextEdge.getId());
			    //reverseCheck=CheckReverse(nextEdge,currentEdge,0);//////
			    //System.out.println("REVERSE_CHECK22:"+reverseCheck);
				if (nextEdge==null) {isOutSide=false; break;}
				currentEdge=getEdge(nextEdge.getTile().getButton(),"south");
			}
			nextEdge=findClosest2 (currentEdge);
			if (nextEdge!=null) {isLineFull=CheckFullLine(current, adjacent);
			System.out.println("IsLineFull::"+isLineFull);
			isOutSide=isLineFull;}
		}
		if(current.getSide().equals("south")) {
			int itre=0;
			currentEdge=getEdge(current.getTile().getButton(),"north");
			for(int i=0; i<3;i++) {
				itre++;
				System.out.println("Itreee:"+itre);
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("Edgess:"+currentEdge.getId()+":"+nextEdge.getId());
				if (nextEdge==null) {isOutSide=false; break;}
				currentEdge=getEdge(nextEdge.getTile().getButton(),"north");
			}
			nextEdge=findClosest2 (currentEdge);
			itre++;
			System.out.println("Itreee:"+itre);
			if (nextEdge!=null) {isLineFull=CheckFullLine(current, adjacent);
			System.out.println("IsLineFull::"+isLineFull);
			isOutSide=isLineFull;}
		}
		return isOutSide;
	}
	
	
	public boolean CheckFullLine(Edge current, Edge adjacent) {
		System.out.println("FULL_LINE");
		boolean isAdjacent=false;
		Edge currentEdge=null; Edge nextEdge=null;
		if(current.getSide().equals("east")) {
			currentEdge=getEdge(current.getTile().getButton(),"west");
			for(int i=0; i<2;i++) {
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("EdgessAdj:"+currentEdge.getId()+":"+nextEdge.getId());
				if (nextEdge==null) return false;
				isAdjacent=IsNeighboursAdjacent(currentEdge,nextEdge);
				if (isAdjacent) { 
				currentEdge=getEdge(nextEdge.getTile().getButton(),"west"); }
				else return false;
			}
		}
		if(current.getSide().equals("west")) {
			currentEdge=getEdge(current.getTile().getButton(),"east");
			for(int i=0; i<2;i++) {
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("EdgessAdj:"+currentEdge.getId()+":"+nextEdge.getId());
				if (nextEdge==null) return false;
				isAdjacent=IsNeighboursAdjacent(currentEdge,nextEdge);
				if (isAdjacent) {
				currentEdge=getEdge(nextEdge.getTile().getButton(),"east"); }
				else return false;
			}
		}
		if(current.getSide().equals("north")) {
			currentEdge=getEdge(current.getTile().getButton(),"south");
			for(int i=0; i<3;i++) {
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("EdgessAdj:"+currentEdge.getId()+":"+nextEdge.getId());
				if (nextEdge==null) return false;
				isAdjacent=IsNeighboursAdjacent(currentEdge,nextEdge);
				if (isAdjacent) {
				currentEdge=getEdge(nextEdge.getTile().getButton(),"south");}
				else return false;
			}
		}
		if(current.getSide().equals("south")) {
			int itre=0;
			currentEdge=getEdge(current.getTile().getButton(),"north");
			for(int i=0; i<3;i++) {
				itre++;
				nextEdge=findClosest2 (currentEdge);
				if (nextEdge!=null) System.out.println("EdgessAdj:"+currentEdge.getId()+":"+nextEdge.getId());
				if (nextEdge==null) return false; //{isOutSide=false; break;}
				isAdjacent=IsNeighboursAdjacent(currentEdge,nextEdge);
				if (isAdjacent) { System.out.println("ITREE::"+itre);
				currentEdge=getEdge(nextEdge.getTile().getButton(),"north");}
				else return false;
			}
		}
		System.out.println("IsADJAC::"+isAdjacent);
		return isAdjacent;
	}
	
	public void processEdge(Edge edge) {
		//System.out.println("checkkkk::"+this.edges.stream().map(e->e.getTile().getSidesSet()).collect(Collectors.toList()));
		String reverse=getReverse(edge.getSide());
		List<Edge> reverseEdges=this.initEdges.stream().filter(e->e.getSide().equals(reverse)).collect(Collectors.toList());
		System.out.println("SIZE: "+reverseEdges.size());
		if (reverseEdges.size()==0) {this.edges.remove(edge);
		Tile tile0=edge.getTile(); Set<String> sides0=tile0.getSidesSet();
        sides0.remove(edge.getSide());  edge.getTile().setEdgeSet(sides0);
		return;} //return if no opposite side to compare
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
			//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
			}
			}
			else if (edge.getSide().equals("north")) {
				for(int i=0;i<width;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(i,0);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(i,height-1);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
				}
			}
			else if (edge.getSide().equals("west")) {
				for(int i=0;i<height;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(0,i);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(width-1,i);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
				}
			}
			else if (edge.getSide().equals("south")) {
				for(int i=0;i<width;i++) {
				int rgb1=((BufferedImage)((ImageIcon)button.getIcon()).getImage()).getRGB(i,height-1);
				int rgb2=((BufferedImage)((ImageIcon)button2.getIcon()).getImage()).getRGB(i,0);
				int dif=Math.abs(rgb1-rgb2);
				sum+=dif;
				//System.out.println(rgb1+": "+rgb2+": "+(rgb1-rgb2));
				}
			}
			edgeToDiff.put(e,sum);
			if (edge.getId()==38) {System.out.println("38:-"+e.getId()+"-"+sum);}
		}
		List<Integer> sums=new ArrayList<Integer>(edgeToDiff.values());
		List<Integer> sums1=sums.stream().sorted().collect(Collectors.toList());
		int min=sums1.get(0); //get minimal difference
		System.out.println("diff: "+sums1);
		System.out.println("min: "+min);
		Edge adjacent=null;
		for(Edge ed:edgeToDiff.keySet()) {
			if (edgeToDiff.get(ed).equals(min)) adjacent=ed;
		}
		System.out.println("joining:"+edge.getId()+edge.getSide()+edge.getTile().getSidesSet());
		System.out.println("adjacent:"+adjacent.getId()+adjacent.getSide()+adjacent.getTile().getSidesSet());
		//int[] getsums=getSums(edge,adjacent);
		//System.out.println(getsums[0]+":"+getsums[1]+":"+getsums[2]);
		//System.out.println(edge.getSum()+":"+adjacent.getSum()+":"+(edge.getSum()-adjacent.getSum()));
		boolean reverseCheck=CheckReverse2(adjacent,edge,min);//////
		if (reverseCheck) {
			if (edge.getTile().getButton()!=currentButton) {currentButton=edge.getTile().getButton();}
		};
		System.out.println("REVERSE_CHECK:"+reverseCheck);
		boolean isOutSide=isOutside2(edge,adjacent);
		System.out.println("IS_outside:"+isOutSide);
		//if (reverseCheck==false) return; && !isOutSide //min<160000000 && 
		if (reverseCheck && !isOutSide) { this.edges.remove(edge); this.edges.remove(adjacent);
		if (first) first=false;
		//System.out.println("EDGESSS000---"+edges.size());
		//System.out.println("afterrr:"+this.edges.size()); System.out.println("true-true");
        //Set<String> sides=edge.getTile().getSidesSet();
		Tile tile1=edge.getTile(); Set<String> sides=tile1.getSidesSet();
        System.out.println("tile1:"+edge.getTile()); System.out.println("0022ed:"+sides);
        sides.remove(edge.getSide()); 
        //Set<String> sides2=adjacent.getTile().getSidesSet();
        Tile tile2=adjacent.getTile(); Set<String> sides2=tile2.getSidesSet();
        System.out.println("tile2:"+adjacent.getTile()); System.out.println("0022ad:"+sides2);
        sides2.remove(adjacent.getSide()); 
        adjacent.getTile().setEdgeSet(sides2);
        ////
        //if (tile1.getButton()!=currentButton) {currentButton=tile1.getButton();}
        boolean notAdded=mapper.addTile(adjacent.getTile().getButton(),currentButton, edge.getSide());
        //
        //if (notAdded) {this.edges.add(adjacent); sides2.add(adjacent.getSide()); adjacent.getTile().setEdgeSet(sides2);
        //if (!currentTiles.contains(edge.getTile())) currentTiles.add(edge.getTile());
        //}
        ///////////////////////////
        //
        if (!currentTiles.contains(edge.getTile())) currentTiles.add(edge.getTile()); 
        if (!currentTiles.contains(adjacent.getTile())) currentTiles.add(adjacent.getTile());
        System.out.println("2222ed:"+sides);
        System.out.println("2222ad:"+sides2);
        System.out.println("sides"+this.edges.stream().map(e->e.getSide()).collect(Collectors.toList()).size());
        System.out.println("currents"+currentTiles.size());
		} //|| isOutSide //min>160000000 || 
		else if(!reverseCheck || isOutSide) {this.edges.remove(edge);
		Set<String> sides=edge.getTile().getSidesSet(); sides.remove(edge.getSide()); edge.getTile().setEdgeSet(sides);
		//System.out.println("EDGESSS001---"+edges.size());
		////
		if (first) {mapper.addTile(edge.getTile().getButton(), null, null); first=false;}
		//
		if (!currentTiles.contains(edge.getTile())) currentTiles.add(edge.getTile());
		System.out.println("2222ed:"+sides);
		System.out.println("sides:"+this.edges.stream().map(e->e.getSide()).collect(Collectors.toList()).size());
		System.out.println("currents"+currentTiles.size());
		}	
	}
	
	public Edge findClosest2 (Edge edge){
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
		reverseCheck=CheckReverse2(adjacent1,edge,sums1.get(1)); 
		}
		else reverseCheck=CheckReverse2(adjacent,edge,min); //////
		System.out.println("REVERSE33:"+reverseCheck);
		if (reverseCheck && ratio<=ratio12) return adjacent1;
		else if (reverseCheck && ratio>ratio12) return adjacent;
		else return null;
	}
	
	public boolean CheckReverse2(Edge reverseEdge, Edge directEdge, int suma) {
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
				//if (reverseEdge.getId()==17) {
				//	System.out.println("17::"+reverseEdge.getId()+":"+e.getId()+"::"+sum);
				//}
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
	
	
	public boolean IsNeighboursAdjacent(Edge current, Edge adjacent) { //CheckMinimunm - was previous name
		boolean isTrue=false;
		System.out.println("Neighbours:"+current.getId()+":"+adjacent.getId());
		
		if(current.getSide().equals("east")) {
			try{
			Edge north=getEdge(current.getTile().getButton(),"north");//
			Edge southToNorth=findClosest2(north);//
			Edge edgeNorth=getEdge(southToNorth.getTile().getButton(),current.getSide());
			Edge opposite=findClosest2(edgeNorth);//More Check
			Edge south1=getEdge(opposite.getTile().getButton(),"south");
			Edge north1=getEdge(adjacent.getTile().getButton(),"north");
			Edge south2=findClosest2(north1);
			if (south1==south2) return true;
			}
			catch(Exception ex) {}
			
			try{
			Edge south=getEdge(current.getTile().getButton(),"south");//
			Edge northToSouth=findClosest2(south); //
			Edge edgeSouth=getEdge(northToSouth.getTile().getButton(),current.getSide());
			Edge opposite1=findClosest2(edgeSouth);//More Check
			Edge north2=getEdge(opposite1.getTile().getButton(),"north");
			Edge south2=getEdge(adjacent.getTile().getButton(),"south");
			Edge south3=findClosest2(north2);
			if (south2==south3) return true;
			}
			catch(Exception ex) {}
		}
		
		if(current.getSide().equals("west")) {
			    try{
				Edge north=getEdge(current.getTile().getButton(),"north");//
				Edge southToNorth=findClosest2(north);//
				Edge edgeNorth=getEdge(southToNorth.getTile().getButton(),current.getSide());
				Edge opposite=findClosest2(edgeNorth);//More Check
				Edge south1=getEdge(opposite.getTile().getButton(),"south");
				Edge north1=getEdge(adjacent.getTile().getButton(),"north");
				Edge south2=findClosest2(north1);
				if (south1==south2) return true;
			    }
				catch(Exception ex) {}
			    
				try{
				Edge south=getEdge(current.getTile().getButton(),"south");//
				Edge northToSouth=findClosest2(south); //
				Edge edgeSouth=getEdge(northToSouth.getTile().getButton(),current.getSide());
				Edge opposite1=findClosest2(edgeSouth);//More Check
				Edge north2=getEdge(opposite1.getTile().getButton(),"north");
				Edge south2=getEdge(adjacent.getTile().getButton(),"south");
				Edge south3=findClosest2(north2);
				if (south2==south3) return true;
				}
				catch(Exception ex) {}
		}
		
		if(current.getSide().equals("north")) {
			    try{
				Edge west=getEdge(current.getTile().getButton(),"west");//
				Edge eastToWest=findClosest2(west);//
				Edge edgeWest=getEdge(eastToWest.getTile().getButton(),current.getSide());
				Edge opposite=findClosest2(edgeWest);//More Check
				Edge east1=getEdge(opposite.getTile().getButton(),"east");
				Edge west1=getEdge(adjacent.getTile().getButton(),"west");
				Edge east2=findClosest2(west1); //Edge south2=findClosest2(north1);
				if (east1==east2) return true;
			    }
				catch(Exception ex) {}
			    
				try{
				Edge east=getEdge(current.getTile().getButton(),"east");//
				Edge westToEast=findClosest2(east);//
				Edge edgeEast=getEdge(westToEast.getTile().getButton(),current.getSide());
				Edge opposite1=findClosest2(edgeEast);//More Check
				Edge west2=getEdge(opposite1.getTile().getButton(),"west");
				Edge east2=getEdge(adjacent.getTile().getButton(),"east");
				Edge west3=findClosest2(east2);
				System.out.println("NORTHHH"+west2.getId()+":"+west3.getId()+"::"+east2.getId());
				if (west2==west3) return true;
				}
				catch(Exception ex) {}
		}
		
		if(current.getSide().equals("south")) {
			    try{
				Edge west=getEdge(current.getTile().getButton(),"west");//
				Edge eastToWest=findClosest2(west);//
				Edge edgeWest=getEdge(eastToWest.getTile().getButton(),current.getSide());
				Edge opposite=findClosest2(edgeWest);//More Check
				Edge east1=getEdge(opposite.getTile().getButton(),"east");
				Edge west1=getEdge(adjacent.getTile().getButton(),"west");
				Edge east2=findClosest2(west1);
				if (east1==east2) return true;
			    }
				catch(Exception ex) {}
			    
				try{
				Edge east=getEdge(current.getTile().getButton(),"east");//
				Edge westToEast=findClosest2(east);//
				Edge edgeEast=getEdge(westToEast.getTile().getButton(),current.getSide());
				Edge opposite1=findClosest2(edgeEast);//More Check
				Edge west2=getEdge(opposite1.getTile().getButton(),"west");
				Edge east2=getEdge(adjacent.getTile().getButton(),"east");
				Edge west3=findClosest2(east2);
				if (west3==west2) return true;
				}
				catch(Exception ex) {}
			}
		
		return isTrue;
	}
}
