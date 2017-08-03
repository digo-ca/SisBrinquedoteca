/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entidade.Brinquedo;
import entidade.Classificacao;
import entidade.Estado;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class CadastroBrinquedo extends Application {

    private AnchorPane pane;
    private static Stage stage;

    private Label lFoto;
    private ImageView img;

    private JFXTextField txNome;
    private JFXTextField txFabricante;
    private JFXTextField txFaixaEtaria;

    private JFXComboBox cbEstado;
    private JFXComboBox cbClassificacao;

    private JFXButton btCadastrar;
    private JFXButton btCancelar;

    private Spinner<Integer> sQuantidade;

    private List<Classificacao> classificacoes = Dao.listar(Classificacao.class);

    private FileChooser fileChooser;
    private byte[] bImagem;

    private Brinquedo brinquedo;

    public void setBrinquedo(Brinquedo b) {
        if (b != null) {
            brinquedo = b;
        }
    }

    //ImageView vizualizador;
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initLayout();
        initListeners();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("Cadastro Brinquedo");

        stage.setScene(scene);
        stage.setResizable(false);

        if (brinquedo != null) {
            preencheTela();
        }

        stage.initOwner(parent);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(500, 300);
        pane.getStyleClass().add("pane");

        lFoto = new Label("            Foto");
        lFoto.setStyle("-fx-border-color: blue");
        lFoto.setPrefSize(105, 120);
        pane.getChildren().add(lFoto);

        img = new ImageView();

        txNome = new JFXTextField();
        txNome.setPromptText("Nome");
        txNome.setLabelFloat(true);
        txNome.setPrefWidth(250);
        pane.getChildren().add(txNome);

        sQuantidade = new Spinner<>();
        sQuantidade.setPrefWidth(60);
        sQuantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
        pane.getChildren().add(sQuantidade);

        txFabricante = new JFXTextField();
        txFabricante.setPromptText("Fabricante");
        txFabricante.setLabelFloat(true);
        txFabricante.setPrefWidth(250);
        pane.getChildren().add(txFabricante);

        txFaixaEtaria = new JFXTextField();
        txFaixaEtaria.setPromptText("Faixa Etária: ex 5-10");
        txFaixaEtaria.setLabelFloat(true);
        pane.getChildren().add(txFaixaEtaria);

        cbEstado = new JFXComboBox(FXCollections.observableArrayList(Estado.values()));
        cbEstado.setPromptText("Estado");
        cbEstado.setLabelFloat(true);
        pane.getChildren().add(cbEstado);

        cbClassificacao = new JFXComboBox(FXCollections.observableArrayList(classificacoes));
        cbClassificacao.setPromptText("Classificação");
        cbClassificacao.setLabelFloat(true);
        //cbClassificacao.setItems(FXCollections.observableArrayList(classificacoes));
        pane.getChildren().add(cbClassificacao);

        btCadastrar = new JFXButton("Cadastrar");
        btCadastrar.getStyleClass().add("btCadastrar");
        pane.getChildren().add(btCadastrar);
        btCancelar = new JFXButton("Cancelar");
        btCancelar.getStyleClass().add("btCancelar");
        pane.getChildren().add(btCancelar);

        fileChooser = new FileChooser();
    }

    private void initLayout() {
        lFoto.setLayoutX(420);
        lFoto.setLayoutY(10);

        txNome.setLayoutX(10);
        txNome.setLayoutY(20);

        sQuantidade.setLayoutX(280);
        sQuantidade.setLayoutY(20);

        txFabricante.setLayoutX(10);
        txFabricante.setLayoutY(70);
        cbEstado.setLayoutX(10);
        cbEstado.setLayoutY(170);
        cbClassificacao.setLayoutX(10);
        cbClassificacao.setLayoutY(220);

        txFaixaEtaria.setLayoutX(10);
        txFaixaEtaria.setLayoutY(120);

        btCadastrar.setLayoutX(457);
        btCadastrar.setLayoutY(280);
        btCancelar.setLayoutX(390);
        btCancelar.setLayoutY(280);
    }

    private void initListeners() {
        btCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroBrinquedo.getStage().hide();
            }
        });

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (brinquedo == null) {
                    brinquedo = new Brinquedo();
                }
                brinquedo.setNome(txNome.getText());
                brinquedo.setFabricante(txFabricante.getText());
                brinquedo.setEstado((Estado) cbEstado.getSelectionModel().getSelectedItem());
                brinquedo.setClassificacao((Classificacao) cbClassificacao.getSelectionModel().getSelectedItem());
                brinquedo.setFoto(bImagem);
                brinquedo.setFaixaEtaria(txFaixaEtaria.getText());
                Dao.salvar(brinquedo);
                if (sQuantidade.getValue() > 1) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Salvando multiplos objetos");
                    alert.setHeaderText("Ao clicar em SIM, você irá cadastrar " + sQuantidade.getValue() + " unidades deste brinquedo");
                    alert.setContentText("Deseja proceder com esta operação?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.YES) {
                        for (int i = 1; i < sQuantidade.getValue(); i++) {
                            brinquedo = new Brinquedo();
                            brinquedo.setNome(txNome.getText());
                            brinquedo.setFabricante(txFabricante.getText());
                            brinquedo.setEstado((Estado) cbEstado.getSelectionModel().getSelectedItem());
                            brinquedo.setClassificacao((Classificacao) cbClassificacao.getSelectionModel().getSelectedItem());
                            brinquedo.setFoto(bImagem);
                            brinquedo.setFaixaEtaria(txFaixaEtaria.getText());
                            Dao.salvar(brinquedo);
                        }
                    }
                }
                CadastroBrinquedo.getStage().hide();
            }
        });

        lFoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fileChooser.setTitle("Selecione a Foto");
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Imagem", "*.png", "*.jpg"));

                File selectedFile = fileChooser.showOpenDialog(stage);

                try {
                    bImagem = imageToByte(selectedFile.getPath());
                } catch (IOException ex) {
                    Logger.getLogger(CadastroBrinquedo.class.getName()).log(Level.SEVERE, null, ex);
                }

                Image imagem = null;

                if (selectedFile != null) {
                    try {
                        imagem = new Image(selectedFile.toURI().toURL().toString(), lFoto.getWidth(), lFoto.getHeight(), false, false);
                        //imagem = new Image(selectedFile.toURI().toURL().toString());
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(CadastroBrinquedo.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    img.setImage(imagem);
                    lFoto.setText("");
                    lFoto.setGraphic(img);
                }
            }
        });
    }
    //Imagem para Byte, conversão.

    public byte[] imageToByte(String image) throws IOException {
        InputStream is = null;
        byte[] buffer = null;
        is = new FileInputStream(image);
        buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return buffer;
    }

    public void preencheTela() throws IOException {
        txNome.setText(brinquedo.getNome());
        txFabricante.setText(brinquedo.getFabricante());
        txFaixaEtaria.setText(brinquedo.getFaixaEtaria());
        cbClassificacao.getSelectionModel().select(brinquedo.getClassificacao());
        cbEstado.getSelectionModel().select(brinquedo.getEstado());
        if (brinquedo.getFoto() != null) {
            exibeFoto();
        }
    }

    public void exibeFoto() throws IOException {
        byte[] foto = null;
        BufferedImage buffer = null;
        buffer = ImageIO.read(new ByteArrayInputStream(brinquedo.getFoto()));
        Image imagem = SwingFXUtils.toFXImage(buffer, null);
        img.setImage(imagem);
        img.setFitWidth(lFoto.getPrefWidth());
        img.setFitHeight(lFoto.getPrefHeight());

        lFoto.setText("");
        lFoto.setGraphic(img);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
