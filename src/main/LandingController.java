package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.jpl7.*;
import org.jpl7.Integer;
import javax.swing.*;
import java.math.BigInteger;
import java.util.*;

public class LandingController {
    private ArrayList<Term> books = new ArrayList<>();
    private ArrayList<Term> orders = new ArrayList<>();

    @FXML
    public void searchBook() {
        books.clear();
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("How would you like to search for book?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Title");
        ButtonType buttonTypeTwo = new ButtonType("Author");
        ButtonType buttonTypeThree = new ButtonType("Isbn");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne){
            String title = JOptionPane.showInputDialog("Please input the book title: ").toLowerCase().replace(' ', '_');
            Variable X = new Variable("L1");
            Query q2 = new Query(
                    "search_title",
                    new Term[] {new Atom(title), X}
            );
            showBooks(q2);

        } else if (result.isPresent() && result.get() == buttonTypeTwo) {
            String author = JOptionPane.showInputDialog("Please input the book author: ").toLowerCase().replace(' ', '_');
            Variable X = new Variable("L1");
            Query q2 = new Query(
                    "search_author",
                    new Term[] {new Atom(author), X}
            );
            showBooks(q2);

        } else if (result.isPresent() && result.get() == buttonTypeThree) {
            int isbn = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the book author: ").toLowerCase().replace(' ', '_'));
            Variable X = new Variable("L1");
            Query q2 = new Query(
                    "search_isbn",
                    new Term[] {new Integer(isbn), X}
            );
            showBooks(q2);

        } else {
            alert.close();
        }
    }

    private void showBooks(Query q) {
        Map<String, Term> solution;
        solution = q.oneSolution();
        Term booksTerm = solution.get("L1");

        books.addAll(Arrays.asList(booksTerm.toTermArray()));

        ObservableList<String> booksList = FXCollections.<String>observableArrayList();
        for(Term book: books) {
            String title = book.args()[0].name().replace('_', ' ');
            String upper_case_title = "";
            Scanner lineScan = new Scanner(title);
            while(lineScan.hasNext()) {
                String word = lineScan.next();
                upper_case_title += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
            }

            String author = book.args()[1].name().replace('_', ' ');
            String upper_case_author= "";
            Scanner lineScan2 = new Scanner(author);
            while(lineScan2.hasNext()) {
                String word = lineScan2.next();
                upper_case_author += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
            }

            booksList.add(upper_case_title + " (" + upper_case_author + ")");
        }

        ListView<String> allBooks = new ListView<>(booksList);
        allBooks.setPrefSize(400, 200);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("List of Books");
        alert.setHeaderText("Below are list of books based on your search");

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(allBooks, 0, 0);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();
    }

    @FXML
    public void searchUser() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("How would you like to search for user?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Name");
        ButtonType buttonTypeTwo = new ButtonType("Id");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne){
            String name = JOptionPane.showInputDialog("Please input the user name: ").toLowerCase().replace(' ', '_');
            Variable X = new Variable("L1");
            Query q2 = new Query(
                    "search_name",
                    new Term[] {new Atom(name), X}
            );

            if (q2.oneSolution().get("L1").name().length() != 2) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Information Dialog");
                alert1.setHeaderText(null);
                alert1.setContentText("User " + name + " exist.");

                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Information Dialog");
                alert1.setHeaderText(null);
                alert1.setContentText("User " + name + " is not found!");

                alert1.showAndWait();
            }


        } else if (result.isPresent() && result.get() == buttonTypeTwo) {
            int id = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the user Id: "));

            Variable X = new Variable("L1");
            Query q2 = new Query(
                    "search_id",
                    new Term[] {new Integer(id), X}
            );

            if (q2.oneSolution().get("L1").name().length() != 2) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Information Dialog");
                alert1.setHeaderText(null);
                alert1.setContentText("User with ID " + id + " exist.");

                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Information Dialog");
                alert1.setHeaderText(null);
                alert1.setContentText("User with ID " + id + " is not found!");

                alert1.showAndWait();
            }

        } else {
            alert.close();
        }
    }

    @FXML
    public void searchOrders() {
        orders.clear();
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("How would you like to search for orders history?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("User Id");
        ButtonType buttonTypeTwo = new ButtonType("Book Isbn");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeOne){
            int id = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the user Id: "));
            Variable X = new Variable("L");
            Query q2 = new Query(
                    "search_emp_id",
                    new Term[] {new Integer(id), X}
            );
            showOrders(q2);

        } else if (result.isPresent() && result.get() == buttonTypeTwo) {
            int id = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the book Isbn: "));
            Variable X = new Variable("L");
            Query q2 = new Query(
                    "search_emp_isbn",
                    new Term[] {new Integer(id), X}
            );
            showOrders(q2);

        } else {
            alert.close();
        }
    }

    private void showOrders(Query q) {
        Map<String, Term> solution;
        solution = q.oneSolution();
        Term ordersTerm = solution.get("L");

        orders.addAll(Arrays.asList(ordersTerm.toTermArray()));

        ObservableList<String> orderList = FXCollections.<String>observableArrayList();
        for(Term order: orders) {
            BigInteger userId = order.args()[0].bigValue();
            BigInteger bookId = order.args()[1].bigValue();

            orderList.add(userId + " is borrowing " + bookId);
        }

        ListView<String> allBooks = new ListView<>(orderList);
        allBooks.setPrefSize(400, 200);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("List of Orders");
        alert.setHeaderText("Below are list of orders based on your search");

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(allBooks, 0, 0);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();
    }

    @FXML
    public void registerBook() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');
        String name = JOptionPane.showInputDialog("Please input the book title: ").toLowerCase().replace(' ', '_');
        String author = JOptionPane.showInputDialog("Please input the book author: ").toLowerCase().replace(' ', '_');
        int isbn = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the book ISBN: "));

        try {
            Query q2 = new Query(
                    "register_book",
                    new Term[] {new Atom(name), new Atom(author), new Integer(isbn)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Book successfully added to database!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Book is not successfully added! Error occured.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Book is not successfully added! Error occured.");
            alert.showAndWait();
        }
    }

    @FXML
    public void registerUser() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');
        String name = JOptionPane.showInputDialog("Please input the user's name: ").toLowerCase().replace(' ', '_');
        int id = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the user ID: "));

        try {
            Query q2 = new Query(
                    "register_user",
                    new Term[] {new Atom(name), new Integer(id)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("User successfully added to database!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("User is not successfully added! Error occurred.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("User is not successfully added! Error occurred.");
            alert.showAndWait();
        }
    }

    @FXML
    public void borrowBook() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        int id = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the user ID: "));
        int isbn = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the book ISBN: "));

        try {
            Query q2 = new Query(
                    "borrow_book",
                    new Term[] {new Integer(id), new Integer(isbn)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("You have ordered book successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error occurred while ordering.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error occurred while ordering.");
            alert.showAndWait();
        }
    }

    @FXML
    public void returnBook() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        int isbn = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the book ISBN: "));

        try {
            Query q2 = new Query(
                    "return",
                    new Term[] {new Integer(isbn)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("You have returned book successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error occurred while returning.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error occurred while returning.");
            alert.showAndWait();
        }
    }

    @FXML
    public void removeUser() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        int id = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the user ID: "));

        try {
            Query q2 = new Query(
                    "remove_user",
                    new Term[] {new Integer(id)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("User removed successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("User has not return their books.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error occurred while removing.");
            alert.showAndWait();
        }
    }

    @FXML
    public void removeBook() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        int isbn = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the book ISBN: "));

        try {
            Query q2 = new Query(
                    "remove_book",
                    new Term[] {new Integer(isbn)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Book removed successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Book has not been returned.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error occurred while removing.");
            alert.showAndWait();
        }
    }

    @FXML
    public void editUser() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        int id = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the user ID to be edited: "));
        String name = JOptionPane.showInputDialog("Please input the user's new name: ").toLowerCase().replace(' ', '_');

        try {
            Query q2 = new Query(
                    "edit_user",
                    new Term[] {new Atom(name), new Integer(id)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("User edited successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("User is still borrowing books.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error occurred while editing user.");
            alert.showAndWait();
        }
    }

    @FXML
    public void editBook() {
        Query q1 = new Query("consult('kb.pl').");
        System.out.println( "consult " + (q1.hasSolution() ? "succeeded" : "failed") + '\n');

        int isbn = java.lang.Integer.parseInt(JOptionPane.showInputDialog("Please input the book ISBN to be edited: "));
        String title = JOptionPane.showInputDialog("Please input the book's new title: ").toLowerCase().replace(' ', '_');
        String author = JOptionPane.showInputDialog("Please input the book's new author: ").toLowerCase().replace(' ', '_');

        try {
            Query q2 = new Query(
                    "edit_book",
                    new Term[] {new Atom(title), new Atom(author), new Integer(isbn)}
            );

            if (q2.hasSolution()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Book edited successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Book has not been returned.");
                alert.showAndWait();
            }

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error occurred while editing book.");
            alert.showAndWait();
        }
    }
}
