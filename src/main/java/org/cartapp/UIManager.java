package org.cartapp;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.HashMap;

public class UIManager extends Parent {

    private HashMap<String, Node> nodes;

    public UIManager(HashMap<String, Node> nodes) {
        this.nodes = nodes;
    }
    public UIManager() {
        this.nodes = new HashMap<>();
    }
    public void give(String name, Node node) {
        nodes.put(name, node);
    }
    public Node retrieve(String name) {
        return nodes.get(name);
    }
    public void giveNewNode(String name, NodeImplementation node) {

        Node ui = node.createNode();
        nodes.put(name, ui);
        this.getChildren().add(ui);
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }
}
