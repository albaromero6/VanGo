import defaultTheme from 'tailwindcss/defaultTheme';
import forms from '@tailwindcss/forms';

/** @type {import('tailwindcss').Config} */
export default {
    content: [
        './vendor/laravel/framework/src/Illuminate/Pagination/resources/views/*.blade.php',
        './storage/framework/views/*.php',
        './resources/views/**/*.blade.php',
    ],

    theme: {
        extend: {
            fontFamily: {
                sans: ['Figtree', ...defaultTheme.fontFamily.sans],
                poppins: ['Poppins', 'sans-serif'],
                volkhov: ['Volkhov', 'serif'],
            },
            colors: {
                ocean:  '#05889C', 
                hovers: '#046B7D',
                tittle: '#181E4B',
            },
            backgroundImage: {
                'login-bg': "url('/images/fondo.jpg')",
            },
        },
    },

    plugins: [forms],
};

