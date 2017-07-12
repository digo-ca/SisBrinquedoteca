/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listagem;

import entidade.Responsavel;
import static java.awt.PageAttributes.MediaType.E;
import static java.lang.Math.E;
import static java.lang.StrictMath.E;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
    TableColumn colunaId;
    TableColumn colunaNome;
    TableColumn colunaTelefone;
    TableColumn colunaEndereco;
    List<Responsavel> responsaveis = Dao.listar(Responsavel.class);
    ObservableList<Responsavel> listItens = FXCollections.observableArrayList(responsaveis);

    @Override
    public void start(Stage stage) {
        initComponents();

        initListeners();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Tabela no JavaFX");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    public void initComponents() {
        pane = new AnchorPane();
        pane.setPrefSize(795, 595);

        txPesquisa = new TextField();
        txPesquisa.setPromptText("Pesquisar");

        //responsaveis = Dao.listar(Responsavel.class);
        tabela = new TableView<>();
        colunaId = new TableColumn<>("Id");
        colunaNome = new TableColumn<>("Nome");
        colunaTelefone = new TableColumn<>("Telefone");
        colunaEndereco = new TableColumn<>("Endereço");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tabela.setItems(listItens);
        tabela.setPrefSize(785, 550);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela

        initLayout();
        tabela.getColumns().addAll(colunaId, colunaNome, colunaTelefone, colunaEndereco);
        pane.getChildren().addAll(txPesquisa, tabela);
    }

    public void initLayout() {
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
    }

    private ObservableList<Responsavel> findItens() {
        ObservableList<Responsavel> itensEncontrados = FXCollections.observableArrayList();

        for (int i = 0; i < listItens.size(); i++) {
            if (listItens.get(i).getNome().equals(txPesquisa.getText())){
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
