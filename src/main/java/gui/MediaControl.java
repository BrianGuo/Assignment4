package gui;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class MediaControl extends BorderPane {
    private MediaPlayer mp;
    private MediaView mediaView;
    private HBox mediaBar;
    Controller controller;

    public MediaControl(final MediaPlayer mp, Controller controller) {
        this.mp = mp;
        //setStyle("-fx-background-color: #bfc2c7;");
        mediaView = new MediaView(mp);
        Pane mvPane = new Pane() {                };
        mvPane.getChildren().add(mediaView);
        //mvPane.setStyle("-fx-background-color: black;");
        setCenter(mvPane);
        mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(5, 10, 5, 10));
        BorderPane.setAlignment(mediaBar, Pos.CENTER);
        this.controller = controller;
        
        try{
        	Image img = new Image(new FileInputStream(new File("play-button.png")));
        	ImageView imgv = new ImageView(img);
        	imgv.setFitHeight(20.0);
        	imgv.setFitWidth(20.0);
        	final ToggleButton playButton  = new ToggleButton("", imgv);
        	Tooltip t = new Tooltip("Step Indefinitely");
        	playButton.setTooltip(t);
        	
        	mediaBar.getChildren().add(playButton);
        	Image img2 = new Image(new FileInputStream(new File("next-button.png")));
        	ImageView imgv2 = new ImageView(img2);
        	imgv2.setFitHeight(20.0);
        	imgv2.setFitWidth(20.0);
        	final Button nextButton = new Button("", imgv2);
            nextButton.setOnAction(event ->{
                        controller.advance(1);
                    }
            );
           
        	Tooltip t2 = new Tooltip("Step Once");
            nextButton.setTooltip(t2);
        	mediaBar.getChildren().add(nextButton);
        	Slider s = new Slider(0.0,60.0,30.0);
        	mediaBar.getChildren().add(s);
        	TextField fps = new TextField();
        	fps.setMaxWidth(180.0);
        	fps.setEditable(false);
            fps.setText("Steps per second: 30");
        	s.valueProperty().addListener(evt -> {
                fps.setText("Steps per second: " + (int) s.getValue());
                if(playButton.isSelected())
                    controller.play(s.getValue());
            });
        	mediaBar.getChildren().add(fps);
            playButton.setOnAction((event) -> {
            	nextButton.setDisable(!(nextButton.isDisable()));
                System.out.println(playButton.isSelected());
                if(playButton.isSelected())
                    controller.play(s.getValue());
                else
                    controller.stop();
            });

        }
        catch(FileNotFoundException e) {
        	e.printStackTrace();
        }
        setBottom(mediaBar); 
     }
}