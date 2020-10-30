package meshCreator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import meshCreator.threeDimensions.RMCreator3D;
import meshCreator.twoDimensionTimer.Timer2D;
import meshCreator.twoDimensions.RMCreator2D;

public class RMStart extends Application {

	@Override
	public void start(Stage primary_stage) {
		HBox hbox = new HBox();
		Button button_2d = new Button("New 2D Mesh");
		button_2d.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Stage stage = new Stage();
				RMCreator2D amc = new RMCreator2D();
				amc.start(stage);
				((Node) (t.getSource())).getScene().getWindow().hide();
			}
		});
		Button button_2d_timer = new Button("2D Timer");
		button_2d_timer.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Stage stage = new Stage();
				Timer2D amc = new Timer2D();
				amc.start(stage);
				((Node) (t.getSource())).getScene().getWindow().hide();
			}
		});
		Button button_3d = new Button("New 3D Mesh");
		button_3d.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Stage stage = new Stage();
				RMCreator3D amc = new RMCreator3D();
				amc.start(stage);
				((Node) (t.getSource())).getScene().getWindow().hide();
			}
		});

		hbox.getChildren().addAll(button_2d, button_2d_timer, button_3d);
		StackPane root = new StackPane();
		root.getChildren().add(hbox);

		Scene scene = new Scene(root, 500, 500);
		primary_stage.setTitle("Refined Mesh Creator");
		primary_stage.setScene(scene);
		primary_stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
