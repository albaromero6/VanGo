<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\Pivot;

class UsuarioVehiculo extends Pivot
{
    /**
     * Nombre real de la tabla en la BD
     * @var string
     */
    protected $table = 'usuario_vehiculo';

    /**
     * Nombre real de la Pk en la BD
     * @var string
     */
    protected $primaryKey = 'idReser'; 

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
        'fecha_reserva', 
        'estado'
    ];
}