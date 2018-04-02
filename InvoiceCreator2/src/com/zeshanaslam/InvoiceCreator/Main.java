package com.zeshanaslam.InvoiceCreator;

import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import legacy.ConfigLoader;

/**
 * Demonstrates how to embed Browser instance into JavaFX application.
 */
public class Main extends Application {

    public static BorderPane borderPane;
    public static Browser browser;
    public static Helpers helpers;
    public static ContextMenu contextMenu;
    public static ConfigLoader configLoader;

    @Override
    public void init() throws Exception {
        // On Mac OS X Chromium engine must be initialized in non-UI thread.
        if (Environment.isMac()) {
            BrowserCore.initialize();
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        BrowserContextParams params = new BrowserContextParams("user-data-dir");
        params.setStorageType(StorageType.MEMORY);

        BrowserContext browserContext = new BrowserContext(params);
        browser = new Browser(browserContext);
        BrowserView view = new BrowserView(browser);

        SpellCheckerService spellCheckerService = browser.getContext().getSpellCheckerService();
        spellCheckerService.setEnabled(true);
        spellCheckerService.setLanguage("en-US");

        borderPane = new BorderPane(view);
        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("Invoice Creator 2");
        primaryStage.setScene(scene);
        primaryStage.show();

        contextMenu = new ContextMenu();
        browser.setContextMenuHandler(new SpellCheckMenu(view, borderPane));
        browser.loadURL("https://zeshanaslam.com/test/home.html");

        helpers = new Helpers(browser);

        browser.addScriptContextListener(new ScriptContextAdapter() {
            @Override
            public void onScriptContextCreated(ScriptContextEvent event) {
                Browser browser = event.getBrowser();
                JSValue window = browser.executeJavaScriptAndReturnValue("window");
                window.asObject().setProperty("java", new Bridge());
            }
        });

        browser.addLoadListener(new LoadListener() {
            @Override
            public void onStartLoadingFrame(StartLoadingEvent startLoadingEvent) {

            }

            @Override
            public void onProvisionalLoadingFrame(ProvisionalLoadingEvent provisionalLoadingEvent) {

            }

            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent finishLoadingEvent) {
                browser.executeJavaScript("FooTable.init('#table', {\n" +
                        "\t\t\"columns\": " + helpers.getColumns() + ",\n" +
                        "\t\t\"rows\": " + helpers.loadTable() + "\n" +
                        "\t});");

                browser.executeJavaScript("$('thead .footable-filtering').hide();");
            }

            @Override
            public void onFailLoadingFrame(FailLoadingEvent failLoadingEvent) {

            }

            @Override
            public void onDocumentLoadedInFrame(FrameLoadEvent frameLoadEvent) {

            }

            @Override
            public void onDocumentLoadedInMainFrame(LoadEvent loadEvent) {

            }
        });
    }

    public static void main(String[] args) {
        configLoader = new ConfigLoader();
        launch(args);
    }

    @Override
    public void stop(){
        browser.dispose();
    }
}