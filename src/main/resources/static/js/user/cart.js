/**
 * User Cart Management
 * Gestion du panier utilisateur
 */

function updateQuantity(productId, change) {
    const input = document.querySelector(`input[data-product-id="${productId}"]`);
    let newValue = parseInt(input.value) + change;
    if (newValue > 0) {
        input.value = newValue;
        updateCart();
    }
}

function removeFromCart(productId) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet article?')) {
        fetch('/user/cart/remove/' + productId, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            }
        });
    }
}

function updateCart() {
    const items = [];
    document.querySelectorAll('.cart-item').forEach(row => {
        const productId = row.querySelector('input[data-product-id]').dataset.productId;
        const quantity = parseInt(row.querySelector('input[data-product-id]').value);
        items.push({productId, quantity});
    });

    fetch('/user/cart/update', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({items})
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            location.reload();
        }
    });
}

function applyPromo() {
    const code = document.getElementById('promoCode').value;
    fetch('/user/cart/apply-promo', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({code})
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Code promotionnel appliqué avec succès!');
            location.reload();
        } else {
            alert('Code promotionnel invalide');
        }
    });
}

function checkout() {
    window.location.href = '/user/payment';
}
