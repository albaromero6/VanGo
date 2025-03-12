<?php

use App\Http\Controllers\ProfileController;
use App\Http\Controllers\AuthController; 
use App\Http\Controllers\VehiculoController;
use Illuminate\Support\Facades\Route;

# Muestra la página home
Route::get('/', function () {
    return view('welcome');
})->name('home'); 

# Muestra el formulario de creación de vehículos
Route::get('/vehiculos/crear', [VehiculoController::class, 'crear'])->name('vehiculos.crear');

# Guarda un nuevo vehículo
Route::post('/vehiculos', [VehiculoController::class, 'guardar'])->name('vehiculos.guardar'); 

# Elimina un vehículo
Route::delete('/vehiculos/{vehiculo}', [VehiculoController::class, 'eliminar'])->name('vehiculos.eliminar');

# Edita un vehículo
Route::get('/vehiculos/{vehiculo}/editar', [VehiculoController::class, 'editar'])->name('vehiculos.editar');

# Actualiza un vehículo
Route::put('/vehiculos/{vehiculo}/actualizar', [VehiculoController::class, 'actualizar'])->name('vehiculos.actualizar');

# Página de catálogo
Route::get('/catalogo', [VehiculoController::class, 'listar'])->name('catalogo');

# Página de elijenos
Route::get('/elijenos', function () {
    return view('secciones.elijenos'); 
})->name('elijenos');

# Página de rutas
Route::get('/rutas', function () {
    return view('secciones.rutas'); 
})->name('rutas');

# Página de contacto
Route::get('/contacto', function () {
    return view('secciones.contacto'); 
})->name('contacto');

######## NO TOCAR ########

Route::get('/dashboard', function () {
    return view('dashboard');
})->middleware(['auth', 'verified'])->name('dashboard');

Route::middleware('auth')->group(function () {
    Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
    Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
    Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');
});

require __DIR__.'/auth.php';

# Auth::routes();

Route::get('/home', [App\Http\Controllers\HomeController::class, 'index'])->name('home');
