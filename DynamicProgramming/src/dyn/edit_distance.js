// edit_distance.js
// JavaScript edit distance calculator
// Uses a dictionary for memoization, and Levenshtein's algorithm

// ported from my Python code
// may suffer from Javascript inefficiencies, etc.
max = Math.max, min = Math.min;
ld = function (a, b) {
    var memo = {};
    var step = function (i, j) {
        var k = [i, j];
        if (memo[k] === undefined) {
            memo[k] = min(i, j) === 0 ? max(i, j) : min(
            step(i - 1, j) + 1, // \pm
            step(i, j - 1) + 1, // \mp
            step(i - 1, j - 1) + (a[i - 1] === b[j - 1] ? 0 : 1) // sub
            );
        }
        return memo[k];
    }
    return step(a.length, b.length);
}
