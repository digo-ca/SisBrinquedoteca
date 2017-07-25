package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import entidade.VisitacaoEscola;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ItemVisitaEscola extends Application{
    private AnchorPane pane;
    private JFXDatePicker data;
    private JFXTextField txPeriodo;
    private JFXTextField txMonitor;
    private JFXTextField txEscola;
    private JFXTextField txProfessor;
    private JFXTextField txAtivMinistradas;
    private JFXTextField txFaixaEtaria;
    private Label nAlunos;
    private JFXListView<String> listAlunos;
    private static Stage stage;
    private JFXButton bFechar;
    private static VisitacaoEscola visitaEscola;
    
    public static void setVisita(VisitacaoEscola ve){
        visitaEscola = ve;
    }
    
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();
        initLayout();
        preencheTela();
        Scene scene = new Scene(pane);
        
        stage.setScene(scene);
        stage.setTitle("Visita de Escola");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        stage.showAndWait();
    }
    
    public void initComponents(){
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(500, 370);
        
        data = new JFXDatePicker();
        data.setDisable(true);
        pane.getChildren().add(data);
        
        txPeriodo = new JFXTextField();
        txPeriodo.setEditable(false);
        txPeriodo.setPromptText("Período");
        txPeriodo.setLabelFloat(true);
        pane.getChildren().add(txPeriodo);
        
        txMonitor = new JFXTextField();
        txMonitor.setEditable(false);
        txMonitor.setPromptText("Monitor");
        txMonitor.setLabelFloat(true);
        txMonitor.setPrefWidth(220);
        pane.getChildren().add(txMonitor);
        
        txEscola = new JFXTextField();
        txEscola.setEditable(false);
        txEscola.setPromptText("Escola");
        txEscola.setLabelFloat(true);
        txEscola.setPrefWidth(220);
        pane.getChildren().add(txEscola);
        
        txProfessor = new JFXTextField();
        txProfessor.setEditable(false);
        txProfessor.setPromptText("Professor");
        txProfessor.setLabelFloat(true);
        txProfessor.setPrefWidth(220);
        pane.getChildren().add(txProfessor);
        
        txAtivMinistradas = new JFXTextField();
        txAtivMinistradas.setEditable(false);
        txAtivMinistradas.setPromptText("Atividades Ministradas");
        txAtivMinistradas.setLabelFloat(true);
        txAtivMinistradas.setPrefWidth(220);
        pane.getChildren().add(txAtivMinistradas);
        
        txFaixaEtaria = new JFXTextField();
        txFaixaEtaria.setEditable(false);
        txFaixaEtaria.setPromptText("Faixa Etária das Crianças");
        txFaixaEtaria.setLabelFloat(true);
        pane.getChildren().add(txFaixaEtaria);
        
        nAlunos = new Label("Alunos:");
        pane.getChildren().add(nAlunos);
        
        listAlunos = new JFXListView<String>();
        listAlunos.setPrefSize(497, 100);
        pane.getChildren().add(listAlunos);
        
        bFechar = new JFXButton("Fechar");
        pane.getChildren().add(bFechar);
        
    }
    
    public void initLayout(){
        data.setLayoutX(10);
        data.setLayoutY(20);
        txPeriodo.setLayoutX(370);
        txPeriodo.setLayoutY(20);
        txMonitor.setLayoutX(10);
        txMonitor.setLayoutY(70);
        txEscola.setLayoutX(10);
        txEscola.setLayoutY(120);
        txProfessor.setLayoutX(285);
        txProfessor.setLayoutY(70);
        txAtivMinistradas.setLayoutX(285);
        txAtivMinistradas.setLayoutY(120);
        txFaixaEtaria.setLayoutX(10);
        txFaixaEtaria.setLayoutY(170);
        
        nAlunos.setLayoutX(10);
        nAlunos.setLayoutY(210);
        listAlunos.setLayoutX(10);
        listAlunos.setLayoutY(240);
        
        bFechar.setLayoutX(453);
        bFechar.setLayoutY(350);
    }
    
    public void initListeners(){
        bFechar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ItemVisitaEscola.stage.hide();
            }
        });
    }
    
    public void preencheTela(){
        data.setValue(visitaEscola.getData());
        txPeriodo.setText(visitaEscola.getPeriodo()+"");
        txMonitor.setText(visitaEscola.getMonitor()+"");
        txProfessor.setText(visitaEscola.getProfessor());
        txEscola.setText(visitaEscola.getEscola()+"");
        txAtivMinistradas.setText(visitaEscola.getAtividadesMinistradas());
        txFaixaEtaria.setText(visitaEscola.getFaixaEtariaCriancas());
        
        listAlunos.setItems(FXCollections.observableArrayList(visitaEscola.getAlunos()));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
