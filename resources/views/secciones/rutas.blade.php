<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Van&Go</title>
    <link rel="icon" href="{{ asset('favicon.ico') }}" type="image/x-icon">

    @vite('resources/css/rutas.css')

</head>

<body>

    <!-- Barra de navegación -->
    <nav class="bg-white border-gray-200 dark:bg-gray-900 w-full">
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

    <!-- Título y subtítulo -->
    <div class="titulo-subtitulo-container">
        <div class="titulo font-volkhov">Rutas</div>
        <div class="subtitulo font-poppins">"Una mirada a las experiencias increíbles que el mundo tiene para ofrecer"</div>
    </div>

    <!-- Contenido de las rutas -->
    <div class="content">
        <div class="ruta">
            <div class="ruta-texto">
                <h2>Pirineos</h2>
                <p>Recorrer los Pirineos en camper es una experiencia única, donde la aventura y la naturaleza se
                    combinan a la perfección. Desde los valles de Cataluña hasta los picos más altos, encontrarás
                    paisajes espectaculares y pueblos pintorescos. Explora parques como Aigüestortes o el Valle de Arán,
                    y disfruta de la tranquilidad de la montaña. Con tu camper, puedes parar en lugares tranquilos y
                    dormir bajo un cielo estrellado. Una vía ideal para los amantes de la naturaleza y la libertad.</p>
                <a href="#">Read More →</a>
            </div>
            <img src="{{ asset('storage/imagenes/Pirineos.jpg') }}" alt="Pirineos">
        </div>

        <div class="ruta">
            <div class="ruta-texto">
                <h2>Costa Brava</h2>
                <p>Recorrer la Costa Brava en camper es una experiencia única, explorando hermosos pueblos costeros como
                    Cadaqués, Tamariu y Begur. Disfrutarás de calas secretas, aguas cristalinas y acantilados
                    impresionantes que dan paso a vistas espectaculares del Mediterráneo. La ruta te lleva a
                    encantadores pueblos pesqueros llenos de historia, tradición y un ambiente único. Podrás disfrutar
                    de rutas de senderismo junto al mar o explorar senderos rodeados de naturaleza.</p>
                <a href="#">Read More →</a>
            </div>
            <img src="{{ asset('storage/imagenes/Costa Brava.jpg') }}" alt="Costa Brava">
        </div>
    </div>

</body>

</html>