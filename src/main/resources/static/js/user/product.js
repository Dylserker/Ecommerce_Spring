/**
 * User Product Management
 * Gestion des produits côté utilisateur
 */

const qtyInput = document.getElementById('qty');
const maxQty = qtyInput ? parseInt(qtyInput.max) || 999 : 999;

if (document.getElementById('decreaseQty')) {
    document.getElementById('decreaseQty').addEventListener('click', function() {
        if (qtyInput.value > 1) qtyInput.value--;
    });
}

if (document.getElementById('increaseQty')) {
    document.getElementById('increaseQty').addEventListener('click', function() {
        if (qtyInput.value < maxQty) qtyInput.value++;
    });
}

function addToCart(productId) {
    const quantity = document.getElementById('qty') ? document.getElementById('qty').value : '1';
    fetch('/user/cart/add/' + productId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({quantity: quantity})
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Produit ajouté au panier avec succès!');
        } else {
            alert('Erreur lors de l\'ajout au panier');
        }
    });
}

if (document.getElementById('wishlistBtn')) {
    document.getElementById('wishlistBtn').addEventListener('click', function() {
        this.classList.toggle('active');
        const text = document.getElementById('wishlistText');
        text.textContent = this.classList.contains('active') ? '❤ Dans ma liste' : '❤ Ajouter à ma liste';
    });
}

document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', function() {
        const tabName = this.dataset.tab;
        document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.getElementById(tabName).classList.add('active');
        this.classList.add('active');
    });
});
