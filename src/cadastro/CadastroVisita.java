/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.skins.JFXTimePickerContent;
import entidade.Crianca;
import entidade.DiarioDeBordo;
import entidade.Estado;
import entidade.Livro;
import entidade.Monitor;
import entidade.Visita;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import static java.time.temporal.TemporalQueries.zone;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class CadastroVisita extends Application {

    private AnchorPane pane;
    private static Stage stage;

    private Label lData;
    private Label lHoraEntrada;
    private Label lHoraSaida;
    private Label lCrianca;
    private Label lMonitor;

    private JFXDatePicker cData;
    private JFXTimePicker tpHoraEntrada;
    private JFXTimePicker tpHoraSaida;
    private JFXComboBox cbCrianca;
    private JFXComboBox cbMonitor;

    //private CheckBox chSupervisor;
    private JFXButton btCadastrar;
    private JFXButton btCancelar;

    private List<Crianca> criancas = Dao.listar(Crianca.class);
    private List<Monitor> monitores = Dao.listar(Monitor.class);

    private Monitor monitor;
    private Visita visita;

    public void setVisita(Visita v) {
        visita = v;
    }

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();

        if (visita != null) {
            preencheTela();
        }
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("Cadastro de Visitas");

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Mantém o foco apenas nessa tela enquanto aberta
        stage.setResizable(false);
        initLayout();
        stage.initOwner(parent);
        //CadastroVisita.stage = stage;
        stage.showAndWait();
    }

    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(550, 290); //definindo o tamanho da janela de Login
        pane.getStyleClass().add("pane");

        lData = new Label("Data");
        pane.getChildren().add(lData);
        lHoraEntrada = new Label("Hora de Entrada");
        pane.getChildren().add(lHoraEntrada);
        lHoraSaida = new Label("Hora de Saída");
        pane.getChildren().add(lHoraSaida);
        lCrianca = new Label("Criança");
        pane.getChildren().add(lCrianca);
        lMonitor = new Label("Monitor");
        //pane.getChildren().add(lMonitor);

        cData = new JFXDatePicker();
        cData.setEditable(false);
        cData.setValue(new Date(System.currentTimeMillis()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pane.getChildren().add(cData);
        tpHoraEntrada = new JFXTimePicker();
        if (visita == null) {
            tpHoraEntrada.setValue(LocalTime.now());
        }
        tpHoraEntrada.setEditable(false);
        //tpHoraEntrada.setPromptText("Hora de Entrada");
        pane.getChildren().add(tpHoraEntrada);
        tpHoraSaida = new JFXTimePicker();
        tpHoraSaida.setEditable(false);
        pane.getChildren().add(tpHoraSaida);
        cbCrianca = new JFXComboBox(FXCollections.observableArrayList(criancas));
        cbCrianca.setMaxWidth(220);
        cbCrianca.setPromptText("Selecione uma Criança");
        cbCrianca.setLabelFloat(true);
        pane.getChildren().add(cbCrianca);
        cbMonitor = new JFXComboBox(FXCollections.observableArrayList(monitores));
        cbMonitor.getSelectionModel().select(monitor);
        cbMonitor.setDisable(true);
        cbMonitor.setPrefWidth(190);
        cbMonitor.setPromptText("Monitor");
        cbMonitor.setLabelFloat(true);
        pane.getChildren().add(cbMonitor);

        btCadastrar = new JFXButton("Cadastrar");
        btCadastrar.getStyleClass().add("btCadastrar");
        pane.getChildren().add(btCadastrar);
        btCancelar = new JFXButton("Cancelar");
        btCancelar.getStyleClass().add("btCancelar");
        pane.getChildren().add(btCancelar);
    }

    private void initLayout() {
        lData.setLayoutX(10);
        lData.setLayoutY(20);
        lHoraEntrada.setLayoutX(10);
        lHoraEntrada.setLayoutY(70);
        lHoraSaida.setLayoutX(10);
        lHoraSaida.setLayoutY(115);
        lCrianca.setLayoutX(10);
        lCrianca.setLayoutY(160);
        lMonitor.setLayoutX(10);
        lMonitor.setLayoutY(170);

        cData.setLayoutX(130);
        cData.setLayoutY(20);
        tpHoraEntrada.setLayoutX(130);
        tpHoraEntrada.setLayoutY(65);
        tpHoraSaida.setLayoutX(130);
        tpHoraSaida.setLayoutY(110);
        cbCrianca.setLayoutX(130);
        cbCrianca.setLayoutY(160);
        cbMonitor.setLayoutX(365);
        cbMonitor.setLayoutY(20);

        btCadastrar.setLayoutX(490);
        btCadastrar.setLayoutY(270);
        btCancelar.setLayoutX(420);
        btCancelar.setLayoutY(270);
    }

    private void initListeners() {
        btCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroVisita.getStage().close();
            }
        });

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int flag = 0;
                if (visita == null) {
                    visita = new Visita();
                    flag = 1;
                }
                visita.setDia(cData.getValue());
                visita.setHoraEntrada(tpHoraEntrada.getValue());
                visita.setHoraSaida(tpHoraSaida.getValue());
                visita.setCrianca((Crianca) cbCrianca.getSelectionModel().getSelectedItem());
                visita.setMonitor((Monitor) cbMonitor.getSelectionModel().getSelectedItem());

                if (flag == 1) {
                    DiarioDeBordo diario;
                    if (Dao.consultarDiarioHoje().isEmpty()) {
                        diario = new DiarioDeBordo();
                        diario.setDia(LocalDate.now());
                        diario.setMonitorAbriu(monitor);
                        diario.setVisitasNoDia(1);
                    } else {
                        diario = Dao.consultarDiarioHoje().get(0);
                        if (diario.getMonitorFechou() == null) {
                            diario.setVisitasNoDia(diario.getVisitasNoDia() + 1);
                            Dao.salvar(visita);
                        }else{
                            new Alert(Alert.AlertType.ERROR, "O diário de hoje está fechado, Impossível adicionar uma nova visita", ButtonType.OK).showAndWait();
                        }
                    }

                    Dao.salvar(diario);
                }else{
                    Dao.salvar(visita);
                }
                CadastroVisita.getStage().close();
            }
        });
    }

    public void preencheTela() {
        cData.setValue(visita.getDia());
        tpHoraEntrada.setValue(visita.getHoraEntrada());
        //tpHoraSaida.setValue(LocalTime.parse((CharSequence) visita.getHoraSaida()));
        tpHoraSaida.setValue(visita.getHoraSaida());
        cbCrianca.getSelectionModel().select(visita.getCrianca());
        cbMonitor.getSelectionModel().select(visita.getMonitor());
    }

    public void setMonitor(Monitor m) {
        monitor = m;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
