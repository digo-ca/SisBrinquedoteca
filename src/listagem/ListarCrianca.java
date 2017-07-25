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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.persistence.RollbackException;
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
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
    
    public void setMonitor(Monitor m){
        monitor = m;
    }
    

    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initValues();
        initListeners();
        Scene scene = new Scene(pane);
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
        editar = new JFXButton("Editar");
        remover = new JFXButton("Remover");
        
        initLayout();
        tabela.getColumns().addAll(colunaId, colunaNome, colunaNascimento);
        pane.getChildren().addAll(txPesquisa, tabela, sair, editar);
        if(monitor.getSupervisor())
            pane.getChildren().add(remover);
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
        txPesquisa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!txPesquisa.getText().equals("")) {
                    tabela.setItems(findItens());
                }
            }
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
                if(tabela.getSelectionModel().getSelectedIndex() != -1){
                    edita.setCrianca((Crianca) tabela.getSelectionModel().getSelectedItem());
                    try {
                        edita.start(ListarCrianca.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarCrianca.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tabela.refresh();
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione um item na tabela");
                }
            }
        });
        
        remover.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tabela.getSelectionModel().getSelectedIndex() != -1){
                    if(JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o item selecionado?") == 0){
                        try {
                            Dao.remover(tabela.getSelectionModel().getSelectedItem());
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarCrianca.class.getName()).log(Level.SEVERE, null, ex);
                        }catch(RollbackException re){
                            JOptionPane.showMessageDialog(null, "Impossível excluir o item selecionado, pois o mesmo está inserido em uma visita");
                        }
                        tabela.refresh();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Selecione um item na tabela");
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

    public ObservableList<Crianca> findItens() {
        ObservableList<Crianca> itensEncontrados = FXCollections.observableArrayList();

        for (int i = 0; i < criancas.size(); i++) {
            if (criancas.get(i).getNome().equals(txPesquisa.getText())) {
                itensEncontrados.add(criancas.get(i));
            }
        }

        return itensEncontrados;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
