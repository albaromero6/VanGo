<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Van&Go - Editar Vehículo</title>
    
    <style>
        nav {
            font-family: 'font-poppins', sans-serif;
        }

        .unalinea {
            height: 38px;
            overflow: hidden;
            resize: none;
        }
    </style>

    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="bg-white-100 mx-auto">

    <nav class="bg-white border-gray-200 dark:bg-gray-900 w-full">
        <div class="flex flex-wrap items-center justify-between p-4">
            <div class="flex items-center">
                <a href="/" class="flex items-center space-x-3 rtl:space-x-reverse">
                    <img src="{{ asset('storage/imagenes/Logo.png') }}" alt="Logo Van&Go" class="h-12 w-auto">
                </a>
                <ul class="flex font-medium font-poppins font-bold ml-4 md:flex-row md:space-x-8">
                    <li>
                        <a href="#" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 font-bold">Elíjenos</a>
                    </li>
                    <li>
                        <a href="#" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 font-bold">Rutas</a>
                    </li>
                    <li>
                        <a href="#" class="block py-2 px-3 text-gray-900 rounded-sm hover:bg-gray-hovers md:hover:bg-transparent md:hover:text-hovers md:dark:hover:text-blue-500 dark:text-white dark:hover:bg-gray-700 dark:hover:text-white md:dark:hover:bg-transparent dark:border-gray-700 font-bold">Contacto</a>
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
    
    <h1 class="text-3xl font-bold mb-8 text-center my-8 font-volkhov text-tittle">Editar Vehículo</h1>

    <div class="px-6">  
        <div class="mx-auto w-full max-w-xl font-poppins"> 

            <form action="{{ route('vehiculos.actualizar', $vehiculo->idVeh) }}" method="POST">
                @csrf
                @method('PUT')

                <div class="mb-4">
                    <label for="marca" class="block text-gray-700 font-bold mb-2">Marca:</label>
                    <input type="text" name="marca" id="marca" value="{{ old('marca', $vehiculo->marca) }}" 
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    @error('marca')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                    @enderror
                </div>

                <div class="mb-4">
                    <label for="modelo" class="block text-gray-700 font-bold mb-2">Modelo:</label>
                    <input type="text" name="modelo" id="modelo" value="{{ old('modelo', $vehiculo->modelo) }}" 
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    @error('modelo')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                    @enderror
                </div>

                <div class="mb-4">
                    <label for="año" class="block text-gray-700 font-bold mb-2">Año:</label>
                    <input type="number" name="año" id="año" value="{{ old('año', $vehiculo->año) }}" 
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    @error('año')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                    @enderror
                </div>

                <div class="mb-4">
                    <label for="capacidad" class="block text-gray-700 font-bold mb-2">Capacidad:</label>
                    <input type="number" name="capacidad" id="capacidad" value="{{ old('capacidad', $vehiculo->capacidad) }}" 
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    @error('capacidad')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                    @enderror
                </div>

                <div class="mb-4">
                    <label for="precio_dia" class="block text-gray-700 font-bold mb-2">Precio/día:</label>
                    <input type="number" step="0.01" name="precio_dia" id="precio_dia" value="{{ old('precio_dia', $vehiculo->precio_dia) }}" 
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    @error('precio_dia')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                    @enderror
                </div>

                <div class="mb-6">
                    <label for="descripcion" class="block text-gray-700 font-bold mb-2">Descripción:</label>
                    <textarea name="descripcion" id="descripcion" 
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline unalinea">{{ old('descripcion', $vehiculo->descripcion) }}</textarea>
                    @error('descripcion')
                        <p class="text-red-500 text-sm mt-1">{{ $message }}</p>
                    @enderror
                </div>

                <div class="flex items-center justify-between">
                    <button type="submit" class="text-white bg-ocean hover:bg-hovers focus:ring-4 focus:outline-none focus:ring-blue-300 rounded-lg text-sm px-4 py-2 text-center font-poppins font-bold mb-12">Actualizar</button>
                </div>
            </form>
        </div>  
    </div>  
</body>
</html>
