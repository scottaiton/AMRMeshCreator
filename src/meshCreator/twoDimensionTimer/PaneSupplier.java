package meshCreator.twoDimensionTimer;

import java.util.function.Supplier;

import javafx.scene.Node;

public abstract class PaneSupplier implements Supplier<Node> {
    private String name;

    public PaneSupplier(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
