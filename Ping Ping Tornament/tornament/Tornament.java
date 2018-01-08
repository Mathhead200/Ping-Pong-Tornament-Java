package tornament;

import java.util.List;


public class Tornament
{
	protected List<Player> players;
	protected List<Match> matches;

	protected void organize() {
		for( int rPos = 1; rPos < players.size(); rPos++ )
			for( int lPos = 0; lPos < rPos; lPos++ )
				if( players.get(rPos).compareTo( players.get(lPos) ) > 0 ) {
					players.add( lPos, players.remove(rPos) );
					break;
				}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Match> getMatches() {
		return matches;
	}
}
