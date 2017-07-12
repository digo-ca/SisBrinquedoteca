/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import entidade.Monitor;
import static java.awt.Color.red;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class Login extends Application {

    private AnchorPane pane;
    private Pane inPane;
    
    private Label lLogin;
    private Label lSenha;
    private JFXTextField txLogin;
    private JFXPasswordField txSenha;
    private JFXButton btEntrar, btSair;
    private static Stage stage;
    
    private ImageView img;

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        // Remove a opção de maximizar a tela
        stage.setResizable(false);
        // Dá um título para a tela
        stage.setTitle("Autenticação");
        initLayout();
        stage.initOwner(parent);
        //Login.stage = stage;
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(500, 365); //definindo o tamanho da janela de Login
        pane.getStyleClass().add("Pane");
        inPane = new Pane();
        inPane.setPrefSize(450, 315);
        inPane.getStyleClass().add("inPane");
        
        lLogin = new Label("Login");
        lSenha = new Label("Senha");
        
        RequiredFieldValidator validator = new RequiredFieldValidator();
        //Especificando campo de login que é um textfield
        txLogin = new JFXTextField();
        txLogin.getValidators().add(validator);
        txLogin.setPrefWidth(300);
        txLogin.setPrefHeight(30);
        txLogin.setLabelFloat(true);
        txLogin.setPromptText("Login");

        validator.setMessage("Campo obrigatório");
        
        //Especificando campo de senha que é um passwordfield
        txSenha = new JFXPasswordField();
        txSenha.setPrefWidth(300);
        txSenha.setPrefHeight(30);
        txSenha.setLabelFloat(true);
        txSenha.setPromptText("Senha");

        //Especificando botões
        btEntrar = new JFXButton("Entrar");
        btEntrar.getStyleClass().add("btEntrar");
        btEntrar.setPrefSize(70, 30);

        
        btSair = new JFXButton("Sair");
        btSair.getStyleClass().add("btSair");
        btSair.setPrefWidth(40);
        
        img = new ImageView(new Image("img/unigran.png"));
        img.setFitHeight(120);
        img.setFitWidth(200);

        inPane.getChildren().addAll(txLogin, txSenha, btEntrar, btSair, img);
        pane.getChildren().add(inPane);
    }

    private void initLayout() {
        inPane.setLayoutX(30);
        inPane.setLayoutY(30);
        lLogin.setLayoutX(200);
        lLogin.setLayoutY(140);
        txLogin.setLayoutX(70);
        txLogin.setLayoutY(120);
        lSenha.setLayoutX(200);
        lSenha.setLayoutY(170);
        txSenha.setLayoutX(70);
        txSenha.setLayoutY(180);
        btEntrar.setLayoutX(300);
        btEntrar.setLayoutY(220);
        btSair.setLayoutX(10);
        btSair.setLayoutY(280);
        img.setLayoutX(120);
        img.setLayoutY(0);
    }

    private void initListeners() {
        btSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fecharAplicacao();
            }
        });

        btEntrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logar();
            }
        });
        txSenha.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logar();
            }
        });
        /*txLogin.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    txLogin.validate();
                }
            }
        });*/
    }

    private void fecharAplicacao() {
        System.exit(0);
    }

    private void logar() {
        Principal p = new Principal();
        Monitor m = autenticacao();
        if (m == null) {
            JOptionPane.showMessageDialog(null, "Login e/ou Senha inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            Login.getStage().close();
            p.setMonitor(m);
           p.start(Login.stage);
        }
    }

    public Monitor autenticacao() {
        List<Monitor> monitores = Dao.consultarTodos(Monitor.class);
        for (Monitor m : monitores) {
            if (m.getNomeUsuario().equals(txLogin.getText())) {
                //JOptionPane.showMessageDialog(null, "Passou Login");
                if (m.getSenha().equals(txSenha.getText())) {
                    return m;
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
