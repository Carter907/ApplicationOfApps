package org.cartapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;


public class Application extends javafx.application.Application {

    private final int width = 700;
    private final int height = 500;
    private BorderPane root;
    private UIManager uiManager;
    private ForkJoinPool taskHandler;

    @Override
    public void start(Stage stage) {

        root = new BorderPane();
        uiManager = new UIManager();
        taskHandler = new ForkJoinPool();
        root.setBackground(new Background(new BackgroundFill(Color.OLDLACE, null, null)));

        MenuBar bar = new MenuBar();

        Menu file = new Menu("file");
        MenuItem openFile = new MenuItem("open file");
        file.getItems().add(openFile);


        Menu image = new Menu("image");
        MenuItem searchImage = new MenuItem("search for an image");
        image.getItems().add(searchImage);


        Menu paint = new Menu("paint");
        MenuItem draw = new MenuItem("draw or paint on a canvas");
        paint.getItems().add(draw);


        Menu text = new Menu("text");
        MenuItem mlaDocs = new MenuItem("view MLA documentation");
        text.getItems().add(mlaDocs);


        Menu notes = new Menu("note-taking");
        MenuItem openTextFile = new MenuItem("open text file");
        MenuItem reviewTextFile = new MenuItem("review notes");
        MenuItem storeTextFile = new MenuItem("save file");
        notes.getItems().addAll(openTextFile, reviewTextFile, storeTextFile);

        Menu sorting = new Menu("explore sorting");
        MenuItem sortRectangles = new MenuItem("sort rectangles");
        uiManager.giveNewNode("sort rectangles", this::sortRectanglesPane);
        sortRectangles.setOnAction(e -> {
            root.setCenter(uiManager.retrieve("sort rectangles"));
            root.requestFocus();
        });
        MenuItem viewDocumentation = new MenuItem("sorting browse sorting algorithms");
        sorting.getItems().addAll(sortRectangles, viewDocumentation);

        bar.getMenus().addAll(file, image, paint, text, notes, sorting);


        TextArea area = new TextArea();


        root.setCenter(area);
        root.setTop(bar);

        Scene scene = new Scene(root, 700, 500);

        scene.getStylesheets().add("application.css");

        stage.setScene(scene);
        stage.setTitle("database manager");
        stage.show();
    }

    public Node sortRectanglesPane() {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));
        VBox buttons = new VBox();
        buttons.setSpacing(10);
        Button sort = new Button("sort rectangles");
        uiManager.give("sort", sort);
        Button shuffle = new Button("shuffle rectangles");
        uiManager.give("shuffle", shuffle);
        HBox rects = new HBox();
        LinkedList<Rectangle> rectList = new LinkedList<>();
        sort.setOnAction(this::sortRectangles);
        shuffle.setOnAction(e -> {

            for (int i = 20; i <= 300; i += 20) {
                Rectangle element = new Rectangle(20, i, Color.WHEAT);
                element.setStroke(Color.BLACK);
                rectList.add(element);
            }
            Collections.shuffle(rectList);
            rects.getChildren().setAll(rectList);
            rectList.clear();
        });

        rects.setSpacing(5);

        for (int i = 20; i <= 300; i += 20) {
            Rectangle element = new Rectangle(20, i, Color.WHEAT);
            element.setStroke(Color.BLACK);
            rectList.add(element);
        }
        Collections.shuffle(rectList);
        rects.getChildren().addAll(rectList);
        rectList.clear();

        uiManager.give("rects", rects);
        rects.setRotate(180);
        rects.setTranslateX(-width / 4+20);
        rects.setTranslateY(-height / 4);
        pane.setCenter(rects);
        buttons.getChildren().addAll(sort, shuffle);
        pane.setLeft(buttons);

        return pane;
    }

    private void sortRectangles(ActionEvent event) {
        final int speed = 50;
        HBox rectsHouse = (HBox) uiManager.retrieve("rects");
        LinkedList<Rectangle> rects = new LinkedList<>(rectsHouse.getChildren().stream().map(n -> (Rectangle) n).collect(Collectors.toList()));
        uiManager.retrieve("sort").setDisable(true);
        uiManager.retrieve("shuffle").setDisable(true);
        taskHandler.execute(new RecursiveAction() {
            @Override
            public void compute() {
                try {

                    int length = rects.size();
                    for (int i = 1; i < length; i++) {
                        Rectangle rect = rects.get(i);
                        rect.setFill(Color.LIGHTGRAY);
                        Thread.sleep(speed);
                        int j = i - 1;
                        while (j >= 0 && rects.get(j).getHeight() > rect.getHeight()) {
                            Thread.sleep(speed);

                            rects.set(j + 1, rects.get(j));
                            Platform.runLater(() -> {
                                try {
                                    rectsHouse.getChildren().setAll(rects);
                                } catch (IllegalArgumentException e) {
                                }
                            });
                            j--;
                        }
                        rect.setFill(Color.WHEAT);
                        rects.set(j + 1, rect);
                    }


                } catch (Exception e) {
                    System.err.println("exception in sortRectangles: " + e.getMessage());
                }

                Platform.runLater(() -> {
                    uiManager.retrieve("sort").setDisable(false);
                    uiManager.retrieve("shuffle").setDisable(false);
                });
            }
        });


    }


    public static void main(String[] args) {
        launch(args);
    }
}