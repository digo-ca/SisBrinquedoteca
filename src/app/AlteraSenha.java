/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import entidade.Monitor;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author Ivanildo
 */
public class AlteraSenha extends Application{
    private AnchorPane pane;
    private static Stage stage;
    private JFXPasswordField txSenhaAtual;
    private JFXPasswordField txNovaSenha;
    private JFXPasswordField txConfirmaSenha;
    
    private JFXButton bAltera;
    private JFXButton bCancelar;
    
    private Monitor monitor;
    
    public void setMonitor(Monitor m){
        monitor = m;
    }
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initLayout();
        initListeners();
        
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        stage.setTitle("Alteração de Senha");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        stage.showAndWait();
    }
    
    public void initComponents(){
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(400, 250);
        pane.getStyleClass().add("pane");
        
        txSenhaAtual = new JFXPasswordField();
        txSenhaAtual.setPrefSize(220, 30);
        txSenhaAtual.setPromptText("Senha Atual");
        txSenhaAtual.setLabelFloat(true);
        
        txNovaSenha = new JFXPasswordField();
        txNovaSenha.setPrefSize(220, 30);
        txNovaSenha.setPromptText("Nova Senha");
        txNovaSenha.setLabelFloat(true);
        
        txConfirmaSenha = new JFXPasswordField();
        txConfirmaSenha.setPrefSize(220, 30);
        txConfirmaSenha.setPromptText("Confirmação de Senha");
        txConfirmaSenha.setLabelFloat(true);
        
        bAltera = new JFXButton("Alterar");
        bAltera.getStyleClass().add("btCadastrar");
        bCancelar = new JFXButton("Cancelar");
        bCancelar.getStyleClass().add("btCancelar");
        
        pane.getChildren().addAll(txSenhaAtual, txNovaSenha, txConfirmaSenha, bAltera, bCancelar);
    }
    
    public void initLayout(){
        txSenhaAtual.setLayoutX(10);
        txSenhaAtual.setLayoutY(20);
        
        txNovaSenha.setLayoutX(10);
        txNovaSenha.setLayoutY(70);
        
        txConfirmaSenha.setLayoutX(10);
        txConfirmaSenha.setLayoutY(120);
        
        bAltera.setLayoutX(350);
        bAltera.setLayoutY(235);
        
        bCancelar.setLayoutX(280);
        bCancelar.setLayoutY(235);
    }
    
    public static Stage getStage(){
        return stage;
    }
    
    public void initListeners(){
        bAltera.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(txSenhaAtual.getText().equals(monitor.getSenha())){
                    if(txNovaSenha.getText().equals(txConfirmaSenha.getText())){
                        monitor.setSenha(txNovaSenha.getText());
                        Dao.salvar(monitor);
                        
                        AlteraSenha.getStage().hide();
                        
                        new Alert(Alert.AlertType.NONE, "Senha alterada com Sucesso!", ButtonType.OK).show();
                        
                    }else{
                        new Alert(Alert.AlertType.NONE, "Os campos 'Nova senha' e 'Confimação de senha' devem serem identicos", ButtonType.OK).show();
                    }
                }else{
                    new Alert(Alert.AlertType.NONE, "A senha atual não está correta", ButtonType.OK).show();
                }
            }
        });
        bCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AlteraSenha.getStage().hide();
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
