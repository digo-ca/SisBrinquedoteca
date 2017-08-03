/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listagem;

import app.ItemCrianca;
import cadastro.CadastroCrianca;
import com.jfoenix.controls.JFXButton;
import entidade.Crianca;
import entidade.Monitor;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.persistence.RollbackException;
import persistencia.Dao;

public class ListarCrianca extends Application {

    private AnchorPane pane;
    private TextField txPesquisa;
    private TableView tabela;
    private TableColumn colunaId;
    private TableColumn colunaNome;
    private TableColumn<Crianca, LocalDate> colunaNascimento;

    private static Stage stage;

    private JFXButton sair;
    private JFXButton editar;
    private JFXButton remover;
    //private Button detalhes;

    private ObservableList<Crianca> criancas;
    private Monitor monitor;

    public void setMonitor(Monitor m) {
        monitor = m;
    }

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initValues();
        initListeners();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.initOwner(parent);
        stage.setScene(scene);
        stage.setTitle("Tabela de Crianças");
        stage.setResizable(false);
        stage.show();
    }

    public void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(795, 445);

        txPesquisa = new TextField();
        txPesquisa.setPromptText("Pesquisar");

        tabela = new TableView();
        colunaId = new TableColumn("Id");
        colunaNome = new TableColumn("Nome");
        colunaNascimento = new TableColumn("D. Nascimento");

        colunaId.setCellValueFactory(new PropertyValueFactory("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory("nome"));
        colunaNascimento.setCellValueFactory(new PropertyValueFactory("nascimento"));

        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colunaNascimento.setCellFactory(column -> {
            return new TableCell<Crianca, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
        tabela.setPrefSize(785, 400);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        sair = new JFXButton("Sair");
        //sair.getStyleClass().add("btExit");
        editar = new JFXButton("Editar");
        remover = new JFXButton("Remover");

        initLayout();
        tabela.getColumns().addAll(colunaId, colunaNome, colunaNascimento);
        pane.getChildren().addAll(tabela, txPesquisa, sair, editar);
        if (monitor.getSupervisor()) {
            pane.getChildren().add(remover);
        }
    }

    private void initValues() {
        criancas = FXCollections.observableArrayList(Dao.listar(Crianca.class));
        tabela.setItems(criancas);
        tabela.refresh();
    }

    public void initLayout() {
        tabela.setLayoutX(10);
        tabela.setLayoutY(45);
        txPesquisa.setLayoutX(645);
        txPesquisa.setLayoutY(10);

        sair.setLayoutX(10);
        sair.setLayoutY(10);
        editar.setLayoutX(50);
        editar.setLayoutY(10);
        remover.setLayoutX(100);
        remover.setLayoutY(10);
    }

    public static Stage getStage() {
        return stage;
    }

    public void initListeners() {

        FilteredList<Crianca> filteredData = new FilteredList<>(criancas, (e) -> true);
        txPesquisa.setOnKeyReleased((e) -> {
            txPesquisa.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Crianca>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if ((user.getId() + "").contains(newValue)) {
                        return true;
                    } else if (user.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (user.getEscola().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            SortedList<Crianca> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tabela.comparatorProperty());
            tabela.setItems(sortedData);
        });

        sair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarCrianca.getStage().close();
            }
        });
        editar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroCrianca edita = new CadastroCrianca();
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    edita.setCrianca((Crianca) tabela.getSelectionModel().getSelectedItem());
                    try {
                        edita.start(ListarCrianca.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarCrianca.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    criancas.setAll(Dao.listar(Crianca.class));
                    tabela.requestFocus();
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });

        remover.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    if (new Alert(Alert.AlertType.NONE, "Tem certeza que deseja remover o item selecionado?",ButtonType.CANCEL,ButtonType.YES).showAndWait().get().equals(ButtonType.YES)) {
                        try {
                            Dao.remover(tabela.getSelectionModel().getSelectedItem());
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarCrianca.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RollbackException re) {
                            new Alert(Alert.AlertType.ERROR, "Impossível excluir o item selecionado, pois o mesmo está inserido em uma visita", ButtonType.OK).show();
                        }
                        criancas.setAll(Dao.listar(Crianca.class));
                        tabela.requestFocus();
                    } else {
                    }
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });

        tabela.setRowFactory(tv -> {
            TableRow<Crianca> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ItemCrianca.setCrianca(row.getItem());
                    try {
                        new ItemCrianca().start(ListarCrianca.getStage());
                    } catch (Exception ex) {
                        Logger.getLogger(ListarCrianca.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    initValues();
                }

            });
            return row;
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
