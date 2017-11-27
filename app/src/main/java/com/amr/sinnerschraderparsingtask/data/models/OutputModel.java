package com.amr.sinnerschraderparsingtask.data.models;

import java.util.List;

/**
 * Created by Amr El-Madah on 11/27/2017.
 */
public class OutputModel {
    private List<EmojiMentionModel> emojis;
    private List<EmojiMentionModel> mentions;
    private List<LinkModel> links;

    public List<EmojiMentionModel> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<EmojiMentionModel> emojis) {
        this.emojis = emojis;
    }

    public List<EmojiMentionModel> getMentions() {
        return mentions;
    }

    public void setMentions(List<EmojiMentionModel> mentions) {
        this.mentions = mentions;
    }

    public List<LinkModel> getLinks() {
        return links;
    }

    public void setLinks(List<LinkModel> links) {
        this.links = links;
    }

    public OutputModel() {

    }

    public OutputModel(List<EmojiMentionModel> emojis, List<EmojiMentionModel> mentions, List<LinkModel> links) {
        this.emojis = emojis;
        this.mentions = mentions;
        this.links = links;
    }


}
