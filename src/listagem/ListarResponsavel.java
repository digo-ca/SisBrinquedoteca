/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listagem;

import cadastro.CadastroResponsavel;
import com.jfoenix.controls.JFXButton;
import entidade.Monitor;
import entidade.Responsavel;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.RollbackException;
import javax.swing.JOptionPane;
import persistencia.Dao;

public class ListarResponsavel extends Application {

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
    TableColumn colunaTelefone;
    TableColumn colunaEndereco;
    TableColumn colunaNumeroVinculo;
    TableColumn colunaVinculo;

    List<Responsavel> responsaveis = Dao.listar(Responsavel.class);
    ObservableList<Responsavel> listItens = FXCollections.observableArrayList(responsaveis);
    
    public void setMonitor(Monitor m){
        monitor = m;
    }

    @Override
    public void start(Stage parent) {
        initComponents();

        initListeners();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Tabela no JavaFX");
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
        colunaNome = new TableColumn<>("Nome");
        colunaTelefone = new TableColumn<>("Telefone");
        colunaEndereco = new TableColumn<>("Endereço");
        colunaNumeroVinculo = new TableColumn<>("Número de Vínculo");
        colunaVinculo = new TableColumn<>("Vínculo");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colunaNumeroVinculo.setCellValueFactory(new PropertyValueFactory<>("numeroVinculo"));
        colunaVinculo.setCellValueFactory(new PropertyValueFactory<>("vinculo"));

        tabela.setItems(listItens);
        tabela.setPrefSize(785, 400);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela

        initLayout();
        tabela.getColumns().addAll(colunaId, colunaNome, colunaTelefone, colunaEndereco, colunaNumeroVinculo, colunaVinculo);
        pane.getChildren().addAll(tabela, txPesquisa, bSair, bEditar);
        if(monitor.getSupervisor())
            pane.getChildren().add(bRemover);
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
        FilteredList<Responsavel> filteredData = new FilteredList<>(listItens, (e) -> true);
        txPesquisa.setOnKeyReleased((e) -> {
            txPesquisa.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Responsavel>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if ((user.getId() + "").contains(newValue)) {
                        return true;
                    } else if (user.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if ((user.getEndereco()+ "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if ((user.getNumeroVinculo()+ "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if ((user.getVinculo()+ "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            SortedList<Responsavel> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tabela.comparatorProperty());
            tabela.setItems(sortedData);
        });

        bSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarResponsavel.stage.hide();
            }
        });

        bEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroResponsavel cr = new CadastroResponsavel();
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    cr.setResponsavel((Responsavel) tabela.getSelectionModel().getSelectedItem());
                    try {
                        cr.start(ListarResponsavel.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarResponsavel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    listItens.setAll(Dao.listar(Responsavel.class));
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
                            Dao.remover((Responsavel) tabela.getSelectionModel().getSelectedItem());
                        } catch (RollbackException re) {
                            JOptionPane.showMessageDialog(null, "Impossível remover item, pois o mesmo está alocado a uma criança");
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarResponsavel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        listItens.setAll(Dao.listar(Responsavel.class));
                        tabela.requestFocus();
                    }
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });
    }

    private ObservableList<Responsavel> findItens() {
        ObservableList<Responsavel> itensEncontrados = FXCollections.observableArrayList();

        for (int i = 0; i < listItens.size(); i++) {
            if (listItens.get(i).getNome().equals(txPesquisa.getText())) {
                itensEncontrados.add(listItens.get(i));
            }
        }
        //if(listItens)

        return itensEncontrados;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
