package com.gekn.productivityapp;

import com.gekn.productivityapp.db.SQLConnection;
import com.gekn.productivityapp.interfaces.DialogCallback;
import com.gekn.productivityapp.models.ProjectModel;
import com.gekn.productivityapp.utils.Helper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable, DialogCallback {

    public Label projectNameTitle;
    public Label editId;
    public TextField editName;
    public TextField editDesc;
    public Label editTotalTime;
    public Label editLastTime;
    public Label editCreated;
    public Label editUpdated;
    public Label editFinished;
    public Label editStatus;

    @FXML
    private Button sessionStartBtn;
    @FXML
    private Label totalTimeClock;
    @FXML
    private Label sessionTimeClock;
    @FXML
    protected TableView<ProjectModel> tvData;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colName;
    private final ObservableList<ProjectModel> tvObservableList = FXCollections.observableArrayList();
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button editNameBtn;
    @FXML
    private Button editDescBtn;
    @FXML
    private TextField txtAddItem;

    @FXML
    private HBox timerBox;

    // Project deleted restore TableView position
    private boolean deleteFlag;

    // Project added TableView select the newly added project
    private boolean addFlag;

    private final BackgroundFill bgFillRed = new BackgroundFill(
            Color.valueOf("#FF4F4F"),
            new CornerRadii(0),
            new Insets(10)
    );

    private final BackgroundFill bgFillBlue = new BackgroundFill(
            Color.valueOf("#D0F7FF"),
            new CornerRadii(0),
            new Insets(10)
    );

    private Timeline timeline;

    private long totalTime = 0;
    private long sessionTime = 0;

    // IF the Project timer is active
    private boolean timerStarted;

    // Reference to the curr selected Project
    private ProjectModel selectedProject;

    private int lastSelectedRow = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // SQLConnection instance
        SQLConnection connection = new SQLConnection();

        // Add all projects from db to TableView
        tvObservableList.addAll(connection.getAllProjects());

        // Only row selection in TableView
        tvData.getSelectionModel().setCellSelectionEnabled(false);

        // Assign Observable List to TableView
        tvData.setItems(tvObservableList);

        // Auto select the first project in the TableView
        if (tvData.getItems().size() > 0)
            tvData.getSelectionModel().selectFirst();

        // Add selection listener to the table
        tvData.getFocusModel().focusedCellProperty().addListener((observableValue, tablePosition, t1) -> {
            if (tvData.getItems().size() > 0) {
                ProjectModel p = tvData.getItems().get(t1.getRow());

                // Store ref to the curr selected Project
                setSelectedProject(p);

                // Project Title
                projectNameTitle.setText(p.getName());
                editId.setText(String.valueOf(p.getId()));
                editName.setText(p.getName());
                editDesc.setText(p.getDesc());
                editTotalTime.setText(Helper.formatDuration(p.getTotalTime()));
                editLastTime.setText(Helper.formatDuration(p.getLastTime()));
                editCreated.setText(String.valueOf(p.getDateCreated()));
                editUpdated.setText(String.valueOf(p.getDateUpdated()));
                editFinished.setText(String.valueOf(p.getDateFinished()));
                editStatus.setText(String.valueOf(p.getStatus()));

                // Reset the timers
                totalTimeClock.setText("00:00:00");
                sessionTimeClock.setText("00:00:00");
            }
        });

        ObservableList<ProjectModel> selectedProjects = tvData.getSelectionModel().getSelectedItems();
        selectedProjects.addListener(new ListChangeListener<ProjectModel>() {
            @Override
            public void onChanged(Change<? extends ProjectModel> change) {
//                System.out.println("Selected Projects selected " + change.getList());
            }
        });

        // Disable the TextField's
        editName.setDisable(true);
        editDesc.setDisable(true);

        // Add Listener to start session timer button
        sessionStartBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (timerStarted) {
                    // Change the background color
                    timerBox.setBackground(new Background(bgFillBlue));
                    sessionStartBtn.setText("START");
                    timeline.stop();
                    timerStarted = false;

                    // Reset session time
                    sessionTimeClock.setText("00:00:00");

                    // Apply the changes to Project in db
                    getSelectedProject().setTotalTime(totalTime);
                    getSelectedProject().setLastTime(sessionTime);

                    // Update the selected project
                    updateProject(getSelectedProject());
                } else {
                    // Change the background color
                    timerBox.setBackground(new Background(bgFillRed));

                    // Set the timestamps
                    totalTime = selectedProject.getTotalTime();
                    sessionTime = 0;

                    sessionStartBtn.setText("STOP");
                    timerStarted = true;

                    timeline = new Timeline();
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            totalTime = totalTime + 1000;
                            sessionTime = sessionTime + 1000;
                            totalTimeClock.setText(Helper.formatDuration(totalTime));
                            sessionTimeClock.setText(Helper.formatDuration(sessionTime));
                        }
                    }));
                    timeline.playFromStart();
                }
            }
        });

//        txtAddItem.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//                if (txtAddItem.isFocused()) {
//                    addBtn.setDisable(false);
//                }
//            }
//        });
//
//        projectList.focusedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//                if (projectList.isFocused()) {
//                    deleteBtn.setDisable(false);
//                }
//            }
//        });

    }

    /**
     * Update TableView Data
     */
    @FXML
    private void updateTable() {

        // Store ref to the last selected Project
        lastSelectedRow = tvData.getSelectionModel().getSelectedIndex();

        // SQLConnection instance
        SQLConnection connection = new SQLConnection();

        // Clear the TableView dataset
        tvObservableList.clear();

        // Add all projects to TableView
        tvObservableList.addAll(connection.getAllProjects());

        // Assign Observable List to TableView
        tvData.setItems(tvObservableList);

        // Select the correct table row
        if (deleteFlag) {   // Select first row
            lastSelectedRow = 0;
            deleteFlag = false;
        } else if (addFlag) {   // Select the newly added project
            lastSelectedRow = tvData.getItems().size() - 1;
            addFlag = false;
        }

        // Reselect the curr Project in the TableView
        if (tvData.getItems().size() > 0)
            tvData.getSelectionModel().select(lastSelectedRow);

    }

    private void updateProject(ProjectModel project) {
        // SQLConnection instance
        SQLConnection connection = new SQLConnection();

        // Apply project changes
        connection.updateProject(project);

        // Update the TableView
        updateTable();
    }

    /**
     * Callback from Add new project Dialog
     * Add project to db
     *
     * @param project ProjectModel to add to db
     */
    @Override
    public void onProjectAdded(ProjectModel project) {

        SQLConnection connection = new SQLConnection();

        // Create project in db
        connection.updateProject(project);

        // Set flag so that the TableView selects the newly added project
        addFlag = true;

        // Update TableView data
        updateTable();
    }

    /**
     * Delete project
     * @param project to be deleted
     */
    @Override
    public void onProjectDelete(ProjectModel project) {

        System.out.println("Deleted project : " + project.getName());

        // Get SQLite connection
        SQLConnection connection = new SQLConnection();
        connection.deleteProject(project);

        // Set flag so that the TableView position is correctly refreshed
        deleteFlag = true;

        // Update TableView data
        updateTable();

    }

    /**
     * Handle changes to selected project
     *
     * @param event item selected
     */
    @FXML
    protected void editInput(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();

        switch (data) {
            case "name":
                if (editNameBtn.getText().equals("Edit")) {
                    editName.setDisable(false);
                    editNameBtn.setText("Apply");
                } else {
                    editName.setDisable(true);
                    editNameBtn.setText("Edit");

                    // Set the new project name
                    getSelectedProject().setName(editName.getText());

                    // Update the selected project
                    updateProject(getSelectedProject());
                }
                break;
            case "desc":
                if (editDescBtn.getText().equals("Edit")) {
                    editDesc.setDisable(false);
                    editDescBtn.setText("Apply");
                } else {
                    editDesc.setDisable(true);
                    editDescBtn.setText("Edit");

                    // Set the new project desc
                    getSelectedProject().setDesc(editDesc.getText());

                    // Update the selected project
                    updateProject(getSelectedProject());
                }
                break;
            case "status":
                break;
        }

    }

    /**
     * Open the add new project dialog
     *
     * @param event unused
     * @throws IOException
     */
    @FXML
    private void openDialogAdd(ActionEvent event) throws IOException {
        URL fxmlLocation = getClass().getResource("add-project-dialog.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent parent = fxmlLoader.load();
        AddProjectDialogController dialogController = fxmlLoader.getController();
        dialogController.setProjectObservableList(tvObservableList);
        dialogController.setCallback(this);
        Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Open the add new project dialog
     *
     * @param event unused
     * @throws IOException
     */
    @FXML
    private void openDialogDelete(ActionEvent event) throws IOException {
        URL fxmlLocation = getClass().getResource("delete-project-dialog.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent parent = fxmlLoader.load();
        DeleteProjectDialogController dialogController = fxmlLoader.getController();
        dialogController.setProject(getSelectedProject());
        dialogController.setCallback(this);
        Scene scene = new Scene(parent, 300, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Set the selected project
     * @param project selected
     */
    private void setSelectedProject(ProjectModel project) {
        this.selectedProject = project;
    }

    /**
     * Get the currently selected Project
     * @return The selected Project
     */
    public ProjectModel getSelectedProject() {
        return selectedProject;
    }

    /*
     *
     *  DEBUG FUNCTIONS
     *
     */

    /**
     * Reset TableView data
     */
    @FXML
    private void resetTable() {

        // SQLConnection instance
        SQLConnection connection = new SQLConnection();

        // Reset table by name
        connection.resetTable("project");

        // Unselect in TableView
        tvData.getSelectionModel().select(-1);

        // Clear the Table observable list
        tvObservableList.clear();
    }

    /**
     * Add dummy data to db
     */
    @FXML
    private void addDummyData() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Timestamp timestampCreate = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(4));
        Timestamp timestampUpdate = new Timestamp(System.currentTimeMillis());

        Timestamp timestamp2Create = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2));
        Timestamp timestamp2Update = new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(6));

        Timestamp timestamp3Create = new Timestamp(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        Timestamp timestamp3Update = new Timestamp(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(4));

        String ts1Create = sdf.format(timestampCreate);
        String ts1Update = sdf.format(timestampUpdate);

        String ts2Create = sdf.format(timestamp2Create);
        String ts2Update = sdf.format(timestamp2Update);

        String ts3Create = sdf.format(timestamp3Create);
        String ts3Update = sdf.format(timestamp3Update);

        // SQLConnection instance
        SQLConnection connection = new SQLConnection();

        List<ProjectModel> projects = new ArrayList<>();

        ProjectModel project = new ProjectModel.ProjectBuilder()
                .id(1)
                .name("Test Project")
                .desc("Test desc")
                .totalTime(TimeUnit.HOURS.toMillis(8))
                .lastTime(TimeUnit.MINUTES.toMillis(68))
                .dateCreated(ts1Create)
                .dateUpdated(ts1Update)
                .dateFinished(null)
                .status(0).build();

        projects.add(project);

        project = new ProjectModel.ProjectBuilder()
                .id(2)
                .name("Test Project 2")
                .desc("Test desc 2")
                .totalTime(TimeUnit.HOURS.toMillis(4))
                .lastTime(TimeUnit.MINUTES.toMillis(45))
                .dateCreated(ts2Create)
                .dateUpdated(ts2Update)
                .dateFinished(null)
                .status(0).build();

        projects.add(project);

        project = new ProjectModel.ProjectBuilder()
                .id(3)
                .name("Test Project 3")
                .desc("Test desc 3")
                .totalTime(TimeUnit.HOURS.toMillis(2))
                .lastTime(TimeUnit.MINUTES.toMillis(30))
                .dateCreated(ts3Create)
                .dateUpdated(ts3Update)
                .dateFinished(null)
                .status(0).build();

        projects.add(project);

        // Create projects in db
        connection.updateProjects(projects);

        // Update TableView data
        updateTable();

    }

}