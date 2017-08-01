/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.skins.JFXTextAreaSkin;
import entidade.Estado;
import entidade.Livro;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
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
public class CadastroLivro extends Application{
    private AnchorPane pane;
    private static Stage stage;

    private Label lObservacoes;
    
    private JFXTextField txTitulo;
    private JFXTextField txAutor;
    private JFXTextField txEditora;
    private TextArea taObservacoes;
    //private JFXTextAreaSkin taSkin;
    private JFXComboBox cbEstado;
    
    //private CheckBox chSupervisor;
    
    private Button btCadastrar;
    private Button btCancelar;
    
    private Livro livro;
    
    public void setLivro(Livro l){
        livro = l;
    }
    
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();
        if(livro != null){
            preencheTela();
        }
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("Cadastro de Livros");
        
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        initLayout();
        stage.initOwner(parent);
        //CadastroLivro.stage = stage;
        stage.showAndWait();
    }
    
    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(550, 290);
        pane.getStyleClass().add("pane");
        
        lObservacoes=  new Label("Observações");
        pane.getChildren().add(lObservacoes);
        
        txTitulo = new JFXTextField();
        txTitulo.setPrefWidth(220);
        txTitulo.setPromptText("Título");
        txTitulo.setLabelFloat(true);
        pane.getChildren().add(txTitulo);
        txAutor = new JFXTextField();
        txAutor.setPrefWidth(220);
        txAutor.setPromptText("Autor");
        txAutor.setLabelFloat(true);
        pane.getChildren().add(txAutor);
        txEditora = new JFXTextField();
        txEditora.setPromptText("Editora");
        txEditora.setLabelFloat(true);
        pane.getChildren().add(txEditora);
        taObservacoes = new TextArea();
        taObservacoes.setWrapText(true);
        taObservacoes.setPrefSize(220, 105);
        pane.getChildren().add(taObservacoes);
        cbEstado = new JFXComboBox(FXCollections.observableArrayList(Estado.values()));
        cbEstado.setPromptText("Estado");
        cbEstado.setLabelFloat(true);
        pane.getChildren().add(cbEstado);
        
        btCadastrar = new Button("Cadastrar");
        btCadastrar.getStyleClass().add("btCadastrar");
        pane.getChildren().add(btCadastrar);
        btCancelar = new Button("Cancelar");
        btCancelar.getStyleClass().add("btCancelar");
        pane.getChildren().add(btCancelar);
    }
    private void initLayout(){
        txTitulo.setLayoutX(10);
        txTitulo.setLayoutY(20);
        txAutor.setLayoutX(10);
        txAutor.setLayoutY(65);
        txEditora.setLayoutX(10);
        txEditora.setLayoutY(110);
        cbEstado.setLayoutX(10);
        cbEstado.setLayoutY(155);
        
        lObservacoes.setLayoutX(343);
        lObservacoes.setLayoutY(20);
        taObservacoes.setLayoutX(343);
        taObservacoes.setLayoutY(50);
        
        
        btCadastrar.setLayoutX(495);
        btCadastrar.setLayoutY(270);
        btCancelar.setLayoutX(425);
        btCancelar.setLayoutY(270);
    }
    private void initListeners() {
        btCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroLivro.getStage().close();
            }
        });

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(livro == null)
                    livro = new Livro();
                
                livro.setTitulo(txTitulo.getText());
                livro.setAutor(txAutor.getText());
                livro.setEditora(txEditora.getText());
                livro.setEstado((Estado) cbEstado.getSelectionModel().getSelectedItem());
                livro.setObservacoes(taObservacoes.getText());
                
                Dao.salvar(livro);
                CadastroLivro.getStage().close();
            }
        });
    }
    
    public void preencheTela(){
        txTitulo.setText(livro.getTitulo());
        txAutor.setText(livro.getAutor());
        txEditora.setText(livro.getEditora());
        taObservacoes.setText(livro.getObservacoes());
        cbEstado.getSelectionModel().select(livro.getEstado());
    }
    
    public static Stage getStage(){
        return stage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
  
}
