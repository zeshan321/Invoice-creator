package com.zeshanaslam.InvoiceCreator;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ContextMenuHandler;
import com.teamdev.jxbrowser.chromium.ContextMenuParams;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.awt.*;

public class SpellCheckMenu implements ContextMenuHandler {

    private final BrowserView browserView;
    private final BorderPane borderPane;

    public SpellCheckMenu(BrowserView browserView, BorderPane borderPane) {
        this.browserView = browserView;
        this.borderPane = borderPane;
    }

    @Override
    public void showContextMenu(ContextMenuParams contextMenuParams) {
        Platform.runLater(() -> {
            if (Main.contextMenu.isShowing()) Main.contextMenu.hide();
            Main.contextMenu.getItems().clear();

            for (String suggestion: contextMenuParams.getDictionarySuggestions()) {
                MenuItem menuItem = new MenuItem(suggestion);

                menuItem.setOnAction(t -> contextMenuParams.getBrowser().replaceMisspelledWord(menuItem.getText()));

                Main.contextMenu.getItems().add(menuItem);
            }

            Main.contextMenu.show(borderPane, MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
        });
    }
}
