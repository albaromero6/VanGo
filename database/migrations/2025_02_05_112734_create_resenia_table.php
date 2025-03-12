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
        Schema::create('resenia', function (Blueprint $table) {
            $table->id('idResen'); 
            $table->foreignId('idUsu')->constrained('usuario', 'idUsu')->onDelete('cascade'); 
            $table->foreignId('idVeh')->constrained('vehiculo', 'idVeh')->onDelete('cascade'); 
            $table->tinyInteger('puntuacion')->unsigned();
            $table->text('comentario')->nullable(); 
            $table->timestamp('fecha_resenia')->useCurrent(); 
            $table->timestamps(); 
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('resenia');
    }
};