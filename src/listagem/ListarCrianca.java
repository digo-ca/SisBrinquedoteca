/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listagem;

import app.ItemCrianca;
import entidade.Crianca;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class ListarCrianca extends Application{
    private AnchorPane pane;
    private TextField txPesquisa;
    private TableView tabela;
    private TableColumn colunaId;
    private TableColumn colunaNome;
    private TableColumn colunaIdade;
    private TableColumn colunaDetalhes;
    
    private static Stage stage;
    
    private Button sair;
    //private Button detalhes;
    
    private ObservableList<Crianca> criancas ;
    
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initValues();
        initListeners();
        Scene scene = new Scene(pane);
        stage.initOwner(parent);
        stage.setScene(scene);
        stage.setTitle("Tabela de Crianças");
        stage.setResizable(false);
        stage.show();
    }
    
    public void initComponents(){
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(795, 595);
        
        txPesquisa = new TextField();
        txPesquisa.setPromptText("Pesquisar");
        
        tabela = new TableView();
        colunaId = new TableColumn("Id");
        colunaNome = new TableColumn("Nome");
        colunaIdade = new TableColumn("Idade");
        colunaDetalhes = new TableColumn("Detalhes");
        
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory("nome"));
        colunaIdade.setCellValueFactory(new PropertyValueFactory("idade"));
        colunaDetalhes.setCellValueFactory(new PropertyValueFactory("detalhes"));
        
        tabela.setPrefSize(785, 550);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        sair = new Button("Sair");
        
        
        //detalhes = new Button("Detalhes");
        
        //colunaDetalhes.setGraphic(detalhes);
        initLayout();
        tabela.getColumns().addAll(colunaId, colunaNome, colunaIdade, colunaDetalhes);
        
        
        pane.getChildren().addAll(txPesquisa, tabela, sair);
    }
    
    private void initValues() {
        criancas = FXCollections.observableArrayList(Dao.listar(Crianca.class));
        tabela.setItems(criancas);
    }
    
    public void initLayout(){
        tabela.setLayoutX(10);
        tabela.setLayoutY(45);
        txPesquisa.setLayoutX(645);
        txPesquisa.setLayoutY(10);
        
        sair.setLayoutX(10);
        sair.setLayoutY(10);
    }
    
    public static Stage getStage(){
        return stage;
    }
    
    public void initListeners(){
        txPesquisa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!txPesquisa.getText().equals("")) {
                    tabela.setItems(findItens());
                }
            }
        });
        sair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarCrianca.getStage().close();
            }
        });
        tabela.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                ItemCrianca.setCrianca((Crianca) tabela.getSelectionModel().getSelectedItem());
                
                ItemCrianca.setIndex(tabela.getSelectionModel().getSelectedIndex());
                
                try {
                    new ItemCrianca().start(ListarCrianca.getStage());
                } catch (Exception ex) {
                    Logger.getLogger(ListarCrianca.class.getName()).log(Level.SEVERE, null, ex);
                }
                criancas.setAll(Dao.listar(Crianca.class));
            }
        });
    }
    
    public ObservableList<Crianca> findItens(){
        ObservableList<Crianca> itensEncontrados = FXCollections.observableArrayList();
        
        for (int i = 0; i < criancas.size(); i++) {
            if (criancas.get(i).getNome().equals(txPesquisa.getText())){
                itensEncontrados.add(criancas.get(i));
            }
        }
        
        
        return itensEncontrados;
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
