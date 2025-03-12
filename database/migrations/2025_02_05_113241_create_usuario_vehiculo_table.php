<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('usuario_vehiculo', function (Blueprint $table) {
            $table->id('idReser'); 
            $table->foreignId('idUsu')->constrained('usuario', 'idUsu')->onDelete('cascade'); 
            $table->foreignId('idVeh')->constrained('vehiculo', 'idVeh')->onDelete('cascade'); 
            $table->date('fecha_reserva');
            $table->enum('estado', ['pendiente', 'confirmada', 'cancelada'])->default('pendiente'); 
            $table->timestamps(); 
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('usuario_vehiculo');
    }
};