
import java.io.*;
import java.util.*;
import tornament.*;
import tornament.round_robin.*;


public class TextBased
{
	static BufferedReader stringToReader(String filename) throws IOException {
		return new BufferedReader( new FileReader( new File(filename) ) );
	}

	static BufferedWriter stringToWriter(String filename) throws IOException {
		return new BufferedWriter( new FileWriter( new File(filename) ) );
	}

	public static void main(String[] args) throws IOException {
		Properties settings = new Properties();
		File settingsFile = new File("settings.ini");
		if( !settingsFile.isFile() ) {
			settingsFile.createNewFile();
			settings.setProperty("input", "players.txt");
			settings.setProperty("output", "matches.txt");
			settings.setProperty("rounds", "1");
			settings.store( new FileOutputStream(settingsFile), "Tornament Settings");
		}
		settings.load( new FileInputStream(settingsFile) );
		BufferedReader inFile = stringToReader( settings.getProperty("input", "players.txt") );
		BufferedWriter outFile = stringToWriter( settings.getProperty("output", "matches.txt") );
		int roundCount = Integer.parseInt( settings.getProperty("rounds", "1") );

		ArrayList<Player> pList = new ArrayList<Player>();
		for( String line = inFile.readLine(); line != null; line = inFile.readLine() ) {
			if( line.matches("^\\s*$") )
				continue;
			pList.add( new Player(line) );
		}

		for( Match match : new RoundRobinTornament(pList, roundCount).getMatches() ) {
			outFile.write( match.get(0).name() + " vs " + match.get(1).name() + String.format("%n") );
		}

		inFile.close();
		outFile.close();
	}
}
