package principal;

import java.util.Set;

import javax.swing.JButton;

public class Tile {
	private int id;
	private JButton button;
	private Set<String> sidesSet;
	
	public Tile(int id, JButton button, Set<String> sidesSet) {
		this.id=id;
		this.button = button;
		this.sidesSet = sidesSet;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public JButton getButton() {
		return button;
	}
	public void setButton(JButton button) {
		this.button = button;
	}
	public Set<String> getSidesSet() {
		return sidesSet;
	}
	public void setEdgeSet(Set<String> sidesSet) {
		this.sidesSet = sidesSet;
	}
	
	public String getSide() {
		String side=null;
		if (sidesSet.contains("east")) side="east";
		else if (sidesSet.contains("north")) side="north";
		else if (sidesSet.contains("west")) side="west";
		else if (sidesSet.contains("south")) side="south";
		System.out.println("1111:"+side+"--"+sidesSet);
		return side;
	}

	@Override
	public String toString() {
		return "Tile [id=" + id + ", button=" + button + ", sidesSet=" + sidesSet + "]";
	}

}
