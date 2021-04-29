package com.vivek;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.javafx.webkit.WebConsoleListener;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Main {

	private void initAndShowGUI() {

		JFrame frame = new JFrame("Google ");
		JFXPanel mainPanel = new JFXPanel();

		try {
			frame.setSize(950, 350);
			frame.setVisible(true);
			frame.isResizable();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.add(mainPanel).setBounds(0, 0, 950, 350);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					initFx(mainPanel);
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	private void initFx(JFXPanel mainPanel) {

		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();

		webView.setContextMenuEnabled(false);

		try {
			ContextMenu contextMenu = new ContextMenu();
			MenuItem reload = new MenuItem("Reload");
			MenuItem printPage = new MenuItem("Print Page");
			MenuItem exitItem = new MenuItem("Exit");
			exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
			exitItem.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent t) {
					System.exit(0);
				}
			});
			contextMenu.getItems().addAll(reload, printPage, exitItem);
			webView.setOnMousePressed(e -> {
				if (e.getButton() == MouseButton.SECONDARY) {
					contextMenu.show(webView, e.getScreenX(), e.getScreenY());
				} else {
					contextMenu.hide();
				}
			});

			VBox snapshotBox = new VBox();
			String url = "http://www.google.com";

			webEngine.setJavaScriptEnabled(true);
			try {
				if (!url.isEmpty()) {
					webEngine.load(url);
					snapshotBox.getChildren().add(webView);
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Loading Failed");
					alert.setContentText("Error Occured,Check URL");
					alert.showAndWait();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Page Loading Failed");
				alert.setContentText("Error Occured while Loading Page,Plz try Again ");
				alert.showAndWait();

				WebConsoleListener.setDefaultListener((webview, message, lineNumber, sourceId) -> System.out
						.println("Console: [" + sourceId + ":" + lineNumber + "] " + message + "\n"));
				webEngine.setOnError(event -> System.out.println(event.getMessage()));
				webEngine.locationProperty()
						.addListener((observable, oldValue, newValue) -> System.out.println(newValue));
			}

			// When user click on the Exit item.
			exitItem.setOnAction(e -> System.exit(0));

			// reload action
			reload.setOnAction(e -> {
				webView.getEngine().reload();
				System.out.println("reloaded");
			});
			// printPage Action
			printPage.setOnAction(e -> {
				try {
					PrinterJob job = PrinterJob.createPrinterJob();
					boolean pageSetup = job.showPageSetupDialog(null);
					if (pageSetup) {
						boolean printingPage = job.showPrintDialog(null);
						if (printingPage) {
							webEngine.print(job);
							job.endJob();
						} else {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setTitle("SAVED");
							alert.setContentText("Operation Cancelled or Some error occured");
							alert.showAndWait();
							job.endJob();
						}
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("SAVED");
						alert.setContentText("Operation Cancelled or Some error occured");
						alert.showAndWait();
						job.endJob();
					}
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Failed");
					alert.setContentText("Error Occured,Plz try Again!!! ");
					alert.showAndWait();
				}
			});

			Scene companySnapshotScene = new Scene(snapshotBox);
			mainPanel.setScene(companySnapshotScene);
		} catch (

		Exception e5) {
			// TODO: handle exception
			e5.printStackTrace();
			System.out.println(e5.toString());
		}
	}

	public static void main(String[] args) {
		Main companySnapshotObject = new Main();
		companySnapshotObject.runApplication();
	}

	private void runApplication() {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					initAndShowGUI();
				}
			});
		} catch (Exception e6) {
			e6.printStackTrace();
			System.out.println(e6.toString());

		}
	}
}
