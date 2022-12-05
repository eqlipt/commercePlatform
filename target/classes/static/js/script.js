'use strict'

// Сортировка товаров по цене
let changeOrderButton = document.querySelector('.input__price-sort_button');
const productCards = document.querySelectorAll('.product-card');
// Формируем массив из цен. Изначально все товары приходят уже отсортированными в порядке возрастания цены
const productPricesArray = Array.from(productCards).map(card => Number.parseInt(card.querySelector('.product-card__price-label').innerHTML));
let order = productPricesArray;

changeOrderButton.addEventListener('click', sortProductCards);
function sortProductCards() {
    // меняем иконку на кнопке
    if (changeOrderButton.classList.contains('fa-sort-up')) {
        changeOrderButton.classList.remove('fa-sort-up');
        changeOrderButton.classList.add('fa-sort-down');
    } else {
        changeOrderButton.classList.remove('fa-sort-down');
        changeOrderButton.classList.add('fa-sort-up');
    }
    // меняем порядок на обратный
    order = order.reverse();
    productCards.forEach((card, index) => card.style.order = order[index]);
}