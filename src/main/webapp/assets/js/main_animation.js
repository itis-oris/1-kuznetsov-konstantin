function onPreEntry(entry) {
    entry.forEach(change => {
        if (change.isIntersecting) {
            for (let child of change.target.children) {
                child.classList.add("anti-hidden");
            }
        }
    });
}

function onEntry2(entry) {
    setTimeout(function() { onEntry(entry) }, 20);
}

function onEntry(entry) {
    entry.forEach(change => {
        if (change.isIntersecting) {
            for (let child of change.target.children) {
                child.classList.add("show");
            }
        }
    });
}

let options2 = { threshold: 0.5 };
let preObserver = new IntersectionObserver(onPreEntry, options2);
let elements = document.querySelectorAll(".description__color");
for (let elm of elements) {
    preObserver.observe(elm);
}

let options = { threshold: 0.5 };
let observer = new IntersectionObserver(onEntry2, options);
for (let elm of elements) {
    observer.observe(elm);
}