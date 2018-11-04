package StringSearch;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Gui för string projektet
 * @author Barry Al-Jawari
 *
 */
public class GUI implements ActionListener {
	private final JFrame SearchString;
	private final Read file;
	private JTextArea InputString;
	private JTextArea SearchInput;
	private JButton search;
	private long FirstNano;
	private long SecondNano;
	private double DifferenceNaiveKMP;

	/**
	 * Konstrktur som läser en fil
	 * @param file
	 */
	public GUI(Read file) {
		this.file = file;
		SearchString = new JFrame();
		SearchString.setBounds(0, 0, 1000, 700);
		SearchString.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SearchString.setTitle("Search sträng by Barry & Edi");
		initializeGUI();
		SearchString.setVisible(true);
		SearchString.setResizable(false);
		SearchString.setLocationRelativeTo(null);
		InputString.setEditable(false);
		try {

			char[] out = file.readFile();
			for (char Kub : out) {

				InputString.append("" + Kub);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initierar GUI-komponenter
	 */
	private void initializeGUI() {
		JPanel mainPnl = new JPanel();
		Border border = BorderFactory.createTitledBorder("Text sträng");
		Border border2 = BorderFactory.createTitledBorder("Din sökning-sträng");
		
		
		InputString = new JTextArea();
		InputString.setLineWrap(true);
		InputString.setBounds(0, 0, 994, 522);
		SearchInput = new JTextArea();
		SearchInput.setBounds(0, 525, 994, 85);
		InputString.setBorder(border);
		SearchInput.setBorder(border2);

		search = new JButton("Search!");
		search.setBounds(0, 609, 994, 62);
		mainPnl.setLayout(null);
		mainPnl.add(InputString);
		mainPnl.add(SearchInput);
		mainPnl.add(search);
		
		SearchString.getContentPane().add(mainPnl);
		search.addActionListener(this);

	}
	/**
	 * Algoritmen för både naiv sökning och KMP
	 * @throws IOException
	 */
	private void start() {

		NaiveKMP KMP = new NaiveKMP();
		long NanoFirst = System.nanoTime();
		try {
			KMP.indexPatternKMP(file.readFile(), getSearchInput().toCharArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		FirstNano = System.nanoTime() - (NanoFirst);

		long NanoSecond = System.nanoTime();
		try {
			KMP.naiveMatching(file.readFile(), getSearchInput().toCharArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		SecondNano = System.nanoTime() - (NanoSecond);

		printTime();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == search) {
				start();
		}
	}
	private String getSearchInput() {
		return SearchInput.getText();
	}

	private void printTime() {
		double KMP = NANOSECONDS.toMillis(SecondNano);
		double Naive = NANOSECONDS.toMillis(FirstNano);

		if (Naive > KMP) {
			System.out.println("\n" + "'Naive String Matching' WON this battle of algorithm");
			DifferenceNaiveKMP = (Naive - KMP);
		}
		if (KMP > Naive) {
			System.out.println("\n" + "'Pattern Index KMP' WON this battle of algorithm");
			DifferenceNaiveKMP = (KMP - Naive);
		}

		
		System.out.println("\n" +"Pattern Index KMP time: " + Naive);
		System.out.println("Naive String Matching time:  " + KMP);
		System.out.println("Difference: " + DifferenceNaiveKMP + " milliseconds" );


	}
}
