<?php

namespace Database\Seeders;

use App\Models\Usuario;
use App\Models\Vehiculo;
use App\Models\Resenia;
use App\Models\UsuarioVehiculo;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        # Crea el usuario administrador predefinido
        $this->call(UsuarioTableSeeder::class);

        # Crea 9 usuarios aleatorios
        Usuario::factory(9)->create();

        # Crea 30 vehículos aleatorios
        Vehiculo::factory(30)->create();

        # Crea 20 reseñas aleatorias
        Resenia::factory(20)->create();

        # Crea las relaciones entre usuarios y vehículos, es decir, las reservas
        $this->call(UsuarioVehiculoTableSeeder::class);
    }
}