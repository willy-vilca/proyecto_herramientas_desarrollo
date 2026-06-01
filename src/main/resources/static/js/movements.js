//Función para el boton de confirmacion al eliminar un movimiento
function setDeleteMovement(id, clientId) {
    const btn =
        document.getElementById(
            "confirmDeleteMovement"
        );
    btn.href =
        "/movements/delete?id="
        + id
        + "&clientId="
        + clientId;
}

function setDeleteCategory(id){

    const btn =
        document.getElementById(
            "confirmDeleteCategory"
        );

    btn.href =
        "/categories/delete?id="
        + id;
}

//cargar data en el form de modificar movimiento
function loadMovementData(id, type, amount, date, description, categoryId) {
    document.getElementById("editId").value = id;
    document.getElementById("editType").value = type;
    document.getElementById("editAmount").value = amount;
    document.getElementById("editDate").value = date;
    document.getElementById("editDescription").value = description;
    document.getElementById("editCategory").value = categoryId;
}

//funcionalidad del menú hamburguesa
function toggleSidebar() {
    document.querySelector('.sidebar').classList.toggle('active');
    document.querySelector('.sidebar-overlay').classList.toggle('active');
}

document.addEventListener(
    'DOMContentLoaded',
    function () {

        const storageKey =
            'movementsTableState';

        const filterType =
            document.getElementById(
                'filterType'
            );

        const filterCategory =
            document.getElementById(
                'filterCategory'
            );

        const searchInput =
            document.getElementById(
                'movementSearch'
            );

        const resetButton =
            document.getElementById(
                'resetMovementFilters'
            );

        const exportButton =
            document.getElementById(
                'exportMovementsCsv'
            );

        const visibleCount =
            document.getElementById(
                'movementsVisibleCount'
            );

        const visibleIncome =
            document.getElementById(
                'visibleIncome'
            );

        const visibleExpense =
            document.getElementById(
                'visibleExpense'
            );

        const visibleBalance =
            document.getElementById(
                'visibleBalance'
            );

        const tableBody =
            document.querySelector(
                '.table-container tbody'
            );

        const sortButtons =
            Array.from(
                document.querySelectorAll(
                    '.table-sort[data-table="movements"]'
                )
            );

        if (!tableBody || !filterType || !filterCategory) {
            return;
        }

        let currentSortKey = 'date';
        let currentSortDirection = 'desc';

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
                        type: filterType.value,
                        category: filterCategory.value,
                        query: searchInput
                            ? searchInput.value
                            : '',
                        sortKey: currentSortKey,
                        sortDirection: currentSortDirection
                    })
                );
            } catch (error) {
                // Ignore storage errors to avoid breaking filters.
            }
        }

        function getRows() {
            return Array.from(
                tableBody.querySelectorAll(
                    '.movement-row'
                )
            );
        }

        function formatNumber(value) {
            return Number(value || 0)
                .toLocaleString(
                    'es-PE',
                    {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    }
                );
        }

        function updateVisibleSummary() {
            let visibleRows = 0;
            let income = 0;
            let expense = 0;

            getRows().forEach(function (row) {
                if (row.style.display === 'none') {
                    return;
                }

                visibleRows += 1;

                const amount = parseFloat(
                    row.dataset.amount || '0'
                );

                if (row.dataset.type === 'INGRESO') {
                    income += amount;
                }

                if (row.dataset.type === 'GASTO') {
                    expense += amount;
                }
            });

            if (visibleCount) {
                visibleCount.textContent = visibleRows;
            }

            if (visibleIncome) {
                visibleIncome.textContent = formatNumber(income);
            }

            if (visibleExpense) {
                visibleExpense.textContent = formatNumber(expense);
            }

            if (visibleBalance) {
                visibleBalance.textContent = formatNumber(
                    income - expense
                );
            }
        }

        function matchesSearch(row, query) {
            if (!query) {
                return true;
            }

            const haystack = [
                row.dataset.type,
                row.dataset.categoryName,
                row.dataset.description,
                row.dataset.date,
                row.dataset.amount
            ].join(' ').toLowerCase();

            return haystack.includes(query);
        }

        function applyFilters() {
            const typeValue = filterType.value;
            const categoryValue = filterCategory.value;
            const query = searchInput
                ? searchInput.value.trim().toLowerCase()
                : '';

            getRows().forEach(function (row) {
                const matchType =
                    typeValue === 'ALL'
                    || row.dataset.type === typeValue;

                const matchCategory =
                    categoryValue === 'ALL'
                    || row.dataset.category === categoryValue;

                const matchSearch =
                    matchesSearch(row, query);

                row.style.display =
                    (matchType && matchCategory && matchSearch)
                        ? ''
                        : 'none';
            });

            updateVisibleSummary();
            writeState();
        }

        function getComparableValue(row, sortKey) {
            if (sortKey === 'amount') {
                return parseFloat(
                    row.dataset.amount || '0'
                );
            }

            if (sortKey === 'date') {
                return row.dataset.date || '';
            }

            if (sortKey === 'categoryName') {
                return row.dataset.categoryName || '';
            }

            return row.dataset[sortKey] || '';
        }

        function exportVisibleRowsToCsv() {
            const headers = [
                'Tipo',
                'Monto',
                'Fecha',
                'Descripcion',
                'Categoria'
            ];

            const rows = getRows()
                .filter(function (row) {
                    return row.style.display !== 'none';
                })
                .map(function (row) {
                    return [
                        row.dataset.type || '',
                        formatNumber(row.dataset.amount || 0),
                        row.dataset.date || '',
                        row.dataset.description || '',
                        row.dataset.categoryName || ''
                    ];
                });

            downloadCsv(
                'movimientos',
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
                const leftValue = getComparableValue(left, sortKey);
                const rightValue = getComparableValue(right, sortKey);

                let result = 0;

                if (typeof leftValue === 'number'
                    && typeof rightValue === 'number') {
                    result = leftValue - rightValue;
                } else {
                    result = String(leftValue).localeCompare(
                        String(rightValue),
                        'es',
                        { numeric: true, sensitivity: 'base' }
                    );
                }

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

            applyFilters();
            writeState();
        }

        const persistedState = readState();

        if (persistedState.type) {
            filterType.value = persistedState.type;
        }

        if (persistedState.category) {
            filterCategory.value = persistedState.category;
        }

        if (searchInput && persistedState.query) {
            searchInput.value = persistedState.query;
        }

        if (persistedState.sortKey) {
            currentSortKey = persistedState.sortKey;
        }

        if (persistedState.sortDirection) {
            currentSortDirection = persistedState.sortDirection;
        }

        filterType.addEventListener(
            'change',
            applyFilters
        );

        filterCategory.addEventListener(
            'change',
            applyFilters
        );

        if (searchInput) {
            searchInput.addEventListener(
                'input',
                applyFilters
            );
        }

        if (resetButton) {
            resetButton.addEventListener(
                'click',
                function () {
                    filterType.value = 'ALL';
                    filterCategory.value = 'ALL';

                    if (searchInput) {
                        searchInput.value = '';
                    }

                    applyFilters();
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
                        currentSortDirection =
                            sortKey === 'date'
                                ? 'desc'
                                : 'asc';
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

        const categorySearchInput =
            document.getElementById(
                'categorySearch'
            );

        const categoryTypeFilter =
            document.getElementById(
                'categoryTypeFilter'
            );

        const resetCategoryFiltersButton =
            document.getElementById(
                'resetCategoryFilters'
            );

        const categoriesVisibleCount =
            document.getElementById(
                'categoriesVisibleCount'
            );

        const categoriesEmptyRow =
            document.getElementById(
                'categoriesEmptyRow'
            );

        const categoriesTableBody =
            document.getElementById(
                'categoriesTableBody'
            );

        const categorySortButtons =
            Array.from(
                document.querySelectorAll(
                    '.table-sort[data-table="categories"]'
                )
            );

        if (!categoriesTableBody || !categoryTypeFilter) {
            return;
        }

        let currentCategorySortKey = 'name';
        let currentCategorySortDirection = 'asc';

        function getCategoryRows() {
            return Array.from(
                categoriesTableBody.querySelectorAll(
                    '.category-row'
                )
            );
        }

        function updateCategoryVisibleCount() {
            const visibleRows = getCategoryRows()
                .filter(function (row) {
                    return row.style.display !== 'none';
                }).length;

            if (categoriesVisibleCount) {
                categoriesVisibleCount.textContent = visibleRows;
            }

            if (categoriesEmptyRow) {
                categoriesEmptyRow.classList.toggle(
                    'd-none',
                    visibleRows !== 0 || getCategoryRows().length === 0
                );
            }
        }

        function matchesCategorySearch(row, query) {
            if (!query) {
                return true;
            }

            const haystack = [
                row.dataset.name,
                row.dataset.description,
                row.dataset.type
            ].join(' ').toLowerCase();

            return haystack.includes(query);
        }

        function applyCategoryFilters() {
            const typeValue = categoryTypeFilter.value;
            const query = categorySearchInput
                ? categorySearchInput.value.trim().toLowerCase()
                : '';

            getCategoryRows().forEach(function (row) {
                const matchType =
                    typeValue === 'ALL'
                    || row.dataset.type === typeValue;

                const matchSearch =
                    matchesCategorySearch(row, query);

                row.style.display =
                    (matchType && matchSearch)
                        ? ''
                        : 'none';
            });

            updateCategoryVisibleCount();
        }

        function getCategoryComparableValue(row, sortKey) {
            return row.dataset[sortKey] || '';
        }

        function sortCategoryRows(sortKey, direction) {
            const rows = getCategoryRows();

            rows.sort(function (left, right) {
                const leftValue = getCategoryComparableValue(left, sortKey);
                const rightValue = getCategoryComparableValue(right, sortKey);
                const result = String(leftValue).localeCompare(
                    String(rightValue),
                    'es',
                    { numeric: true, sensitivity: 'base' }
                );

                return direction === 'asc'
                    ? result
                    : -result;
            });

            rows.forEach(function (row) {
                categoriesTableBody.appendChild(row);
            });

            categorySortButtons.forEach(function (button) {
                if (button.dataset.sortKey === sortKey) {
                    button.dataset.direction = direction;
                    return;
                }

                button.dataset.direction = '';
            });

            applyCategoryFilters();
        }

        categoryTypeFilter.addEventListener(
            'change',
            applyCategoryFilters
        );

        if (categorySearchInput) {
            categorySearchInput.addEventListener(
                'input',
                applyCategoryFilters
            );
        }

        if (resetCategoryFiltersButton) {
            resetCategoryFiltersButton.addEventListener(
                'click',
                function () {
                    categoryTypeFilter.value = 'ALL';

                    if (categorySearchInput) {
                        categorySearchInput.value = '';
                    }

                    applyCategoryFilters();
                }
            );
        }

        categorySortButtons.forEach(function (button) {
            button.addEventListener(
                'click',
                function () {
                    const sortKey = button.dataset.sortKey;

                    if (currentCategorySortKey === sortKey) {
                        currentCategorySortDirection =
                            currentCategorySortDirection === 'asc'
                                ? 'desc'
                                : 'asc';
                    } else {
                        currentCategorySortKey = sortKey;
                        currentCategorySortDirection = 'asc';
                    }

                    sortCategoryRows(
                        currentCategorySortKey,
                        currentCategorySortDirection
                    );
                }
            );
        });

        sortCategoryRows(
            currentCategorySortKey,
            currentCategorySortDirection
        );
    }
);


