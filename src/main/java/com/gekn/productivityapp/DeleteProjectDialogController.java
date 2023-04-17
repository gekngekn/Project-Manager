package com.gekn.productivityapp;

import com.gekn.productivityapp.interfaces.DialogCallback;
import com.gekn.productivityapp.models.ProjectModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeleteProjectDialogController {

    private DialogCallback callback;

    @FXML
    private Label projectName;

    private ProjectModel project;

    @FXML
    private void btnDeleteProject(ActionEvent event) {

        // Add project to database and tableview
        callback.onProjectDelete(project);

        closeStage(event);
    }

    @FXML
    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setProject(ProjectModel project) {
        this.project = project;
        projectName.setText(project.getName());
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }
}
