package com.gekn.productivityapp;

import com.gekn.productivityapp.db.SQLConnection;
import com.gekn.productivityapp.interfaces.DialogCallback;
import com.gekn.productivityapp.models.ProjectModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class AddProjectDialogController {

    private DialogCallback callback;

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfDesc;

    @FXML
    private TextField tfStatus;

    private ObservableList<ProjectModel> projectObservableList;

    @FXML
    private void btnAddNewProject(ActionEvent event) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);

        // Get the highest id in db
        ArrayList<Integer> lastId = new ArrayList<>();
        for (ProjectModel p : projectObservableList) {
            lastId.add(p.getId());
        }

        // Assign project with the highest id + 1
        int id = Collections.max(lastId) + 1;

        ProjectModel project = new ProjectModel.ProjectBuilder()
                .id(id)
                .name(tfName.getText())
                .desc(tfDesc.getText())
                .totalTime(0)
                .lastTime(0)
                .dateCreated(ts)
                .dateUpdated(ts)
                .dateFinished(null)
                .status(Integer.parseInt(tfStatus.getText())).build();

//        SQLConnection connection = new SQLConnection();
//        connection.updateProject(project);

        projectObservableList.add(project);

        // Add project to database and tableview
        callback.onProjectAdded(project);

        closeStage(event);
    }

    @FXML
    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    public void setProjectObservableList(ObservableList<ProjectModel> list) {
        this.projectObservableList = list;
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }

}
