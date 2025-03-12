<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Van&Go</title>
    <link rel="icon" href="{{ asset('favicon.ico') }}" type="image/x-icon">

    @vite('resources/css/catalogo.css')

</head>
<body class="bg-white-100 mx-auto">

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

    <div class="px-6 font-poppins flex flex-col items-center">

        <div class="mb-16 mt-16"> <!-- Añadido margen abajo -->
            <a href="{{ route('vehiculos.crear') }}" class="text-white bg-ocean hover:bg-hovers rounded-lg text-sm px-4 py-2">Añadir vehículo</a>
        </div>
        

        <!-- Contenedor para las tarjetas -->
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-16 mb-12">
            @foreach ($vehiculos as $vehiculo)
                <div class="vehiculo-card">
                    <!-- Imagen con tamaño ajustado -->
                    <img class="vehiculo-imagen" src="{{ asset($vehiculo->imagen) }}" alt="Imagen de {{ $vehiculo->modelo }}">

                    <div class="vehiculo-info">
                        <div class="vehiculo-header font-volkhov">
                            <div class="nombre-modelo">{{ $vehiculo->marca }} {{ $vehiculo->modelo }}</div>
                        </div>
                    
                        <div class="flex items-center space-x-2 mt-2">
                            <div class="flex items-center">
                                <img src="{{ asset('storage/imagenes/Calendario.png') }}" alt="Calendario"> 
                                <span class="text-sm">{{ $vehiculo->año }}</span> 
                            </div>
                            
                            <div class="flex items-center">
                                <img src="{{ asset('storage/imagenes/Usuario.png') }}" alt="Capacidad"> 
                                <span class="text-sm">{{ $vehiculo->capacidad }} personas</span> 
                            </div>
                        </div>
                        
                    
                        <p class="descripcion descripcion-truncada">{{ $vehiculo->descripcion }}</p>
                    
                        <div class="precio font-volkhov">{{ $vehiculo->precio_dia }} €/Día</div>
                    </div>
                    

                    <div class="botones">
                        <form action="{{ route('vehiculos.eliminar', $vehiculo->idVeh) }}" method="POST" class="inline-block">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="text-white bg-ocean hover:bg-hovers focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg text-sm px-4 py-2">Borrar</button>
                        </form>
                        <a href="{{ route('vehiculos.editar', $vehiculo->idVeh) }}" class="text-white bg-yellow-500 hover:bg-yellow-600 rounded-lg text-sm px-4 py-2">Editar</a>
                    </div>
                </div>
            @endforeach
        </div>
    </div>

</body>
</html>
