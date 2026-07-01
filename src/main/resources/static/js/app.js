(function () {
    const root =
        document.documentElement;

    const themeStorageKey =
        'accountingTheme';

    function applyTheme(theme) {
        root.setAttribute(
            'data-theme',
            theme
        );

        updateThemeToggleButtons(theme);
    }

    function getInitialTheme() {
        const persistedTheme =
            localStorage.getItem(
                themeStorageKey
            );

        if (persistedTheme === 'dark'
            || persistedTheme === 'light') {
            return persistedTheme;
        }

        return window.matchMedia
            && window.matchMedia('(prefers-color-scheme: dark)').matches
            ? 'dark'
            : 'light';
    }

    function updateThemeToggleButtons(theme) {
        document.querySelectorAll(
            '[data-theme-toggle]'
        ).forEach(function (button) {
            const icon =
                button.querySelector('i');

            const label =
                button.querySelector(
                    '[data-theme-toggle-label]'
                );

            const nextThemeLabel =
                theme === 'dark'
                    ? 'Tema claro'
                    : 'Tema oscuro';

            if (icon) {
                icon.className =
                    theme === 'dark'
                        ? 'bi bi-sun-fill'
                        : 'bi bi-moon-stars-fill';
            }

            if (label) {
                label.textContent = nextThemeLabel;
            }

            button.setAttribute(
                'aria-label',
                'Cambiar a ' + nextThemeLabel.toLowerCase()
            );
        });
    }

    function toggleTheme() {
        const nextTheme =
            root.getAttribute('data-theme') === 'dark'
                ? 'light'
                : 'dark';

        localStorage.setItem(
            themeStorageKey,
            nextTheme
        );

        applyTheme(nextTheme);
    }

    function initThemeToggle() {
        applyTheme(
            getInitialTheme()
        );

        document.querySelectorAll(
            '[data-theme-toggle]'
        ).forEach(function (button) {
            button.addEventListener(
                'click',
                toggleTheme
            );
        });
    }

    function showLoadingOverlay() {
        const overlay =
            document.getElementById(
                'appLoadingOverlay'
            );

        if (!overlay) {
            return;
        }

        overlay.classList.add('active');
        overlay.setAttribute(
            'aria-hidden',
            'false'
        );
    }

    function enhanceSubmitButton(button) {
        if (!button || button.dataset.loadingState === 'active') {
            return;
        }

        button.dataset.loadingState = 'active';
        button.dataset.originalHtml = button.innerHTML;
        button.disabled = true;
        button.innerHTML =
            '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Guardando...';
    }

    function initLoadingState() {
        document.querySelectorAll(
            'form[method="post"]'
        ).forEach(function (form) {
            form.addEventListener(
                'submit',
                function () {
                    const submitButton =
                        document.activeElement
                        && document.activeElement.form === form
                            ? document.activeElement
                            : form.querySelector('[type="submit"]');

                    enhanceSubmitButton(
                        submitButton
                    );

                    showLoadingOverlay();
                }
            );
        });

        document.querySelectorAll(
            '[data-loading-link]'
        ).forEach(function (link) {
            link.addEventListener(
                'click',
                function () {
                    showLoadingOverlay();
                }
            );
        });
    }

    function initToasts() {
        if (!window.bootstrap) {
            return;
        }

        document.querySelectorAll(
            '.toast'
        ).forEach(function (element) {
            const toast =
                bootstrap.Toast.getOrCreateInstance(
                    element
                );

            toast.show();
        });
    }

    window.toggleSidebar = function () {
        const sidebar =
            document.querySelector(
                '.sidebar'
            );

        const overlay =
            document.querySelector(
                '.sidebar-overlay'
            );

        if (!sidebar || !overlay) {
            return;
        }

        sidebar.classList.toggle('active');
        overlay.classList.toggle('active');
    };

    document.addEventListener(
        'DOMContentLoaded',
        function () {
            initThemeToggle();
            initLoadingState();
            initToasts();
        }
    );
})();
