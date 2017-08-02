/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import cadastro.CadastroOcorrencia;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import entidade.Brinquedo;
import entidade.DiarioDeBordo;
import entidade.ItemDiarioDeBordo;
import entidade.Monitor;
import java.awt.Color;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class DiarioBordo extends Application {

    private AnchorPane pane;
    private static Stage stage;
    private Label lData;
    private Label lOcorrencia;
    private Label lBrinquedo;

    private JFXDatePicker dpData;
    private JFXTextField txMAbriu;
    private JFXTextField txMFechou;
    private JFXTextField txVisitas;
    private JFXComboBox cbBrinquedos;

    //Botões
    private JFXButton bAddBrinquedos;
    private JFXButton bRemoveBrinquedos;
    private JFXButton bAddOcorrencia;
    private JFXButton bEditaOcorrencia;
    private JFXButton bRemoveOcorrencia;

    private TableView tabelaBrinquedos;
    private TableColumn colunaId;
    private TableColumn colunaNome;

    private TableView tabelaOcorrencia;
    private TableColumn id;
    private TableColumn descricao;
    private TableColumn colunaMonitor;

    private StackPane stackPane;
    
    private Separator sBrinquedoCima;
    private Separator sBrinquedoRight;
    private Separator sBrinquedoLeft;
    private Separator sBrinquedoBotton;
    
    private Separator sOcorrenciaCima;
    private Separator sOcorrenciaRight;
    private Separator sOcorrenciaBotton;
    private Separator sOcorrenciaLeft;
    
    private JFXButton bFechar;

    Monitor monitor;
    DiarioDeBordo db;

    public void setDiario(DiarioDeBordo diario) {
        db = diario;
    }

    public void setMonitor(Monitor m) {
        monitor = m;
    }

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initLayout();
        initListeners();

        //Caso não tenha nenhum diário aberto no dia, pergunta se o usuário deseja abrir um novo============================
        if (!Dao.consultarDiarioHoje().isEmpty()) {
            db = Dao.consultarDiarioHoje().get(0);
            preencheTela(db);
//            dpData.setValue(db.getDia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            dpData.setValue(db.getDia());
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Não há nenhum diario cadastrado para esse dia! Deseja abrir um novo?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait().ifPresent(b -> {
                if (b == ButtonType.YES) {
                    db = new DiarioDeBordo();
                    db.setDia(LocalDate.now());
                    db.setMonitorAbriu(monitor);

                    Dao.salvar(db);
                    dpData.setValue(db.getDia());
                    preencheTela(db);
                }
            });
        }
        //==================================================================================================================

        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Diário de Bordo");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        stage.showAndWait();
    }

    public void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(900, 500);
        pane.getStyleClass().add("pane");

        lData = new Label("Dia");
        pane.getChildren().add(lData);
        lOcorrencia = new Label("Ocorrências");
        pane.getChildren().add(lOcorrencia);
        
        lBrinquedo = new Label("Brinquedos mais usados");
        pane.getChildren().add(lBrinquedo);

        dpData = new JFXDatePicker();
        dpData.setPrefWidth(110);
        dpData.setEditable(false);
        pane.getChildren().add(dpData);
        txMAbriu = new JFXTextField();
        txMAbriu.setEditable(false);
        txMAbriu.setPrefWidth(220);
        txMAbriu.setPromptText("Monitor Abriu");
        txMAbriu.setLabelFloat(true);
        pane.getChildren().add(txMAbriu);
        txMFechou = new JFXTextField();
        txMFechou.setEditable(false);
        txMFechou.setPrefWidth(220);
        txMFechou.setPromptText("Monitor Fechou");
        txMFechou.setLabelFloat(true);
        pane.getChildren().add(txMFechou);
        txVisitas = new JFXTextField();
        txVisitas.setEditable(false);
        txVisitas.setPrefWidth(50);
        txVisitas.setPromptText("Visitas");
        txVisitas.setLabelFloat(true);
        pane.getChildren().add(txVisitas);
        cbBrinquedos = new JFXComboBox(FXCollections.observableArrayList(Dao.listar(Brinquedo.class)));
        cbBrinquedos.setPrefWidth(173);
        cbBrinquedos.setPromptText("Selecione o Brinquedo");
        cbBrinquedos.setLabelFloat(true);
        pane.getChildren().add(cbBrinquedos);

        bAddBrinquedos = new JFXButton("Adicionar Brinquedo");
        bAddBrinquedos.setPrefWidth(150);
        bAddBrinquedos.getStyleClass().add("btAddBrinquedo");
        pane.getChildren().add(bAddBrinquedos);
        bRemoveBrinquedos = new JFXButton("Remover Brinquedo");
        bRemoveBrinquedos.setPrefWidth(150);
        bRemoveBrinquedos.getStyleClass().add("btRemoveBrinquedo");
        pane.getChildren().add(bRemoveBrinquedos);
        bAddOcorrencia = new JFXButton("Adicionar Ocorrência");
        bAddOcorrencia.setPrefWidth(150);
        bAddOcorrencia.getStyleClass().add("btAddOcorrencia");
        pane.getChildren().add(bAddOcorrencia);

        bEditaOcorrencia = new JFXButton("Editar");
        bEditaOcorrencia.setPrefWidth(150);
        pane.getChildren().add(bEditaOcorrencia);

        bRemoveOcorrencia = new JFXButton("Remover Ocorrência");
        bRemoveOcorrencia.setPrefWidth(150);
        bRemoveOcorrencia.getStyleClass().add("btRemoveOcorrencia");
        pane.getChildren().add(bRemoveOcorrencia);

        sBrinquedoCima = new Separator(Orientation.HORIZONTAL);
        sBrinquedoCima.setPrefWidth(780);
        sBrinquedoLeft = new Separator(Orientation.VERTICAL);
        sBrinquedoLeft.setPrefHeight(195);
        sBrinquedoRight = new Separator(Orientation.VERTICAL);
        sBrinquedoRight.setPrefHeight(200);
        sBrinquedoBotton = new Separator(Orientation.HORIZONTAL);
        sBrinquedoBotton.setPrefWidth(913);
        
        pane.getChildren().addAll(sBrinquedoBotton, sBrinquedoCima, sBrinquedoLeft, sBrinquedoRight);
        
        tabelaBrinquedos = new TableView();
        colunaId = new TableColumn("Identificador");
        colunaNome = new TableColumn("Nome");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tabelaBrinquedos.setPrefSize(700, 170);
        tabelaBrinquedos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaBrinquedos.getColumns().addAll(colunaId, colunaNome);

        pane.getChildren().add(tabelaBrinquedos);

        sOcorrenciaCima = new Separator(Orientation.HORIZONTAL);
        sOcorrenciaCima.setPrefWidth(847);
        
        sOcorrenciaRight = new Separator(Orientation.VERTICAL);
        sOcorrenciaRight.setPrefHeight(200);
        
        sOcorrenciaBotton = new Separator(Orientation.HORIZONTAL);
        sOcorrenciaBotton.setPrefWidth(913);
        
        sOcorrenciaLeft = new Separator(Orientation.VERTICAL);
        sOcorrenciaLeft.setPrefHeight(195);
        
        pane.getChildren().addAll(sOcorrenciaCima, sOcorrenciaRight, sOcorrenciaBotton, sOcorrenciaLeft);
        
        tabelaOcorrencia = new TableView();
        id = new TableColumn("Identificador");
        descricao = new TableColumn("Descrição");
        colunaMonitor = new TableColumn("Monitor");

        id.setCellValueFactory(new PropertyValueFactory("id"));
        descricao.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaMonitor.setCellValueFactory(new PropertyValueFactory("monitor"));

        tabelaOcorrencia.setPrefSize(700, 170);
        tabelaOcorrencia.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaOcorrencia.getColumns().addAll(id, descricao, colunaMonitor);
        pane.getChildren().add(tabelaOcorrencia);
        
        bFechar = new JFXButton("Fechar diário");
        bFechar.getStyleClass().add("btFechaDiario");
        pane.getChildren().add(bFechar);
    }

    public void initLayout() {
        lData.setLayoutX(10);
        lData.setLayoutY(23);
        
        lBrinquedo.setLayoutX(10);
        lBrinquedo.setLayoutY(71);
        lOcorrencia.setLayoutX(10);
        lOcorrencia.setLayoutY(295);

        dpData.setLayoutX(50);
        dpData.setLayoutY(20);
        txMAbriu.setLayoutX(290);
        txMAbriu.setLayoutY(20);
        txMFechou.setLayoutX(580);
        txMFechou.setLayoutY(20);
        txVisitas.setLayoutX(878);
        txVisitas.setLayoutY(20);
        cbBrinquedos.setLayoutX(25);
        cbBrinquedos.setLayoutY(105);

        bAddBrinquedos.setLayoutX(25);
        bAddBrinquedos.setLayoutY(195);
        bRemoveBrinquedos.setLayoutX(25);
        bRemoveBrinquedos.setLayoutY(238);
        bAddOcorrencia.setLayoutX(25);
        bAddOcorrencia.setLayoutY(340);
        bEditaOcorrencia.setLayoutX(25);
        bEditaOcorrencia.setLayoutY(380);
        bRemoveOcorrencia.setLayoutX(25);
        bRemoveOcorrencia.setLayoutY(420);

        sBrinquedoCima.setLayoutX(145);
        sBrinquedoCima.setLayoutY(80);
        sBrinquedoLeft.setLayoutX(12);
        sBrinquedoLeft.setLayoutY(85);
        sBrinquedoRight.setLayoutX(924);
        sBrinquedoRight.setLayoutY(80);
        sBrinquedoBotton.setLayoutX(13);
        sBrinquedoBotton.setLayoutY(280);
        tabelaBrinquedos.setLayoutX(210);
        tabelaBrinquedos.setLayoutY(95);
        
        sOcorrenciaCima.setLayoutX(80);
        sOcorrenciaCima.setLayoutY(305);
        sOcorrenciaRight.setLayoutX(924);
        sOcorrenciaRight.setLayoutY(305);
        sOcorrenciaBotton.setLayoutX(13);
        sOcorrenciaBotton.setLayoutY(504);
        sOcorrenciaLeft.setLayoutX(12);
        sOcorrenciaLeft.setLayoutY(310);
        tabelaOcorrencia.setLayoutX(210);
        tabelaOcorrencia.setLayoutY(320);
        
        bFechar.setLayoutX(840);
        bFechar.setLayoutY(530);
    }

    public void initListeners() {
        dpData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<DiarioDeBordo> listaDiario = Dao.listar(DiarioDeBordo.class);
                int existe = -1;
//                Date data = new Date();
//                data = Date.from(dpData.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                for (int i = 0; i < listaDiario.size(); i++) {
                    if (listaDiario.get(i).getDia().equals(dpData.getValue())) {
                        preencheTela(listaDiario.get(i));
                        existe = 0;
                    }
                }
                if (existe == -1) {
                    preencheTela(null);
                    txMAbriu.setText("");
                    txMFechou.setText("");
                    txVisitas.setText("");
                }
            }
        });
        bAddBrinquedos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cbBrinquedos.getSelectionModel().getSelectedIndex() != -1) {
                    db.getBrinquedosMaisUsados().add((Brinquedo) cbBrinquedos.getSelectionModel().getSelectedItem());
                    tabelaBrinquedos.getItems().add(cbBrinquedos.getSelectionModel().getSelectedItem());
                    cbBrinquedos.getItems().remove(cbBrinquedos.getSelectionModel().getSelectedItem());
                    Dao.salvar(db);
                    cbBrinquedos.getSelectionModel().select(-1);
                } else {
                    //JOptionPane.showMessageDialog(null, "Nenhum brinquedo selecionado");
                    new Alert(Alert.AlertType.NONE, "Selecione um brinquedo acima para adiciona-lo", ButtonType.OK).show();
                }
            }
        });
        bRemoveBrinquedos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabelaBrinquedos.getSelectionModel().getSelectedIndex() != -1) {
                    db.getBrinquedosMaisUsados().remove((Brinquedo) tabelaBrinquedos.getSelectionModel().getSelectedItem());
                    cbBrinquedos.getItems().add(tabelaBrinquedos.getSelectionModel().getSelectedItem());
                    tabelaBrinquedos.getItems().remove(tabelaBrinquedos.getSelectionModel().getSelectedItem());
                    Dao.salvar(db);
                } else {
                    //JOptionPane.showMessageDialog(null, "Nenhum item selecionado na tabela");
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela para ser removido", ButtonType.OK).show();
                }
            }
        });
        bAddOcorrencia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroOcorrencia cadastro = new CadastroOcorrencia();
                cadastro.setMonitor(monitor);
                try {
                    cadastro.start(DiarioBordo.stage);
                } catch (Exception ex) {
                    Logger.getLogger(DiarioBordo.class.getName()).log(Level.SEVERE, null, ex);
                }
                tabelaOcorrencia.getItems().clear();
                tabelaOcorrencia.setItems(FXCollections.observableArrayList(db.getOcorrencias()));
            }
        });
        bEditaOcorrencia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabelaOcorrencia.getSelectionModel().getSelectedIndex() != -1) {
                    CadastroOcorrencia cad = new CadastroOcorrencia();
                    cad.setOcorrencia((ItemDiarioDeBordo) tabelaOcorrencia.getSelectionModel().getSelectedItem());
                    cad.setMonitor(monitor);
                    try {
                        cad.start(DiarioBordo.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(DiarioBordo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tabelaOcorrencia.refresh();
                } else {
                    //JOptionPane.showMessageDialog(null, "Nenhum Item Selecionado na Tabela");
                    new Alert(Alert.AlertType.NONE, "Selecione um item para ser editado na tabela", ButtonType.OK).show();
                }
            }
        });
        bRemoveOcorrencia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabelaOcorrencia.getSelectionModel().getSelectedIndex() != -1) {
                    DiarioDeBordo diario;
                    diario = Dao.consultarDiarioHoje().get(0);
                    diario.getOcorrencias().remove((ItemDiarioDeBordo) tabelaOcorrencia.getSelectionModel().getSelectedItem());
                    Dao.salvar(diario);
                    try {
                        Dao.remover(tabelaOcorrencia.getSelectionModel().getSelectedItem());
                    } catch (SQLIntegrityConstraintViolationException ex) {
                        Logger.getLogger(DiarioBordo.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex);
                    }
                    tabelaOcorrencia.setItems(FXCollections.observableArrayList(db.getOcorrencias()));
                } else {
                    //JOptionPane.showMessageDialog(null, "Nenhum Item Selecionado na Tabela");
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela para ser removido", ButtonType.OK).show();
                }
            }
        });
    }

    public void abilitaDesabilitaComponets(Boolean b) {
        tabelaOcorrencia.setDisable(b);
        tabelaBrinquedos.setDisable(b);
        txVisitas.setDisable(b);
        txMAbriu.setDisable(b);
        txMFechou.setDisable(b);
        cbBrinquedos.setDisable(b);
        bAddBrinquedos.setDisable(b);
        bAddOcorrencia.setDisable(b);
        bEditaOcorrencia.setDisable(b);
        bRemoveBrinquedos.setDisable(b);
        bRemoveOcorrencia.setDisable(b);
        if (b) {
            tabelaOcorrencia.setItems(null);
            tabelaBrinquedos.setItems(null);
        }
        if (b) {
            dpData.setStyle("-fx-border-color: red;-fx-border-width: 2;");
        } else {
            dpData.setStyle("-fx-border-width: 0;");
        }

    }

    public void preencheTela(DiarioDeBordo diario) {
        if (diario == null) {
            abilitaDesabilitaComponets(true);
            new Alert(Alert.AlertType.NONE, "Não há um diário cadastrado para esta data", ButtonType.OK).show();
        } else {
            abilitaDesabilitaComponets(false);
//        if(diario.getMonitorAbriu() != null)
            txMAbriu.setText(diario.getMonitorAbriu() + "");

            if(diario.getMonitorFechou() != null)
                txMFechou.setText(diario.getMonitorFechou() + "");

            //       if(diario.getVisitasNoDia() >= 0)
            txVisitas.setText(diario.getVisitasNoDia() + "");

//        if(diario.getBrinquedosMaisUsados() != null){
            tabelaBrinquedos.setItems(FXCollections.observableArrayList(diario.getBrinquedosMaisUsados()));
            cbBrinquedos.getItems().removeAll(tabelaBrinquedos.getItems());
//        }

            //      if(diario.getOcorrencias() != null)
            tabelaOcorrencia.setItems(FXCollections.observableArrayList(diario.getOcorrencias()));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
