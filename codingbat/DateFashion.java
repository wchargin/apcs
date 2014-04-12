public boolean isVeryStylish(int who) {
    return who >= 8;
}
public boolean isTerriblyStylish(int who) {
    return who <= 2;
}
public int dateFashion(int you, int date) {
    if (isTerriblyStylish(you) || isTerriblyStylish(date)) {
        return 0;
    } else if (isVeryStylish(you) || isVeryStylish(date)) {
        return 2;
    } else {
        return 1;
    }
}
