/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import listagem.ListarResponsavel;
import cadastro.CadastroBrinquedo;
import cadastro.CadastroClassificacao;
import cadastro.CadastroCrianca;
import cadastro.CadastroEscola;
import cadastro.CadastroLivro;
import cadastro.CadastroMonitor;
import cadastro.CadastroResponsavel;
import cadastro.CadastroVisita;
import cadastro.CadastroVisitacaoEscola;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import entidade.DiarioDeBordo;
import entidade.Monitor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import listagem.ListarBrinquedo;
import listagem.ListarClassificacao;
import listagem.ListarCrianca;
import listagem.ListarDiarios;
import listagem.ListarEscola;
import listagem.ListarLivro;
import listagem.ListarMonitor;
import listagem.ListarVisita;
import listagem.ListarVisitaEscola;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class Principal extends Application {
    private static Stage stage;
    
    private AnchorPane pane;
    private GridPane grid;
    private JFXDrawer sideBar;
    private MenuBar menuBar;
    private Menu MenuCad;
    private Menu mListar;
    private Menu mUsuario;
    private Menu mPatrimonio;
    private Menu mVisita;
    private Menu mListaPatrimonio;
    private Menu mListaVisita;
    
    private MenuItem itemBri;
    private MenuItem itemCri;
    private MenuItem itemResp;
    private MenuItem itemEscola;
    private MenuItem itemLivro;
    private MenuItem itemMonitor;
    private MenuItem itemVisita;
    private MenuItem itemVisitaEscola;
    private MenuItem itemClassificacao;
    
    
    
    //Menus de listagem
    private MenuItem itemLCrianca;
    private MenuItem itemLBrinquedo;
    private MenuItem itemSair;
    private Menu itemNomeUser;
    private MenuItem itemLResp;
    private MenuItem itemLEscola;
    private MenuItem itemLLivro;
    private MenuItem itemLMonitor;
    private MenuItem itemLVisita;
    private MenuItem itemLVisitaEscola;
    private MenuItem itemLClassificacao;
    private MenuItem itemLDiario;
    
    private JFXButton bDiario;
    private JFXButton sair;
    
    
    private MenuItem alteraSenha;
    
    private Monitor monitor;
    
    private DiarioDeBordo diario;

    public void setMonitor(Monitor m){
        monitor = m;
    }

    @Override
    public void start(Stage parent) {
        initComponents();
        BorderPane root = new BorderPane();
        BorderPane rootGrid = new BorderPane();
        VBox vboxSuperior = new VBox();
        VBox vboxInferior = new VBox();
        
        root.setTop(menuBar);
        root.getStyleClass().add("raiz");
        Scene scene = new Scene(root, 1500, 697);
        scene.getStylesheets().add("css/style.css");
        //Scene scene = new Scene(root, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        
        vboxSuperior.getChildren().addAll(bDiario);
        vboxInferior.getChildren().add(sair);

        rootGrid.setTop(vboxSuperior);
        rootGrid.setBottom(vboxInferior);

        //bDiario.setPrefSize(100, 40);
        //sair.setPrefSize(100, 40);  
        
        rootGrid.getStyleClass().add("grid");
        root.setLeft(rootGrid);
        
        itemSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                try {
                    new Login().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        sair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                try {
                    new Login().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        initListeners();

        stage.setTitle("SisBrinquedoteca");
        //stage.initModality(Modality.WINDOW_MODAL);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.initOwner(parent);
        stage.show();
    }
    
    private void initComponents() {
        stage = new Stage();
        // Create MenuBar
        menuBar = new MenuBar();
        //menuBar.getStyleClass().add("menu-bar");

        // Create menus
        MenuCad = new Menu("Cadastrar");
        mListar = new Menu("Listar");
        mUsuario = new Menu("Usuário");
        mPatrimonio = new Menu("Patrimônio");
        mVisita = new Menu("Visita");
        mListaPatrimonio = new Menu("Patrimônio");
        mListaVisita = new Menu("Visita");

        // Create MenuItems
        itemBri = new MenuItem("Brinquedo");
        itemCri = new MenuItem("Criança");
        itemResp = new MenuItem("Responsável");
        itemEscola = new MenuItem("Escola");
        itemLivro = new MenuItem("Livro");
        itemMonitor = new MenuItem("Monitor");
        itemVisita = new MenuItem("Visita de Criança");
        itemVisitaEscola = new MenuItem("Visita de Escola");
        itemClassificacao = new MenuItem("Classificação");

        itemLCrianca = new MenuItem("Criança");
        itemLBrinquedo = new MenuItem("Brinquedo");
        itemLResp = new MenuItem("Responsável");
        itemLEscola = new MenuItem("Escola");
        itemLLivro = new MenuItem("Livro");
        itemLMonitor = new MenuItem("Monitor");
        itemLVisita = new MenuItem("Visita de Criança");
        itemLVisitaEscola = new MenuItem("Visita de Escola");
        itemLClassificacao = new MenuItem("Classificação");
        itemLDiario = new MenuItem("Diário de Bordo");
        
        
        itemSair = new MenuItem("Sair");
        itemNomeUser = new Menu(monitor.getNomeUsuario());
        
        alteraSenha = new MenuItem("Alterar Senha");
        itemNomeUser.getItems().add(alteraSenha);
        
        grid = new GridPane();
        bDiario = new JFXButton("Exibir Diário");
        //bDiario.setPrefHeight(30);
        bDiario.getStyleClass().add("buttonPrincipal");
        sair = new JFXButton("Sair");
        sair.getStyleClass().add("buttonPrincipalSair");

        // Add menuItems to the Menus
        if(monitor.getSupervisor()){
            MenuCad.getItems().add(itemMonitor);
            mPatrimonio.getItems().add(itemClassificacao);
            mListaPatrimonio.getItems().add(itemLClassificacao);
        }
        mVisita.getItems().addAll(itemVisita, itemVisitaEscola);
        mPatrimonio.getItems().addAll(itemBri, itemLivro);
        mListaVisita.getItems().addAll(itemLVisita, itemLVisitaEscola);
        mListaPatrimonio.getItems().addAll(itemLBrinquedo, itemLLivro);
        MenuCad.getItems().addAll(itemCri,itemResp,itemEscola, mVisita, mPatrimonio);
        mListar.getItems().addAll(itemLMonitor, itemLCrianca, itemLResp, itemLEscola, itemLDiario, mListaVisita, mListaPatrimonio);
        mUsuario.getItems().addAll(itemNomeUser, itemSair);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(MenuCad, mListar, mUsuario);

    }
    
    public void initListeners(){
        itemCri.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroCrianca().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        itemResp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroResponsavel().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        itemBri.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroBrinquedo().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        itemEscola.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroEscola().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        itemLivro.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroLivro().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        itemMonitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroMonitor().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        itemVisita.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroVisita cv = new CadastroVisita();
                cv.setMonitor(monitor);
                try {
                    cv.start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        itemVisitaEscola.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroVisitacaoEscola cve = new CadastroVisitacaoEscola();
                cve.setMonitor(monitor);
                try {
                    cve.start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        itemClassificacao.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new CadastroClassificacao().start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        itemLCrianca.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarCrianca lc = new ListarCrianca();
                lc.setMonitor(monitor);
                try {
                    lc.start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        itemLBrinquedo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarBrinquedo lb = new ListarBrinquedo();
                try {
                    lb.start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        itemLResp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarResponsavel lr = new ListarResponsavel();
                lr.setMonitor(monitor);
                lr.start(Principal.stage);
            }
        });
        
        itemLEscola.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarEscola le = new ListarEscola();
                le.setMonitor(monitor);
                le.start(Principal.stage);
            }
        });
        
        itemLLivro.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarLivro lLivro = new ListarLivro();
                lLivro.start(Principal.stage);
            }
        });
        itemLMonitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarMonitor lm = new ListarMonitor();
                lm.setMonitor(monitor);
                lm.start(Principal.stage);
            }
        });
        itemLVisita.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarVisita lv = new ListarVisita();
                lv.setMonitor(monitor);
                lv.start(Principal.stage);
            }
        });
        
        itemLVisitaEscola.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarVisitaEscola lve = new ListarVisitaEscola();
                lve.setMonitor(monitor);
                lve.start(Principal.stage);
            }
        });
        
        itemLDiario.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarDiarios ld = new ListarDiarios();
                ld.setMonitor(monitor);
                ld.start(Principal.stage);
            }
        });
        
        itemLClassificacao.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarClassificacao lc = new ListarClassificacao();
                lc.setMonitor(monitor);
                lc.start(Principal.stage);
            }
        });
        
        bDiario.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DiarioBordo db = new DiarioBordo();
                db.setMonitor(monitor);
                //db.setDiario(diario);
                
                try {
                    db.start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        alteraSenha.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AlteraSenha novaSenha = new AlteraSenha();
                novaSenha.setMonitor(monitor);
                try {
                    novaSenha.start(Principal.stage);
                } catch (Exception ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public static Stage getStage() {
        return stage;
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(Dao.consultarTodos(Monitor.class).isEmpty()){
            CadastroMonitor.main(args);
        }else{
            Login.main(args);
        }
    }
}
