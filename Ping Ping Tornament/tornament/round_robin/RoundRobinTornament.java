package tornament.round_robin;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import tornament.*;


public class RoundRobinTornament extends  Tornament
{
	public RoundRobinTornament(List<Player> players, int count) {
		this.players = players;
		this.matches = new ArrayList<Match>();
		for( int _ = 0; _ < count; _++ ) {
			for( int i = 0; i < this.players.size(); i++ )
				for( int j = i + 1; j < this.players.size(); j++ ) {
					Match m = new Match(2);
					m.add( this.players.get(i) );
					m.add( (j + i + _) % 2, this.players.get(j) );
					this.matches.add(m);
				}
		}
		randomizeMatches();
	}


	public void randomizeMatches() {
		Random rand = new Random();
		for( int i = 0; i < this.matches.size(); i++ ) {
			ArrayList<Integer> notNextchs;
			try {
				notNextchs = new ArrayList<Integer>();
				for( int j = i; j < this.matches.size(); j++ ) {
					boolean notNext = false;
					PLAYER_LOOP: for( Player p : this.matches.get(i - 1) )
						for( Player q : this.matches.get(j) )
							if( p.equals(q) ) {
								notNext = true;
								break PLAYER_LOOP;
							}
					if(notNext)
						notNextchs.add(j);
				}
			} catch(IndexOutOfBoundsException e) {
				notNextchs = new ArrayList<Integer>(0);
			}
			int r;
			try {
				r = rand.nextInt( this.matches.size() - i - notNextchs.size() ) + i;
			} catch(IllegalArgumentException e) {
				r = i;
			}
			for( int z : notNextchs )
				if( r >= z )
					r++;
			try {
				//assert r >= i
				this.matches.add( i, this.matches.remove(r) );
			} catch(IndexOutOfBoundsException e) {
				//this.matches.add( i, this.matches.remove(i) );
				//do nothing, skip to the next element
			}
		}
	}
}
