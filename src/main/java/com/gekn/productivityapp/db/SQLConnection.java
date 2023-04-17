package com.gekn.productivityapp.db;

import com.gekn.productivityapp.models.ProjectModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLConnection {

    public SQLConnection() {
    }

    /**
     * Connect to DB
     */
    public void connect() {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:productivity.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("create table if not exists project (" +
                    "id integer PRIMARY KEY AUTOINCREMENT, " +
                    "name string, " +
                    "desc string, " +
                    "totalTime long, " +
                    "lastTime long, " +
                    "dateCreated string, " +
                    "dateUpdated string, " +
                    "dateFinished string, " +
                    "status integer)"
            );

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public void resetTable(String table) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:productivity.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("drop table if exists project");
            statement.executeUpdate("create table if not exists project (" +
                    "id integer PRIMARY KEY AUTOINCREMENT, " +
                    "name string, " +
                    "desc string, " +
                    "totalTime long, " +
                    "lastTime long, " +
                    "dateCreated string, " +
                    "dateUpdated string, " +
                    "dateFinished string, " +
                    "status integer)"
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Update single project in db
     * @param project
     */
    public void updateProject(ProjectModel project) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:productivity.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("insert or replace into project values(" +
                    project.getId() + ", " +
                    "'" + project.getName() + "'" + ", " +
                    "'" + project.getDesc() + "'" + ", " +
                    project.getTotalTime() + ", " +
                    project.getLastTime() + ", " +
                    "'" + project.getDateCreated() + "'" + ", " +
                    "'" + project.getDateUpdated() + "'" + ", " +
                    "'" + project.getDateFinished() + "'" + ", " +
                    project.getStatus() + ")"
            );

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Add projects to db
     * @param projects
     */
    public void updateProjects(List<ProjectModel> projects) {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:productivity.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            for (ProjectModel p: projects) {
                statement.executeUpdate("insert or replace into project values(" +
                        p.getId() + ", " +
                        "'" + p.getName() + "'" + ", " +
                        "'" + p.getDesc() + "'" + ", " +
                        p.getTotalTime() + ", " +
                        p.getLastTime() + ", " +
                        "'" + p.getDateCreated() + "'" + ", " +
                        "'" + p.getDateUpdated() + "'" + ", " +
                        "'" + p.getDateFinished() + "'" + ", " +
                        p.getStatus() + ")"
                );
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    /**
     * Get single project from db
     * @param name
     * @return
     */
    public ProjectModel getProject(String name) {

        ProjectModel project = null;
        String query = "SELECT * FROM project WHERE name = ?";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:productivity.db");
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                project = new ProjectModel.ProjectBuilder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .desc(rs.getString("desc"))
                        .totalTime(rs.getLong("totalTime"))
                        .lastTime(rs.getLong("lastTime"))
                        .dateCreated(rs.getString("dateCreated"))
                        .dateUpdated(rs.getString("dateUpdated"))
                        .dateFinished(rs.getString("dateFinished"))
                        .status(rs.getInt("status")).build();
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return project;
    }

    /**
     * Get all projects from DB
     * @return
     */
    public List<ProjectModel> getAllProjects() {

        List<ProjectModel> projectList = new ArrayList<>();

        String query = "SELECT * FROM project";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:productivity.db");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                ProjectModel project = new ProjectModel.ProjectBuilder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .desc(rs.getString("desc"))
                        .totalTime(rs.getLong("totalTime"))
                        .lastTime(rs.getLong("lastTime"))
                        .dateCreated(rs.getString("dateCreated"))
                        .dateUpdated(rs.getString("dateUpdated"))
                        .dateFinished(rs.getString("dateFinished"))
                        .status(rs.getInt("status")).build();

                projectList.add(project);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return projectList;
    }

    public void deleteProject(ProjectModel project) {

        Connection connection = null;
        String query = "DELETE FROM project WHERE name = ?";

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:productivity.db");
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, project.getName());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }


}
