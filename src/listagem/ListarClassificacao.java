/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listagem;

import cadastro.CadastroClassificacao;
import com.jfoenix.controls.JFXButton;
import entidade.Classificacao;
import entidade.Monitor;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.RollbackException;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class ListarClassificacao extends Application {

    private AnchorPane pane;
    private TextField txPesquisa;
    private TableView tabela;
    private JFXButton bEditar;
    private JFXButton bRemover;
    private JFXButton bSair;
    private static Stage stage;

    private Monitor monitor;

    TableColumn colunaId;
    TableColumn colunaNome;
    TableColumn colunaDescricao;

    ObservableList<Classificacao> listItens = FXCollections.observableArrayList(Dao.listar(Classificacao.class));

    public void setMonitor(Monitor m) {
        monitor = m;
    }

    @Override
    public void start(Stage parent) {
        initComponents();

        initListeners();
        initLayout();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Relatório de Classificações");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        stage.showAndWait();
    }

    public void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(795, 445);

        txPesquisa = new TextField();
        txPesquisa.setPromptText("Pesquisar");

        bEditar = new JFXButton("Editar");

        bRemover = new JFXButton("Remover");

        bSair = new JFXButton("Sair");

        //responsaveis = Dao.listar(Responsavel.class);
        tabela = new TableView<>();
        colunaId = new TableColumn<>("Id");
        colunaId.setMaxWidth(900);
        colunaNome = new TableColumn<>("Nome");
        colunaNome.setMaxWidth(3000);
        colunaDescricao = new TableColumn<>("Descrição");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tabela.setItems(listItens);
        tabela.setPrefSize(785, 400);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela

        tabela.getColumns().addAll(colunaId, colunaNome, colunaDescricao);
        pane.getChildren().addAll(tabela, txPesquisa, bSair);
        if (monitor.getSupervisor()) {
            pane.getChildren().add(bRemover);
            pane.getChildren().add(bEditar);
        }
    }

    public void initLayout() {
        bSair.setLayoutX(10);
        bSair.setLayoutY(10);
        bEditar.setLayoutX(50);
        bEditar.setLayoutY(10);
        bRemover.setLayoutX(100);
        bRemover.setLayoutY(10);

        tabela.setLayoutX(10);
        tabela.setLayoutY(45);
        txPesquisa.setLayoutX(645);
        txPesquisa.setLayoutY(10);
    }

    public void initListeners() {
        FilteredList<Classificacao> filteredData = new FilteredList<>(listItens, (e) -> true);
        txPesquisa.setOnKeyReleased((e) -> {
            txPesquisa.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Classificacao>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if ((user.getId() + "").contains(newValue)) {
                        return true;
                    } else if (user.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if ((user.getNome() + "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if ((user.getDescricao() + "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            SortedList<Classificacao> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tabela.comparatorProperty());
            tabela.setItems(sortedData);
        });

        bSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarClassificacao.stage.hide();
            }
        });

        bEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroClassificacao cc = new CadastroClassificacao();
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    cc.setClassificacao((Classificacao) tabela.getSelectionModel().getSelectedItem());

                    try {
                        cc.start(ListarClassificacao.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarClassificacao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    listItens.setAll(Dao.listar(Classificacao.class));
                    tabela.requestFocus();
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });

        bRemover.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    if (new Alert(Alert.AlertType.NONE, "Tem certeza que deseja remover o item selecionado?", ButtonType.CANCEL, ButtonType.YES).showAndWait().get().equals(ButtonType.YES)) {

                        try {
                            Dao.remover((Classificacao) tabela.getSelectionModel().getSelectedItem());
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarClassificacao.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RollbackException rb){
                            new Alert(Alert.AlertType.ERROR, "Impossível excluir este item, pois o mesmo está alocado a um item de brinquedo", ButtonType.OK).show();
                        }

                        listItens.setAll(Dao.listar(Classificacao.class));
                        tabela.requestFocus();
                    }
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
