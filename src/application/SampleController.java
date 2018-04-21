package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SampleController {

	private static int i;

	private ObservableList<Film> data = FXCollections.observableArrayList();

	@FXML
	private TextField nazwa;
	@FXML
	private TextField opis;
	@FXML
	private TextField czasTrwania;
	@FXML
	private TextField limitWiekowy;

	@FXML
	private Button dodajButton;
	@FXML
	private Button wczytajButton;

	@FXML
	private TableView<Film> tableView;

	@FXML
	private TableColumn<Film, Integer> idColumn;
	@FXML
	private TableColumn<Film, String> nazwaColumn;
	@FXML
	private TableColumn<Film, String> opisColumn;
	@FXML
	private TableColumn<Film, String> czasTrwaniaColumn;
	@FXML
	private TableColumn<Film, String> limitWiekowyColumn;

	@FXML
	public void dodaj() {
		try {
			jakieID();
			Connection c = null;
			Statement stmt = null;
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Theater.db");
			c.setAutoCommit(false);

			stmt = c.createStatement();

			String sql = "INSERT INTO THEATER (ID,NAZWA,OPIS,CZASTRWANIA,LIMITWIEKOWY) " + "VALUES ("
					+ Integer.toString(i + 1) + ", '" + nazwa.getText() + "', '" + opis.getText() + "', '"
					+ czasTrwania.getText() + "', '" + limitWiekowy.getText() + "' );";
			System.out.println(sql);
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Film zosta³ dodany do bazy");
	}

	@FXML
	public void wczytaj() throws ClassNotFoundException, SQLException {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Theater.db");
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM THEATER;");

			while (rs.next()) {
				int id = rs.getInt("id");
				String nazwa = rs.getString("nazwa");
				String opis = rs.getString("opis");
				String czasTrwania = rs.getString("czastrwania");
				String limitWiekowy = rs.getString("limitwiekowy");

				System.out.println("ID = " + id);
				System.out.println("NAZWA = " + nazwa);
				System.out.println("OPIS = " + opis);
				System.out.println("CZASTRWANIA = " + czasTrwania);
				System.out.println("LIMITWIEKOWY = " + limitWiekowy);

				data.add(new Film(id, nazwa, opis, czasTrwania, limitWiekowy));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		tableView.setItems(data);
	}

	public void jakieID() throws ClassNotFoundException, SQLException {
		Connection c = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:Theater.db");
		c.setAutoCommit(false);

		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM THEATER;");
		i = rs.getInt(1);

		rs.close();
		stmt.close();
		c.close();
	}

	public void initialize() {
		idColumn.setCellValueFactory(new PropertyValueFactory<Film, Integer>("id"));
		nazwaColumn.setCellValueFactory(new PropertyValueFactory<Film, String>("nazwa"));
		opisColumn.setCellValueFactory(new PropertyValueFactory<Film, String>("opis"));
		czasTrwaniaColumn.setCellValueFactory(new PropertyValueFactory<Film, String>("czasTrwania"));
		limitWiekowyColumn.setCellValueFactory(new PropertyValueFactory<Film, String>("limitWiekowy"));
	}

}
