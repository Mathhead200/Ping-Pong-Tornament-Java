package tornament;


public class Player implements Comparable<Player>
{
	private String name;
	public int wins = 0;
	public int ties = 0;
	public int losses = 0;

	public Player(String name) {
		this.name = name;
	}


	public String name() {
		return name;
	}

	public double record(double tieWeight) {
		return ( wins + tieWeight * ties ) / ( wins + ties + losses );
	}

	public double record() {
		return record(.5);
	}


	public int compareTo(Player p) {
		if( p.record() > record() )
			return 1;
		else if( p.record() < record() )
			return -1;
		else
			return 0;
	}

	public String toString() {
		return name + ": " + wins + "-" + losses + "-" + ties;
	}

	public boolean equals(Object o) {
		if( !(o instanceof Player) )
			return false;
		Player p = (Player)o;
		return p.name == name;
	}
}
