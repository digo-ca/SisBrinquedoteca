/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entidade.Responsavel;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class CadastroResponsavel extends Application{
    
    private AnchorPane pane;
    private static Stage stage;
    
    private JFXTextField txNome;
    private JFXTextField txTelefone;
    private JFXTextField txEndereco;
    private JFXTextField txNVinculo;
    private JFXTextField txVinculo;
    
    private JFXButton btCadastrar;
    private JFXButton btCancelar;
    
    private Responsavel responsavel;
    
    public void setResponsavel(Responsavel r){
        if(r != null)
            responsavel = r;
    }

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();
        if(responsavel != null){
            preencheTela();
        }
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("Cadastro Responsável");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        initLayout();
        stage.initOwner(parent);

        stage.showAndWait();
    }

    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(480, 290); //definindo o tamanho da janela de Login
        pane.getStyleClass().add("pane");

        txNome = new JFXTextField();
        txNome.setPrefWidth(220);
        txNome.setPromptText("Nome");
        txNome.setLabelFloat(true);
        pane.getChildren().add(txNome);
        txTelefone = new JFXTextField();
        txTelefone.setPromptText("Telefone");
        txTelefone.setLabelFloat(true);
        pane.getChildren().add(txTelefone);
        txEndereco = new JFXTextField();
        txEndereco.setPromptText("Endereço");
        txEndereco.setLabelFloat(true);
        pane.getChildren().add(txEndereco);
        txNVinculo = new JFXTextField();
        txNVinculo.setPromptText("Número de Vínculo");
        txNVinculo.setLabelFloat(true);
        pane.getChildren().add(txNVinculo);
        txVinculo = new JFXTextField();
        txVinculo.setPromptText("Vínculo");
        txVinculo.setLabelFloat(true);
        pane.getChildren().add(txVinculo);
        
        btCadastrar = new JFXButton("Cadastrar");
        btCadastrar.getStyleClass().add("btCadastrar");
        pane.getChildren().add(btCadastrar);
        btCancelar = new JFXButton("Cancelar");
        btCancelar.getStyleClass().add("btCancelar");
        pane.getChildren().add(btCancelar);
    }

    private void initLayout() {
        txNome.setLayoutX(10);
        txNome.setLayoutY(20);
        txTelefone.setLayoutX(10);
        txTelefone.setLayoutY(70);
        txEndereco.setLayoutX(10);
        txEndereco.setLayoutY(115);
        txNVinculo.setLayoutX(280);
        txNVinculo.setLayoutY(20);
        txVinculo.setLayoutX(280);
        txVinculo.setLayoutY(70);

        btCadastrar.setLayoutX(420);
        btCadastrar.setLayoutY(270);
        btCancelar.setLayoutX(350);
        btCancelar.setLayoutY(270);
    }

    private void initListeners() {
        btCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroResponsavel.getStage().close();
            }
        });

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(responsavel == null)
                    responsavel = new Responsavel();
                
                responsavel.setNome(txNome.getText());
                responsavel.setTelefone(txTelefone.getText());
                responsavel.setEndereco(txEndereco.getText());
                responsavel.setNumeroVinculo(Integer.parseInt(txNVinculo.getText()));
                responsavel.setVinculo(txVinculo.getText());
                
                Dao.salvar(responsavel);
                CadastroResponsavel.getStage().close();
            }
        });
    }
    
    public void preencheTela(){
        txNome.setText(responsavel.getNome());
        txTelefone.setText(responsavel.getTelefone());
        txEndereco.setText(responsavel.getEndereco());
        txVinculo.setText(responsavel.getVinculo());
        txNVinculo.setText(responsavel.getNumeroVinculo()+"");
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
