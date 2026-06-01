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

document.addEventListener(
    'DOMContentLoaded',
    function () {

        const storageKey =
            'clientsTableState';

        const searchInput =
            document.getElementById(
                'clientsSearch'
            );

        const resetButton =
            document.getElementById(
                'clientsResetFilters'
            );

        const exportButton =
            document.getElementById(
                'exportClientsCsv'
            );

        const tableBody =
            document.getElementById(
                'clientsTableBody'
            );

        const visibleCount =
            document.getElementById(
                'clientsVisibleCount'
            );

        const sortButtons =
            Array.from(
                document.querySelectorAll(
                    '.table-sort[data-table="clients"]'
                )
            );

        if (!tableBody) {
            return;
        }

        let currentSortKey = 'name';
        let currentSortDirection = 'asc';

        function readState() {
            try {
                return JSON.parse(
                    localStorage.getItem(storageKey)
                ) || {};
            } catch (error) {
                return {};
            }
        }

        function writeState() {
            try {
                localStorage.setItem(
                    storageKey,
                    JSON.stringify({
                        query: searchInput
                            ? searchInput.value
                            : '',
                        sortKey: currentSortKey,
                        sortDirection: currentSortDirection
                    })
                );
            } catch (error) {
                // Ignore storage errors to avoid breaking table behavior.
            }
        }

        function getRows() {
            return Array.from(
                tableBody.querySelectorAll(
                    '.client-row'
                )
            );
        }

        function updateVisibleCount() {
            if (!visibleCount) {
                return;
            }

            const count = getRows()
                .filter(function (row) {
                    return row.style.display !== 'none';
                })
                .length;

            visibleCount.textContent = count;
        }

        function matchesSearch(row, query) {
            if (!query) {
                return true;
            }

            const haystack = [
                row.dataset.name,
                row.dataset.document,
                row.dataset.company,
                row.dataset.email,
                row.dataset.phone,
                row.dataset.address
            ].join(' ').toLowerCase();

            return haystack.includes(query);
        }

        function applySearch() {
            const query = searchInput
                ? searchInput.value.trim().toLowerCase()
                : '';

            getRows().forEach(function (row) {
                row.style.display =
                    matchesSearch(row, query)
                        ? ''
                        : 'none';
            });

            updateVisibleCount();
            writeState();
        }

        function compareValues(a, b) {
            return a.localeCompare(
                b,
                'es',
                { numeric: true, sensitivity: 'base' }
            );
        }

        function exportVisibleRowsToCsv() {
            const headers = [
                'Nombre',
                'Documento',
                'Empresa',
                'Email',
                'Telefono',
                'Direccion'
            ];

            const rows = getRows()
                .filter(function (row) {
                    return row.style.display !== 'none';
                })
                .map(function (row) {
                    return [
                        row.dataset.name || '',
                        row.dataset.document || '',
                        row.dataset.company || '',
                        row.dataset.email || '',
                        row.dataset.phone || '',
                        row.dataset.address || ''
                    ];
                });

            downloadCsv(
                'clientes',
                headers,
                rows
            );
        }

        function downloadCsv(filePrefix, headers, rows) {
            const csvRows = [headers].concat(rows);
            const csvContent = csvRows
                .map(function (row) {
                    return row
                        .map(function (value) {
                            const safeValue = String(value || '')
                                .replace(/"/g, '""');
                            return '"' + safeValue + '"';
                        })
                        .join(',');
                })
                .join('\n');

            const blob = new Blob(
                ['\ufeff' + csvContent],
                { type: 'text/csv;charset=utf-8;' }
            );

            const link = document.createElement('a');
            const url = URL.createObjectURL(blob);

            link.href = url;
            link.download = filePrefix + '.csv';
            link.click();

            URL.revokeObjectURL(url);
        }

        function sortRows(sortKey, direction) {
            const rows = getRows();

            rows.sort(function (left, right) {
                const leftValue =
                    (left.dataset[sortKey] || '')
                        .trim();

                const rightValue =
                    (right.dataset[sortKey] || '')
                        .trim();

                const result = compareValues(
                    leftValue,
                    rightValue
                );

                return direction === 'asc'
                    ? result
                    : -result;
            });

            rows.forEach(function (row) {
                tableBody.appendChild(row);
            });

            sortButtons.forEach(function (button) {
                if (button.dataset.sortKey === sortKey) {
                    button.dataset.direction = direction;
                    return;
                }

                button.dataset.direction = '';
            });

            applySearch();
            writeState();
        }

        const persistedState = readState();

        if (searchInput && persistedState.query) {
            searchInput.value = persistedState.query;
        }

        if (persistedState.sortKey) {
            currentSortKey = persistedState.sortKey;
        }

        if (persistedState.sortDirection) {
            currentSortDirection = persistedState.sortDirection;
        }

        if (searchInput) {
            searchInput.addEventListener(
                'input',
                applySearch
            );
        }

        if (resetButton) {
            resetButton.addEventListener(
                'click',
                function () {
                    if (searchInput) {
                        searchInput.value = '';
                    }

                    applySearch();
                }
            );
        }

        if (exportButton) {
            exportButton.addEventListener(
                'click',
                exportVisibleRowsToCsv
            );
        }

        sortButtons.forEach(function (button) {
            button.addEventListener(
                'click',
                function () {
                    const sortKey = button.dataset.sortKey;

                    if (currentSortKey === sortKey) {
                        currentSortDirection =
                            currentSortDirection === 'asc'
                                ? 'desc'
                                : 'asc';
                    } else {
                        currentSortKey = sortKey;
                        currentSortDirection = 'asc';
                    }

                    sortRows(
                        currentSortKey,
                        currentSortDirection
                    );
                }
            );
        });

        sortRows(
            currentSortKey,
            currentSortDirection
        );
    }
);