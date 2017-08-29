/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import entidade.Crianca;
import entidade.Responsavel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class CadastroCrianca extends Application {

    private AnchorPane pane;
    private static Stage stage;

    private Label lFoto;

    private JFXTextField txNome;
    private JFXDatePicker dpNascimento;
    private JFXTextField txEscola;
    private JFXComboBox cbResponsavel;

    private List<Responsavel> responsaveis = Dao.listar(Responsavel.class);
    //private CheckBox chSupervisor;
    private ObservableList<Responsavel> listItens = FXCollections.observableArrayList(responsaveis);

    private JFXButton addResp;
    private JFXButton removeResp;
    private JFXButton cadResp;

    private JFXButton btCadastrar;
    private JFXButton btCancelar;

    private TableView tabela;

    private TableColumn colunaId;
    private TableColumn colunaNome;
    private TableColumn colunaTelefone;
    private TableColumn colunaEndereco;

    private Crianca crianca;

    private FileChooser filechooser;
    private ImageView img;
    private byte[] bImagem = null;

    public void setCrianca(Crianca c) {
        crianca = c;
    }

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();
        if (crianca != null) {
            preencheTela();
        }
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("Cadastro Criança");

        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        initLayout();
        stage.initOwner(parent);
        //CadastroCrianca.stage = stage;
        stage.showAndWait();
    }

    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(600, 400); //definindo o tamanho da janela de Login
        pane.getStyleClass().add("pane");

        lFoto = new Label("Foto");
        lFoto.setAlignment(Pos.CENTER);
        //lFoto.setStyle("-fx-border-color: blue");
        lFoto.getStyleClass().add("lFoto");
        //lFoto.setPrefSize(105, 120);
        lFoto.setMaxWidth(105);
        lFoto.setMaxHeight(120);
        lFoto.setMinWidth(105);
        lFoto.setMinHeight(120);
        pane.getChildren().add(lFoto);

        txNome = new JFXTextField();
        txNome.setFocusColor(Paint.valueOf("#009999"));
        txNome.setPrefWidth(220);
        txNome.setPromptText("Nome");
        txNome.setLabelFloat(true);
        pane.getChildren().add(txNome);
        dpNascimento = new JFXDatePicker();
        dpNascimento.setPromptText("Data de Nascimento");
        dpNascimento.setEditable(false);
        //txIdade.setLabelFloat(true);
        pane.getChildren().add(dpNascimento);
        txEscola = new JFXTextField();
        txEscola.setFocusColor(Paint.valueOf("#009999"));
        txEscola.setPrefWidth(220);
        txEscola.setPromptText("Escola");
        txEscola.setLabelFloat(true);
        pane.getChildren().add(txEscola);

        cbResponsavel = new JFXComboBox(FXCollections.observableArrayList(responsaveis));
        //cbResponsavel.setEditable(true);
        cbResponsavel.setPrefWidth(190);
        cbResponsavel.setPromptText("Responsável");
        cbResponsavel.setLabelFloat(true);
        cbResponsavel.setFocusColor(Paint.valueOf("#009999"));
        pane.getChildren().add(cbResponsavel);

        cadResp = new JFXButton("+");
        cadResp.getStyleClass().add("btCadResp");
        pane.getChildren().add(cadResp);

        addResp = new JFXButton("Adicionar Responsável");
        addResp.getStyleClass().add("btAddResp");
        pane.getChildren().add(addResp);

        removeResp = new JFXButton("Remover");
        removeResp.getStyleClass().add("btRemoveResp");
        pane.getChildren().add(removeResp);

        tabela = new TableView<>();
        colunaId = new TableColumn<>("Id");
        colunaNome = new TableColumn<>("Nome");
        colunaTelefone = new TableColumn<>("Telefone");
        colunaEndereco = new TableColumn<>("Endereço");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tabela.setPrefSize(590, 170);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tabela.getColumns().addAll(colunaId, colunaNome, colunaTelefone, colunaEndereco);

        pane.getChildren().add(tabela);

        btCadastrar = new JFXButton("Cadastrar");
        btCadastrar.getStyleClass().add("btCadastrar");
        pane.getChildren().add(btCadastrar);
        btCancelar = new JFXButton("Cancelar");
        btCancelar.getStyleClass().add("btCancelar");
        pane.getChildren().add(btCancelar);

        filechooser = new FileChooser();
        img = new ImageView();
    }

    private void initLayout() {
        lFoto.setLayoutX(490);
        lFoto.setLayoutY(10);

        txNome.setLayoutX(10);
        txNome.setLayoutY(20);
        dpNascimento.setLayoutX(10);
        dpNascimento.setLayoutY(70);
        txEscola.setLayoutX(10);
        txEscola.setLayoutY(120);
        cbResponsavel.setLayoutX(40);
        cbResponsavel.setLayoutY(170);
        cadResp.setLayoutX(10);
        cadResp.setLayoutY(170);

        addResp.setLayoutX(395);
        addResp.setLayoutY(170);
        removeResp.setLayoutX(535);
        removeResp.setLayoutY(170);

        tabela.setLayoutX(10);
        tabela.setLayoutY(200);

        btCadastrar.setLayoutX(533);
        btCadastrar.setLayoutY(380);
        btCancelar.setLayoutX(465);
        btCancelar.setLayoutY(380);
    }

    private void initListeners() {
        addResp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cbResponsavel.getSelectionModel().getSelectedIndex() != -1) {
                    tabela.getItems().add(cbResponsavel.getSelectionModel().getSelectedItem());
                    cbResponsavel.getItems().remove(cbResponsavel.getSelectionModel().getSelectedItem());
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um responsável", ButtonType.OK).show();
                }
            }
        });

        removeResp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    cbResponsavel.getItems().add(tabela.getSelectionModel().getSelectedItem());
                    tabela.getItems().remove(tabela.getSelectionModel().getSelectedIndex());
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela", ButtonType.OK).show();
                }
            }
        });

        btCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroCrianca.getStage().hide();
            }
        });

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (crianca == null) {
                    crianca = new Crianca();
                }

                if ((txNome.getText().trim().isEmpty()) || dpNascimento.getValue().equals(null) || txEscola.getText().trim().isEmpty()
                        || tabela.getItems().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Preencha todos os campos", ButtonType.OK).showAndWait();
                } else {
                    crianca.setNome(txNome.getText());
                    crianca.setNascimento(dpNascimento.getValue());
                    crianca.setEscola(txEscola.getText());
                    crianca.setResponsaveis(tabela.getItems());
                    crianca.setFoto(bImagem);
                    
                    Dao.salvar(crianca);
                        
                    CadastroCrianca.getStage().hide();
                }
            }
        });

        lFoto.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                filechooser.setTitle("Selecione a Imagem");
                filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagem", "*.png", "*.jpg"));

                File selectedFile = filechooser.showOpenDialog(stage);

                try {
                    bImagem = imageToByte(selectedFile.getPath());
                } catch (IOException ex) {
                    Logger.getLogger(CadastroCrianca.class.getName()).log(Level.SEVERE, null, ex);
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

        lFoto.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (crianca == null) {
                    lFoto.setText("Inserir Foto");
                } else {
                    lFoto.setText("Alterar Foto");
                }

            }
        });
        lFoto.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lFoto.setText(null);
            }
        });
        cadResp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroResponsavel().start(CadastroCrianca.stage);
                } catch (Exception ex) {
                    Logger.getLogger(CadastroCrianca.class.getName()).log(Level.SEVERE, null, ex);
                }
                cbResponsavel.setItems(FXCollections.observableArrayList(Dao.consultarTodos(Responsavel.class)));
                cbResponsavel.getItems().removeAll(tabela.getItems());
            }
        });
    }

    private ObservableList<Responsavel> findItens() {
        ObservableList<Responsavel> itensEncontrados = FXCollections.observableArrayList();

        for (int i = 0; i < listItens.size(); i++) {
            if (listItens.get(i).getNome().equals(cbResponsavel.getSelectionModel().getSelectedItem())) {
                itensEncontrados.add(listItens.get(i));
            }
        }
        //if(listItens)

        return itensEncontrados;
    }

    public void preencheTela() throws IOException {
        txNome.setText(crianca.getNome());
        dpNascimento.setValue(crianca.getNascimento());
        txEscola.setText(crianca.getEscola());
        tabela.setItems(FXCollections.observableArrayList(crianca.getResponsaveis()));
        cbResponsavel.getItems().removeAll(crianca.getResponsaveis());
        if (crianca.getFoto() != null) {
            exibeFoto();
        }
    }

    public void exibeFoto() throws IOException {
        byte[] foto = null;
        BufferedImage buffer = null;
        buffer = ImageIO.read(new ByteArrayInputStream(crianca.getFoto()));
        Image imagem = SwingFXUtils.toFXImage(buffer, null);
        img.setImage(imagem);
        img.setFitWidth(lFoto.getMaxWidth());
        img.setFitHeight(lFoto.getMaxHeight());

        lFoto.setText("");
        lFoto.setGraphic(img);
    }

    public static Stage getStage() {
        return stage;
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

    //Byte para imagem, conversão.
    public static void main(String[] args) {
        launch(args);
    }
}
