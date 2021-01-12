package principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JPanel;

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
		drawingPanel.remove(oldIndex);
		return index;
	}
	
	public void movePuzzle(String direction) {
		int w=5;
		List<JButton> sortedButtons=newButtons.stream().sorted(Comparator.comparing(e->((Point)((JButton)e).
		getClientProperty("index")).getY()).thenComparing(Comparator.comparing(e->((Point)((JButton)e).
		getClientProperty("index")).getX())))
		.collect(Collectors.toList());
		    Point index=null;
			Point newIndex=null;
			if (direction.equals("left")) {
				    for(int i=0; i<sortedButtons.size();i++) {
					index=getOldIndexAndRemove(sortedButtons.get(i));
					newIndex=new Point(index.getX()-1,index.getY());
					System.out.println("OLDIND-"+index+":"+newIndex);
					drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
					sortedButtons.get(i).putClientProperty("index",newIndex);
					}
			}
			else if (direction.equals("right")) {
				for(int i=sortedButtons.size()-1;i>=0;i--) {
				index=getOldIndexAndRemove(sortedButtons.get(i));
				newIndex=new Point(index.getX()+1,index.getY());
				System.out.println("OLDIND-"+index+":"+newIndex);
				drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
				sortedButtons.get(i).putClientProperty("index",newIndex);
				}
			}
			else if (direction.equals("up")) {
				for(int i=0;i<=sortedButtons.size()-1;i++) {
					index=getOldIndexAndRemove(sortedButtons.get(i)); 
					newIndex=new Point(index.getX(),index.getY()-1);
					System.out.println("OLDIND-"+index+":"+newIndex);
					drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
					sortedButtons.get(i).putClientProperty("index",newIndex); }
			}
			else if (direction.equals("down")) {
				for(int i=sortedButtons.size()-1;i>=0;i--) {
					index=getOldIndexAndRemove(sortedButtons.get(i)); 
					newIndex=new Point(index.getX(),index.getY()+1);
					System.out.println("OLDIND-"+index+":"+newIndex);
					drawingPanel.add(sortedButtons.get(i),newIndex.getX()+newIndex.getY()*w);
					sortedButtons.get(i).putClientProperty("index",newIndex); }
			}
	}
	
	public boolean addTile(JButton addedButton, JButton curButton, String side) {
		System.out.println("BBBSIZE:"+newButtons.size());
		if (newButtons.size()>12) return true;
		count++;
		int w=5; int h=6;
		if (newButtons.size()==0 && curButton==null) {
			addedButton.putClientProperty("index",new Point(1,1));
			drawingPanel.remove(1+1*w);
			drawingPanel.add(addedButton,1+1*w);
		}
		else {
			currentButton=curButton; if(newButtons.size()==0) {
			currentIndex=new Point(1,1); currentButton.putClientProperty("index",currentIndex);
			drawingPanel.remove(1+1*w);
			drawingPanel.add(currentButton,1+1*w);
			if (!newButtons.contains(currentButton) && currentButton!=null) newButtons.add(currentButton);///33333
			}
			else currentIndex=(Point)currentButton.getClientProperty("index");
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
				addedButton.putClientProperty("index",new Point(x,y));
			}
			int xx=((Point)addedButton.getClientProperty("index")).getX();
			int yy=((Point)addedButton.getClientProperty("index")).getY();
			drawingPanel.remove(xx+yy*w);
			drawingPanel.add(addedButton,xx+yy*w);
		}
		System.out.println("COMPONENTCOUNT"+drawingPanel.getComponentCount());
		if (!newButtons.contains(addedButton)) newButtons.add(addedButton);
		if (!newButtons.contains(currentButton) && currentButton!=null) newButtons.add(currentButton);///33333
		drawingPanel.revalidate();
		System.out.println("BBBAFTER:"+newButtons.size());
		return false;
		}

}
