package com.amr.sinnerschraderparsingtask.data.models;

/**
 * Created by Amr El-Madah on 11/27/2017.
 */

public class EmojiMentionModel {
    private String value;

    public EmojiMentionModel(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Emoji [value=" + value + "]";
    }
}
