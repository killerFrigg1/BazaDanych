package application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = (Parent) FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root, 960, 640);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("null")
	public static void main(String[] args) throws SQLException {
		Connection c = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Theater.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		DatabaseMetaData meta = c.getMetaData();
		ResultSet res = meta.getTables(null, null, "THEATER", new String[] { "TABLE" });

		if (!res.next()) {
			try {
				Statement stmt = c.createStatement();
				String sql = "CREATE TABLE THEATER " + "(ID INT PRIMARY KEY     NOT NULL,"
						+ " NAZWA           TEXT    NOT NULL, " + " OPIS            TEXT     NOT NULL, "
						+ " CZASTRWANIA        TEXT, " + " LIMITWIEKOWY         TEXT)";
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				System.exit(0);
			}
			System.out.println("Tabela zosta³a stworzona.");
		} else {
			c.close();
			System.out.println("£¹czê z istniej¹c¹ baz¹ danych.");
		}

		launch(args);
	}
}
