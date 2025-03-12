<?php

namespace App\Http\Controllers;

use App\Models\Vehiculo;
use Illuminate\Http\Request;

class VehiculoController extends Controller
{
    /**
     * Listar todos los vehículos.
     *
     * @param Request $request
     * @return \Illuminate\View\View
     */
    public function listar(Request $request)
    {
        return view('vehiculos.listar', ['vehiculos' => Vehiculo::all()]);
    }

    /**
     * Mostrar formulario de creación de vehículo.
     *
     * @return \Illuminate\View\View
     */
    public function crear()
    {
        return view('vehiculos.crear');
    }

    /**
     * Guardar un nuevo vehículo en la base de datos.
     *
     * @param Request $request
     * @return \Illuminate\Http\RedirectResponse
     */
    public function guardar(Request $request)
    {
        $request->validate([
            "marca"       => "required|string|max:30",
            "modelo"      => "required|string|max:30",
            "año"         => "required|integer|min:1995|max:" . date('Y'),
            "capacidad"   => "required|integer|min:2|max:6",
            "precio_dia"  => "required|numeric|min:20|max:200",
            "descripcion" => "required|string",
        ]);

        Vehiculo::create([
            'marca'       => $request->input('marca'),
            'modelo'      => $request->input('modelo'),
            'año'         => $request->input('año'),
            'capacidad'   => $request->input('capacidad'),
            'precio_dia'  => $request->input('precio_dia'),
            'descripcion' => $request->input('descripcion'),
        ]);

        return to_route('vehiculos.listar');
    }

    /**
     * Mostrar formulario de edición de un vehículo.
     *
     * @param Vehiculo $vehiculo
     * @return \Illuminate\View\View
     */
    public function editar(Vehiculo $vehiculo)
    {
        return view('vehiculos.editar', ['vehiculo' => $vehiculo]);
    }

    /**
     * Actualizar la información de un vehículo.
     *
     * @param Request $request
     * @param Vehiculo $vehiculo
     * @return \Illuminate\Http\RedirectResponse
     */
    public function actualizar(Request $request, Vehiculo $vehiculo)
    {
        $request->validate([
            "marca"       => "required|string|max:30",
            "modelo"      => "required|string|max:30",
            "año"         => "required|integer|min:1995|max:" . date('Y'),
            "capacidad"   => "required|integer|min:2|max:6",
            "precio_dia"  => "required|numeric|min:20|max:200",
            "descripcion" => "required|string",
        ]);

        $vehiculo->update([
            'marca'       => $request->input('marca'),
            'modelo'      => $request->input('modelo'),
            'año'         => $request->input('año'),
            'capacidad'   => $request->input('capacidad'),
            'precio_dia'  => $request->input('precio_dia'),
            'descripcion' => $request->input('descripcion'),
        ]);

        return to_route('vehiculos.listar');
    }

    /**
     * Eliminar un vehículo de la base de datos.
     *
     * @param Vehiculo $vehiculo
     * @return \Illuminate\Http\RedirectResponse
     */
    public function eliminar(Vehiculo $vehiculo)
    {
        $vehiculo->delete();

        return to_route('vehiculos.listar');
    }
}
