package meshCreator.twoDimensionTimer;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class EmptyPaneSupplier extends PaneSupplier {

    public EmptyPaneSupplier(String name) {
        super(name);
    }

    @Override
    public Node get() {
        return new Pane();
    }

}
