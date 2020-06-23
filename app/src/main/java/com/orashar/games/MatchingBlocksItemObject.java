package com.orashar.games;

public class MatchingBlocksItemObject {
    String text, answer;
    boolean isSelected, isMatched;

    public MatchingBlocksItemObject(String text, String answer, boolean isSelected, boolean isMatched) {
        this.text = text;
        this.answer = answer;
        this.isSelected = isSelected;
        this.isMatched = isMatched;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }
}
