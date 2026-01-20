/**
 * Admin Users Management
 * Gestion des utilisateurs administrateur
 */

function toggleSelectAll() {
    const checked = document.getElementById('selectAll').checked;
    document.querySelectorAll('.user-checkbox').forEach(cb => cb.checked = checked);
    updateBulkActionsDisplay();
}

function updateBulkActionsDisplay() {
    const selected = document.querySelectorAll('.user-checkbox:checked').length;
    const bulkActions = document.getElementById('bulkActions');
    if (selected > 0) {
        bulkActions.style.display = 'flex';
        document.getElementById('selectedCount').textContent = selected + ' utilisateur(s) sélectionné(s)';
    } else {
        bulkActions.style.display = 'none';
    }
}

document.querySelectorAll('.user-checkbox').forEach(cb => {
    cb.addEventListener('change', updateBulkActionsDisplay);
});

function deleteUser(userId) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur?')) {
        fetch('/admin/user/' + userId, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                location.reload();
            }
        });
    }
}

function editUser(userId) {
    window.location.href = '/admin/user/' + userId + '/edit';
}

function bulkChangeRole() {
    const selected = Array.from(document.querySelectorAll('.user-checkbox:checked')).map(cb => cb.value);
    window.location.href = '/admin/users/bulk-role?ids=' + selected.join(',');
}

function bulkDeactivate() {
    const selected = Array.from(document.querySelectorAll('.user-checkbox:checked')).map(cb => cb.value);
    fetch('/admin/users/bulk-deactivate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userIds: selected })
    }).then(() => location.reload());
}

function bulkBan() {
    if (confirm('Êtes-vous sûr de vouloir bannir ces utilisateurs?')) {
        const selected = Array.from(document.querySelectorAll('.user-checkbox:checked')).map(cb => cb.value);
        fetch('/admin/users/bulk-ban', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userIds: selected })
        }).then(() => location.reload());
    }
}

document.getElementById('searchInput').addEventListener('input', filterUsers);
if (document.getElementById('roleFilter')) {
    document.getElementById('roleFilter').addEventListener('change', filterUsers);
}
if (document.getElementById('statusFilter')) {
    document.getElementById('statusFilter').addEventListener('change', filterUsers);
}

function filterUsers() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const role = document.getElementById('roleFilter') ? document.getElementById('roleFilter').value : '';
    const status = document.getElementById('statusFilter') ? document.getElementById('statusFilter').value : '';
    const rows = document.querySelectorAll('.user-row');
    let visibleCount = 0;

    rows.forEach(row => {
        const name = row.querySelector('.user-name').textContent.toLowerCase();
        const email = row.querySelector('.user-email').textContent.toLowerCase();
        const rowRole = row.querySelector('.user-role').textContent;
        const rowStatus = row.querySelector('.user-status') ? row.querySelector('.user-status').textContent : '';

        const matchesSearch = name.includes(searchTerm) || email.includes(searchTerm);
        const matchesRole = role === '' || rowRole.includes(role === 'admin' ? 'Admin' : 'Client');
        const matchesStatus = status === '' ||
            (status === 'active' && rowStatus.includes('Actif')) ||
            (status === 'inactive' && rowStatus.includes('Inactif')) ||
            (status === 'banned' && rowStatus.includes('Banni'));

        if (matchesSearch && matchesRole && matchesStatus) {
            row.style.display = '';
            visibleCount++;
        } else {
            row.style.display = 'none';
        }
    });

    const noResults = document.getElementById('noResults');
    if (noResults) {
        noResults.style.display = visibleCount === 0 ? 'block' : 'none';
    }
}
