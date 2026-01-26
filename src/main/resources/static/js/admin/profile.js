/**
 * Admin Profile Management
 * Gestion du profil administrateur
 */


function switchVTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(tab => tab.classList.remove('active'));
    document.querySelectorAll('.v-tab-btn').forEach(btn => btn.classList.remove('active'));
    document.getElementById(tabName).classList.add('active');
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
}

function setupTwoFA() {
    alert('Configuration 2FA - Redirection vers la page de configuration');
}
