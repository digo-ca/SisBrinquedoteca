package app;

import cadastro.CadastroOcorrencia;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import entidade.Brinquedo;
import entidade.DiarioDeBordo;
import entidade.ItemDiarioDeBordo;
import entidade.Monitor;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import listagem.ListarDiarios;
import persistencia.Dao;


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

        if (db == null) {
            db = new DiarioDeBordo();
            db.setDia(LocalDate.now());
            db.setMonitorAbriu(monitor);

            Dao.salvar(db);

            preencheTela(db);
            //==================================================================================================================
        } else {
            preencheTela(db);
            dpData.setValue(db.getDia());
            if(parent.equals(ListarDiarios.getStage())){
                abilitaOcorrencias(Boolean.FALSE);
            }
        }
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
        lOcorrencia.getStyleClass().add("labelDiario");
        pane.getChildren().add(lOcorrencia);

        lBrinquedo = new Label("Brinquedos mais usados");
        lBrinquedo.getStyleClass().add("labelDiario");
        pane.getChildren().add(lBrinquedo);

        dpData = new JFXDatePicker();
        dpData.setPrefWidth(110);
        dpData.setEditable(false);
        pane.getChildren().add(dpData);
        txMAbriu = new JFXTextField();
        txMAbriu.setFocusColor(Paint.valueOf("#009999"));
        txMAbriu.setEditable(false);
        txMAbriu.setPrefWidth(220);
        txMAbriu.setPromptText("Monitor Abriu");
        txMAbriu.setLabelFloat(true);
        //txMAbriu.setFocusColor(Paint.valueOf("green"));
        pane.getChildren().add(txMAbriu);
        txMFechou = new JFXTextField();
        txMFechou.setFocusColor(Paint.valueOf("#009999"));
        txMFechou.setEditable(false);
        txMFechou.setPrefWidth(220);
        txMFechou.setPromptText("Monitor Fechou");
        txMFechou.setLabelFloat(true);
        pane.getChildren().add(txMFechou);
        txVisitas = new JFXTextField();
        txVisitas.setFocusColor(Paint.valueOf("#009999"));
        txVisitas.setEditable(false);
        txVisitas.setPrefWidth(50);
        txVisitas.setPromptText("Visitas");
        txVisitas.setLabelFloat(true);
        pane.getChildren().add(txVisitas);
        cbBrinquedos = new JFXComboBox(FXCollections.observableArrayList(Dao.consultarTodos(Brinquedo.class)));
        cbBrinquedos.setFocusColor(Paint.valueOf("#009999"));
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
        bEditaOcorrencia.getStyleClass().add("btEditaOcorrencia");
        bEditaOcorrencia.setPrefWidth(150);
        pane.getChildren().add(bEditaOcorrencia);

        bRemoveOcorrencia = new JFXButton("Remover Ocorrência");
        bRemoveOcorrencia.setPrefWidth(150);
        bRemoveOcorrencia.getStyleClass().add("btRemoveOcorrencia");
        pane.getChildren().add(bRemoveOcorrencia);

        sBrinquedoCima = new Separator(Orientation.HORIZONTAL);
        sBrinquedoCima.setPrefWidth(744);
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
        sOcorrenciaCima.setPrefWidth(830);

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
        lBrinquedo.setLayoutY(65);
        lOcorrencia.setLayoutX(10);
        lOcorrencia.setLayoutY(290);

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

        sBrinquedoCima.setLayoutX(182);
        sBrinquedoCima.setLayoutY(80);
        sBrinquedoLeft.setLayoutX(12);
        sBrinquedoLeft.setLayoutY(85);
        sBrinquedoRight.setLayoutX(924);
        sBrinquedoRight.setLayoutY(80);
        sBrinquedoBotton.setLayoutX(13);
        sBrinquedoBotton.setLayoutY(280);
        tabelaBrinquedos.setLayoutX(210);
        tabelaBrinquedos.setLayoutY(95);

        sOcorrenciaCima.setLayoutX(95);
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
                Boolean existe = false;
//                Date data = new Date();
//                data = Date.from(dpData.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                for (int i = 0; i < listaDiario.size(); i++) {
                    if (listaDiario.get(i).getDia().equals(dpData.getValue())) {
                        abilitaDesabilitaComponets(false);
                        preencheTela(listaDiario.get(i));
                        existe = true;
                    }
                }
                if (!existe) {
                    zeraTudo();
                    abilitaDesabilitaComponets(true);
                    new Alert(Alert.AlertType.INFORMATION, "Não há um diário para este dia!", ButtonType.OK).show();
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
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela para ser removido", ButtonType.OK).show();
                }
            }
        });
        bAddOcorrencia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroOcorrencia cadastro = new CadastroOcorrencia();
                cadastro.setMonitor(monitor);
                cadastro.setDiario(db);
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
                    cad.setDiario(db);
                    try {
                        cad.start(DiarioBordo.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(DiarioBordo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tabelaOcorrencia.refresh();
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item para ser editado na tabela", ButtonType.OK).show();
                }
            }
        });
        bRemoveOcorrencia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabelaOcorrencia.getSelectionModel().getSelectedIndex() != -1) {
                    db.getOcorrencias().remove((ItemDiarioDeBordo) tabelaOcorrencia.getSelectionModel().getSelectedItem());
                    Dao.salvar(db);
                    // }
                    try {
                        Dao.remover(tabelaOcorrencia.getSelectionModel().getSelectedItem());
                    } catch (SQLIntegrityConstraintViolationException ex) {
                        Logger.getLogger(DiarioBordo.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex);
                    }
                    tabelaOcorrencia.setItems(FXCollections.observableArrayList(db.getOcorrencias()));
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela para ser removido", ButtonType.OK).show();
                }
            }
        });

        bFechar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Realmente deseja Fechar o diário de Hoje?", ButtonType.YES, ButtonType.NO);
                alerta.showAndWait().ifPresent((b) -> {
                    if (b == ButtonType.YES) {
                        List<DiarioDeBordo> diarios = Dao.listar(DiarioDeBordo.class);
                        for (int i = 0; i < diarios.size(); i++) {
                            if (diarios.get(i).getDia().equals(dpData.getValue())) {
                                diarios.get(i).setMonitorFechou(monitor);
                                Dao.salvar(diarios.get(i));
                                txMFechou.setText(diarios.get(i).getMonitorFechou() + "");
                            }
                        }
                        bFechar.setDisable(true);
                        cbBrinquedos.setDisable(true);
                        bAddBrinquedos.setDisable(true);
                        bRemoveBrinquedos.setDisable(true);
                        bAddOcorrencia.setDisable(true);
                        bEditaOcorrencia.setDisable(true);
                        bRemoveOcorrencia.setDisable(true);
                    }
                });
            }
        });
    }

    public void abilitaDesabilitaComponets(Boolean b) {
        txVisitas.setDisable(b);
        txMAbriu.setDisable(b);
        txMFechou.setDisable(b);
        cbBrinquedos.setDisable(b);
        bAddBrinquedos.setDisable(b);
        bAddOcorrencia.setDisable(b);
        bEditaOcorrencia.setDisable(b);
        bRemoveBrinquedos.setDisable(b);
        bRemoveOcorrencia.setDisable(b);
        bFechar.setDisable(b);
    }
    
    public void abilitaOcorrencias(Boolean b){
        bAddOcorrencia.setDisable(b);
        bEditaOcorrencia.setDisable(b);
        bRemoveOcorrencia.setDisable(b);
    }

    public void zeraTudo() {
        txMAbriu.setText("");
        txMFechou.setText("");
        txVisitas.setText("");
        tabelaBrinquedos.setItems(null);
        tabelaOcorrencia.setItems(null);

    }

    public void preencheTela(DiarioDeBordo diario) {

        txMAbriu.setText(diario.getMonitorAbriu().getNome());
        txVisitas.setText(diario.getVisitasNoDia() + "");

        dpData.setValue(diario.getDia());

        tabelaBrinquedos.setItems(FXCollections.observableArrayList(diario.getBrinquedosMaisUsados()));
        cbBrinquedos.getItems().removeAll(tabelaBrinquedos.getItems());

        tabelaOcorrencia.setItems(FXCollections.observableArrayList(diario.getOcorrencias()));

        if (diario.getMonitorFechou() == null) {
            txMFechou.setText("");
            bFechar.setDisable(false);
        } else {
            txMFechou.setText(diario.getMonitorFechou().getNome());
            bFechar.setDisable(true);
            abilitaDesabilitaComponets(Boolean.TRUE);
            new Alert(Alert.AlertType.INFORMATION, "Este diário está fechado", ButtonType.OK).showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
