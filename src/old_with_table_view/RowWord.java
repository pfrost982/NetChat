package old_with_table_view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RowWord {
    private SimpleStringProperty word;
    private SimpleIntegerProperty count;

    public RowWord(String word, int count) {
        this.word = new SimpleStringProperty(word);
        this.count = new SimpleIntegerProperty(count);
    }

    public String getWord() {
        return word.get();
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public int getCount() {
        return count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public void incCount() {
        count.set(getCount() + 1);
    }
}
