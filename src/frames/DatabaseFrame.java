package frames;

import database.Database;
import database.DatabaseParser;
import database.Table;
import queries.Queries;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class DatabaseFrame extends JFrame implements ActionListener {

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 750;

	private Database db;
	private JTextArea sql_input;
	private JTable table;

	DatabaseFrame(Database db) {
		this.db = db;
		Queries.init();

		getContentPane().setLayout(null);

		JMenuBar menubar = new JMenuBar();
		JMenu utenti = new JMenu("Utenti");
		menubar.add(utenti);
		JMenuItem inserisci = new JMenuItem("Crea utente");
		inserisci.addActionListener((e) -> new CreaUtente(db));
		utenti.add(inserisci);
		JMenuItem aggiorna = new JMenuItem("Modifica utente");
		aggiorna.addActionListener((e) -> new ModificaUtente(db));
		utenti.add(aggiorna);
		setJMenuBar(menubar);


		JPanel sql_panel = new JPanel(new BorderLayout());
		sql_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(sql_panel, BorderLayout.NORTH);

		JComboBox<String> sel = new JComboBox<>();
		sel.setFont(new Font(sel.getFont().getFontName(), Font.PLAIN, 13));
		sel.addItem("Inserisci una query manualmente...");
		for (String title : Queries.titles) {
			sel.addItem(title);
		}
		sql_panel.add(sel, BorderLayout.NORTH);

		sql_input = new JTextArea();
		sql_input.setBorder(new EmptyBorder(10, 10, 10, 10));
		sql_input.setFont(new Font("Consolas", Font.PLAIN, 15));

		JScrollPane scroll_input = new JScrollPane(sql_input);
		sql_panel.add(scroll_input, BorderLayout.CENTER);

		JButton btn = new JButton("Esegui");
		btn.setFont(new Font(btn.getFont().getFontName(), Font.PLAIN, 13));
		sql_panel.add(btn, BorderLayout.SOUTH);

		// Eventi
		sel.addActionListener((e) -> {
			int index = sel.getSelectedIndex();
			if (index > 0) {
				sql_input.setText(Queries.queries[index - 1]);
				sql_input.setCaretPosition(0);
			}
		});
		btn.addActionListener(this);
		sql_input.addKeyListener(new KeyAdapter() {
			private boolean ctrl, enter, catched;

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrl = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enter = true;
				}
				if (!catched && ctrl && enter) {
					btn.doClick();
					catched = true;
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrl = catched = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enter = catched = false;
				}
			}
		});

		table = new JTable();
		table.setRowHeight(20);
		table.setFont(new Font(table.getFont().getFontName(), Font.PLAIN, 14));
		table.getTableHeader().setFont(new Font(table.getTableHeader().getFont().getFontName(), Font.PLAIN, 14));
		table.getTableHeader().setReorderingAllowed(false);

		JPanel table_panel = new JPanel(null);
		JScrollPane sp = new JScrollPane(table);
		table_panel.add(sp);
		add(sp, BorderLayout.CENTER);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize().getSize();
		setLocation((screen.width - WIDTH) / 2, (screen.height - HEIGHT) / 2);
		setSize(WIDTH, HEIGHT);
		setTitle("Client MySQL - Umberto Loria (0512105102)");
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		sql_panel.setBounds(0, 0, getContentPane().getWidth(), getContentPane().getHeight() / 3);
		sp.setLocation(10, getContentPane().getHeight() / 3);
		sp.setSize(getContentPane().getWidth() - 20, getContentPane().getHeight() / 3 * 2 - 10);
		saveTableOnJTable(table, null);
	}

	private void saveTableOnJTable(JTable table, Table t) {
		DefaultTableModel dtm;
		if (t == null) {
			dtm = new DefaultTableModel(new Object[][]{{}}, new Object[]{}) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		} else {
			dtm = new DefaultTableModel(t.getData(), t.getHeader()) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		table.setModel(dtm);
		if (t != null) {
			int tableWidth = table.getWidth();
			int i = 0;
			int[] sizes = new int[t.getCols()];
			for (double preferredSize : t.getPreferredSizes()) {
				sizes[i] = (int) (preferredSize * tableWidth);
				i++;
			}
			DefaultTableColumnModel dtcm = (DefaultTableColumnModel) table.getColumnModel();
			for (i = 0; i < sizes.length; i++) {
				dtcm.getColumn(i).setPreferredWidth(sizes[i]);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			String query = sql_input.getText();
			if (DatabaseParser.lonelyQuery(query)) {
				if (DatabaseParser.returnsSomething(query)) {
					saveTableOnJTable(table, db.query(query));
				} else {
					db.update(query);
					saveTableOnJTable(table, null);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Bisogna fornire una sola query", "Errore MySQL",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(this, e1.getMessage(), "Errore MySQL", JOptionPane.WARNING_MESSAGE);
		}
	}

}
