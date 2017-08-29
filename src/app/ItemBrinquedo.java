/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import com.jfoenix.controls.JFXButton;
import entidade.Brinquedo;
import entidade.Crianca;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialException;

/**
 *
 * @author Ivanildo
 */
public class ItemBrinquedo extends Application{
    private AnchorPane pane;
    private ImageView img;
    private Label lFoto;
    private Label lNome;
    private Label lEstado;
    private Label lFabricante;
    private Label lFaixaEtaria;
    private Label lClassificacao;
    
    private JFXButton bSair;
    private static Stage stage;
    private static Brinquedo brinquedo;
    private static int index;

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initValues();
        initListeners();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        stage.setTitle("Criança");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        stage.showAndWait();
    }

    public void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(600, 270);
        pane.getStyleClass().add("pane");
        img = new ImageView();
        lFoto = new Label("Não possui foto");
        lFoto.setStyle("-fx-border-color: black");
        lFoto.setPrefSize(200, 220);
        lFoto.setMaxSize(200, 220);
        lFoto.setAlignment(Pos.CENTER);
        lNome = new Label();
        lEstado = new Label();
        lFabricante = new Label();
        lFaixaEtaria = new Label();
        
        lClassificacao = new Label();

        bSair = new JFXButton("Fechar");
        bSair.getStyleClass().add("btCancelar");
        pane.getChildren().addAll(lNome, lFoto, lEstado, lFabricante, lFaixaEtaria, lClassificacao, bSair);

        initLayout();

    }

    public void initValues() throws ParseException, SQLException, SerialException, IOException{
        lNome.setText("Nome:       " + brinquedo.getNome());
        lEstado.setText("Estado:      "+ brinquedo.getEstado());
        lFabricante.setText("Fabricante: "+ brinquedo.getFabricante());
        lFaixaEtaria.setText("Faixa-Etária: "+ brinquedo.getFaixaEtaria());
        lClassificacao.setText("Classificação: "+brinquedo.getClassificacao());
        if(brinquedo.getFoto() != null)
            exibeFoto();
    }
    
    public void exibeFoto() throws IOException{
        byte[] foto = null;
        BufferedImage buffer = null;
        buffer = ImageIO.read(new ByteArrayInputStream(brinquedo.getFoto()));
        Image imagem = SwingFXUtils.toFXImage(buffer, null);                
        img.setImage(imagem);
        img.setFitWidth(lFoto.getPrefWidth());
        img.setFitHeight(lFoto.getPrefHeight());
        //img.setPreserveRatio(true);
        
        lFoto.setText("");
        lFoto.setGraphic(img);
    }

    public void initListeners() {
        bSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ItemBrinquedo.getStage().hide();

            }
        });
    }

    public void initLayout() {
        lFoto.setLayoutX(10);
        lFoto.setLayoutY(10);
        lNome.setLayoutX(250);
        lNome.setLayoutY(10);
        lEstado.setLayoutX(250);
        lEstado.setLayoutY(50);
        lFabricante.setLayoutX(250);
        lFabricante.setLayoutY(90);
        lFaixaEtaria.setLayoutX(250);
        lFaixaEtaria.setLayoutY(130);
        lClassificacao.setLayoutX(250);
        lClassificacao.setLayoutY(170);
        bSair.setLayoutX(598);
        bSair.setLayoutY(255);
    }

    public static Stage getStage() {
        return stage;
    }

    public Brinquedo getBrinquedo() {
        return brinquedo;
    }

    public static void setBrinquedo(Brinquedo brinquedo) {
        ItemBrinquedo.brinquedo = brinquedo;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        ItemBrinquedo.index = index;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
