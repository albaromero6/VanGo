<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Van&Go</title>
    <link rel="icon" href="{{ asset('favicon.ico') }}" type="image/x-icon">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    @vite('resources/css/app.css')

</head>
<body>

    <!-- Barra de navegación -->
    <nav class="bg-white border-gray-200 dark:bg-gray-900 w-full font-poppins">
        <div class="flex flex-wrap items-center justify-between p-4">
            <div class="flex items-center">
                <a href="/" class="flex items-center space-x-3 rtl:space-x-reverse">
                    <img src="{{ asset('storage/imagenes/Logo.png') }}" alt="Logo Van&Go" class="h-12 w-auto">
                </a>
                <ul class="flex font-medium font-poppins font-bold ml-4 md:flex-row md:space-x-8">
                    <li>
                        <a href="{{ route('catalogo') }}" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 font-bold">Catálogo</a>
                    </li>
                    <li>
                        <a href="{{ route('elijenos') }}" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 font-bold">Elíjenos</a>
                    </li>
                    <li>
                        <a href="{{ route('rutas') }}" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 font-bold">Rutas</a>
                    </li>
                    <li>
                        <a href="{{ route('contacto') }}" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 font-bold">Contacto</a>
                    </li>
                </ul>
            </div>

            <div class="flex md:order-2 items-center">
                <a href="#" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 mr-4 font-poppins font-bold">Regístrate</a>
                <button type="button" onclick="window.location.href='{{ route('login') }}'" class="text-white bg-ocean hover:bg-hovers focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg text-sm px-4 py-2 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 font-poppins font-bold">Iniciar sesión</button>
                <button data-collapse-toggle="navbar-cta" type="button" class="inline-flex items-center p-2 w-10 h-10 justify-center text-sm text-gray-500 rounded-lg md:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600" aria-controls="navbar-cta" aria-expanded="false">
                    <span class="sr-only font-poppins">Open main menu</span>
                    <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 17 14">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M1 1h15M1 7h15M1 13h15"/>
                    </svg>
                </button>
            </div>
        </div>
    </nav>

    <header>
        <div class="hero flex flex-col items-center justify-center text-center">
            <h1>Viaja, disfruta y vive un mundo de emociones</h1>
            <p>Consigue una camper o una caravana donde y cuando lo necesites, con tu dispositivo IOS y Android</p>
            <div class="app-buttons">
                <a href="#" class="google-play-button">
                    <img src="{{ asset('storage/imagenes/Google.png') }}" alt="Google Play" class="store-logo">
                </a>
                <a href="#" class="app-store-button">
                    <img src="{{ asset('storage/imagenes/IOS.png') }}" alt="App Store" class="store-logo">
                </a>
            </div>
        </div>
        <!-- Hero Image -->
        <div class="hero-image">
            <img src="{{ asset('storage/imagenes/Portada.png') }}" alt="Portada">
        </div>
    </header>

    <!-- Búsqueda -->
    <section class="search">
        <div class="container mx-auto flex justify-center">
            <form action="#" method="GET">
                <div class="form-group">
                    <div class="input-icon">
                        <i class="fas fa-map-marker-alt"></i> <!-- Icono de localización -->
                        <input type="text" name="ubicacion_salida" placeholder="Ubicación salida">
                    </div>
                </div>
                <div class="form-group input-calendar">
                    <i class="fas fa-calendar-alt"></i> <!-- Icono de calendario -->
                    <input type="datetime-local" name="fecha_salida">
                </div>
                <div class="form-group">
                    <div class="input-icon">
                        <i class="fas fa-map-marker-alt"></i> <!-- Icono de localización -->
                        <input type="text" name="ubicacion_llegada" placeholder="Ubicación llegada">
                    </div>
                </div>
                <div class="form-group input-calendar">
                    <i class="fas fa-calendar-alt"></i> <!-- Icono de calendario -->
                    <input type="datetime-local" name="fecha_llegada">
                </div>
                <button type="submit" class="btn btn-search">Buscar</button>
            </form>
        </div>
    </section>

    <!-- Tipos -->
    <section class="tipos">
        <div class="container mx-auto text-center">
            <h2>¿Eres de capuchina, perfilada o furgo camper?</h2>
            <p>La clave de los mejores roadtrips es plantearse las preguntas adecuadas antes de alquilar una autocaravana o una furgoneta camper</p>
            <div class="tipo-cars">
                <div class="tipo-item">
                    <img src="{{ asset('storage/imagenes/FurgoCamper.png') }}" alt="Furgoneta Camper">
                    <h3>Furgoneta Camper</h3>
                    <p>La furgoneta camper es la más accesible. Ideal para los amantes de las actividades al aire libre y los viajeros que desean estar más cerca de la naturaleza.</p>
                </div>
                <div class="tipo-item">
                    <img src="{{ asset('storage/imagenes/CamperGranVol.png') }}" alt="Camper Gran Volumen">
                    <h3>Camper Gran Volumen</h3>
                    <p>Perfecta para parejas y viajes cortos en familia, la camper gran volumen es sinónimo de practicidad. De fácil manejo y compacta, te llevará por todas partes.</p>
                </div>
                <div class="tipo-item">
                    <img src="{{ asset('storage/imagenes/Perfilada.png') }}" alt="Caravana Perfilada">
                    <h3>Caravana Perfilada</h3>
                    <p>El vehículo más compacto con todo el equipo necesario. ¿El pequeño extra? Su forma aerodinámica: mejor para el presupuesto del viaje y el planeta.</p>
                </div>
                <div class="tipo-item">
                    <img src="{{ asset('storage/imagenes/Capuchina.png') }}" alt="Caravana Capuchina">
                    <h3>Caravana Capuchina</h3>
                    <p>¡Toda la comodidad! La capuchina te ofrece un auténtico sabor a libertad y al mismo tiempo simplifica la autonomía de tu viaje. A los niños les encanta.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Pasos -->
    <section class="pasos">
        <div class="container mx-auto text-center">
            <h2>Tu alquiler con tres sencillos pasos</h2>
            <p>La forma más sencilla de alquilar tu camper para disfrutar de unas vaciones inolvidables</p>
            <div class="paso">
                <div class="paso-item">
                    <i class="fas fa-map-marker-alt"></i>
                    <h3>Selecciona tu ubicación</h3>
                    <p>Elige dónde empezar y terminar tu viaje</p>
                </div>
                <div class="paso-item">
                    <i class="fas fa-calendar-alt"></i>
                    <h3>Selecciona tus fechas</h3>
                    <p>Elige cuándo empezar y terminar tu viaje</p>
                </div>
                <div class="paso-item">
                    <i class="fas fa-car"></i>
                    <h3>Selecciona tu coche</h3>
                    <p>Elige la camper de tus sueños y disfrútala</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Reseñas -->
    <section class="reseñas">
        <div class="container mx-auto text-center">
            <h2>¿Qué dicen nuestros clientes?</h2>
            <div class="reseña">
                <div class="testimonio">
                    <div class="imagen">
                        <img src="{{ asset('storage/imagenes/MiguelDuran.jpg') }}" alt="Testimonio 1">
                    </div>
                    <div class="contenido">
                        <div class="estrellas">
                            ★★★★★
                        </div>
                        <p>“Mi experiencia con Van&Go fue fantástica. La app es fácil de usar, y las caravanas están en perfecto estado. ¡Ideal para explorar sin preocupaciones!”</p>
                        <p>Miguel Durán, Málaga</p>
                    </div>
                </div>
                <div class="testimonio">
                    <div class="imagen">
                        <img src="{{ asset('storage/imagenes/LauraPerez.jpg') }}" alt="Testimonio 2">
                    </div>
                    <div class="contenido">
                        <div class="estrellas">
                            ★★★★★
                        </div>
                        <p>“Me siento muy segura usando la app de Van&Go para alquilar caravanas. La atención al cliente es excelente y los vehículos siempre están listos a tiempo. ¡Totalmente recomendable!”</p>
                        <p>Laura Pérez, Valencia</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- App -->
    <section class="app">
        <div class="container mx-auto text-center">
            <div class="app-content">
                <div class="app-left">
                    <h2>Descarga nuestra aplicación ¡GRATIS!</h2>
                    <p>Reserva más rápido y fácil, y accede a ofertas exclusivas</p>
                    <div class="app-buttons">
                        <a href="#" class="google-play-button">
                            <img src="{{ asset('storage/imagenes/Google.png') }}" alt="Google Play" class="store-logo">
                        </a>
                        <a href="#" class="app-store-button">
                            <img src="{{ asset('storage/imagenes/IOS.png') }}" alt="App Store" class="store-logo">
                        </a>
                    </div>
                </div>

                <div class="app-right">
                    <img src="{{ asset('storage/imagenes/APP.png') }}" alt="App en móvil">
                </div>
            </div>
        </div>
    </section>

    <footer>
        <div class="container flex justify-between items-start">
            <div class="logo-social">
                <h1>Van&Go.</h1>
                <p>¡Comienza tu próxima aventura con nosotros!</p>
                <div class="social-icons">
                    <a href="https://www.instagram.com" target="_blank"><i class="fab fa-instagram"></i></a>
                    <a href="https://www.x.com" target="_blank"><i class="fa-solid fa-x"></i></a>
                    <a href="https://www.facebook.com" target="_blank"><i class="fab fa-facebook"></i></a>
                    <a href="https://www.discord.com" target="_blank"><i class="fa-brands fa-discord"></i></a>
                </div>
            </div>

            <div class="footer-links ml-auto">
                <div>
                    <p class="font-semibold"><strong>Explora</strong></p>
                    <a href="{{ route('catalogo') }}" class="block">Catálogo</a>
                    <a href="{{ route('elijenos') }}" class="block">Elíjenos</a>
                    <a href="{{ route('rutas') }}" class="block">Rutas</a>
                </div>
                <div>
                    <p class="font-semibold"><strong>Soporte</strong></p>
                    <a href="#" class="block">Centro de ayuda</a>
                    <a href="{{ route('contacto') }}" class="block">Contacto</a>
                    <a href="#" class="block">Ubicaciones</a>
                </div>
            </div>
        </div>

        <div class="footer-bottom text-center mt-8">
            <p class="inline-block mx-2">©2024 Van&Go. Todos los derechos reservados.</p>
            <p class="inline-block mx-2"><a href="#">Política de privacidad</a> & <a href="#">Términos y condiciones</a></p>
        </div>
    </footer>

</body>

</html>
