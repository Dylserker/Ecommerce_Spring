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

// Basic modal wiring for adding addresses
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('addressModal');
    const addressTypeInput = document.getElementById('addressType');
    const form = document.getElementById('addressForm');
    const closeBtn = document.querySelector('.modal-close');
    const cancelBtn = document.getElementById('cancelAddress');
    let currentAddressType = null;

    // Profile info edit handling
    const infoInputs = Array.from(document.querySelectorAll('.info-value'));
    const editBtn = document.getElementById('editInfoBtn');
    const saveBtn = document.getElementById('saveInfoBtn');
    const cancelBtnInfo = document.getElementById('cancelInfoBtn');
    const initialInfoState = new Map();

    const setInfoReadOnly = (readonly) => {
        infoInputs.forEach(input => {
            input.readOnly = readonly;
            input.classList.toggle('editing', !readonly);
        });
    };

    const storeInitialValues = () => {
        initialInfoState.clear();
        infoInputs.forEach(input => initialInfoState.set(input.name, input.value));
    };

    const restoreInitialValues = () => {
        infoInputs.forEach(input => {
            if (initialInfoState.has(input.name)) {
                input.value = initialInfoState.get(input.name);
            }
        });
    };

    const enterEditMode = () => {
        storeInitialValues();
        setInfoReadOnly(false);
        editBtn?.classList.add('hidden');
        saveBtn?.classList.remove('hidden');
        cancelBtnInfo?.classList.remove('hidden');
    };

    const exitEditMode = () => {
        setInfoReadOnly(true);
        editBtn?.classList.remove('hidden');
        saveBtn?.classList.add('hidden');
        cancelBtnInfo?.classList.add('hidden');
    };

    editBtn?.addEventListener('click', () => {
        enterEditMode();
    });

    saveBtn?.addEventListener('click', () => {
        // TODO: send infoInputs data to backend before locking fields
        exitEditMode();
    });

    cancelBtnInfo?.addEventListener('click', () => {
        restoreInitialValues();
        exitEditMode();
    });

    // Lock fields initially
    setInfoReadOnly(true);

    const hideModal = () => {
        if (modal) {
            modal.classList.add('hidden');
            form?.reset();
            currentAddressType = null;
        }
    };

    const showModal = (typeKey, typeLabel) => {
        if (!modal || !addressTypeInput) return;
        currentAddressType = typeKey;
        addressTypeInput.value = `Adresse de ${typeLabel}`;
        modal.classList.remove('hidden');
    };

    const createAddressCard = (typeKey, data) => {
        const card = document.createElement('div');
        card.className = 'address-card';
        card.innerHTML = `
            <h3>Adresse de ${typeKey === 'facturation' ? 'facturation' : 'livraison'}</h3>
            <div class="address-info">
                <p>${data.fullName}</p>
                <p>${data.street}${data.addressLine2 ? ' - ' + data.addressLine2 : ''}</p>
                <p>${data.zip} ${data.city} - ${data.country}</p>
                <p>${data.phone}</p>
            </div>
            <div class="address-actions">
                <button class="btn-secondary">Modifier</button>
                <button class="btn-danger">Supprimer</button>
            </div>
        `;
        return card;
    };

    const insertCard = (typeKey, card) => {
        const addBtn = document.querySelector(`.open-address-modal[data-address-type="${typeKey}"]`);
        if (!addBtn) return;
        const addBlock = addBtn.closest('.address-actions');
        if (addBlock && addBlock.parentNode) {
            addBlock.parentNode.insertBefore(card, addBlock);
        }
    };

    document.querySelectorAll('.open-address-modal').forEach(btn => {
        btn.addEventListener('click', (ev) => {
            const typeKey = ev.currentTarget?.getAttribute('data-address-type') || '';
            showModal(typeKey, typeKey);
        });
    });

    closeBtn?.addEventListener('click', hideModal);
    cancelBtn?.addEventListener('click', hideModal);

    modal?.addEventListener('click', (ev) => {
        if (ev.target === modal) hideModal();
    });

    form?.addEventListener('submit', (ev) => {
        ev.preventDefault();
        if (!currentAddressType) return;

        const formData = new FormData(form);
        const data = {
            fullName: formData.get('fullName') || '',
            phone: formData.get('phone') || '',
            street: formData.get('street') || '',
            addressLine2: formData.get('addressLine2') || '',
            city: formData.get('city') || '',
            zip: formData.get('zip') || '',
            country: formData.get('country') || ''
        };

        const newCard = createAddressCard(currentAddressType, data);
        insertCard(currentAddressType, newCard);

        // TODO: connect to backend to persist the new address.
        hideModal();
    });
});
