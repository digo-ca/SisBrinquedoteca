/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listagem;

import cadastro.CadastroResponsavel;
import com.jfoenix.controls.JFXButton;
import entidade.Responsavel;
import static java.awt.PageAttributes.MediaType.E;
import static java.lang.Math.E;
import static java.lang.StrictMath.E;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.E;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.persistence.RollbackException;
import javax.swing.JOptionPane;
import static jdk.nashorn.internal.objects.NativeMath.E;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class ListarResponsavel extends Application {

    private AnchorPane pane;
    private TextField txPesquisa;
    private TableView tabela;
    private JFXButton bEditar;
    private JFXButton bRemover;
    private JFXButton bSair;
    private static Stage stage;

    TableColumn colunaId;
    TableColumn colunaNome;
    TableColumn colunaTelefone;
    TableColumn colunaEndereco;
    TableColumn colunaNumeroVinculo;
    TableColumn colunaVinculo;

    List<Responsavel> responsaveis = Dao.listar(Responsavel.class);
    ObservableList<Responsavel> listItens = FXCollections.observableArrayList(responsaveis);

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
        pane.setPrefSize(795, 595);

        txPesquisa = new TextField();
        txPesquisa.setPromptText("Pesquisar");

        bEditar = new JFXButton("Editar");
        pane.getChildren().add(bEditar);

        bRemover = new JFXButton("Remover");
        pane.getChildren().add(bRemover);

        bSair = new JFXButton("Sair");
        pane.getChildren().add(bSair);

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
        tabela.setPrefSize(785, 550);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela

        initLayout();
        tabela.getColumns().addAll(colunaId, colunaNome, colunaTelefone, colunaEndereco, colunaNumeroVinculo, colunaVinculo);
        pane.getChildren().addAll(txPesquisa, tabela);
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
        txPesquisa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!txPesquisa.getText().equals("")) {
                    tabela.setItems(findItens());
                }
            }
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
                    ObservableList<Responsavel> list = FXCollections.observableArrayList(Dao.consultarTodos(Responsavel.class));
                    tabela.getItems().clear();
                    tabela.setItems(list);
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum responsável selecionado na tabela");
                }
            }
        });

        bRemover.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    if (JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o item selecionado?") == 0) {
                        try {
                            Dao.remover((Responsavel) tabela.getSelectionModel().getSelectedItem());
                        } catch (RollbackException re) {
                            JOptionPane.showMessageDialog(null, "Impossível remover item, pois o mesmo está alocado a uma criança");
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarResponsavel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ObservableList<Responsavel> list = FXCollections.observableArrayList(Dao.consultarTodos(Responsavel.class));
                        tabela.getItems().clear();
                        tabela.setItems(list);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum item selecionado");
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
