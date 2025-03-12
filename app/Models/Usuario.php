<?php

namespace App\Models;

use Illuminate\Foundation\Auth\User as Authenticatable; 
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Notifications\Notifiable;

class Usuario extends Authenticatable
{
    use HasFactory, Notifiable;

    /**
     * Nombre real de la tabla en la BD
     * @var string
     */
    protected $table = 'usuario'; 

    /**
     * Nombre real de la Pk en la BD
     * @var string
     */
    protected $primaryKey = 'idUsu'; 

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
        'nombre', 'apellido', 'email', 'password',
        'telefono', 'direccion', 'fecha_nacimiento', 'fecha_registro'
    ];

    /**
     * Atributos que se ocultan en la serialización
     * @var array
     */
    protected $hidden = ['password']; 

    /**
     * Casteo de atributo para usar datos de PHP
     * @var array
     */
    protected $casts = [
        'fecha_nacimiento' => 'date',
        'fecha_registro' => 'datetime',
    ];

    /**
     * Relación 1:N entre Usuario:Resenia
     * Un usuario puede tener muchas reseñas
     * @return void
     */
    public function resenias()
    {
        return $this->hasMany(Resenia::class, 'idUsu', 'idUsu');
    }

    /**
     * Relación N:N entre Usuario:Vehiculo
     * Un usuario puede reservar muchos vehículos, y un vehículo puede tener muchas reservas de usuarios
     * @return void
     */
    public function vehiculos()
    {
        return $this->belongsToMany(Vehiculo::class, 'usuario_vehiculo', 'idUsu', 'idVeh')
                    ->withPivot('fecha_reserva', 'estado')
                    ->withTimestamps();
    }
}