public String makeTags(String tag, String word) {
    return String.format("<%s>%s</%s>", tag, word, tag);
}
