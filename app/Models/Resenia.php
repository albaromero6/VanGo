<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Resenia extends Model
{
    use HasFactory;

    /**
     * Nombre real de la tabla en la BD
     * @var string
     */
    protected $table = 'resenia'; 

    /**
     * Nombre real de la Pk en la BD
     * @var string
     */
    protected $primaryKey = 'idResen';

    /**
     * Activamos los timestamps
     * @var boolean
     */
    public $timestamps = true; 

    /**
     * Atributos asignables en masa
     * @var array
     */
    protected $fillable = [
        'idUsu', 
        'idVeh', 
        'puntuacion', 
        'comentario', 
        'fecha_resenia'
    ];

    /**
     * Casteo de atributo para usar datos de PHP
     * @var array
     */
    protected $casts = [
        'fecha_resenia' => 'datetime',
    ];

    /**
     * Una reseña pertenece a un usuario
     * @return void
     */
    public function usuario()
    {
        return $this->belongsTo(Usuario::class, 'idUsu', 'idUsu');
    }

    /**
     * Una reseña pertenece a un vehículo
     * @return void
     */
    public function vehiculo()
    {
        return $this->belongsTo(Vehiculo::class, 'idVeh', 'idVeh');
    }
}