// =====================================
// ELIMINAR CLIENTE
// =====================================

function setDeleteId(button) {

    const id = button.dataset.id;

    const btn =
        document.getElementById(
            "confirmDeleteBtn"
        );

    btn.href =
        "/deleteClient?id=" + id;
}

// =====================================
// CARGAR DATOS EN MODAL EDITAR
// =====================================

function loadClientData(button) {

    document.getElementById(
        "editClientId"
    ).value = button.dataset.id;

    document.getElementById(
        "editFullName"
    ).value = button.dataset.name;

    document.getElementById(
        "editDocumentType"
    ).value = button.dataset.documenttype;

    document.getElementById(
        "editDocumentNumber"
    ).value = button.dataset.documentnumber;

    document.getElementById(
        "editEmail"
    ).value = button.dataset.email;

    document.getElementById(
        "editPhone"
    ).value = button.dataset.phone;

    document.getElementById(
        "editAddress"
    ).value = button.dataset.address;

    document.getElementById(
        "editCompanyName"
    ).value = button.dataset.company;
}

// =====================================
// MENÚ HAMBURGUESA
// =====================================

function toggleSidebar() {

    document
        .querySelector('.sidebar')
        .classList
        .toggle('active');

    document
        .querySelector('.sidebar-overlay')
        .classList
        .toggle('active');
}