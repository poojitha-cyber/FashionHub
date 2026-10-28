/**
 * Modern FashionHub E-Commerce Client Scripts
 * Vanilla JavaScript for live search, category filtering, quantity steppers,
 * asynchronous cart interactions, and non-intrusive toast notifications.
 */

document.addEventListener('DOMContentLoaded', () => {
    initCategoryFiltering();
    initLiveSearch();
    initQuantitySteppers();
    initAddToCartButtons();
    initCartQuantityAjax();
    initImageFallbacks();
});

/* ==========================================================================
   Toast Notification System
   ========================================================================== */
function showToast(message, type = 'success') {
    let container = document.getElementById('toastContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toastContainer';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast ${type === 'success' ? 'toast-success' : ''}`;
    
    const icon = type === 'success' ? '✓' : 'ℹ';
    toast.innerHTML = `
        <span style="font-weight: bold; background: rgba(255,255,255,0.2); width: 22px; height: 22px; border-radius: 50%; display: flex; align-items: center; justify-content: center;">${icon}</span>
        <span>${message}</span>
    `;

    container.appendChild(toast);

    // Auto remove after 3.5 seconds
    setTimeout(() => {
        toast.classList.add('hide');
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

/* ==========================================================================
   Cart Badge Update Animation
   ========================================================================== */
function updateCartBadge(count) {
    const badges = document.querySelectorAll('.cart-count');
    badges.forEach(badge => {
        badge.textContent = count;
        badge.classList.remove('bump');
        // Force reflow
        void badge.offsetWidth;
        badge.classList.add('bump');
        setTimeout(() => badge.classList.remove('bump'), 300);
    });
}

/* ==========================================================================
   Client-Side Category & Live Search Filtering
   ========================================================================== */
function filterProducts() {
    const searchInput = document.getElementById('liveSearchInput');
    const searchTerm = searchInput ? searchInput.value.trim().toLowerCase() : '';
    
    const activePill = document.querySelector('.category-pill.active');
    const selectedCategory = activePill ? activePill.getAttribute('data-category').toLowerCase() : 'all';

    const cards = document.querySelectorAll('.product-card');
    let visibleCount = 0;

    cards.forEach(card => {
        const cardCategory = (card.getAttribute('data-category') || '').toLowerCase();
        const cardTitle = (card.getAttribute('data-title') || '').toLowerCase();
        const cardDesc = (card.getAttribute('data-desc') || '').toLowerCase();

        const matchesCategory = (selectedCategory === 'all' || cardCategory === selectedCategory);
        const matchesSearch = (searchTerm === '' || cardTitle.includes(searchTerm) || cardDesc.includes(searchTerm));

        if (matchesCategory && matchesSearch) {
            card.style.display = 'flex';
            visibleCount++;
        } else {
            card.style.display = 'none';
        }
    });

    const countDisplay = document.getElementById('productCountDisplay');
    if (countDisplay) {
        countDisplay.textContent = `Showing ${visibleCount} product${visibleCount === 1 ? '' : 's'}`;
    }

    const noResultsState = document.getElementById('noResultsState');
    if (noResultsState) {
        noResultsState.style.display = visibleCount === 0 ? 'block' : 'none';
    }
}

function initCategoryFiltering() {
    const pills = document.querySelectorAll('.category-pill');
    pills.forEach(pill => {
        pill.addEventListener('click', (e) => {
            e.preventDefault();
            pills.forEach(p => p.classList.remove('active'));
            pill.classList.add('active');
            filterProducts();
        });
    });
}

function initLiveSearch() {
    const searchInput = document.getElementById('liveSearchInput');
    if (searchInput) {
        searchInput.addEventListener('input', () => {
            filterProducts();
        });
    }
}

/* ==========================================================================
   Quantity Stepper Controls (- / +)
   ========================================================================== */
function initQuantitySteppers() {
    document.querySelectorAll('.qty-control').forEach(control => {
        const minusBtn = control.querySelector('.qty-minus');
        const plusBtn = control.querySelector('.qty-plus');
        const input = control.querySelector('.qty-input');

        if (!input) return;

        if (minusBtn) {
            minusBtn.addEventListener('click', () => {
                let current = parseInt(input.value) || 1;
                if (current > 1) {
                    input.value = current - 1;
                    input.dispatchEvent(new Event('change', { bubbles: true }));
                }
            });
        }

        if (plusBtn) {
            plusBtn.addEventListener('click', () => {
                let current = parseInt(input.value) || 1;
                input.value = current + 1;
                input.dispatchEvent(new Event('change', { bubbles: true }));
            });
        }
    });
}

/* ==========================================================================
   Asynchronous Add-To-Cart (AJAX with Form Fallback)
   ========================================================================== */
function initAddToCartButtons() {
    document.querySelectorAll('.ajax-add-to-cart-form').forEach(form => {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const productId = form.querySelector('input[name="productId"]')?.value;
            const quantityInput = form.querySelector('input[name="quantity"]');
            const quantity = quantityInput ? quantityInput.value : 1;
            const btn = form.querySelector('button[type="submit"]');

            if (!productId) return;

            const originalBtnHtml = btn ? btn.innerHTML : '';
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = '<span>Adding...</span>';
            }

            try {
                const formData = new URLSearchParams();
                formData.append('productId', productId);
                formData.append('quantity', quantity);

                const response = await fetch('/api/cart/add', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: formData.toString()
                });

                if (response.ok) {
                    const data = await response.json();
                    updateCartBadge(data.cartCount);
                    showToast(`Added ${quantity > 1 ? quantity + 'x ' : ''}"${data.productName}" to cart!`, 'success');
                } else {
                    // Fallback to normal form submission
                    form.submit();
                }
            } catch (err) {
                console.error('AJAX add-to-cart failed, submitting form natively:', err);
                form.submit();
            } finally {
                if (btn) {
                    btn.disabled = false;
                    btn.innerHTML = originalBtnHtml;
                }
            }
        });
    });
}

/* ==========================================================================
   Asynchronous Shopping Cart Page Updates (AJAX)
   ========================================================================== */
function initCartQuantityAjax() {
    const cartTable = document.querySelector('.cart-table');
    if (!cartTable) return;

    // Handle quantity changes in cart
    document.querySelectorAll('.cart-qty-input').forEach(input => {
        input.addEventListener('change', async () => {
            const productId = input.getAttribute('data-product-id');
            const quantity = parseInt(input.value) || 0;
            await updateCartItemQuantity(productId, quantity);
        });
    });

    // Handle AJAX remove button clicks
    document.querySelectorAll('.btn-ajax-remove').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            const productId = btn.getAttribute('data-product-id');
            if (!productId) return;
            await removeCartItem(productId);
        });
    });
}

async function updateCartItemQuantity(productId, quantity) {
    try {
        const formData = new URLSearchParams();
        formData.append('productId', productId);
        formData.append('quantity', quantity);

        const res = await fetch('/api/cart/update', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        });

        if (res.ok) {
            const data = await res.json();
            updateCartBadge(data.cartCount);

            if (data.isEmpty) {
                window.location.reload();
                return;
            }

            const row = document.getElementById(`cart-row-${productId}`);
            if (data.itemRemoved && row) {
                row.remove();
            } else if (row) {
                const subtotalEl = row.querySelector('.item-subtotal-display');
                if (subtotalEl) subtotalEl.textContent = data.itemSubtotal;
            }

            // Update Summary Card
            updateSummaryDisplays(data);
            showToast('Cart updated', 'success');
        } else {
            window.location.reload();
        }
    } catch (err) {
        console.error('Failed to update cart item:', err);
        window.location.reload();
    }
}

async function removeCartItem(productId) {
    try {
        const res = await fetch(`/api/cart/remove/${productId}`, {
            method: 'POST'
        });

        if (res.ok) {
            const data = await res.json();
            updateCartBadge(data.cartCount);

            const row = document.getElementById(`cart-row-${productId}`);
            if (row) {
                row.style.opacity = '0';
                setTimeout(() => {
                    row.remove();
                    if (data.isEmpty) {
                        window.location.reload();
                    }
                }, 200);
            }

            updateSummaryDisplays(data);
            showToast('Item removed from cart', 'success');
        } else {
            window.location.reload();
        }
    } catch (err) {
        console.error('Failed to remove item:', err);
        window.location.reload();
    }
}

function updateSummaryDisplays(data) {
    const subtotalEl = document.getElementById('summarySubtotal');
    const shippingEl = document.getElementById('summaryShipping');
    const taxEl = document.getElementById('summaryTax');
    const totalEl = document.getElementById('summaryTotal');

    if (subtotalEl) subtotalEl.textContent = data.subtotal;
    if (shippingEl) shippingEl.textContent = data.shipping;
    if (taxEl) taxEl.textContent = data.tax;
    if (totalEl) totalEl.textContent = data.total;
}

/* ==========================================================================
   Image Fallback Handler (Provides clean SVG placeholder if offline)
   ========================================================================== */
function initImageFallbacks() {
    const images = document.querySelectorAll('img.product-img, img.cart-thumb, .detail-gallery img');
    images.forEach(img => {
        img.addEventListener('error', () => {
            const alt = encodeURIComponent(img.alt || 'Product');
            // Clean fallback SVG with modern gradient and product label
            img.src = `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="600" height="450" viewBox="0 0 600 450"><defs><linearGradient id="g" x1="0%" y1="0%" x2="100%" y2="100%"><stop offset="0%" stop-color="%231e293b"/><stop offset="100%" stop-color="%23334155"/></linearGradient></defs><rect width="600" height="450" fill="url(%23g)"/><text x="50%" y="48%" dominant-baseline="middle" text-anchor="middle" fill="%2394a3b8" font-family="system-ui,sans-serif" font-size="20" font-weight="600">${alt}</text><text x="50%" y="58%" dominant-baseline="middle" text-anchor="middle" fill="%2364748b" font-family="system-ui,sans-serif" font-size="14">FashionHub</text></svg>`;
        });
    });
}
