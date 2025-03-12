<?php

namespace Database\Seeders;

use App\Models\Usuario;
use App\Models\Vehiculo;
use App\Models\UsuarioVehiculo;
use Illuminate\Database\Seeder;
use Carbon\Carbon;

class UsuarioVehiculoTableSeeder extends Seeder
{
    public function run()
    {
        # Obtener 4 usuarios que no sean el administrador
        $usuarios = Usuario::where('email', '!=', 'admin@vango.com')->inRandomOrder()->take(4)->get(); 

        # Obtener 4 vehículos
        $vehiculos = Vehiculo::inRandomOrder()->take(4)->get(); 

        # Establecer 4 relaciones específicas entre usuarios y vehículos
        foreach ($usuarios as $index => $usuario) {
            $vehiculo = $vehiculos[$index]; 

            # Crea la relación en la tabla pivot UsuarioVehiculo
            UsuarioVehiculo::create([
                'idUsu' => $usuario->idUsu,
                'idVeh' => $vehiculo->idVeh,
                'fecha_reserva' => Carbon::now()->addDays(rand(1, 30)), 
                'estado' => ['pendiente', 'confirmada', 'cancelada'][rand(0, 2)],
            ]);
        }
    }
}
