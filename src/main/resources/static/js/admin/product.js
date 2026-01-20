/**
 * Admin Product Management
 * Gestion des produits administrateur
 */

function switchTab(tabName) {
    document.querySelectorAll('.tab-section').forEach(section => {
        section.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.getElementById(tabName).classList.add('active');
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
}

function updateImagePreview() {
    const url = document.getElementById('imageUrl').value;
    if (url) {
        document.getElementById('mainImage').src = url;
    }
}

function openFileUpload(e) {
    e.preventDefault();
    alert('Fonctionnalité de téléchargement de fichier - À implémenter avec backend');
}

function addGalleryImage() {
    const url = prompt('Entrez l\'URL de l\'image:');
    if (url) {
        const gallery = document.querySelector('.gallery-images');
        const item = document.createElement('div');
        item.className = 'gallery-item';
        item.innerHTML = `<img src="${url}" alt="Galerie"><button type="button" class="btn-remove" onclick="removeGalleryImage(this)">✕</button>`;
        gallery.appendChild(item);
    }
}

function removeGalleryImage(btn) {
    btn.parentElement.remove();
}

function deleteProduct() {
    const productId = new URLSearchParams(window.location.search).get('id');
    if (confirm('Êtes-vous sûr de vouloir supprimer ce produit?')) {
        fetch('/admin/product/' + productId, { method: 'DELETE' })
            .then(() => window.location.href = '/admin/products');
    }
}

// Calcul de la marge bénéficiaire
if (document.getElementById('price')) {
    document.getElementById('price').addEventListener('input', calculateMargin);
}
if (document.getElementById('cost')) {
    document.getElementById('cost').addEventListener('input', calculateMargin);
}

function calculateMargin() {
    const price = parseFloat(document.getElementById('price').value) || 0;
    const cost = parseFloat(document.getElementById('cost').value) || 0;
    const margin = cost > 0 ? Math.round(((price - cost) / price) * 100) : 0;
    document.getElementById('margin').textContent = margin + '%';
}

// Compteurs de caractères
if (document.getElementById('seoTitle')) {
    document.getElementById('seoTitle').addEventListener('input', function() {
        document.getElementById('titleCount').textContent = this.value.length + '/60';
    });
}

if (document.getElementById('seoDescription')) {
    document.getElementById('seoDescription').addEventListener('input', function() {
        document.getElementById('descCount').textContent = this.value.length + '/160';
    });
}
