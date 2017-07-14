/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import cadastro.CadastroCrianca;
import entidade.Crianca;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class ItemCrianca extends Application {

    private AnchorPane pane;
    private ImageView img;
    private Label lFoto;
    private Label lNome;
    //private Label lNome;
    private Label lNascimento;
    private TableView tabelaResponsaveis;
    private TableColumn colunaId;
    private TableColumn colunaNome;
    private Button bEditar;
    private Button bSair;
    private static Stage stage;
    private static Crianca crianca;
    private static int index;
    
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initValues();
        initListeners();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Criança");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(600, 270);
        img = new ImageView();
        lFoto = new Label("   Foto");
        lFoto.setStyle("-fx-border-color: blue");
        lFoto.setPrefSize(200, 220);
        lNome = new Label();
        lNascimento = new Label();
        tabelaResponsaveis = new TableView();
        
        colunaId = new TableColumn("Id");
        colunaNome = new TableColumn("Nome");
        
        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory("nome"));
        
        tabelaResponsaveis.getColumns().addAll(colunaId, colunaNome);
        //tabelaResponsaveis.setDisable(true);
        tabelaResponsaveis.setPrefSize(400, 150);
        tabelaResponsaveis.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        bEditar = new Button("Editar");
        bSair = new Button("Sair");
        pane.getChildren().addAll(lNome, lFoto, lNascimento, bEditar, bSair, tabelaResponsaveis);
        
        initLayout();
        
    }
    
    public void initValues() throws ParseException{
        lNome.setText("Nome: "+crianca.getNome());
        lNascimento.setText("Data de nascimento: "+crianca.getNascimento());
        tabelaResponsaveis.setItems(FXCollections.observableArrayList(crianca.getResponsaveis()));
    }
    
    public void initListeners(){
        bEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroCrianca cadastro = new CadastroCrianca();
                cadastro.setCrianca(crianca);
                try {
                    cadastro.start(ItemCrianca.getStage());
                } catch (Exception ex) {
                    Logger.getLogger(ItemCrianca.class.getName()).log(Level.SEVERE, null, ex);
                }
                crianca = (Crianca) Dao.busca(crianca.getId(), Crianca.class);;
                try {
                    initValues();
                } catch (ParseException ex) {
                    Logger.getLogger(ItemCrianca.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        bSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ItemCrianca.getStage().hide();
                
            }
        });
    }
    
    public void initLayout(){
        lFoto.setLayoutX(10);
        lFoto.setLayoutY(10);
        lNome.setLayoutX(250);
        lNome.setLayoutY(10);
        lNascimento.setLayoutX(250);
        lNascimento.setLayoutY(50);
        bEditar.setLayoutX(602);
        bEditar.setLayoutY(255);
        bSair.setLayoutX(560);
        bSair.setLayoutY(255);
        
        tabelaResponsaveis.setLayoutX(250);
        tabelaResponsaveis.setLayoutY(80);
    }

    public static Stage getStage() {
        return stage;
    }

    public Crianca getCrianca() {
        return crianca;
    }

    public static void setCrianca(Crianca crianca) {
        ItemCrianca.crianca = crianca;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        ItemCrianca.index = index;
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
