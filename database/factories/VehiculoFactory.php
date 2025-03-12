<?php

namespace Database\Factories;

use App\Models\Vehiculo;
use Illuminate\Database\Eloquent\Factories\Factory;

class VehiculoFactory extends Factory
{
    protected $model = Vehiculo::class;

    public function definition()
    {
        $this->faker->locale = 'es_ES'; // Configuro Faker en Español

        $marcas = [
            'Volkswagen', 'Mercedes-Benz', 'Ford', 'Renault', 'Fiat', 'Citroën',
            'Peugeot', 'Opel', 'Nissan', 'Toyota', 'Hyundai', 'Iveco'
        ];

        $modelos = [
            'Crafter', 'Sprinter', 'Transit', 'Master', 'Ducato', 'Jumper',
            'Expert', 'Vivaro', 'NV300', 'Proace', 'H350', 'Daily'
        ];

        return [
            'marca' => $this->faker->randomElement($marcas),
            'modelo' => $this->faker->randomElement($modelos),
            'año' => $this->faker->numberBetween(1995, date("Y")), 
            'capacidad' => $this->faker->numberBetween(2, 6),
            'precio_dia' => $this->faker->numberBetween(20, 200),
            'descripcion' => $this->faker->text(),
            'disponible' => $this->faker->boolean(),
            'imagen' => 'storage/imagenes/' . $this->faker->randomElement([
                'Catalogo1.png', 'Catalogo2.png', 'Catalogo3.png', 'Catalogo4.png', 
                'Catalogo5.png', 'Catalogo6.png', 'Catalogo7.png', 'Catalogo8.png', 
                'Catalogo9.png', 'Catalogo10.png', 'Catalogo11.png', 'Catalogo12.png'
            ]), 
        ];
    }
}

