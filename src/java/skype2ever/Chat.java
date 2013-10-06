package skype2ever;

import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.ListCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Chat extends CheckBoxListCell<String> {

    private SimpleStringProperty  nameProperty;
    private SimpleBooleanProperty isChecked;

    /* A no argument constructor is required */
    public Chat() {
        this.nameProperty = new SimpleStringProperty(this, "name", "unknown");
        this.isChecked = new SimpleBooleanProperty(this, "checked");
    }

    public Chat(String name) {
        this.nameProperty = new SimpleStringProperty(this, "name", name);
        this.isChecked = new SimpleBooleanProperty(this, "checked");
    }

    /* Getter(s) */
    public SimpleStringProperty getNameProperty() {
        return this.nameProperty;
    }

    /* implicit toString is called to display list text (cuz Generics type is String) */
    @Override
    public String toString() {
        // TODO: group unit and displayed text are under consideration.
        // currently showing 'name' column of chats table, which I fould absolutely unique.
        return this.nameProperty.getValue();
    }

    /* return current selected status. used as return value of Callback#call in UI side. */
    public SimpleBooleanProperty getChecked() {
        return this.isChecked;
    }

    /* updateItem seems not required */
    // @Override
    // public void updateItem(String item, boolean empty) {
    //     super.updateItem(item, empty);
    //     if (!empty) {
    //         setText(item);
    //     }
    // }
}
