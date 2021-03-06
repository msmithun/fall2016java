/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.database.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 *
 * @author kmhasan
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField cgpaField;
    @FXML
    private TextField nameField;

    private ObservableList<Student> studentsList;
    private int currentIndex;
    @FXML
    private ListView<Student> studentsListView;
    @FXML
    private ComboBox<Student> studentComboBox;
    @FXML
    private TableView<Student> studentsTableView;
    @FXML
    private TableColumn<Student, String> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, Number> cgpaColumn;

    private void displayCurrentStudent() {
        idField.setText(studentsList.get(currentIndex).getStudentId());
        nameField.setText(studentsList.get(currentIndex).getStudentName());
        cgpaField.setText("" + studentsList.get(currentIndex).getCgpa());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentsList = FXCollections.observableArrayList();
        currentIndex = 0;

        studentsListView.setItems(studentsList);
        studentComboBox.setItems(studentsList);
        studentsTableView.setItems(studentsList);
        
        idColumn.setCellValueFactory(
                new Callback<CellDataFeatures<Student, String>,
                        ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Student, String> param) {
                return new SimpleStringProperty(param.getValue().getStudentId());
            }
        });
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStudentName()));
        cgpaColumn.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getCgpa()));
        
        final String HOSTNAME = "172.17.0.134";
        final String DBNAME = "studentinfodb";
        final String USERNAME = "cse2015fall2016";
        final String PASSWORD = "java";
        final String DBURL = "jdbc:mysql://" + HOSTNAME + "/" + DBNAME;

        try {
            Connection connection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            System.out.println("Connected");
            Statement statement = connection.createStatement();
            String query = "select * from studentInfo;";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Student student = new Student(resultSet.getString("studentId"),
                        resultSet.getString("studentName"),
                        resultSet.getDouble("cgpa"));
                studentsList.add(student);
            }

            if (studentsList.size() > 0) {
                displayCurrentStudent();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleSaveAction(ActionEvent event) {
        String id = idField.getText();
        String name = nameField.getText();
        double cgpa = Double.parseDouble(cgpaField.getText());

        final String HOSTNAME = "172.17.0.134";
        final String DBNAME = "studentinfodb";
        final String USERNAME = "cse2015fall2016";
        final String PASSWORD = "java";
        final String DBURL = "jdbc:mysql://" + HOSTNAME + "/" + DBNAME;

        try {
            Connection connection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            System.out.println("Connected");
            Statement statement = connection.createStatement();
            String query = "insert into studentInfo values('" + id + "', '" + name + "', " + cgpa + ");";
            statement.executeUpdate(query);
            Student student = new Student(id, name, cgpa);
            studentsList.add(student);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handlePreviousAction(ActionEvent event) {
        currentIndex--;
        displayCurrentStudent();
    }

    @FXML
    private void handleNextAction(ActionEvent event) {
        currentIndex++;
        displayCurrentStudent();
    }

    @FXML
    private void loadStudentAction(ActionEvent event) {
        currentIndex = studentComboBox.getSelectionModel().getSelectedIndex();
        displayCurrentStudent();
    }

    @FXML
    private void handleListViewMouseClickAction(MouseEvent event) {
        currentIndex = studentsListView.getSelectionModel().getSelectedIndex();
        displayCurrentStudent();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        String id = idField.getText();
        String name = nameField.getText();
        double cgpa = Double.parseDouble(cgpaField.getText());

        final String HOSTNAME = "172.17.0.134";
        final String DBNAME = "studentinfodb";
        final String USERNAME = "cse2015fall2016";
        final String PASSWORD = "java";
        final String DBURL = "jdbc:mysql://" + HOSTNAME + "/" + DBNAME;

        try {
            Connection connection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            System.out.println("Connected");
            Statement statement = connection.createStatement();
            String query = "update studentInfo set studentName = '" + name + "', cgpa = " + cgpa + " where studentId = " + id + ";";
            statement.executeUpdate(query);
            
            Student student = new Student(id, name, cgpa);
            studentsList.set(currentIndex, student);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
