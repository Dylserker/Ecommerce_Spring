document.addEventListener('DOMContentLoaded', function() {
    const mainImage = document.getElementById('mainProductImage');
    const thumbs = [
        document.getElementById('mainProductImage'),
        document.getElementById('thumb1'),
        document.getElementById('thumb2'),
        document.getElementById('thumb3')
    ];
    let current = 0;
    setInterval(() => {
        current = (current + 1) % thumbs.length;
        mainImage.src = thumbs[current].src;
    }, 5000);
});
