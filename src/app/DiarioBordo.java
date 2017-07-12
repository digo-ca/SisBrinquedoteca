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
import com.jfoenix.controls.JFXTextField;
import entidade.Brinquedo;
import entidade.DiarioDeBordo;
import entidade.ItemDiarioDeBordo;
import entidade.Monitor;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class DiarioBordo extends Application{
    private AnchorPane pane;
    private static Stage stage;
    private Label lData;
    
    private JFXDatePicker dpData;
    private JFXTextField txMAbriu;
    private JFXTextField txMFechou;
    private JFXTextField txVisitas;
    private JFXComboBox cbBrinquedos;
    
    //Botões
    private JFXButton bAddBrinquedos;
    private JFXButton bRemoveBrinquedos;
    private JFXButton bAddOcorrencia;
    private JFXButton bRemoveOcorrencia;
    private JFXButton bSalvar;
    
    
    private TableView tabelaBrinquedos;
    private TableColumn colunaId;
    private TableColumn colunaNome;
    
    private TableView tabelaOcorrencia;
    private TableColumn id;
    private TableColumn descricao;
    private TableColumn colunaMonitor;
    
    Monitor monitor;
    DiarioDeBordo db;
    
    public void setDiario(DiarioDeBordo diario){
        db = diario;
    }
    
    public void setMonitor(Monitor m){
        monitor = m;
    }
    
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initLayout();
        initListeners();
        db = Dao.consultarDiarioHoje().get(0);
        if(db != null){
            preencheTela(db);
            dpData.setValue(db.getDia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Diário de Bordo");
        stage.initOwner(parent);
        stage.showAndWait();
    }
    
    
    public void initComponents(){
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(900, 600);
        pane.getStyleClass().add("pane");
        
        lData = new Label("Dia");
        pane.getChildren().add(lData);
        
        dpData = new JFXDatePicker();
        dpData.setPrefWidth(110);
        dpData.setEditable(false);
        pane.getChildren().add(dpData);
        txMAbriu = new JFXTextField();
        txMAbriu.setPrefWidth(220);
        txMAbriu.setPromptText("Monitor Abriu");
        txMAbriu.setLabelFloat(true);
        pane.getChildren().add(txMAbriu);
        txMFechou = new JFXTextField();
        txMFechou.setPrefWidth(220);
        txMFechou.setPromptText("Monitor Fechou");
        txMFechou.setLabelFloat(true);
        pane.getChildren().add(txMFechou);
        txVisitas = new JFXTextField();
        txVisitas.setPrefWidth(90);
        txVisitas.setPromptText("Visitas");
        txVisitas.setLabelFloat(true);
        pane.getChildren().add(txVisitas);
        cbBrinquedos = new JFXComboBox(FXCollections.observableArrayList(Dao.listar(Brinquedo.class)));
        cbBrinquedos.setPrefWidth(185);
        cbBrinquedos.setPromptText("Selecione o Brinquedo");
        cbBrinquedos.setLabelFloat(true);
        pane.getChildren().add(cbBrinquedos);
        
        bAddBrinquedos = new JFXButton("Adicionar Brinquedo");
        bAddBrinquedos.getStyleClass().add("btAddBrinquedo");
        pane.getChildren().add(bAddBrinquedos);
        bRemoveBrinquedos = new JFXButton("Remover Brinquedo");
        bRemoveBrinquedos.getStyleClass().add("btRemoveBrinquedo");
        pane.getChildren().add(bRemoveBrinquedos);
        bAddOcorrencia = new JFXButton("Adicionar Ocorrência");
        bAddOcorrencia.getStyleClass().add("btAddOcorrencia");
        pane.getChildren().add(bAddOcorrencia);
        bRemoveOcorrencia = new JFXButton("Remover Ocorrência");
        bRemoveOcorrencia.getStyleClass().add("btRemoveOcorrencia");
        pane.getChildren().add(bRemoveOcorrencia);
        
        tabelaBrinquedos = new TableView();
        colunaId = new TableColumn("Id");
        colunaNome = new TableColumn("Nome");
        
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        tabelaBrinquedos.setPrefSize(729, 170);
        tabelaBrinquedos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaBrinquedos.getColumns().addAll(colunaId, colunaNome);
        
        pane.getChildren().add(tabelaBrinquedos);
        
        tabelaOcorrencia = new TableView(FXCollections.observableArrayList(Dao.listar(ItemDiarioDeBordo.class)));
        id = new TableColumn("Id");
        descricao = new TableColumn("Descrição");
        colunaMonitor = new TableColumn("Monitor");
        
        id.setCellValueFactory(new PropertyValueFactory("id"));
        descricao.setCellValueFactory(new PropertyValueFactory("descricao"));
        colunaMonitor.setCellValueFactory(new PropertyValueFactory("monitor"));
        
        tabelaOcorrencia.setPrefSize(729, 170);
        tabelaOcorrencia.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaOcorrencia.getColumns().addAll(id, descricao, colunaMonitor);
        pane.getChildren().add(tabelaOcorrencia);
        
        bSalvar = new JFXButton("Salvar");
        bSalvar.getStyleClass().add("bSalvar");
        pane.getChildren().add(bSalvar);
    }
    
    public void initLayout(){
        lData.setLayoutX(10);
        lData.setLayoutY(23);
        
        dpData.setLayoutX(50);
        dpData.setLayoutY(20);
        txMAbriu.setLayoutX(290);
        txMAbriu.setLayoutY(20);
        txMFechou.setLayoutX(580);
        txMFechou.setLayoutY(20);
        txVisitas.setLayoutX(840);
        txVisitas.setLayoutY(20);
        cbBrinquedos.setLayoutX(10);
        cbBrinquedos.setLayoutY(110);
        
        bAddBrinquedos.setLayoutX(10);
        bAddBrinquedos.setLayoutY(200);
        bRemoveBrinquedos.setLayoutX(10);
        bRemoveBrinquedos.setLayoutY(242);
        bAddOcorrencia.setLayoutX(10);
        bAddOcorrencia.setLayoutY(340);
        bRemoveOcorrencia.setLayoutX(10);
        bRemoveOcorrencia.setLayoutY(380);
        
        tabelaBrinquedos.setLayoutX(200);
        tabelaBrinquedos.setLayoutY(100);
        tabelaOcorrencia.setLayoutX(200);
        tabelaOcorrencia.setLayoutY(340);
    }
    
    public void initListeners(){
        dpData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<DiarioDeBordo> listaDiario = Dao.listar(DiarioDeBordo.class);
                Date data = new Date();
                data = Date.from(dpData.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                for(int i = 0; i < listaDiario.size(); i++){
                    if(listaDiario.get(i).getDia().equals(data)){
                        preencheTela(listaDiario.get(i));
                    }
                }
            }
        });
        bAddBrinquedos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cbBrinquedos.getSelectionModel().getSelectedIndex() != -1){
                    //db.getBrinquedosMaisUsados().add((Brinquedo) cbBrinquedos.getSelectionModel().getSelectedItem());
                    //Dao.salvar(db);
                    tabelaBrinquedos.getItems().add(cbBrinquedos.getSelectionModel().getSelectedItem());
                    cbBrinquedos.getItems().remove(cbBrinquedos.getSelectionModel().getSelectedItem());
                }else{
                    JOptionPane.showMessageDialog(null, "Nenhum brinquedo selecionado");
                }
            }
        });
        bRemoveBrinquedos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tabelaBrinquedos.getSelectionModel().getSelectedIndex() != -1){
                    cbBrinquedos.getItems().add(tabelaBrinquedos.getSelectionModel().getSelectedItem());
                    tabelaBrinquedos.getItems().remove(tabelaBrinquedos.getSelectionModel().getSelectedItem());
                }else{
                    JOptionPane.showMessageDialog(null, "Nenhum item selecionado na tabela");
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
                //tabelaOcorrencia.getItems().clear();
                //tabelaOcorrencia.setItems(FXCollections.observableArrayList(Dao.listar(ItemDiarioDeBordo.class)));
            }
        });
        bRemoveOcorrencia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tabelaOcorrencia.getSelectionModel().getSelectedIndex() != -1){
                    try {
                        //db.getOcorrencias().remove(tabelaOcorrencia.getSelectionModel().getSelectedItem());
                        Dao.remover(tabelaOcorrencia.getSelectionModel().getSelectedItem());
                    } catch (SQLIntegrityConstraintViolationException ex) {
                        Logger.getLogger(DiarioBordo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tabelaOcorrencia.getItems().remove(tabelaOcorrencia.getSelectionModel().getSelectedItem());
                }else{
                    JOptionPane.showMessageDialog(null, "Nenhum Item Selecionado na Tabela");
                }
                //tabela.getItems().clear();
            }
        });
        bSalvar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
    }
    
    public void preencheTela(DiarioDeBordo diario){
        txMAbriu.setText(diario.getMonitorAbriu()+"");
        txMFechou.setText(diario.getMonitorFechou()+"");
        txVisitas.setText(diario.getVisitasNoDia()+"");
        tabelaBrinquedos.setItems(FXCollections.observableArrayList(diario.getBrinquedosMaisUsados()));
        tabelaOcorrencia.setItems(FXCollections.observableArrayList(diario.getOcorrencias()));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
   
}
