/**
 * User Profile Management
 * Gestion du profil utilisateur
 */

function switchVTab(tabId, ev) {
    document.querySelectorAll('.v-tab-btn').forEach(btn => btn.classList.remove('active'));
    if (ev && ev.currentTarget) {
        ev.currentTarget.classList.add('active');
    }

    document.querySelectorAll('.tab-content').forEach(p => p.classList.remove('active'));
    const target = document.getElementById(tabId);
    if (target) {
        target.classList.add('active');
    }
}
