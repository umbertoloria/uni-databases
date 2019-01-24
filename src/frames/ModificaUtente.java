package frames;

import database.Database;
import database.Record;
import database.Table;
import validations.UserValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModificaUtente extends JFrame implements ActionListener {

	private static final int WIDTH = 540;
	private static final int HEIGHT = 280;

	private Database db;
	private int uid;
	private JTextField email_txt, usr_txt;
	private JPasswordField pwd_txt;
	private JComboBox<String> ruo_cmb;

	ModificaUtente(Database db) {
		this.db = db;

		String id_str = JOptionPane.showInputDialog("Fornisci l'ID dell'utente");
		try {
			uid = Integer.parseInt(id_str);
		} catch (NumberFormatException e) {
			dispose();
			JOptionPane.showMessageDialog(null, "Input non valido.", "Impossibile continuare", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String email, username, password, ruolo;

		try {
			Table t = db.query("SELECT username, email, password, ruolo FROM utenti WHERE id = '" + uid + "'");
			if (t.getRows() != 1) {
				throw new Exception("Input inadeguato.");
			}
			Record utente = t.get(0);
			ruolo = (String) utente.get(3);
			if (ruolo == null) {
				ruolo = "NULL";
			} else if (!ruolo.equals("Amministratore") && !ruolo.equals("Moderatore")) {
				throw new Exception("Input inadeguato.");
			}
			username = (String) utente.get(0);
			email = (String) utente.get(1);
			password = (String) utente.get(2);
		} catch (Exception e) {
			dispose();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Errore interno", JOptionPane.ERROR_MESSAGE);
			return;
		}

		getContentPane().setLayout(new BorderLayout());

		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[]{0, 0};
		gbl.rowHeights = new int[]{0, 0, 0, 0};
		gbl.columnWeights = new double[]{0, 1};
		gbl.rowWeights = new double[]{0, 0, 0, 0};

		JPanel data = new JPanel(gbl);
		data.setBorder(new EmptyBorder(0, 20, 0, 20));

		add(data, BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);

		JLabel email_lbl = new JLabel("E-Mail");
		ConnectionFrame.smartAdd(data, email_lbl, 0, 0);

		JLabel usr_lbl = new JLabel("Username");
		ConnectionFrame.smartAdd(data, usr_lbl, 0, 1);

		JLabel pwd_lbl = new JLabel("Password");
		ConnectionFrame.smartAdd(data, pwd_lbl, 0, 2);

		JLabel ruo_lbl = new JLabel("Ruolo");
		ConnectionFrame.smartAdd(data, ruo_lbl, 0, 3);

		email_txt = new JTextField(email);
		email_txt.addActionListener(this);
		ConnectionFrame.smartAdd(data, email_txt, 1, 0);

		usr_txt = new JTextField(username);
		usr_txt.addActionListener(this);
		ConnectionFrame.smartAdd(data, usr_txt, 1, 1);

		pwd_txt = new JPasswordField(password);
		pwd_txt.addActionListener(this);
		ConnectionFrame.smartAdd(data, pwd_txt, 1, 2);

		ruo_cmb = new JComboBox<>();
		ruo_cmb.addItem("NULL");
		ruo_cmb.addItem("Amministratore");
		ruo_cmb.addItem("Moderatore");
		ruo_cmb.setSelectedItem(ruolo);

		ConnectionFrame.smartAdd(data, ruo_cmb, 1, 3);

		JButton conn = new JButton("Crea");
		conn.addActionListener(this);
		bottom.add(conn);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize().getSize();
		setLocation((screen.width - WIDTH) / 2, (screen.height - HEIGHT) / 2);
		setSize(WIDTH, HEIGHT);
		setTitle("Client MySQL - Umberto Loria (0512105102)");
		setVisible(true);
		setResizable(false);

		email_txt.requestFocus();
	}

	public void actionPerformed(ActionEvent event) {
		String email = email_txt.getText().trim();
		String username = usr_txt.getText().trim();
		String ruolo = (String) ruo_cmb.getSelectedItem();
		String password = new String(pwd_txt.getPassword()).trim();
		try {
			if (ruolo == null) {
				ruolo = "NULL";
			}
			UserValidator.validate(email, username, password, ruolo);
			String updateQuery;
			if (ruolo.equals("NULL")) {
				updateQuery = "UPDATE utenti SET username = '" + username + "', email = '" + email + "', password = '" + password + "', ruolo = NULL WHERE id = '" + uid + "'";
			} else {
				updateQuery = "UPDATE utenti SET username = '" + username + "', email = '" + email + "', password = '" + password + "', ruolo = '" + ruolo + "' WHERE id = '" + uid + "'";
			}
			db.update(updateQuery);
			JOptionPane.showMessageDialog(null, "L'utente è stato modificato", "Operazione riuscita", JOptionPane.PLAIN_MESSAGE);
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Problema", JOptionPane.ERROR_MESSAGE);
		}
	}

}
