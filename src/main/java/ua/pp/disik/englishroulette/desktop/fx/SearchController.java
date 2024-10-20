package ua.pp.disik.englishroulette.desktop.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchController {
    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private VBox dataContainer;

    @FXML
    private TableView<Person> tableView;

    @FXML
    private void initialize() {
        // search panel
        searchButton.setText("Search");
        searchButton.setOnAction(event -> loadData());
        searchButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill: #ffffff;");

        initTable();
    }

    ObservableList<Person> masterData = FXCollections.observableList(new ArrayList<>() {{
        add(
                new Person(
                        new SimpleIntegerProperty(1),
                        new SimpleStringProperty("Evelyn"),
                        new SimpleBooleanProperty(false)
                )
        );
        add(
                new Person(
                        new SimpleIntegerProperty(2),
                        new SimpleStringProperty("Everett"),
                        new SimpleBooleanProperty(false)
                )
        );
        add(
                new Person(
                        new SimpleIntegerProperty(3),
                        new SimpleStringProperty("Ellis"),
                        new SimpleBooleanProperty(true)
                )
        );
        add(
                new Person(
                        new SimpleIntegerProperty(4),
                        new SimpleStringProperty("Eleonor"),
                        new SimpleBooleanProperty(false)
                )
        );
    }});
    ObservableList<Person> results = FXCollections.observableArrayList();

    private void initTable() {
        tableView = new TableView<>(FXCollections.observableList(masterData));
        TableColumn<Person, SimpleIntegerProperty> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Person, SimpleStringProperty> name = new TableColumn<>("NAME");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Person, SimpleBooleanProperty> employed = new TableColumn<>("EMPLOYED");
        employed.setCellValueFactory(new PropertyValueFactory<>("isEmployed"));

        tableView.getColumns().addAll(id, name, employed);
        dataContainer.getChildren().add(tableView);
    }

    private void loadData() {
        String searchText = searchField.getText();

        Task<ObservableList<Person>> task = new Task<>() {
            @Override
            protected ObservableList<Person> call() throws Exception {
                updateMessage("Loading data");
                return FXCollections.observableArrayList(
                        masterData
                        .stream()
                        .filter(value -> value.nameProperty().get().toLowerCase().contains(searchText))
                        .collect(Collectors.toList())
                );
            }
        };

        task.setOnSucceeded(event -> {
            results = task.getValue();
            tableView.setItems(FXCollections.observableList(results));
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
