/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listagem;

import cadastro.CadastroEscola;
import cadastro.CadastroResponsavel;
import com.jfoenix.controls.JFXButton;
import entidade.Escola;
import entidade.Monitor;
import entidade.Responsavel;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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

/**
 *
 * @author Ivanildo
 */
public class ListarEscola extends Application{
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
    TableColumn colunaEndereco;
    TableColumn colunaResponsavel;
    TableColumn colunaCgResponsavel;
    TableColumn colunaTelefone;

    List<Escola> escolas = Dao.listar(Escola.class);
    ObservableList<Escola> listItens = FXCollections.observableArrayList(escolas);
    
    public void setMonitor(Monitor m){
        monitor = m;
    }

    @Override
    public void start(Stage parent) {
        initComponents();

        initListeners();
        initLayout();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Tabela Escola");
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
        pane.getChildren().add(bEditar);

        bRemover = new JFXButton("Remover");
        if(monitor.getSupervisor())
            pane.getChildren().add(bRemover);

        bSair = new JFXButton("Sair");
        pane.getChildren().add(bSair);

        //responsaveis = Dao.listar(Responsavel.class);
        tabela = new TableView<>();
        colunaId = new TableColumn<>("Id");
        colunaNome = new TableColumn<>("Nome");
        colunaEndereco = new TableColumn<>("Endereço");
        colunaResponsavel = new TableColumn<>("Responsável");
        colunaCgResponsavel = new TableColumn<>("Cargo do Responsável");
        colunaTelefone = new TableColumn<>("Telefone");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colunaResponsavel.setCellValueFactory(new PropertyValueFactory<>("responsavel"));
        colunaCgResponsavel.setCellValueFactory(new PropertyValueFactory<>("cargoResponsavel"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        tabela.setItems(listItens);
        tabela.setPrefSize(785, 400);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela

        tabela.getColumns().addAll(colunaId, colunaNome, colunaEndereco, colunaResponsavel, colunaCgResponsavel, colunaTelefone);
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
                ListarEscola.stage.hide();
            }
        });

        bEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroEscola ce = new CadastroEscola();
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    ce.setEscola((Escola) tabela.getSelectionModel().getSelectedItem());
                    try {
                        ce.start(ListarEscola.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarEscola.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    tabela.refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum item selecionado na tabela");
                }
            }
        });

        bRemover.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    if (JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o item selecionado?") == 0) {
                        
                        try {
                            Dao.remover((Escola) tabela.getSelectionModel().getSelectedItem());
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarEscola.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        ObservableList<Escola> list = FXCollections.observableArrayList(Dao.consultarTodos(Escola.class));
                        tabela.getItems().clear();
                        tabela.setItems(list);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum item selecionado");
                }
            }
        });
    }

    private ObservableList<Escola> findItens() {
        ObservableList<Escola> itensEncontrados = FXCollections.observableArrayList();

        for (int i = 0; i < listItens.size(); i++) {
            if (listItens.get(i).getNome().equals(txPesquisa.getText())) {
                itensEncontrados.add(listItens.get(i));
            }
        }
        return itensEncontrados;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
