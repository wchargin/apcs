// You probably want to go to:
// http://jsfiddle.net/WChargin/Cf586/

// ported from my Python code
// may suffer from Javascript inefficiencies, etc.

words = undefined; // wait for it

max = Math.max, min = Math.min;
ld = function (a, b) {
    var memo = {};
    var step = function (i, j) {
        var k = [i, j];
        if (memo[k] === undefined) {
            memo[k] = min(i, j) === 0 ? max(i, j) : min(
            step(i - 1, j) + 1, // \pm
            step(i, j - 1) + 1, // \mp
            step(i - 1, j - 1) + (a[i - 1] == b[j - 1] ? 0 : 1) // sub
            );
        }
        return memo[k];
    }
    return step(a.length, b.length);
}

go = function () {
    var word = document.getElementById("user").value;
    var bestValue = Number.MAX_VALUE;
    var bestWord = undefined;
    for (var i = words.length - 1; i >= 0; i--) {
        var possible = words[i];
        var distance = ld(word, possible);
        if (distance < bestValue) {
            bestValue = distance;
            bestWord = possible;
            if (bestValue == 0) {
                message.innerHTML = 'You nailed it! That is a word.';
                return;
            }
        }
    }
    if (bestWord !== undefined) {
        message.innerHTML = 'How about <strong>' + bestWord + '</strong>, which is ' + bestValue + ' off?';
    } else {
        message.innerHTML = 'Is this word list empty? Better luck next time.';
    }
}

// words = "omitted,for,brevity".split(",")
