<?php

namespace Database\Factories;

use App\Models\Resenia;
use App\Models\Usuario;
use App\Models\Vehiculo;
use Illuminate\Database\Eloquent\Factories\Factory;

class ReseniaFactory extends Factory
{
    protected $model = Resenia::class;

    public function definition()
    {
        $this->faker->locale = 'es_ES';  # Configuro Faker en Español

        return [
            'idUsu' => Usuario::factory(),  
            'idVeh' => Vehiculo::factory(),   
            'puntuacion' => $this->faker->numberBetween(1, 5), 
            'comentario' => $this->faker->paragraph(), 
            'fecha_resenia' => $this->faker->dateTimeThisYear(), 
        ];
    }
}