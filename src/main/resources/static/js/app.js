// Modern Sport Management System - Enhanced JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Initialize tooltips everywhere
    initializeTooltips();
    
    // Initialize auto-dismiss alerts
    initializeAlerts();
    
    // Initialize form enhancements
    initializeFormEnhancements();
    
    // Initialize interactive elements
    initializeInteractiveElements();
    
    // Initialize animations
    initializeAnimations();
    
    // Initialize theme management
    initializeTheme();
    
    // Initialize data visualization components
    initializeDataVisualization();
    
    // Initialize chart components
    initializeChartComponents();
    
    // Initialize accessibility features
    initializeAccessibility();
    
    // Initialize performance monitoring
    initializePerformanceMonitoring();
    
    // Initialize real-time updates
    initializeRealTimeUpdates();
}

// Tooltip initialization
function initializeTooltips() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Auto-dismiss alerts
function initializeAlerts() {
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
        alerts.forEach(function(alert) {
            if (bootstrap.Alert.getInstance(alert) || new bootstrap.Alert(alert)) {
                var bsAlert = bootstrap.Alert.getInstance(alert) || new bootstrap.Alert(alert);
                bsAlert.close();
            }
        });
    }, 5000);
}

// Form enhancements
function initializeFormEnhancements() {
    // Enhanced form validation
    var forms = document.querySelectorAll('form[data-validate]');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                
                // Focus on first invalid field
                const firstInvalid = form.querySelector(':invalid');
                if (firstInvalid) {
                    firstInvalid.focus();
                    firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }
            form.classList.add('was-validated');
        });
    });

    // Date/Time input helpers with better UX
    var dateTimeInputs = document.querySelectorAll('input[type="datetime-local"]');
    dateTimeInputs.forEach(function(input) {
        if (!input.value) {
            var now = new Date();
            var minDate = new Date(now.getTime() - (now.getTimezoneOffset() * 60000));
            input.min = minDate.toISOString().slice(0, 16);
            
            // Set default to next hour
            var nextHour = new Date(now.getTime() + (60 * 60 * 1000));
            nextHour.setMinutes(0);
            input.value = new Date(nextHour.getTime() - (nextHour.getTimezoneOffset() * 60000)).toISOString().slice(0, 16);
        }
    });

    // Enhanced search functionality
    var searchInputs = document.querySelectorAll('input[name="term"]');
    searchInputs.forEach(function(input) {
        let searchTimeout;
        
        input.addEventListener('input', function(event) {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                if (this.value.length > 2 || this.value.length === 0) {
                    // Could implement live search here
                }
            }, 300);
        });
        
        input.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                this.closest('form').submit();
            }
        });
    });

    // Price calculation for reservations with real-time updates
    initializePriceCalculation();
}

// Real-time price calculation
function initializePriceCalculation() {
    var salleSelect = document.querySelector('#salle');
    var dateDebutInput = document.querySelector('#dateDebut');
    var dateFinInput = document.querySelector('#dateFin');
    
    if (salleSelect && dateDebutInput && dateFinInput) {
        function calculateAndShowPrice() {
            var salleOption = salleSelect.options[salleSelect.selectedIndex];
            var dateDebut = new Date(dateDebutInput.value);
            var dateFin = new Date(dateFinInput.value);
            
            if (salleOption && salleOption.value && dateDebutInput.value && dateFinInput.value) {
                var prixHeure = extractPriceFromText(salleOption.text);
                var diffMs = dateFin - dateDebut;
                var heures = diffMs / (1000 * 60 * 60);
                
                if (heures > 0 && prixHeure > 0) {
                    var prixTotal = heures * prixHeure;
                    showPriceEstimate(prixTotal, heures, prixHeure);
                } else if (heures <= 0) {
                    showPriceError("La date de fin doit être après la date de début");
                }
            } else {
                removePriceEstimate();
            }
        }
        
        salleSelect.addEventListener('change', calculateAndShowPrice);
        dateDebutInput.addEventListener('change', calculateAndShowPrice);
        dateFinInput.addEventListener('change', calculateAndShowPrice);
        
        // Initial calculation
        calculateAndShowPrice();
    }
}

// Interactive elements
function initializeInteractiveElements() {
    // Enhanced loading states for buttons
    var submitButtons = document.querySelectorAll('button[type="submit"]');
    submitButtons.forEach(function(button) {
        const form = button.closest('form');
        if (form) {
            form.addEventListener('submit', function(event) {
                // Only show loading if form is valid
                if (form.checkValidity()) {
                    showButtonLoading(button);
                    
                    // Re-enable after timeout as failsafe
                    setTimeout(() => {
                        hideButtonLoading(button);
                    }, 10000);
                }
            });
        }
    });

    // Enhanced confirmation dialogs
    var dangerButtons = document.querySelectorAll('.btn-danger[href*="delete"]');
    dangerButtons.forEach(function(button) {
        // Remove the onclick attribute if it exists
        const originalOnclick = button.getAttribute('onclick');
        button.removeAttribute('onclick');
        
        button.addEventListener('click', function(event) {
            event.preventDefault();
            showCustomConfirmDialog(
                'Confirmation requise',
                'Êtes-vous sûr de vouloir effectuer cette action? Cette opération ne peut pas être annulée.',
                () => {
                    window.location.href = this.href;
                }
            );
        });
    });

    // Smart table interactions
    initializeSmartTables();
}

// Smart table functionality
function initializeSmartTables() {
    const tables = document.querySelectorAll('table');
    tables.forEach(table => {
        // Add row hover highlighting
        const rows = table.querySelectorAll('tbody tr');
        rows.forEach(row => {
            row.addEventListener('mouseenter', function() {
                this.style.backgroundColor = 'rgba(102, 126, 234, 0.1)';
            });
            row.addEventListener('mouseleave', function() {
                this.style.backgroundColor = '';
            });
        });

        // Add sortable headers if not already present
        const headers = table.querySelectorAll('thead th');
        headers.forEach((header, index) => {
            if (!header.querySelector('.sort-icon')) {
                header.style.cursor = 'pointer';
                header.innerHTML += ' <i class="bi bi-arrow-down-up sort-icon opacity-25"></i>';
                
                header.addEventListener('click', function() {
                    sortTableByColumn(table, index);
                });
            }
        });
    });
}

// Animations
function initializeAnimations() {
    // Intersection Observer for scroll animations
    const animatedElements = document.querySelectorAll('.fade-in-up, .stats-card, .dashboard-card');
    
    if (animatedElements.length > 0) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.animation = 'fadeInUp 0.6s ease-out forwards';
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.1 });

        animatedElements.forEach(el => {
            el.style.opacity = '0';
            el.style.transform = 'translateY(30px)';
            observer.observe(el);
        });
    }

    // Number animation for stats
    animateNumbers();
}

// Theme management
function initializeTheme() {
    // Check for saved theme preference or default to system preference
    const savedTheme = localStorage.getItem('theme');
    const systemTheme = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    const theme = savedTheme || systemTheme;
    
    if (theme === 'dark') {
        document.body.classList.add('dark-theme');
    }

    // Listen for system theme changes
    window.matchMedia('(prefers-color-scheme: dark)').addListener(e => {
        if (!localStorage.getItem('theme')) {
            if (e.matches) {
                document.body.classList.add('dark-theme');
            } else {
                document.body.classList.remove('dark-theme');
            }
        }
    });
}

// Utility Functions
function extractPriceFromText(text) {
    const match = text.match(/(\d+(?:,\d+)?)\s*Ar/);
    return match ? parseFloat(match[1].replace(',', '')) : 0;
}

function showPriceEstimate(prixTotal, heures, prixHeure) {
    removePriceEstimate();
    
    const estimate = document.createElement('div');
    estimate.id = 'price-estimate';
    estimate.className = 'alert alert-info mt-3 price-estimate-animation';
    estimate.innerHTML = `
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <i class="bi bi-calculator"></i>
                <strong>Estimation du coût</strong>
            </div>
            <div class="text-end">
                <div class="small text-muted">${heures}h × ${Math.round(prixHeure).toLocaleString()} Ar</div>
                <div class="fs-5 fw-bold text-success">${Math.round(prixTotal).toLocaleString()} Ar</div>
            </div>
        </div>
    `;
    
    const dateFinContainer = document.querySelector('#dateFin').closest('.mb-3');
    dateFinContainer.insertAdjacentElement('afterend', estimate);
}

function showPriceError(message) {
    removePriceEstimate();
    
    const error = document.createElement('div');
    error.id = 'price-estimate';
    error.className = 'alert alert-warning mt-3';
    error.innerHTML = `<i class="bi bi-exclamation-triangle"></i> ${message}`;
    
    const dateFinContainer = document.querySelector('#dateFin').closest('.mb-3');
    dateFinContainer.insertAdjacentElement('afterend', error);
}

function removePriceEstimate() {
    const existingEstimate = document.querySelector('#price-estimate');
    if (existingEstimate) {
        existingEstimate.remove();
    }
}

function showButtonLoading(button) {
    const originalText = button.innerHTML;
    button.setAttribute('data-original-text', originalText);
    button.disabled = true;
    button.innerHTML = '<span class="loading"></span> Chargement...';
}

function hideButtonLoading(button) {
    const originalText = button.getAttribute('data-original-text') || 'Valider';
    button.disabled = false;
    button.innerHTML = originalText;
}

function showCustomConfirmDialog(title, message, onConfirm) {
    const modal = document.createElement('div');
    modal.className = 'modal fade';
    modal.innerHTML = `
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header border-0">
                    <h5 class="modal-title">${title}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="text-center mb-4">
                        <i class="bi bi-exclamation-triangle text-warning" style="font-size: 3rem;"></i>
                    </div>
                    <p class="text-center">${message}</p>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                    <button type="button" class="btn btn-danger" id="confirmAction">Confirmer</button>
                </div>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    const modalInstance = new bootstrap.Modal(modal);
    
    modal.querySelector('#confirmAction').addEventListener('click', function() {
        onConfirm();
        modalInstance.hide();
    });
    
    modal.addEventListener('hidden.bs.modal', function() {
        document.body.removeChild(modal);
    });
    
    modalInstance.show();
}

function sortTableByColumn(table, columnIndex) {
    const tbody = table.getElementsByTagName('tbody')[0];
    const rows = Array.from(tbody.getElementsByTagName('tr'));
    
    // Determine sort direction
    const header = table.querySelector(`thead th:nth-child(${columnIndex + 1})`);
    const isAscending = !header.classList.contains('sort-desc');
    
    // Remove all sort classes
    table.querySelectorAll('thead th').forEach(h => {
        h.classList.remove('sort-asc', 'sort-desc');
    });
    
    // Add appropriate sort class
    header.classList.add(isAscending ? 'sort-asc' : 'sort-desc');
    
    rows.sort((a, b) => {
        const aValue = a.cells[columnIndex]?.textContent.trim() || '';
        const bValue = b.cells[columnIndex]?.textContent.trim() || '';
        
        // Try to parse as numbers first
        const aNum = parseFloat(aValue.replace(/[^\d.-]/g, ''));
        const bNum = parseFloat(bValue.replace(/[^\d.-]/g, ''));
        
        if (!isNaN(aNum) && !isNaN(bNum)) {
            return isAscending ? aNum - bNum : bNum - aNum;
        } else {
            return isAscending ? 
                aValue.localeCompare(bValue) : 
                bValue.localeCompare(aValue);
        }
    });
    
    rows.forEach(row => tbody.appendChild(row));
}

function animateNumbers() {
    const statsNumbers = document.querySelectorAll('.stats-number');
    statsNumbers.forEach(el => {
        const target = el.textContent;
        const numMatch = target.match(/\d+/);
        if (numMatch) {
            const finalNumber = parseInt(numMatch[0]);
            let current = 0;
            const increment = finalNumber / 50;
            const timer = setInterval(() => {
                current += increment;
                if (current >= finalNumber) {
                    current = finalNumber;
                    clearInterval(timer);
                }
                el.textContent = target.replace(/\d+/, Math.floor(current));
            }, 30);
        }
    });
}

function toggleTheme() {
    document.body.classList.toggle('dark-theme');
    const theme = document.body.classList.contains('dark-theme') ? 'dark' : 'light';
    localStorage.setItem('theme', theme);
}

// Advanced data visualization functions
function createProgressRing(element, percentage, label) {
    const radius = 54;
    const circumference = 2 * Math.PI * radius;
    const offset = circumference - (percentage / 100) * circumference;
    
    element.innerHTML = `
        <svg class="progress-ring" width="120" height="120">
            <circle class="progress-ring-circle progress-ring-background" 
                    cx="60" cy="60" r="${radius}"></circle>
            <circle class="progress-ring-circle progress-ring-progress" 
                    cx="60" cy="60" r="${radius}"
                    stroke-dasharray="${circumference}"
                    stroke-dashoffset="${offset}"></circle>
        </svg>
        <div class="progress-ring-text">
            <span class="progress-ring-value">${percentage}%</span>
            <span class="progress-ring-label">${label}</span>
        </div>
    `;
}

function createSparkline(element, data, color = '#667eea') {
    const width = element.offsetWidth;
    const height = 40;
    const max = Math.max(...data);
    const min = Math.min(...data);
    const range = max - min || 1;
    
    const points = data.map((value, index) => {
        const x = (index / (data.length - 1)) * width;
        const y = height - ((value - min) / range) * height;
        return `${x},${y}`;
    }).join(' ');
    
    element.innerHTML = `
        <svg class="sparkline" width="${width}" height="${height}" viewBox="0 0 ${width} ${height}">
            <polyline points="${points}" 
                      fill="none" 
                      stroke="${color}" 
                      stroke-width="2" 
                      stroke-linecap="round" 
                      stroke-linejoin="round"/>
        </svg>
    `;
}

function initializeDataVisualization() {
    // Initialize progress rings
    document.querySelectorAll('[data-progress-ring]').forEach(element => {
        const percentage = parseInt(element.dataset.percentage) || 0;
        const label = element.dataset.label || '';
        createProgressRing(element, percentage, label);
    });
    
    // Initialize sparklines
    document.querySelectorAll('[data-sparkline]').forEach(element => {
        const data = JSON.parse(element.dataset.sparkline || '[]');
        const color = element.dataset.color || '#667eea';
        createSparkline(element, data, color);
    });
    
    // Initialize metric cards with animations
    initializeMetricCards();
}

function initializeMetricCards() {
    const metricCards = document.querySelectorAll('.metric-card');
    
    if (metricCards.length > 0 && window.IntersectionObserver) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const valueElement = entry.target.querySelector('.metric-value');
                    const trendElement = entry.target.querySelector('.metric-trend');
                    
                    if (valueElement) {
                        animateMetricValue(valueElement);
                    }
                    
                    if (trendElement) {
                        setTimeout(() => {
                            trendElement.style.opacity = '1';
                            trendElement.style.transform = 'translateY(0)';
                        }, 500);
                    }
                    
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.3 });

        metricCards.forEach(card => {
            const trendElement = card.querySelector('.metric-trend');
            if (trendElement) {
                trendElement.style.opacity = '0';
                trendElement.style.transform = 'translateY(10px)';
                trendElement.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
            }
            observer.observe(card);
        });
    }
}

function animateMetricValue(element) {
    const target = element.textContent.replace(/[^\d.-]/g, '');
    const finalNumber = parseFloat(target) || 0;
    let current = 0;
    const increment = finalNumber / 60;
    const suffix = element.textContent.replace(/[\d.-]/g, '');
    
    const timer = setInterval(() => {
        current += increment;
        if (current >= finalNumber) {
            current = finalNumber;
            clearInterval(timer);
        }
        
        const displayValue = Math.floor(current);
        element.textContent = displayValue + suffix;
    }, 16);
}

// Enhanced chart utilities
function initializeChartComponents() {
    // Add chart.js global defaults for consistent styling
    if (window.Chart) {
        Chart.defaults.font.family = '"Inter", sans-serif';
        Chart.defaults.color = '#6b7280';
        Chart.defaults.borderColor = 'rgba(0, 0, 0, 0.1)';
        Chart.defaults.backgroundColor = 'rgba(102, 126, 234, 0.1)';
        
        // Custom gradient plugin
        const gradientPlugin = {
            id: 'gradientBg',
            beforeDraw: (chart, args, options) => {
                if (!options.enabled) return;
                
                const ctx = chart.canvas.getContext('2d');
                const gradient = ctx.createLinearGradient(0, 0, 0, chart.height);
                gradient.addColorStop(0, options.startColor || 'rgba(102, 126, 234, 0.2)');
                gradient.addColorStop(1, options.endColor || 'rgba(102, 126, 234, 0.05)');
                
                ctx.save();
                ctx.fillStyle = gradient;
                ctx.fillRect(0, 0, chart.width, chart.height);
                ctx.restore();
            }
        };
        
        Chart.register(gradientPlugin);
    }
}

// Real-time data updates
function initializeRealTimeUpdates() {
    // Simulate real-time updates for demonstration
    setInterval(() => {
        updateDashboardMetrics();
    }, 30000); // Update every 30 seconds
}

function updateDashboardMetrics() {
    // Update stats numbers with slight variations
    const statsElements = document.querySelectorAll('.stats-number');
    
    statsElements.forEach(element => {
        const currentValue = parseInt(element.textContent) || 0;
        const variation = Math.floor(Math.random() * 3) - 1; // -1, 0, or 1
        const newValue = Math.max(0, currentValue + variation);
        
        if (newValue !== currentValue) {
            animateNumberChange(element, currentValue, newValue);
        }
    });
}

function animateNumberChange(element, from, to) {
    const duration = 1000;
    const steps = 30;
    const stepValue = (to - from) / steps;
    let current = from;
    let step = 0;
    
    const animation = setInterval(() => {
        step++;
        current += stepValue;
        
        if (step >= steps) {
            current = to;
            clearInterval(animation);
        }
        
        element.textContent = Math.round(current);
    }, duration / steps);
}

// Enhanced accessibility features
function initializeAccessibility() {
    // Add keyboard navigation for interactive elements
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Tab') {
            document.body.classList.add('keyboard-navigation');
        }
    });
    
    document.addEventListener('mousedown', () => {
        document.body.classList.remove('keyboard-navigation');
    });
    
    // Enhanced focus indicators
    const focusableElements = document.querySelectorAll('a, button, input, select, textarea, [tabindex]:not([tabindex="-1"])');
    
    focusableElements.forEach(element => {
        element.addEventListener('focus', function() {
            this.classList.add('focus-visible');
        });
        
        element.addEventListener('blur', function() {
            this.classList.remove('focus-visible');
        });
    });
}

// Performance monitoring
function initializePerformanceMonitoring() {
    if ('performance' in window) {
        window.addEventListener('load', () => {
            setTimeout(() => {
                const perfData = window.performance.timing;
                const loadTime = perfData.loadEventEnd - perfData.navigationStart;
                
                console.log(`Page load time: ${loadTime}ms`);
                
                // Send performance data (in a real app, you'd send this to analytics)
                if (loadTime > 3000) {
                    console.warn('Page load time is slow. Consider optimizing resources.');
                }
            }, 0);
        });
    }
}

// Export functions for global use
window.SportApp = {
    toggleTheme,
    showCustomConfirmDialog,
    animateNumbers,
    initializePriceCalculation,
    createProgressRing,
    createSparkline,
    initializeDataVisualization,
    animateMetricValue
};

// Add CSS for animations
const style = document.createElement('style');
style.textContent = `
    .price-estimate-animation {
        animation: slideInDown 0.3s ease-out;
    }
    
    @keyframes slideInDown {
        from {
            opacity: 0;
            transform: translateY(-20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .sort-asc .sort-icon::before {
        content: "\\F145";
        color: var(--primary-color);
    }
    
    .sort-desc .sort-icon::before {
        content: "\\F165";
        color: var(--primary-color);
    }
    
    .modal-dialog-centered {
        animation: fadeInScale 0.2s ease-out;
    }
    
    @keyframes fadeInScale {
        from {
            opacity: 0;
            transform: scale(0.9);
        }
        to {
            opacity: 1;
            transform: scale(1);
        }
    }
`;
document.head.appendChild(style);