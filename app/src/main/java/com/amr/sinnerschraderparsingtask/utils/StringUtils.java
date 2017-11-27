package com.amr.sinnerschraderparsingtask.utils;

import com.amr.sinnerschraderparsingtask.data.models.EmojiMentionModel;
import com.amr.sinnerschraderparsingtask.data.models.LinkModel;
import com.amr.sinnerschraderparsingtask.data.models.OutputModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amr El-Madah on 11/27/2017.
 */

public class StringUtils {
    public static OutputModel extractUrls(String text, OutputModel outputModel) {
        List<LinkModel> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            String url = text.substring(urlMatcher.start(0),
                    urlMatcher.end(0));
            LinkModel linkModel = new LinkModel("", url);
            containedUrls.add(linkModel);
        }
        outputModel.setLinks(containedUrls);
        return outputModel;
    }

    public static OutputModel extractEmojis(String text, OutputModel outputModel) {
        List<EmojiMentionModel> containedEmojis = new ArrayList<>();
        String emojiRegex = "(?<=:)[a-zA-Z0-9]+(?=:)";
        Pattern pattern = Pattern.compile(emojiRegex, Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = pattern.matcher(text);
        while (emojiMatcher.find()) {
            EmojiMentionModel emojiModel = new EmojiMentionModel(":" + (text.substring(emojiMatcher.start(0),
                    emojiMatcher.end(0))) + ":");
            containedEmojis.add(emojiModel);
        }
        outputModel.setEmojis(containedEmojis);
        return outputModel;
    }

    public static OutputModel extractMentions(String text, OutputModel outputModel) {
        List<EmojiMentionModel> containedMentions = new ArrayList<>();
        String emojiRegex = "@([A-Za-z0-9_-]+)";
        Pattern pattern = Pattern.compile(emojiRegex, Pattern.CASE_INSENSITIVE);
        Matcher mentionMatcher = pattern.matcher(text);

        while (mentionMatcher.find()) {
            EmojiMentionModel mentionModel = new EmojiMentionModel((text.substring(mentionMatcher.start(0),
                    mentionMatcher.end(0))));
            containedMentions.add(mentionModel);
        }

        outputModel.setMentions(containedMentions);
        return outputModel;
    }

    public static String formatStringToJson(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                    if (i == 0) {
                        json.append(letter + indentString + "\n");
                    } else {
                        json.append("\n" + indentString + letter);
                        indentString = indentString + "\t";
                        json.append(indentString);
                    }
                    break;
                case '[':
                    json.append(indentString + letter);
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                    if (i == text.length() - 1) {
                        json.append("\n" + letter + indentString);
                    } else {
                        indentString = indentString.replaceFirst("\t", "");
                        json.append(indentString + letter);
                    }
                    break;
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n " + indentString + letter);
                    break;
                case ',':
                    if ((i + 2) < text.length()) {
                        if (text.charAt(i + 1) == '"') {
                            json.append(letter + indentString + "\n");
                        }
                    }
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}
