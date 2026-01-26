function deleteProduct(productId) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce produit?')) {
        fetch('/admin/product/' + productId, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            } else {
                alert('Erreur lors de la suppression du produit');
            }
        });
    }
}

function filterProducts() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const category = document.getElementById('categoryFilter').value;
    const status = document.getElementById('statusFilter').value;
    const rows = document.querySelectorAll('.product-row');
    let visibleCount = 0;

    rows.forEach(row => {
        const name = row.querySelector('.product-name').textContent.toLowerCase();
        const rowCategory = row.querySelector('.product-category').textContent;
        const rowStatus = row.querySelector('.badge').textContent;
        const stock = parseInt(row.querySelector('.product-stock').textContent);

        const matchesSearch = name.includes(searchTerm);
        const matchesCategory = category === '' || rowCategory === category;
        const matchesStatus = status === '' || 
            (status === 'active' && rowStatus === 'Actif') ||
            (status === 'inactive' && rowStatus === 'Inactif') ||
            (status === 'low-stock' && stock <= 10);

        if (matchesSearch && matchesCategory && matchesStatus) {
            row.style.display = '';
            visibleCount++;
        } else {
            row.style.display = 'none';
        }
    });

    document.getElementById('noResults').style.display = visibleCount === 0 ? 'block' : 'none';
}

document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const categoryFilter = document.getElementById('categoryFilter');
    const statusFilter = document.getElementById('statusFilter');
    if (searchInput && categoryFilter && statusFilter) {
        searchInput.addEventListener('input', filterProducts);
        categoryFilter.addEventListener('change', filterProducts);
        statusFilter.addEventListener('change', filterProducts);
    }
});
