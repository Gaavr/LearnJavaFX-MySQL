package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.animations.Shake;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    @FXML
    private Button authSingInButton;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button loginSingUpButton;

    @FXML
    void initialize()
    {
        authSingInButton.setOnAction(actionEvent -> {
            String loginText = login_field.getText().trim();
            String loginPassword = password_field.getText().trim();

            if (!loginText.equals("") && !loginPassword.equals(""))
                loginUser(loginText, loginPassword);
            else
                System.out.println("Login or password is empty!");

        });

        loginSingUpButton.setOnAction( actionEvent ->  {
            openNewScene("/sample/signUp.fxml");
        });
    }

    private void loginUser(String loginText, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setUserName(loginText);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);

        int counter = 0;

        while (true) {
            try {
                if (!result.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            counter++;
        }

        if (counter >= 1) {
            System.out.println("Success!");
            openNewScene("/sample/app.fxml");
        }
        else {
            Shake userLoginAnim = new Shake(login_field);
           // Shake userPassAnim = new Shake(password_field);
            userLoginAnim.playAnim();
            new Shake(password_field).playAnim();
        }
    }

    public void openNewScene(String window) {
        loginSingUpButton.getScene().getWindow().hide(); //прячем первое окно

        FXMLLoader loader = new FXMLLoader(); //выделяем память на лоадер
        loader.setLocation(getClass().getResource(window)); //указываем путь к файлу со сценой

        try {
            loader.load(); //загружаем ранее указанную сцену
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot(); // указываем путь к файлу который необходимо загрузить
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

}
