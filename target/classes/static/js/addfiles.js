'use strict'
const input = document.querySelector( '.input-file' );
const label = input.nextElementSibling;
const imageBlock = document.querySelector( '.upload__image-container' );
const MAX_IMAGES = 5;
input.addEventListener('change', e => {
    while(imageBlock.firstChild) {
        imageBlock.removeChild(imageBlock.firstChild);
    }
    let fileName = '';
    if( input.files) {
        const curFiles = input.files;
        if (input.files.length > 1 ) {
            const totalFiles = input.files.length > (MAX_IMAGES - 1) ? MAX_IMAGES : input.files.length;
            fileName = input.getAttribute('data-multiple-caption').replace('{count}', totalFiles);
        } else {
            fileName = e.target.value.split( '\\' ).pop();
        }

        if (fileName) label.innerHTML= fileName;

        for( let i = 0; i < MAX_IMAGES; i++ ) {
            if(validFileType(curFiles[i])) {
                let image = document.createElement('img');
                image.src = window.URL.createObjectURL(curFiles[i]);
                imageBlock.appendChild(image);
            }
        }
    }
});

const fileTypes = [
    'image/jpeg',
    'image/pjpeg',
    'image/png'
]

function validFileType(file) {
    for(let i = 0; i < fileTypes.length; i++) {
        if(file.type === fileTypes[i]) {
            return true;
        }
    }
    return false;
}