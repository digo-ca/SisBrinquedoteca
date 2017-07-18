/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entidade.Classificacao;
import entidade.Estado;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
public class CadastroClassificacao extends Application {

    private AnchorPane pane;
    private static Stage stage;

    private Label lNome;
    private Label lDescricao;

    private JFXTextField txNome;
    private TextArea taDescricao;
    
    private JFXButton btCadastrar;
    private JFXButton btCancelar;

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("Cadastro Classificação");

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        initLayout();
        stage.initOwner(parent);
        //CadastroClassificacao.stage = stage;
        stage.showAndWait();
    }

    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(450, 200); //definindo o tamanho da janela de Login
        pane.getStyleClass().add("pane");

        lNome = new Label("Nome");
        //pane.getChildren().add(lNome);
        lDescricao = new Label("Descrição");
        pane.getChildren().add(lDescricao);

        txNome = new JFXTextField();
        txNome.setPromptText("Nome");
        txNome.setLabelFloat(true);
        pane.getChildren().add(txNome);
        taDescricao = new TextArea();
        taDescricao.setWrapText(true);
        taDescricao.setPrefSize(220, 105);
        pane.getChildren().add(taDescricao);
        
        btCadastrar = new JFXButton("Cadastrar");
        btCadastrar.getStyleClass().add("btCadastrar");
        pane.getChildren().add(btCadastrar);
        btCancelar = new JFXButton("Cancelar");
        btCancelar.getStyleClass().add("btCancelar");
        pane.getChildren().add(btCancelar);

        /*pane.getChildren().addAll(lNome, lNomeUser, lSenha, lComfirmacaoSenha, lSupervisor, txNome, txNomeUser,
                pfSenha);*/
    }

    private void initLayout() {
        lNome.setLayoutX(10);
        lNome.setLayoutY(10);
        lDescricao.setLayoutX(10);
        lDescricao.setLayoutY(60);

        txNome.setLayoutX(10);
        txNome.setLayoutY(20);
        taDescricao.setLayoutX(10);
        taDescricao.setLayoutY(80);

        btCadastrar.setLayoutX(387);
        btCadastrar.setLayoutY(180);
        btCancelar.setLayoutX(321);
        btCancelar.setLayoutY(180);
    }

    private void initListeners() {
        btCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroClassificacao.getStage().close();
            }
        });

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Classificacao c = new Classificacao();
                
                c.setNome(txNome.getText());
                c.setDescricao(taDescricao.getText());
                
                Dao.salvar(c);
                CadastroClassificacao.getStage().close();
            }
        });
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
