package frames;

import database.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ConnectionFrame extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new ConnectionFrame();
	}

	private static final int WIDTH = 540;
	private static final int HEIGHT = 280;

	private JTextField url_txt, port_txt, usr_txt, dtb_txt;
	private JPasswordField pwd_txt;

	private ConnectionFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
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

		JLabel url_lbl = new JLabel("URL");
		smartAdd(data, url_lbl, 0, 0);

		JLabel port_lbl = new JLabel("Porta");
		smartAdd(data, port_lbl, 0, 1);

		JLabel usr_lbl = new JLabel("Username");
		smartAdd(data, usr_lbl, 0, 2);

		JLabel pwd_lbl = new JLabel("Password");
		smartAdd(data, pwd_lbl, 0, 3);

		JLabel dtb_lbl = new JLabel("Database");
		smartAdd(data, dtb_lbl, 0, 4);

		url_txt = new JTextField("localhost");
		url_txt.addActionListener(this);
		smartAdd(data, url_txt, 1, 0);

		port_txt = new JTextField("3306");
		port_txt.addActionListener(this);
		smartAdd(data, port_txt, 1, 1);

		usr_txt = new JTextField("root");
		usr_txt.addActionListener(this);
		smartAdd(data, usr_txt, 1, 2);

		pwd_txt = new JPasswordField();
		pwd_txt.addActionListener(this);
		smartAdd(data, pwd_txt, 1, 3);

		dtb_txt = new JTextField("films_videogiochi");
		dtb_txt.addActionListener(this);
		smartAdd(data, dtb_txt, 1, 4);

		JButton conn = new JButton("Connettiti");
		conn.addActionListener(this);
		bottom.add(conn);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize().getSize();
		setLocation((screen.width - WIDTH) / 2, (screen.height - HEIGHT) / 2);
		setSize(WIDTH, HEIGHT);
		setTitle("Client MySQL - Umberto Loria (0512105102)");
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pwd_txt.requestFocus();
	}

	static void smartAdd(JPanel panel, JComponent comp, int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = x;
		gbc.gridy = y;
		panel.add(comp, gbc);
	}

	public void actionPerformed(ActionEvent event) {
		try {
			int port = Integer.parseInt(port_txt.getText());
			Database db = new Database(url_txt.getText(), port, usr_txt.getText(), new String(pwd_txt.getPassword()),
					dtb_txt.getText());
			setVisible(false);
			new DatabaseFrame(db);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "La porta inserita non è valida", "Errore di connessione",
					JOptionPane.WARNING_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Errore di connessione", JOptionPane.ERROR_MESSAGE);
		}
	}

}
