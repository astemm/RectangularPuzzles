package principal;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TilesGridMapper { //class to map ongoing assembling of puzzles to correct grid
	List<JButton> newButtons;
	JButton currentButton;
	Point currentIndex;
	JPanel drawingPanel;
	int count;
	
	public TilesGridMapper(JPanel panel) {
		drawingPanel=panel;
		newButtons=new ArrayList<JButton>();
		
	}
	
	public JPanel getDrawingPanel() {
		return drawingPanel;
	}
	
	public List<JButton> getNewButton() {
		return newButtons;
	}
	
	public Point getOldIndexAndRemove(JButton button) {
		int w=5;
		Point index=(Point)button.getClientProperty("index");
		int oldIndex=index.getX()+index.getY()*w;
		System.out.println("REMOVED_BEFORE:"+drawingPanel.getComponentCount());
		drawingPanel.remove(oldIndex);
		System.out.println("REMOVED:"+drawingPanel.getComponentCount());
		return index;
	}
	
	public void movePuzzle(String direction) {
		int RAH=0;
		int w=5;
		List<JButton> sortedButtons=newButtons.stream().sorted(Comparator.comparing(e->((Point)((JButton)e).
		getClientProperty("index")).getY()).thenComparing(Comparator.comparing(e->((Point)((JButton)e).
		getClientProperty("index")).getX())))
		.collect(Collectors.toList());
		//for(JButton b:sortedButtons) {
			//RAH++; /////
			//if (RAH>1) break;
			//System.out.println("RAH:"+RAH);/////
		    Point index=null;
			//Point index=(Point)b.getClientProperty("index");
			Point newIndex=null;
			//int oldIndex=index.getX()+index.getY()*w;
			//drawingPanel.remove(oldIndex);
			if (direction.equals("left")) {
				System.out.println("LEFFT:"+sortedButtons.size());
				//for(int i=sortedButtons.size()-1;i>=0;i--) {
				    for(int i=0; i<sortedButtons.size();i++) {
					System.out.println("BUTTON:"+sortedButtons.get(i));
					index=getOldIndexAndRemove(sortedButtons.get(i));
					newIndex=new Point(index.getX()-1,index.getY());
					System.out.println("OLDIND-"+index+":"+newIndex);
					System.out.println("ADDED_BEFORE:"+drawingPanel.getComponentCount());
					drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
					System.out.println("ADDED:"+drawingPanel.getComponentCount());
					sortedButtons.get(i).putClientProperty("index",newIndex);
					}
            //newIndex=new Point(index.getX()-1,index.getY());
			//b.putClientProperty("index",newIndex);
			}
			else if (direction.equals("right")) {
				for(int i=sortedButtons.size()-1;i>=0;i--) {
				System.out.println("BUTTON:"+sortedButtons.get(i));
				index=getOldIndexAndRemove(sortedButtons.get(i));
				newIndex=new Point(index.getX()+1,index.getY());
				System.out.println("OLDIND-"+index+":"+newIndex);
				drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
				sortedButtons.get(i).putClientProperty("index",newIndex);
				}
			//newIndex=new Point(index.getX()+1,index.getY());
			//b.putClientProperty("index",newIndex);
			}
			else if (direction.equals("up")) {
				for(int i=0;i<=sortedButtons.size()-1;i++) {
					System.out.println("BUTTON:"+sortedButtons.get(i));
					index=getOldIndexAndRemove(sortedButtons.get(i)); 
					newIndex=new Point(index.getX(),index.getY()-1);
					System.out.println("OLDIND-"+index+":"+newIndex);
					drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
					sortedButtons.get(i).putClientProperty("index",newIndex); }
		    //newIndex=new Point(index.getX(),index.getY()-1);
			//b.putClientProperty("index",newIndex); 
			}
			else if (direction.equals("down")) {
				System.out.println("DOWN");
				for(int i=sortedButtons.size()-1;i>=0;i--) {
					System.out.println("BUTTON:"+sortedButtons.get(i));
					index=getOldIndexAndRemove(sortedButtons.get(i)); 
					newIndex=new Point(index.getX(),index.getY()+1);
					System.out.println("OLDIND-"+index+":"+newIndex);
					drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
					sortedButtons.get(i).putClientProperty("index",newIndex); }
			}
			//System.out.println("OLDIND-"+index+":"+newIndex);
			//System.out.println("RemovedSize"+drawingPanel.getComponentCount());
			//drawingPanel.add(b,newIndex.getX()+newIndex.getY()*w);
			//System.out.println("AddedSize"+drawingPanel.getComponentCount());
		//}
	}
	
	public boolean addTile(JButton addedButton, JButton curButton, String side) {
		boolean isNotAdded=false;
		System.out.println("BBBSIZE:"+newButtons.size());
		//for(JButton b:newButtons) {
		//	System.out.print(b.getClientProperty("index"));}
		if (newButtons.size()>12) return true; ////
		count++;
		int w=5; int h=6;
		if (newButtons.size()==0 && curButton==null) {
			addedButton.putClientProperty("index",new Point(1,1));
			drawingPanel.remove(1+1*w);
			System.out.println("REMOVEDsize11"+drawingPanel.getComponentCount());
			drawingPanel.add(addedButton,1+1*w);
			System.out.println("ADDEDsize11"+drawingPanel.getComponentCount());
		}
		else {
			currentButton=curButton; if(newButtons.size()==0) {//333333
			currentIndex=new Point(1,1); currentButton.putClientProperty("index",currentIndex);
			drawingPanel.remove(1+1*w);
			System.out.println("REMOVEDsize22"+drawingPanel.getComponentCount());
			drawingPanel.add(currentButton,1+1*w);
			if (!newButtons.contains(currentButton) && currentButton!=null) newButtons.add(currentButton);///33333
			System.out.println("ADDEDsize22"+drawingPanel.getComponentCount());System.out.println("DOWN");
			}
			else currentIndex=(Point)currentButton.getClientProperty("index");
			System.out.println("POINT_INDEX:"+currentButton.getClientProperty("index"));
			//boolean isFull=isFullLine(currentIndex,side);/////
			//if (isFull) return;
			//if(isOutOfBound(currentIndex,side)) return true;
			System.out.println("INDEXX"+currentIndex+":"+currentButton);
			if (side.equals("east")) {
			    if (currentIndex.getX()==4) {movePuzzle("left"); currentIndex.setX(3);}
				int x=currentIndex.getX()+1;
				int y=currentIndex.getY();
				addedButton.putClientProperty("index",new Point(x,y));
			}
			else if (side.equals("west")) {
			    if (currentIndex.getX()==0) {movePuzzle("right"); currentIndex.setX(1);}
				int x=currentIndex.getX()-1;
				int y=currentIndex.getY();
				addedButton.putClientProperty("index",new Point(x,y));
			}
			else if (side.equals("north")) {
			    if (currentIndex.getY()==0) {movePuzzle("down"); currentIndex.setY(1);}
				int x=currentIndex.getX();
				int y=currentIndex.getY()-1;
				addedButton.putClientProperty("index",new Point(x,y));
			}
			else if (side.equals("south")) {
			    if (currentIndex.getY()==5) {movePuzzle("up"); currentIndex.setY(4);}
				int x=currentIndex.getX();
				int y=currentIndex.getY()+1;
				System.out.println("AddedSOUTH"+x+":"+y);
				addedButton.putClientProperty("index",new Point(x,y));
			}
			int xx=((Point)addedButton.getClientProperty("index")).getX();
			int yy=((Point)addedButton.getClientProperty("index")).getY();
			drawingPanel.remove(xx+yy*w);
			System.out.println("REMOVEDsize33"+drawingPanel.getComponentCount());
			drawingPanel.add(addedButton,xx+yy*w);
			System.out.println("ADDEDsize33"+drawingPanel.getComponentCount());
		}
		System.out.println("COMPONENTCOUNT"+drawingPanel.getComponentCount());
		System.out.println("COUNT_MAPPER:"+count);
		if (!newButtons.contains(addedButton)) newButtons.add(addedButton);
		if (!newButtons.contains(currentButton) && currentButton!=null) newButtons.add(currentButton);///33333
		drawingPanel.revalidate();
		System.out.println("BBBAFTER:"+newButtons.size());
		//for(JButton b:newButtons) {
		//System.out.print(b.getClientProperty("index"));}
		//System.out.println();
		//List<JButton>columns=newButtons.stream().sorted(Comparator.comparing(e->((Point)((JButton)e).
		//		getClientProperty("index")).getY())).collect(Collectors.toList());
		return false;
		}

}
