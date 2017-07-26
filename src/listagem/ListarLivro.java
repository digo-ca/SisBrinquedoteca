package listagem;

import cadastro.CadastroLivro;
import com.jfoenix.controls.JFXButton;
import entidade.Livro;
import entidade.Monitor;
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
import javax.swing.JOptionPane;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class ListarLivro extends Application{
    private AnchorPane pane;
    private TextField txPesquisa;
    private TableView tabela;
    private JFXButton bEditar;
    private JFXButton bRemover;
    private JFXButton bSair;
    private static Stage stage;
    
    private Monitor monitor;

    TableColumn colunaId;
    TableColumn colunaTitulo;
    TableColumn colunaAutor;
    TableColumn colunaEditora;
    TableColumn colunaObservacoes;
    TableColumn colunaEstado;

    List<Livro> livros = Dao.listar(Livro.class);
    ObservableList<Livro> listItens = FXCollections.observableArrayList(livros);
    
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
        stage.setTitle("Tabela Livro");
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
        colunaTitulo = new TableColumn<>("Título");
        colunaAutor = new TableColumn<>("Autor");
        colunaEditora = new TableColumn<>("Editora");
        colunaObservacoes = new TableColumn<>("Observações");
        colunaEstado = new TableColumn<>("Estado");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colunaAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colunaEditora.setCellValueFactory(new PropertyValueFactory<>("editora"));
        colunaObservacoes.setCellValueFactory(new PropertyValueFactory<>("observacoes"));
        colunaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tabela.setItems(listItens);
        tabela.setPrefSize(785, 400);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela

        tabela.getColumns().addAll(colunaId, colunaTitulo, colunaAutor, colunaEditora, colunaObservacoes, colunaEstado);
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
                ListarLivro.stage.hide();
            }
        });

        bEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroLivro cl = new CadastroLivro();
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    cl.setLivro((Livro) tabela.getSelectionModel().getSelectedItem());
                    
                    try {
                        cl.start(ListarLivro.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarLivro.class.getName()).log(Level.SEVERE, null, ex);
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
                            Dao.remover((Livro) tabela.getSelectionModel().getSelectedItem());
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarEscola.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if(!Dao.listar(Livro.class).contains(tabela.getSelectionModel().getSelectedItem()))
                            tabela.getItems().remove(tabela.getSelectionModel().getSelectedItem());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum item selecionado");
                }
            }
        });
    }

    private ObservableList<Livro> findItens() {
        ObservableList<Livro> itensEncontrados = FXCollections.observableArrayList();

        for (int i = 0; i < listItens.size(); i++) {
            if (listItens.get(i).getTitulo().equals(txPesquisa.getText())) {
                itensEncontrados.add(listItens.get(i));
            }
        }
        return itensEncontrados;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
