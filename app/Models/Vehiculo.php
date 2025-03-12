<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Vehiculo extends Model
{
    use HasFactory;

    /**
     * Nombre real de la tabla en la BD
     * @var string
     */
    protected $table = 'vehiculo';

    /**
     * Nombre real de la Pk en la BD
     * @var string
     */ 
    protected $primaryKey = 'idVeh';

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
        'marca',
        'modelo',
        'año',
        'capacidad',
        'precio_dia',
        'descripcion',
        'disponible',
        'fecha_registro',
    ];

    /**
     *  Casteo de atributo para usar datos de PHP
     * @var array
     */
    protected $casts = [
        'año' => 'integer',
        'precio_dia' => 'decimal:2',
        'disponible' => 'boolean',
        'fecha_registro' => 'datetime',
    ];

    /**
     * Relación 1:N entre Vehiculo:Resenia
     * Un vehículo puede tener muchas reseñas
     * @return void
     */
    public function resenias()
    {
        return $this->hasMany(Resenia::class, 'idVeh', 'idVeh');
    }

    /**
     * Relación N:N entre Vehiculo:Usuario
     * Un vehículo puede ser reservado por muchos usuarios, y un usuario puede reservar muchos vehículos
     * @return void
     */
    public function usuarios()
    {
        return $this->belongsToMany(Usuario::class, 'usuario_vehiculo', 'idVeh', 'idUsu')
                    ->withPivot('fecha_reserva', 'estado')
                    ->withTimestamps();
    }
}