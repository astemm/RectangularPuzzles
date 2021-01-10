package principal;

public class Edge {
	private int id;
	private String side;
	private Tile tile;
	private int sum;
	
	public Edge(int id, String side, Tile tile, int sum) {
		this.id = id;
		this.side = side;
		this.tile = tile;
		this.sum = sum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public int getDifference(Edge edge) {
		return Math.abs(this.getSum()-edge.getSum());
	}

	@Override
	public String toString() {
		return "Edge [id=" + id + ", side=" + side + ", tile=" + tile + ", sum=" + sum + "]";
	}
	
}
