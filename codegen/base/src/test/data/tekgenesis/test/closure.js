var increment = (function() {
    var count = 0;
    return function() { return ++count; };
 })();

increment();

increment();

