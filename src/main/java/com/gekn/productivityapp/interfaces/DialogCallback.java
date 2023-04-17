package com.gekn.productivityapp.interfaces;

import com.gekn.productivityapp.models.ProjectModel;

public interface DialogCallback {

    void onProjectAdded(ProjectModel project);
    void onProjectDelete(ProjectModel project);
}
