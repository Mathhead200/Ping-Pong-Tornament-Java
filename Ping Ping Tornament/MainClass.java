
import java.util.*;
import java.io.*;
import tornament.*;
import tornament.round_robin.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainClass
{
	private static void centerFrame(Frame f) {
		Dimension dim = f.getToolkit().getScreenSize();
		f.setLocation( ( dim.width - f.getWidth() ) / 2, ( dim.height - f.getHeight() ) / 2  );
	}

	private static class Fields {
		RoundRobinTornament torney;
		int matchOffset = 0;
	}

	public static void main(String[] args)
	{
		final ArrayList<Player> pList = new ArrayList<Player>();
		final Fields vars = new Fields();

		//Initializing Main JFrame
		final JFrame mainFrame = new JFrame("Ping Pong, Round Robin Tornament");
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		//Making Menu
		mainFrame.setMenuBar( new MenuBar() );
		final Menu fileMenu = new Menu("File"),
			editMenu = new Menu("Edit"),
			torneyMenu = new Menu("Tornament");
		final MenuItem saveSelection = new MenuItem("Save"),
			loadSelection = new MenuItem("Load"),
			addPlayerSelection = new MenuItem("Add Player"),
			editPlayerSelection = new MenuItem("Edit Player"),
			unSkipSelection = new MenuItem("Un-Skip Matches"),
			reRandomizeSelection = new MenuItem("Re-Randomize Matches");
		torneyMenu.setEnabled(false);
		saveSelection.setEnabled(false);
		editPlayerSelection.setEnabled(false);
		mainFrame.getMenuBar().add(fileMenu);
		mainFrame.getMenuBar().add(editMenu);
		mainFrame.getMenuBar().add(torneyMenu);
		fileMenu.add(saveSelection);
		fileMenu.add(loadSelection);
		editMenu.add(addPlayerSelection);
		editMenu.add(editPlayerSelection);
		torneyMenu.add(unSkipSelection);
		torneyMenu.add(reRandomizeSelection);

		//Setting Up GUI Elements
		class GUIElements {
			final JButton startButton = new JButton("Start Tornament"),
				declareP1Button = new JButton(),
				declareP2Button = new JButton(),
				declareTieButton = new JButton("Declare Tie"),
				skipButton = new JButton("Skip Match");
			final JLabel p1Label = new JLabel(),
				p2Label = new JLabel(),
				vsLabel = new JLabel("<html>");

			GUIElements() {
				declareP1Button.setVisible(false);
				declareP2Button.setVisible(false);
				declareTieButton.setVisible(false);
				skipButton.setVisible(false);
				p1Label.setVisible(false);
				p2Label.setVisible(false);
			}

			void updateGUI() {
				try {
					p1Label.setText( getCurrentMatch().get(0).toString() );
					p2Label.setText( getCurrentMatch().get(1).toString() );
					declareP1Button.setText( "Declare " + getCurrentMatch().get(0).name() + " The Winner" );
					declareP2Button.setText( "Declare " + getCurrentMatch().get(1).name() + " The Winner" );
				} catch(NullPointerException e) {}
			}

			Match getCurrentMatch() {
				try {
					return vars.torney.getMatches().get(vars.matchOffset);
				} catch(IndexOutOfBoundsException e) {
					if(vars.matchOffset == 0) {
						java.util.List<Player> pl = vars.torney.getPlayers();
						mainFrame.setContentPane( new JPanel( new GridLayout( pl.size() + 1, 1, 10, 10) ) );
						mainFrame.setMenuBar( new MenuBar() );
						mainFrame.getContentPane().add( new JLabel("<html><h3 style='width:250px; text-align:center;'><u>Tornament Over</u></h3></html>") );
						//Sort Players
						for( int rPos = 1; rPos < pl.size(); rPos++ )
							for( int lPos = 0; lPos < rPos; lPos++ )
								if( pl.get(lPos).compareTo( pl.get(rPos) ) > 0 ) {
									pl.add( lPos, pl.remove(rPos) );
									break;
								}
						for( Player p : vars.torney.getPlayers() )
							mainFrame.getContentPane().add( new JLabel( p.toString(), JLabel.CENTER ) );
						mainFrame.pack();
						centerFrame(mainFrame);
						mainFrame.validate();
						return null;
					} else {
						vars.matchOffset = 0;
						return getCurrentMatch();
					}
				}
			}
		}
		final GUIElements guiEles = new GUIElements();
		guiEles.startButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guiEles.startButton.setVisible(false);
				vars.torney = new RoundRobinTornament(pList, 1);
				for( int i = 0; i < vars.torney.getMatches().size(); i += 3 )
					for( int _ = 0; _ < 2; _++ ) {
						Match m = new Match(2);
						m.add( vars.torney.getMatches().get(i).get(0) );
						m.add( vars.torney.getMatches().get(i).get(1) );
						vars.torney.getMatches().add(i + 1, m);
					}

				guiEles.updateGUI();
				guiEles.vsLabel.setText("vs");
				guiEles.declareP1Button.setVisible(true);
				guiEles.declareP2Button.setVisible(true);
				guiEles.declareTieButton.setVisible(true);
				guiEles.skipButton.setVisible(true);
				guiEles.p1Label.setVisible(true);
				guiEles.p2Label.setVisible(true);

				torneyMenu.setEnabled(true);
				addPlayerSelection.setEnabled(false);
				editPlayerSelection.setEnabled(true);
				loadSelection.setEnabled(false);
				saveSelection.setEnabled(true);

				mainFrame.pack();
				centerFrame(mainFrame);
			}
		});
		guiEles.declareP1Button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( JOptionPane.showConfirmDialog( mainFrame, guiEles.declareP1Button.getText() + "?",
					"Confirm", JOptionPane.YES_NO_OPTION ) == 0
				) {
					guiEles.getCurrentMatch().get(0).wins++;
					guiEles.getCurrentMatch().get(1).losses++;
					vars.torney.getMatches().remove(vars.matchOffset);
					guiEles.updateGUI();
				}
			}
		});
		guiEles.declareP2Button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( JOptionPane.showConfirmDialog( mainFrame, guiEles.declareP2Button.getText() + "?",
						"Confirm", JOptionPane.YES_NO_OPTION ) == 0
				) {
					guiEles.getCurrentMatch().get(0).losses++;
					guiEles.getCurrentMatch().get(1).wins++;
					vars.torney.getMatches().remove(vars.matchOffset);
					guiEles.updateGUI();
				}
			}
		});
		guiEles.declareTieButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( JOptionPane.showConfirmDialog( mainFrame, guiEles.declareTieButton.getText() + "?",
						"Confirm", JOptionPane.YES_NO_OPTION ) == 0
				) {
					guiEles.getCurrentMatch().get(0).ties++;
					guiEles.getCurrentMatch().get(1).ties++;
					vars.torney.getMatches().remove(vars.matchOffset);
					guiEles.updateGUI();
				}
			}
		});
		guiEles.skipButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( JOptionPane.showConfirmDialog( mainFrame, guiEles.skipButton.getText() + "?",
					"Confirm", JOptionPane.YES_NO_OPTION ) == 0
				) {
					vars.matchOffset++;
					guiEles.updateGUI();
				}
			}
		});

		//Setting Menu Selection Events
		saveSelection.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame popup = new JFrame("Save");
				final JFileChooser fc = new JFileChooser();
				popup.getContentPane().add(fc);
				fc.setApproveButtonText("Save");
				fc.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if( !e.getActionCommand().equals("ApproveSelection") )
							return;
						File saveFile = fc.getSelectedFile();
						boolean isFile = saveFile.isFile();
						if( isFile && 1 == JOptionPane.showConfirmDialog( popup,
							"Are you sure you want to overwrite " + saveFile.getName(), "Overwrite?", JOptionPane.YES_NO_OPTION )
						)
							return;
						try {
							if( !isFile )
								saveFile.createNewFile();
							PrintStream fOut = new PrintStream( new FileOutputStream(saveFile) );
							for( Player p : pList )
								fOut.println( p.name() + "," + p.wins + "," + p.losses + "," + p.ties );
							fOut.println();
							for( Match m : vars.torney.getMatches() )
								fOut.println( pList.indexOf(m.get(0)) + " " + pList.indexOf(m.get(1)) );
							fOut.close();
						} catch(IOException f) {
							JOptionPane.showMessageDialog( popup,
								"IO Error Occured\n" + f.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE );
						}
					}
				});
				popup.pack();
				popup.setLocation(100, 100);
				popup.validate();
				popup.setVisible(true);
			}
		});
		loadSelection.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame popup = new JFrame("Load");
				final JFileChooser fc = new JFileChooser();
				popup.getContentPane().add(fc);
				fc.setApproveButtonText("Load");
				fc.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if( !e.getActionCommand().equals("ApproveSelection") )
							return;
						File loadFile = fc.getSelectedFile();
						try {
							BufferedReader fIn = new BufferedReader( new FileReader(loadFile) );
							guiEles.vsLabel.setText("");
							pList.clear();
							for( String line = fIn.readLine(); !line.equals(""); line = fIn.readLine() ) {
								String[] strArr = line.split(",", 4);
								Player p = new Player( strArr[0] );
								p.wins = Integer.parseInt( strArr[1] );
								p.losses = Integer.parseInt( strArr[2] );
								p.ties = Integer.parseInt( strArr[3] );
								pList.add(p);
							}
							ArrayList<Match> mArr = new ArrayList<Match>();
							for( String line = fIn.readLine(); line != null; line = fIn.readLine() ) {
								String[] strArr = line.split(" ", 2);
								Match m = new Match(2);
								m.add( pList.get( Integer.parseInt(strArr[0]) ) );
								m.add( pList.get( Integer.parseInt(strArr[1]) ) );
								mArr.add(m);
							}
							vars.matchOffset = 0;
							guiEles.startButton.doClick();
							vars.torney.getMatches().clear();
							vars.torney.getMatches().addAll(mArr);
							guiEles.updateGUI();
							popup.dispose();
						} catch(IOException f) {
							JOptionPane.showMessageDialog( popup,
								"IO Error (corrupted file?)\n" + f.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE );
						} catch(NumberFormatException f) {
							JOptionPane.showMessageDialog( popup,
								"Number Error (corrupted file?)\n" + f.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE );
						}
					}
				});
				popup.pack();
				popup.setLocation(100, 100);
				popup.validate();
				popup.setVisible(true);
			}
		});
		addPlayerSelection.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame popup = new JFrame("Add Player");
				popup.setContentPane( new Panel( new GridLayout(3, 1, 10, 10) ) );
				final JTextField nameField = new JTextField(25);
				popup.getContentPane().add( new JLabel("Name: ") );
				popup.getContentPane().add(nameField);
				JButton confirmButton = new JButton("Confirm");
				popup.getContentPane().add(confirmButton);
				confirmButton.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						pList.add( new Player( nameField.getText() ) );
						guiEles.vsLabel.setText( guiEles.vsLabel.getText() + nameField.getText() + "<br>" );
						popup.dispose();

						mainFrame.pack();
						centerFrame(mainFrame);
					}
				});
				popup.pack();
				popup.setLocation(100, 100);
				popup.validate();
				popup.setVisible(true);
			}
		});
		editPlayerSelection.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame listFrame = new JFrame("Edit Player");
				listFrame.setContentPane( new JPanel( new GridLayout(vars.torney.getPlayers().size(), 1, 10, 10) ) );
				for( final Player p : vars.torney.getPlayers() ) {
					JButton pButton = new JButton( p.toString() );
					pButton.addActionListener( new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							final JFrame popup = new JFrame( "Edit Player - " + p.name() );
							final JTextField winsField = new JTextField( "" + p.wins, 5 ),
								lossesField = new JTextField( "" + p.losses, 5 ),
								tiesField = new JTextField( "" + p.ties, 5 );
							final JButton saveButton = new JButton("Save Changes");
							popup.setContentPane( new Panel( new GridLayout(5, 2, 10, 10) ) );
							popup.getContentPane().add( new JLabel( p.name() ) );
							popup.getContentPane().add( new JLabel("") );
							popup.getContentPane().add( new JLabel("Wins:") );
							popup.getContentPane().add(winsField);
							popup.getContentPane().add( new JLabel("Losses:") );
							popup.getContentPane().add(lossesField);
							popup.getContentPane().add( new JLabel("Ties:") );
							popup.getContentPane().add(tiesField);
							popup.getContentPane().add( new JLabel("") );
							popup.getContentPane().add(saveButton);
							saveButton.addActionListener( new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									p.wins = Integer.parseInt( winsField.getText() );
									p.losses = Integer.parseInt( lossesField.getText() );
									p.ties = Integer.parseInt( tiesField.getText() );
									guiEles.updateGUI();
									popup.dispose();
								}
							});
							popup.pack();
							popup.setLocation(100, 100);
							popup.validate();
							popup.setVisible(true);
							listFrame.dispose();
						}
					});
					listFrame.add(pButton);
					listFrame.pack();
					listFrame.setLocation(100, 100);
					listFrame.setVisible(true);
				}
			}
		});
		unSkipSelection.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vars.matchOffset = 0;
				guiEles.updateGUI();
			}
		});
		reRandomizeSelection.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vars.torney.randomizeMatches();
				guiEles.updateGUI();
			}
		});

		//Laying-Out GUI Elements
		JPanel stuff = new JPanel( new GridBagLayout() );
		mainFrame.setContentPane(stuff);
		@SuppressWarnings("serial")
		class GBC extends GridBagConstraints {
			public GBC() {
				super();
				insets = new Insets(10, 10, 10, 10);
			}
		}

		GridBagConstraints gbc = new GBC();
		gbc.gridwidth = 3;
		stuff.add(guiEles.startButton, gbc);

		gbc = new GBC();
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		stuff.add(guiEles.p1Label, gbc);

		gbc = new GBC();
		gbc.gridy = 1;
		gbc.gridx = 1;
		stuff.add(guiEles.vsLabel, gbc);

		gbc = new GBC();
		gbc.gridy = 1;
		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.WEST;
		stuff.add(guiEles.p2Label, gbc);

		gbc = new GBC();
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		stuff.add(guiEles.declareP1Button, gbc);

		gbc = new GBC();
		gbc.gridy = 2;
		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.WEST;
		stuff.add(guiEles.declareP2Button, gbc);

		gbc = new GBC();
		gbc.gridy = 3;
		gbc.gridx = 1;
		stuff.add(guiEles.declareTieButton, gbc);

		gbc = new GBC();
		gbc.gridy = 4;
		gbc.gridx = 1;
		gbc.insets.top = 25;
		stuff.add(guiEles.skipButton, gbc);

		//Resizing and Displaying Main JFrame
		mainFrame.pack();
		centerFrame(mainFrame);
		mainFrame.validate();
		mainFrame.setVisible(true);
	}
}
