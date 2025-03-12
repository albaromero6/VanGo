<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Usuario;
use Illuminate\Support\Facades\Hash;

class UsuarioTableSeeder extends Seeder
{
    public function run()
    {
        # Creación de un usuario predefinido

        Usuario::create([
            'nombre' => 'Administrador',
            'apellido' => 'Administrador', 
            'email' => 'admin@vango.com',
            'password' => Hash::make('admin1234'),
            'telefono' => '689562536',
            'direccion' => 'Calle Maldivia 13, Málaga, España',
            'fecha_nacimiento' => '1989-01-01',
        ]);

    }
}